package com.muiboot.shiro.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muiboot.shiro.common.menum.DicType;
import com.muiboot.core.entity.LayerTree;
import com.muiboot.core.service.impl.BaseService;
import com.muiboot.core.util.TreeUtils;
import com.muiboot.shiro.system.dao.SysDicMapper;
import com.muiboot.shiro.system.entity.SysDic;
import com.muiboot.shiro.system.service.DicCacheService;
import com.muiboot.shiro.system.service.DictService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;
import java.util.*;

@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseService<SysDic> implements DictService {

    private static final Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    @Autowired
	private SysDicMapper dicMapper;
	@Autowired
	ObjectMapper jsonMapper;
	@Autowired
	private DicCacheService dicCacheService;
	@Override
	public LayerTree<SysDic> getDictTree(String dicName) {
		List<LayerTree<SysDic>> trees = new ArrayList<>();
		Example example = new Example(SysDic.class);
		Criteria criteria=example.createCriteria();
		//criteria.andEqualTo("valid",1);
		example.orderBy("orderNum");
		List<SysDic> dics = this.selectByExample(example);
		if (StringUtils.isNotBlank(dicName)){
			dics=findDicsWithNameLike(dics,dicName);
		}
		buildTrees(trees, dics);
		return TreeUtils.build(trees);
	}

	/**
	 * 模糊匹配，找到搜索节点
	 * @param dics
	 * @param dicName
	 */
	private List<SysDic> findDicsWithNameLike(List<SysDic> dics, String dicName) {
		List<SysDic> res = new ArrayList<>();
		//1找到搜索节点list
		List<SysDic> owners= findDicsByNameLike(dics,dicName);
		//2.递归查找子节点
		List<SysDic> children = new ArrayList<>();
		findChildren(dics,owners,children);
		//3.递归查找父节点
		List<SysDic> parents=new ArrayList<>();
		findParents(dics,owners,parents);
		if(null!=owners)res.addAll(owners);
		if(null!=children)res.addAll(children);
		if(null!=parents)res.addAll(parents);
		return res;
	}

	private List<SysDic> findParents(List<SysDic> dics, List<SysDic> owners, List<SysDic> parents) {
		if (CollectionUtils.isEmpty(dics)||CollectionUtils.isEmpty(owners))return parents;
		Set<Long> ownerParentsIds=new HashSet<>();
		Iterator<SysDic> ownerIt = owners.iterator();
		while(ownerIt.hasNext()){
			SysDic d = ownerIt.next();
			ownerParentsIds.add(d.getParentId());
		}
		Iterator<SysDic> it = dics.iterator();
		List<SysDic> ownersNew = new ArrayList<>();
		while(it.hasNext()){
			SysDic d = it.next();
			if(ownerParentsIds.contains(d.getDicId())){
				parents.add(d);
				ownersNew.add(d);
				it.remove();
			}
		}
		return findParents(dics,ownersNew,parents);
	}

	private List<SysDic> findChildren(List<SysDic> dics, List<SysDic> owners, List<SysDic> children) {
		if (CollectionUtils.isEmpty(dics)||CollectionUtils.isEmpty(owners))return children;
		Set<Long> ownerIds=new HashSet<>();
		Iterator<SysDic> ownerIt = owners.iterator();
		while(ownerIt.hasNext()){
			SysDic d = ownerIt.next();
			ownerIds.add(d.getDicId());
		}
		Iterator<SysDic> it = dics.iterator();
		List<SysDic> ownersNew = new ArrayList<>();
		while(it.hasNext()){
			SysDic d = it.next();
			if(ownerIds.contains(d.getParentId())){
				children.add(d);
				ownersNew.add(d);
				it.remove();
			}
		}
		return findChildren(dics,ownersNew,children);
	}

	private List<SysDic> findDicsByNameLike(List<SysDic> dics, String dicName) {
		if (CollectionUtils.isEmpty(dics))return null;
		List<SysDic> owners=new ArrayList<>();
		Iterator<SysDic> it = dics.iterator();
		while(it.hasNext()){
			SysDic d = it.next();
			if (d.getDicName().contains(dicName)){
				owners.add(d);
				it.remove();
			}
		}
		return owners;
	}

	@Override
	@Transactional
	@CacheEvict(value="DIC_CACHE",key="'ALLDIC'")
	public void add(SysDic dic) {
		dic.setCreateDate(new Date());
		dic.setUpdateDate(new Date());
		dic.setOrderNum(1);
		dic.setVersion("1.0");
		if (dic.getParentId()==null){
			dic.setParentId(0L);
		}
		this.save(dic);
	}

	@Override
	public Map findDicDetail(Long dicId) {
		SysDic dic = this.mapper.selectByPrimaryKey(dicId);
		Map res =new HashMap();
		Object list= null;
		list = this.buildDicList(dic);
		res.put("info",dic);
		res.put("list",list);
		return res;
	}

	@Override
	public Map loadDics(String[] dicKeys) {
		if (null==dicKeys||dicKeys.length==0)return null;
		Map<String,Object> dicMap = dicCacheService.getAllDicMap();
		Map<String,Object> res=null;
		if (MapUtils.isNotEmpty(dicMap)){
			res=new LinkedHashMap<String,Object>();
			for (int i=0,l=dicKeys.length;i<l;i++){
				if (dicMap.containsKey(dicKeys[i])){
					res.put(dicKeys[i],dicMap.get(dicKeys[i]));
				}
			}
		}
		return res;
	}
	@Override
	public Map nativeSelectBySQL(String msql) {
		if (StringUtils.isBlank(msql))return null;
		List<Map> maps=this.dicMapper.nativeSelectBySQL(msql);
		LinkedHashMap res=null;
		if (null!=maps){
			res = new LinkedHashMap();
			for (Map map:maps){
				if (null!=map.get("K")){
					res.put(map.get("K"),map.get("V"));
				}
			}
		}
		return res;
	}

	@Override
	public List<SysDic> getAllDics() {
		return this.dicMapper.selectAll();
	}

	@Override
	@CacheEvict(value="DIC_CACHE",key="'ALLDIC'")
	public void updateDicNotNull(SysDic dict) {
		this.updateNotNull(dict);
	}

	@Override
	public Object buildDicList(SysDic dic){
		if (dic==null) return null;
		Object m=null;
		if (DicType.SIMPLE.name().equals(dic.getDicType())){
			try {
				m = jsonMapper.readValue(dic.getContent(), LinkedHashMap.class); //json转换成map;
			} catch (IOException e) {
				logger.error("key="+dic.getDicKey()+"字典参数错误，字典转换失败。",e);
				return null;
			}
		}else if (DicType.SQLDIC.name().equals(dic.getDicType())){
			if (StringUtils.isNotBlank(dic.getSqlContent())){
				try {
					m=this.nativeSelectBySQL(dic.getSqlContent());
				}catch (Exception e){
					logger.error("key="+dic.getDicKey()+"SQL执行失败。",e);
					return null;
				}
			}
		}else  if (DicType.TREEDIC.name().equals(dic.getDicType())){
			if (StringUtils.isNotBlank(dic.getSqlContent())){
				try {
					List<Map> maps =this.dicMapper.nativeSelectBySQL(dic.getSqlContent());
					List<LayerTree<Map>> trees = new ArrayList<>();
					buildMapTrees(trees, maps,dic);
					m=TreeUtils.build(trees);
				}catch (Exception e){
					logger.error("key="+dic.getDicKey()+"SQL执行失败。",e);
					return null;
				}
			}
		}else if (DicType.URLDIC.name().equals(dic.getDicType())){
			try {
				m = jsonMapper.readValue(dic.getContent(), LinkedHashMap.class); //json转换成map;
			} catch (IOException e) {
				logger.error("key="+dic.getDicKey()+"字典参数错误，字典转换失败。",e);
				return null;
			}
		}
		return m;
	}

	private void buildMapTrees(List<LayerTree<Map>> trees, List<Map> maps,SysDic dic) {
		for (Map map : maps) {
			LayerTree<Map> tree = new LayerTree<>();
			tree.setId(String.valueOf(map.get("K")));
			tree.setParentId(String.valueOf(map.get("P")));
			tree.setName(String.valueOf(map.get("V")));
			if("1".equals(dic.getShowIcon())){
				Object icon=map.get("I");
				if (null==icon){
					tree.setIcon("layui-icon layui-icon-group");
				}else {
					tree.setIcon(icon.toString());
				}
			}
			Object hasChecked=map.get("hasChecked");
			if (null!=hasChecked){
				tree.setHasChecked(BooleanUtils.toBoolean(((Long) hasChecked).intValue()));
			}
			trees.add(tree);
		}
	}

	private void buildTrees(List<LayerTree<SysDic>> trees, List<SysDic> dics) {
		for (SysDic dic : dics) {
			LayerTree<SysDic> tree = new LayerTree<>();
			tree.setId(dic.getDicId().toString());
			tree.setParentId(dic.getParentId().toString());
			tree.setName(dic.getDicName());
			//tree.setHref(menu.getUrl());
			trees.add(tree);
		}
	}
	@Override
	@Transactional
	@CacheEvict(value="DIC_CACHE",key="'ALLDIC'")
	public void deleteDicts(String dictIds) {
		List<String> list = Arrays.asList(dictIds.split(","));
		this.batchDelete(list, "dicId", SysDic.class);
	}
}
