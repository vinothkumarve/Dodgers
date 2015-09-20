package com.amazon.hackathon.dodgers.fileconsumer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.hackathon.dodgers.fileconsumer.invoker.RedisService;

@Service
public class CsvReader {
	
	@Autowired
	private RedisService redisservice;
	
public void loadToRedis(String filepath) {
	
	System.out.println("loadToRedis");
	
	Map<String, String> it = new HashMap<String, String>();
	HashMap<String, Map<String, String>> item = new HashMap<String, Map<String, String>>();
	
	List<String> lines = null;
	try {
		lines = FileUtils.readLines(new File(filepath));
	} catch (IOException e) {
		e.printStackTrace();
	}
	String pre_item = "";
	for(String line: lines) {
		System.out.println(line);
		String[] field = line.split(",");
		System.out.println("field :: " + field[0]);
		
		if(field[0].equalsIgnoreCase("ID")) {
			System.out.println("First Times");
			pre_item = "";
		}

		if(pre_item.equalsIgnoreCase(field[0]) || pre_item.equalsIgnoreCase("")) {
			System.out.println("IF PREV :: " + pre_item);
			it.put(field[1],field[2]);
		}
		else {
			System.out.println("Prev :: " + pre_item);
			System.out.println("IT :: " + it.toString());
			item.put(pre_item, it);
			if(redisservice.setHashes(item)){			
			it = new HashMap<String, String>();
			item = new HashMap<String, Map<String, String>>();
			}
		}
			if(!field[0].equalsIgnoreCase("ID"))
			pre_item= field[0];
	}
	
	item.put(pre_item, it);
	redisservice.setHashes(item);
}
}
