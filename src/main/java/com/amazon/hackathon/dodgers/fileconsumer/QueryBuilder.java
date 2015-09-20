package com.amazon.hackathon.dodgers.fileconsumer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;

public class QueryBuilder {
	
	@Transformer
	public Message<String> buildquery(Message<File> queryfile) {
		
		String query = null;
		String[] item = null;
		try {
			query = FileUtils.readFileToString(queryfile.getPayload());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(" QUERY :: " + query);
		item = StringUtils.delimitedListToStringArray(query, " ");
		System.out.println(item[1]);
		
		final Message<String> message = MessageBuilder.withPayload(item[1])
				.setHeader("PROPERTIES",      item[1])
				.build();
		return message;
	}

}
