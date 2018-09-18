package com.muiboot.shiro.system.service.impl;

import java.util.*;

import com.muiboot.shiro.common.menum.GroupType;
import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.system.dao.SysGroupMapper;
import com.muiboot.shiro.system.domain.SysGroup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.util.TreeUtils;
import com.muiboot.shiro.system.service.GroupService;
import tk.mybatis.mapper.entity.Example;

@Service("groupService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GroupServiceImpl extends BaseService<SysGroup> implements GroupService {

	@Autowired
	private SysGroupMapper groupMapper;

	@Override
	public LayerTree<SysGroup> getGroupTree() {
		List<LayerTree<SysGroup>> trees = new ArrayList<>();
		List<SysGroup> groups = this.findAllGroups(new SysGroup());
		for (SysGroup group : groups) {
			LayerTree<SysGroup> tree = new LayerTree<>();
			tree.setId(group.getGroupId().toString());
			tree.setParentId(group.getParentId().toString());
			tree.setName(group.getGroupName());
			if (GroupType.ORGAN.getType().equals(group.getGroupType())){
				tree.setIcon("layui-icon layui-icon-group");
			}else {
				tree.setIcon("layui-icon layui-icon-user");
			}
			tree.setLevel(group.getGroupType());
			trees.add(tree);
		}
		return TreeUtils.build(trees);
	}

	@Override
	public List<SysGroup> findAllGroups(SysGroup group) {
		try {
			Example example = new Example(SysGroup.class);
			Example.Criteria criteria=example.createCriteria();
			if (StringUtils.isNotBlank(group.getGroupName())) {
				criteria.andEqualTo("groupName",group.getGroupName());
			}
			if (null!=group.getParentId()) {
				criteria.andEqualTo("parentId",group.getParentId());
			}
			if (StringUtils.isNotBlank(group.getValid())) {
				criteria.andEqualTo("valid",group.getValid());
			}
			if (StringUtils.isNotBlank(group.getGroupType())) {
				criteria.andEqualTo("groupType",group.getGroupType());
			}
			example.setOrderByClause("group_id");
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public SysGroup findByName(String groupName) {
		Example example = new Example(SysGroup.class);
		example.createCriteria().andEqualTo("groupName",groupName);
		List<SysGroup> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public SysGroup findById(Long groupId) {
		return this.selectByKey(groupId);
	}

	@Override
	@Transactional
	public void addGroup(SysGroup group) {
		Long parentId = group.getParentId();
		if (parentId == null)
			group.setParentId(0L);
		group.setCreateTime(new Date());
		this.save(group);
	}

	@Override
	@Transactional
	public void updateGroup(SysGroup group) {
		this.updateNotNull(group);
	}

	@Override
	public void deleteGroups(String groupIds) {
		List<String> list = Arrays.asList(groupIds.split(","));
		this.batchDelete(list, "groupId", SysGroup.class);
	}

	@Override
	public Map getGroupDetail(Long groupId) {
		//1.获取部门详情
		SysGroup group=this.selectByKey(groupId);
		//2.获取部门人员列表
		Map res = new HashMap();
		res.put("info",group);
		return res;
	}

	@Override
	public Map getDeptByParent(Long parentId) {
		SysGroup $group = new SysGroup();
		$group.setParentId(parentId);
		$group.setValid("1");
		$group.setGroupType(GroupType.DEPT.getType());
		List<SysGroup> groups=this.findAllGroups($group);
		LinkedHashMap<Long,String> res = new LinkedHashMap<>();
		if (CollectionUtils.isNotEmpty(groups)){
			for (SysGroup group:groups){
				res.put(group.getGroupId(),group.getGroupName());
			}
		}
		return res;
	}
}
