package com.muiboot.shiro.common.domain;

import java.io.Serializable;

public class QueryRequest implements Serializable {

	private static final long serialVersionUID = -4869594085374385813L;

	private int limit;
	private int page;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}
