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
		 //CreateIndex createIndex = new CreateIndex();
		 //createIndex.create();
		 System.out.println("--------------------------");
		 
		 String query = "中国";
		 String keyword = URLEncoder.encode(query, "utf-8");
		 String urlStr = "http://api.bing.com/osjson.aspx?query=" + keyword;
		 
		 URL url = new URL(urlStr);
		 URLConnection urlConnection = url.openConnection();
		 urlConnection.connect();
		 
		 ArrayList arrayList = new ArrayList();
		 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));		 
		 String line = bufferedReader.readLine();
		 System.out.println(line);
		 String[] s = line.split(",");
		 for (int i = 1 ; i < s.length; i++) {
			 System.out.println(s[i]);
		 }
	}
}