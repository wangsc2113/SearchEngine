package com.search_engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Main {

	public static void main(String[] args) throws Exception {
		 //CreateIndex createIndex = new CreateIndex();
		 //createIndex.create();
		 System.out.println("--------------------------");
		 
		 try {  
	            String key = "中国"; //查询关键字  
	            key = URLEncoder.encode(key, "gb2312");  
	            URL u = new URL("http://www.baidu.com.cn/s?wd=" + key + "&cl=3");  
	            URLConnection conn = u.openConnection();  
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gb2312"));  
	            String str = reader.readLine();  
	            while (str != null) {  
	                System.out.println(str);  
	                str = reader.readLine();  
	            }  
	  
	            reader.close();  
	        } catch (Exception ex) {  
	            ex.printStackTrace();  
	        }  
	}

}
