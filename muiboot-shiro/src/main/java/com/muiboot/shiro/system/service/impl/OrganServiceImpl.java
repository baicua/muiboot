package com.muiboot.shiro.system.service.impl;

import java.util.*;

import com.muiboot.shiro.common.menum.OrganType;
import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.system.dao.OrganMapper;
import com.muiboot.shiro.system.domain.Organ;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.util.TreeUtils;
import com.muiboot.shiro.system.service.OrganService;
import tk.mybatis.mapper.entity.Example;

@Service("organService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrganServiceImpl extends BaseService<Organ> implements OrganService {

	@Autowired
	private OrganMapper organMapper;

	@Override
	public LayerTree<Organ> getOrganTree() {
		List<LayerTree<Organ>> trees = new ArrayList<>();
		List<Organ> organs = this.findAllOrgans(new Organ());
		for (Organ organ : organs) {
			LayerTree<Organ> tree = new LayerTree<>();
			tree.setId(organ.getOrganId().toString());
			tree.setParentId(organ.getParentId().toString());
			tree.setName(organ.getOrganName());
			if (OrganType.ORGAN.getType().equals(organ.getOrganType())){
				tree.setIcon("layui-icon layui-icon-group");
			}else {
				tree.setIcon("layui-icon layui-icon-user");
			}
			tree.setLevel(organ.getOrganType());
			trees.add(tree);
		}
		return TreeUtils.build(trees);
	}

	@Override
	public List<Organ> findAllOrgans(Organ organ) {
		try {
			Example example = new Example(Organ.class);
			if (StringUtils.isNotBlank(organ.getOrganName())) {
				example.createCriteria().andEqualTo("organName",organ.getOrganName());
			}
			example.setOrderByClause("organ_id");
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	@Override
	public Organ findByName(String organName) {
		Example example = new Example(Organ.class);
		example.createCriteria().andEqualTo("organName",organName);
		List<Organ> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	@Transactional
	public void addOrgan(Organ organ) {
		Long parentId = organ.getParentId();
		if (parentId == null)
			organ.setParentId(0L);
		organ.setCreateTime(new Date());
		this.save(organ);
	}

	@Override
	@Transactional
	public void deleteOrgans(String organIds) {
		List<String> list = Arrays.asList(organIds.split(","));
		this.batchDelete(list, "organId", Organ.class);
	}

	@Override
	public Organ findById(Long organId) {
		return this.selectByKey(organId);
	}

	@Override
	@Transactional
	public void updateOrgan(Organ organ) {
		this.updateNotNull(organ);
	}

	@Override
	public Map getOrganDetail(Long organId) {
		//1.获取部门详情
		Organ organ=this.selectByKey(organId);
		//2.获取部门人员列表
		Map res = new HashMap();
		res.put("info",organ);
		return res;
	}

}
