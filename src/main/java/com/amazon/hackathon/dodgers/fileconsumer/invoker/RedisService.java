package com.amazon.hackathon.dodgers.fileconsumer.invoker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;

import redis.clients.jedis.Jedis;

public class RedisService {
	
	private Jedis jedis;
	
	public boolean setHashes(HashMap<String, Map<String, String>> value) {
		
		if(jedis == null)
			jedis = new Jedis("localhost");
		
		if(value == null || value.isEmpty())
			return false;
		System.out.println(value.toString());
		
		
		value.keySet().stream().forEach( key -> {
			System.out.println(value.get(key).toString());
			jedis.hmset(key.toString(), value.get(key));
		});
		jedis.save();
		
		return true;
	}
	
	public Map<String, String> getHashes(String item) {
		
		System.out.println("ITEM :: " + item);
		if(jedis == null)
			jedis = new Jedis("localhost");
		if(item == null || item.equals(""))
			return null;
		
//		return jedis.hmget(item, "tittle", "author", "releasedate", "listprice","publisher");
		Map<String, String> result = jedis.hgetAll(item);
		System.out.println(result.toString());
		return result;
	}
	
	public Map<String, String> adhocquery(Message<String> value) {
		
		String item = value.getPayload();
		if(item == null || item.equals(""))
			return null;
		System.out.println("ITEM :: " + item);
		if(jedis == null)
			jedis = new Jedis("localhost");
		
		return jedis.hgetAll(item);
	}
	
}
