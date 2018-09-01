package com.muiboot.shiro.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muiboot.shiro.common.dic.denum.DicType;
import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.common.util.TreeUtils;
import com.muiboot.shiro.system.dao.CoreDicMapper;
import com.muiboot.shiro.system.domain.CoreDic;
import com.muiboot.shiro.system.service.DicMapService;
import com.muiboot.shiro.system.service.DictService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.io.IOException;
import java.util.*;

@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseService<CoreDic> implements DictService {

	private static Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);
	@Autowired
	private CoreDicMapper dicMapper;
	@Autowired
	ObjectMapper jsonMapper;
	@Autowired
	private DicMapService dicMapService;
	@Override
	public LayerTree<CoreDic> getDictTree(String dicName) {
		List<LayerTree<CoreDic>> trees = new ArrayList<>();
		Example example = new Example(CoreDic.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("valid",1);
		example.orderBy("orderNum");
		List<CoreDic> dics = this.selectByExample(example);
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
	private List<CoreDic> findDicsWithNameLike(List<CoreDic> dics, String dicName) {
		List<CoreDic> res = new ArrayList<>();
		//1找到搜索节点list
		List<CoreDic> owners= findDicsByNameLike(dics,dicName);
		//2.递归查找子节点
		List<CoreDic> children = new ArrayList<>();
		findChildren(dics,owners,children);
		//3.递归查找父节点
		List<CoreDic> parents=new ArrayList<>();
		findParents(dics,owners,parents);
		if(null!=owners)res.addAll(owners);
		if(null!=children)res.addAll(children);
		if(null!=parents)res.addAll(parents);
		return res;
	}

	private List<CoreDic> findParents(List<CoreDic> dics, List<CoreDic> owners,List<CoreDic> parents) {
		if (CollectionUtils.isEmpty(dics)||CollectionUtils.isEmpty(owners))return parents;
		Set<Long> ownerParentsIds=new HashSet<>();
		Iterator<CoreDic> ownerIt = owners.iterator();
		while(ownerIt.hasNext()){
			CoreDic d = ownerIt.next();
			ownerParentsIds.add(d.getParentId());
		}
		Iterator<CoreDic> it = dics.iterator();
		List<CoreDic> ownersNew = new ArrayList<>();
		while(it.hasNext()){
			CoreDic d = it.next();
			if(ownerParentsIds.contains(d.getDicId())){
				parents.add(d);
				ownersNew.add(d);
				it.remove();
			}
		}
		return findParents(dics,ownersNew,parents);
	}

	private List<CoreDic> findChildren(List<CoreDic> dics, List<CoreDic> owners,List<CoreDic> children) {
		if (CollectionUtils.isEmpty(dics)||CollectionUtils.isEmpty(owners))return children;
		Set<Long> ownerIds=new HashSet<>();
		Iterator<CoreDic> ownerIt = owners.iterator();
		while(ownerIt.hasNext()){
			CoreDic d = ownerIt.next();
			ownerIds.add(d.getDicId());
		}
		Iterator<CoreDic> it = dics.iterator();
		List<CoreDic> ownersNew = new ArrayList<>();
		while(it.hasNext()){
			CoreDic d = it.next();
			if(ownerIds.contains(d.getParentId())){
				children.add(d);
				ownersNew.add(d);
				it.remove();
			}
		}
		return findChildren(dics,ownersNew,children);
	}

	private List<CoreDic> findDicsByNameLike(List<CoreDic> dics, String dicName) {
		if (CollectionUtils.isEmpty(dics))return null;
		List<CoreDic> owners=new ArrayList<>();
		Iterator<CoreDic> it = dics.iterator();
		while(it.hasNext()){
			CoreDic d = it.next();
			if (d.getDicName().contains(dicName)){
				owners.add(d);
				it.remove();
			}
		}
		return owners;
	}

	@Override
	@Transactional
	public void add(CoreDic dic) {
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
		CoreDic dic = this.mapper.selectByPrimaryKey(dicId);
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
		Map<String,Object> dicMap = dicMapService.getAllDicMap();
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
	@Cacheable(value="dicCache",key="'ALLDIC'")
	private Map<String,Object> getAllDicMap() {
		List<CoreDic> dics=this.getAllDics();
		Map<String,Object> res=null;
		if (CollectionUtils.isNotEmpty(dics)){
			res=new LinkedHashMap();
			for (int i=0,l=dics.size();i<l;i++){
				Object dicMap = this.buildDicList(dics.get(i));
				if (null!=dicMap){
					res.put(dics.get(i).getDicKey(),dicMap);
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
				if (null!=map.get("k")){
					res.put(map.get("k"),map.get("v"));
				}
			}
		}
		return res;
	}

	@Override
	public List<CoreDic> getAllDics() {
		return this.dicMapper.selectAll();
	}

	@Override
	@CacheEvict(value="dicCache",key="'ALLDIC'")
	public void updateDicNotNull(CoreDic dict) {
		this.updateNotNull(dict);
	}

	@Override
	public Object buildDicList(CoreDic dic){
		if (dic==null) return null;
		Object m=null;
		if (DicType.SIMPLE.name().equals(dic.getDicType())){
			try {
				m = jsonMapper.readValue(dic.getContent(), LinkedHashMap.class); //json转换成map;
			} catch (IOException e) {
				logger.error("参数错误:字典转换失败");
				return null;
			}
		}else if (DicType.SQLDIC.name().equals(dic.getDicType())){
			if (StringUtils.isNotBlank(dic.getSqlContent())){
				try {
					m=this.nativeSelectBySQL(dic.getSqlContent());
				}catch (Exception e){
					logger.error("SQL执行失败："+e.getMessage());
					return null;
				}
			}
		}else  if (DicType.TREEDIC.name().equals(dic.getDicType())){
			if (StringUtils.isNotBlank(dic.getSqlContent())){
				try {
					List<Map> maps =this.dicMapper.nativeSelectBySQL(dic.getSqlContent());
					List<LayerTree<Map>> trees = new ArrayList<>();
					buildMapTrees(trees, maps);
					m=TreeUtils.build(trees);
				}catch (Exception e){
					logger.error("SQL执行失败："+e.getMessage());
					return null;
				}
			}
		}
		return m;
	}

	private void buildMapTrees(List<LayerTree<Map>> trees, List<Map> maps) {
		for (Map map : maps) {
			LayerTree<Map> tree = new LayerTree<>();
			tree.setId(String.valueOf(map.get("k")));
			tree.setParentId(String.valueOf(map.get("p")));
			tree.setName(String.valueOf(map.get("v")));
			trees.add(tree);
		}
	}

	private void buildTrees(List<LayerTree<CoreDic>> trees, List<CoreDic> dics) {
		for (CoreDic dic : dics) {
			LayerTree<CoreDic> tree = new LayerTree<>();
			tree.setId(dic.getDicId().toString());
			tree.setParentId(dic.getParentId().toString());
			tree.setName(dic.getDicName());
			//tree.setHref(menu.getUrl());
			trees.add(tree);
		}
	}
}
