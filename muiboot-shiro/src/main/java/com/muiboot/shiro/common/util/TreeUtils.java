package com.muiboot.shiro.common.util;

import java.util.ArrayList;
import java.util.List;

import com.muiboot.shiro.common.layer.LayerTree;

public class TreeUtils {
	
	public static <T> LayerTree<T> build(List<LayerTree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<LayerTree<T>> topNodes = new ArrayList<>();
		for (LayerTree<T> children : nodes) {
			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (LayerTree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					if ("attribute".equals(children.getLevel())){
						parent.getAttributes().put(children.getId(),children.getName());
					}else {
						parent.getChildren().add(children);
						children.setHasParent(true);
						parent.setChildren(true);
					}
					continue;
				}
			}

		}

		LayerTree<T> root = new LayerTree<>();
		root.setId("0");
		root.setParentId("");
		root.setHasParent(false);
		root.setChildren(true);
		//root.setChecked(true);
		root.setChildren(topNodes);
		root.setName("根节点");
		return root;
	}

	public static <T> List<LayerTree<T>> buildList(List<LayerTree<T>> nodes, String idParam) {
		if (nodes == null) {
			return null;
		}
		List<LayerTree<T>> topNodes = new ArrayList<>();
		for (LayerTree<T> children : nodes) {
			String pid = children.getParentId();
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);
				continue;
			}
			for (LayerTree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
					continue;
				}
			}
		}
		return topNodes;
	}
}