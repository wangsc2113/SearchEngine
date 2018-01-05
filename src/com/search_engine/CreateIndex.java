package com.search_engine;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.*;
import java.nio.file.FileSystems;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.lucene.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.queryparser.surround.query.AndQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.graphics.predictor.Up;
import org.apache.poi.util.IntegerField;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.Zephyr;

import sun.util.logging.resources.logging;

public class CreateIndex {
	private static String INDEXDIR = "/Users/wangshicheng/Desktop/index";
	
	public static String numberIntercept(String number) {
		return Pattern.compile("[^0-9]").matcher(number).replaceAll("");
	}
	
	public static void create() {
		DataBase dataBase = new DataBase();
		Analyzer analyzer = null;
		Directory directory  = null;
		IndexWriter indexWriter = null;
		
		try {
			analyzer = new IKAnalyzer(true);
			directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEXDIR));
			IndexWriterConfig indexWriterConfig  = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			indexWriter.deleteAll();
			
			Connection connection = dataBase.getCon();
			String sql="select * from ir_news_2";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			
			comment comm = new comment();
			HashMap hashMap = comm.commentMap();
						
			while(rs.next()){
				Document document = new Document();
				int id = rs.getInt("id");
				String time = rs.getString("time");
				int up = 0;
				int down = 0;
				
				document.add(new Field("id", id + "", TextField.TYPE_STORED));
				document.add(new Field("url", rs.getString("url"), TextField.TYPE_STORED));
				document.add(new Field("name", rs.getString("name"), TextField.TYPE_STORED));
				document.add(new Field("time", time, TextField.TYPE_STORED));
				document.add(new NumericDocValuesField("longtime", Long.parseLong(numberIntercept(rs.getString("time")))));
				document.add(new StoredField("longtime", Long.parseLong(numberIntercept(rs.getString("time")))));
				
				if (hashMap.containsKey(id)) {
					ArrayList arrayList = new ArrayList();
					arrayList = (ArrayList) hashMap.get(id);
					up = (int) arrayList.get(0);
					down = (int) arrayList.get(1);
					document.add(new Field("up", String.valueOf(up), TextField.TYPE_STORED));
					document.add(new Field("down", String.valueOf(down), TextField.TYPE_STORED));
				}
				else {
					document.add(new Field("up", "0", TextField.TYPE_STORED));
					document.add(new Field("down", "0", TextField.TYPE_STORED));
				}
				
				double hotdegree = reddit(up, down, time);
				document.add(new DoubleDocValuesField("hotdegree", hotdegree));
				document.add(new StoredField("hotdegree", hotdegree));
				//document.add(new Field("author", rs.getString("author"), TextField.TYPE_STORED));
				document.add(new Field("source", rs.getString("source"), TextField.TYPE_STORED));
				document.add(new Field("content", rs.getString("content"), TextField.TYPE_STORED));
				indexWriter.addDocument(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (analyzer != null) analyzer.close();
				if (indexWriter != null) indexWriter.close();
				if (directory != null) directory.close();
			} catch (Exception e) {
			e.printStackTrace();}
		}	
	}
	
	public static double reddit(int up, int down, String time) throws ParseException {
		double result = 0;
		int z = 0;
		String from = "2005-12-8 7:46:43";
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long totime = simpleFormat.parse(time).getTime();
		long fromtime = simpleFormat.parse(from).getTime();
		long seconds = (long) ((totime - fromtime) / 1000);
		
		if (up - down == 0){
			z = 1;
		}
		else {
			z = Math.abs(up - down);
		}

		result = Math.log10(z) + seconds / 45000;
		return result;
	}
	
}
