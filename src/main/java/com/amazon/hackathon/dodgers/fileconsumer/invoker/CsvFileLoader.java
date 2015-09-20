package com.amazon.hackathon.dodgers.fileconsumer.invoker;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.amazon.hackathon.dodgers.fileconsumer.CsvReader;

public class CsvFileLoader {

	private String propfilepath;
	
	@Autowired
	private CsvReader csvreader;
	
	@Autowired
	private RedisService redisservice;
	
	public CsvFileLoader() {
		
	}
	public CsvFileLoader (String propFile) {
		this.propfilepath=propFile;
	}

	public Map<String, Map<String, String>> loadcsv(HashMap<String, String> subscribers) {
		
		System.out.println("loadcsv" + subscribers.toString());
		
		Set<String> subs = subscribers.keySet();
		System.out.println(subscribers.get(subs.iterator().next().toString()));
		String csvfilepath = subscribers.get(subs.iterator().next().toString());
		File csvfile = new File(csvfilepath); 
		
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		
		try
	    {
			
			Class.forName("org.relique.jdbc.csv.CsvDriver");

			Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + csvfile.getParent());
			
			Properties prop = new Properties();
			
			prop.load(new FileInputStream(propfilepath));
			
			String csvQuery="";
			String condition;
			String[] filter;
			
			csvreader.loadToRedis(csvfilepath);
			System.out.println("CSV FILEPATH :: ");
			System.out.println(subs.toString());
		      
			for(String sub : subs) {
				
				condition = prop.getProperty(sub);
					filter = getvalue(condition);
					System.out.println(sub + " :: " + condition);
					
				String IDval;
				
			    if(condition.contains("ID")){
			    	result.put(sub, redisservice.getHashes(filter[filter.length-1]));
			    }else {
			    	csvQuery = "SELECT   ID FROM sample where  NAME ='"+filter[0]+"'" +" OR  PRODVALUE = '"+ filter[filter.length-1]+"'";
			    	System.out.println(" "+csvQuery);

			    	Statement st = conn.createStatement();
	  	    	   ResultSet idResults = st.executeQuery(csvQuery);
	  	    	 String identity = ""; 
		    	  while(idResults.next()){
		    		 // System.out.println(""+idResults.getString("ID"));
		    		  
		    		  System.out.println( idResults.getString("ID"));
		    		  identity = idResults.getString("ID");
		    		  result.put(sub, redisservice.getHashes(identity));
		    	  }
		    	  
			    }
			   conn.close();
			}
					

	      
	    }
		    catch(Exception e)
		    {
		      e.printStackTrace();
		    }
		return result;
		}

	private String[] getvalue(String condition) {

		System.out.println("Condition :: " + condition);
		String[] filter = new String[2];
		if(condition.contains("<"))
			filter[0] = condition.split("<")[0];
		else if(condition.contains(">"))
			filter[0] = condition.split(">")[0];
		else
			filter[0] = condition.split("=")[0];
		
		filter[1] = condition.split("=")[1];
		return filter;
	}
	private static ResultSet queryExcelVAL(String id, Connection conn) throws SQLException {

		System.out.println("query function");

		// Create a Statement object to execute the query with.
		// A Statement is not thread-safe.
		Statement stmt = conn.createStatement();
		ResultSet results = stmt.executeQuery("SELECT NAME,ID,PRODVALUE FROM sample where NAME = '" + id + "'");

		return results;
	}

	public  static HashMap<String, String> getExcelValues(ResultSet rs) throws ParseException, SQLException{
		 
		 
		  String str ="release date";
	      
	            
	      System.out.println("resultset print"+rs.toString());
	      
	      ResultSet queryResult = rs;
		 
		 while(queryResult.next()){
			 System.out.println(""+ rs.getString("NAME")  +  rs.getString("ID") + rs.getString("PRODVALUE"));
	   	  
	   	  }
	   	  
	return new HashMap<String,String>();
}

}
