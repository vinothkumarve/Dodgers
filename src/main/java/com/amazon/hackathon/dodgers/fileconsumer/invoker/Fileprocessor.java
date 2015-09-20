package com.amazon.hackathon.dodgers.fileconsumer.invoker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Fileprocessor {
	
//	@Value("${subscribersList}")
	private String subscribers;
	private String propFilePath;
	
	public Fileprocessor() {
		
	}
	public Fileprocessor(String propFilePath) {
		
		this.propFilePath = propFilePath;
	}
	
	public Map<String, String> getsubscribers(Message<String> scsvpath) {
		
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(propFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		subscribers = prop.getProperty("subscribersList");
		System.out.println(subscribers);
		
		Map<String, String> subscriberMap = new HashMap<String, String>();
		String filepath = scsvpath.getPayload().toString();
		Arrays.asList(subscribers.split(",")).stream().forEach(
				s -> subscriberMap.put(s, filepath));
		
		System.out.println(filepath);
		
		System.out.println("SubscriberMap :: " + subscribers.toString());
		return subscriberMap;
	}

}
