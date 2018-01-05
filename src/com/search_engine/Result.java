package com.search_engine;

import net.sf.json.JSONArray;

public class Result {

	private JSONArray jsonarray;
	private long total;
	public JSONArray getJsonarray() {
		return jsonarray;
	}
	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
}
