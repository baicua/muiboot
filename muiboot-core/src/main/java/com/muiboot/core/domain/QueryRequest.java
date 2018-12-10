package com.muiboot.core.domain;

import java.io.Serializable;
/**
* <p>Description: 分页请求</p>
* @version 1.0 2018/10/12
* @author jin
*/
public class QueryRequest implements Serializable {

	private static final long serialVersionUID = -4869594085374385813L;

	private int limit=10;
	private int page=1;

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
