package com.muiboot.shiro.common.layer;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayerTree<T> implements Serializable{
	/**
	 * 节点ID
	 */
	private String id;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * url
	 */
	private String href;
	/**
	 * 显示节点文本
	 */
	private String name;
	/**
	 * 节点状态，open closed
	 */
	private boolean spread= false;
	/**
	 * 节点是否被选中 true false
	 */
	private boolean checked = false;
	/**
	 * 节点属性
	 */
	private Map<String, Object> attributes;

	/**节点级别*/
	private String level;

	/**
	 * 节点的子节点
	 */
	private List<LayerTree<T>> children = new ArrayList<LayerTree<T>>();

	/**
	 * 父ID
	 */
	private String parentId;
	/**
	 * 是否有父节点
	 */
	private boolean hasParent = false;
	/**
	 * 是否有子节点
	 */
	private boolean hasChildren = false;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<LayerTree<T>> getChildren() {
		return children;
	}

	public void setChildren(List<LayerTree<T>> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public LayerTree(String id, String name, Map<String, Object> state, boolean spread, boolean checked, Map<String, Object> attributes,
                     List<LayerTree<T>> children, String icon, String href, boolean isParent, boolean isChildren, String parentID) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.href = href;
		this.spread = spread;
		this.checked = checked;
		this.attributes = attributes;
		this.children = children;
		this.hasParent = isParent;
		this.hasChildren = isChildren;
		this.parentId = parentID;
	}

	public LayerTree() {
		super();
	}

	@Override
	public String toString() {

		return JSON.toJSONString(this);
	}

}