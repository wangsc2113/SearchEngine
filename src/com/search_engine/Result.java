package com.search_engine;

import java.util.ArrayList;

import com.sun.org.apache.regexp.internal.recompile;

import net.sf.json.JSONArray;

public class Result {

	private JSONArray jsonarray;
	private long total;
	private ArrayList queries;
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
	public ArrayList getQueries() {
		return queries;
	}
	public void setQueries(ArrayList queries) {
		this.queries = queries;
	}
}
