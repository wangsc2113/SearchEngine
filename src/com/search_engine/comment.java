package com.search_engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.map.HashedMap;

import com.sun.javafx.collections.MappingChange.Map;

public class comment {	
	
	public static HashMap commentMap() throws Exception {
	//public static void main(String[] args) {
		
		File file = new File("/Users/wangshicheng/Desktop/comments.txt");
		BufferedReader bufferedReader = null;
		HashMap hashmap = new HashMap();
		
		try{
			bufferedReader = new BufferedReader(new FileReader(file));
			String string = null;
			int line = 1;
			while((string = bufferedReader.readLine()) != null){
				String[] result = string.split("\t");
				List<Integer> list = new ArrayList<Integer>();				
				list.add(new Integer(result[1]));
				list.add(new Integer(result[2]));
				hashmap.put(new Integer(result[0]), list);
				line++;
			}
			bufferedReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (Exception e2) {
					
				}
			}
		}
		return hashmap;
	}
	
}
