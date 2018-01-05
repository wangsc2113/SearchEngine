package com.search_engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Main {

	public static void main(String[] args) throws Exception {
		 CreateIndex createIndex = new CreateIndex();
		 createIndex.create();
		 System.out.println("--------------------------");
		 
	}
}