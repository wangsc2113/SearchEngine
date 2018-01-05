package com.search_engine;

import java.nio.file.FileSystems;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.util.operator.Concatenate;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sun.org.apache.bcel.internal.generic.I2F;

public class Search {
	private static String INDEXDIR = "/Users/wangshicheng/Desktop/index";
	
	public static Result searchByPattern(String keyWord, String pattern) throws Exception{
	//public static void main(String[] args) throws Exception{
		Result result = new Result();
		Analyzer analyzer = null;
		Directory directory = null;
		DirectoryReader directoryReader = null;
		TopDocs topDocs = null;
		System.out.println("--------------");
		
		String prefixHTML = "<font color='red'>";
		String suffixHTML = "</font>";
		
		JSONObject jsonObj=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		
		
		//String keyWord = "记忆";
		//String pattern = "hot";
		
		try {
			analyzer = new IKAnalyzer(true);
			directory = FSDirectory.open(FileSystems.getDefault().getPath(INDEXDIR));
			directoryReader = DirectoryReader.open(directory);
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			String[] fields = {"name", "content"};
			QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
			Query query = parser.parse(keyWord);
			System.out.println("relevance");
			System.out.println(pattern.equals("relevance"));
			if (pattern.equals("relevance")) {
				//按相关度排序
				topDocs = indexSearcher.search(query, 100); 
			} else if (pattern.equals("time")) {
				//按时间排序
				topDocs = indexSearcher.search(query,100,new Sort(new SortedNumericSortField("longtime",SortField.Type.LONG,true)));
			} else if (pattern.equals("hot")) {
				//按热度排序
				topDocs = indexSearcher.search(query, 100, new Sort(new SortedNumericSortField("hotdegree", SortField.Type.DOUBLE,true)));
			}
			System.out.println("匹配" + keyWord + "，总共查询到" + topDocs.totalHits + "个文档");
			result.setTotal(topDocs.totalHits);
			
			ScoreDoc[] hits = topDocs.scoreDocs;
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(prefixHTML, suffixHTML);    
	        Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
	        
	        int pagesize = 10;
	        int curpage = 1;
	        int begin = (curpage - 1) * pagesize;
	        int end = Math.min(begin + pagesize, hits.length);
	        
			for (int i = begin; i < end; i++) {
				
				
				Document hitDoc = indexSearcher.doc(hits[i].doc);
				//2.高亮显示、摘要生成
				
				String hlt = highlighter.getBestFragment(analyzer, "content", hitDoc.get("content"));
				
				System.out.println(hitDoc.get("content"));				
				
				jsonObj.put("url", hitDoc.get("url"));  
				jsonObj.put("title", hitDoc.get("name"));  
				jsonObj.put("contents", hlt);
				jsonObj.put("add_time", hitDoc.get("time"));  
				jsonObj.put("up", hitDoc.get("up"));
				jsonObj.put("down", hitDoc.get("down"));
				
				
				//3.相似新闻查找
				
				int numDocs = indexReader.maxDoc();
				MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
				
				moreLikeThis.setMinTermFreq(2);
				moreLikeThis.setMinDocFreq(1);
				moreLikeThis.setFieldNames(new String[] {"name", "content"});
				moreLikeThis.setAnalyzer(analyzer);
				
				//System.out.println(Integer.parseInt(hitDoc.get("id")));
				//System.out.println();

				Query simlarquery = moreLikeThis.like(hits[i].doc);
				System.out.println(simlarquery);
				System.out.println();
				 
				TopDocs simlartopDocs = indexSearcher.search(simlarquery, 3);
				if (simlartopDocs.totalHits == 0) {
					//System.out.println("None like this");
				}
				else {
					JSONObject similarObj = new JSONObject();
					JSONArray similarArray = new JSONArray();
					for (int j = 1; j < simlartopDocs.scoreDocs.length; j++) {
						if (simlartopDocs.scoreDocs[j].doc != Integer.parseInt(hitDoc.get("id")) - 1) {
							Document doc = indexReader.document(simlartopDocs.scoreDocs[j].doc);
							similarObj.put("url", doc.getField("url").stringValue());
							similarObj.put("title", doc.getField("name").stringValue());
							similarArray.add(similarObj);
							}
						}
					jsonObj.put("similarNews", similarArray);
					jsonArray.add(jsonObj);
					}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (analyzer != null) analyzer.close();
				if (directoryReader != null) directoryReader.close();
				if (directory != null) directory.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result.setJsonarray(jsonArray);
		return result;
	}
}