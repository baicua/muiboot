package com.muiboot.shiro.system.service.impl;

import java.util.*;

import com.muiboot.shiro.common.service.impl.BaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.util.TreeUtils;
import com.muiboot.shiro.system.dao.DeptMapper;
import com.muiboot.shiro.system.domain.Dept;
import com.muiboot.shiro.system.service.DeptService;
import tk.mybatis.mapper.entity.Example;

@Service("deptService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeptServiceImpl extends BaseService<Dept> implements DeptService {

	@Autowired
	private DeptMapper deptMapper;

	@Override
	public LayerTree<Dept> getDeptTree() {
		List<LayerTree<Dept>> trees = new ArrayList<>();
		List<Dept> depts = this.findAllDepts(new Dept());
		for (Dept dept : depts) {
			LayerTree<Dept> tree = new LayerTree<>();
			tree.setId(dept.getDeptId().toString());
			tree.setParentId(dept.getParentId().toString());
			tree.setName(dept.getDeptName());
			if (dept.getDeptLevel()==0){
				tree.setIcon("layui-icon layui-icon-group");
			}else {
				tree.setIcon("layui-icon layui-icon-user");
			}
			tree.setLevel(dept.getDeptLevel());
			trees.add(tree);
		}
		return TreeUtils.build(trees);
	}

	@Override
	public List<Dept> findAllDepts(Dept dept) {
		try {
			Example example = new Example(Dept.class);
			if (StringUtils.isNotBlank(dept.getDeptName())) {
				example.createCriteria().andCondition("dept_name=", dept.getDeptName());
			}
			example.setOrderByClause("dept_id");
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	@Override
	public Dept findByName(String deptName) {
		Example example = new Example(Dept.class);
		example.createCriteria().andCondition("lower(dept_name) =", deptName.toLowerCase());
		List<Dept> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	@Transactional
	public void addDept(Dept dept) {
		Long parentId = dept.getParentId();
		if (parentId == null)
			dept.setParentId(0L);
		dept.setCreateTime(new Date());
		this.save(dept);
	}

	@Override
	@Transactional
	public void deleteDepts(String deptIds) {
		List<String> list = Arrays.asList(deptIds.split(","));
		this.batchDelete(list, "deptId", Dept.class);
	}

	@Override
	public Dept findById(Long deptId) {
		return this.selectByKey(deptId);
	}

	@Override
	@Transactional
	public void updateDept(Dept dept) {
		this.updateNotNull(dept);
	}

	@Override
	public Map getDeptDetail(Long deptId) {
		//1.获取部门详情
		Dept dept=this.selectByKey(deptId);
		//2.获取部门人员列表
		Map res = new HashMap();
		res.put("info",dept);
		return res;
	}

}
