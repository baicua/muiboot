package com.muiboot.shiro.system.service.impl;

import com.muiboot.shiro.common.layer.LayerTree;
import com.muiboot.shiro.common.service.impl.BaseService;
import com.muiboot.shiro.common.util.TreeUtils;
import com.muiboot.shiro.system.domain.CoreDic;
import com.muiboot.shiro.system.domain.Dict;
import com.muiboot.shiro.system.service.DictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseService<CoreDic> implements DictService {

	@Override
	public LayerTree<CoreDic> getDictTree() {
		List<LayerTree<CoreDic>> trees = new ArrayList<>();
		Example example = new Example(CoreDic.class);
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("valid",1);
		example.orderBy("orderNum");
		List<CoreDic> dics = this.selectByExample(example);
		buildTrees(trees, dics);
		return TreeUtils.build(trees);
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
