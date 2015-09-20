/**
 * 
 */
package com.amazon.hackathon.dodgers.fileconsumer;

import java.io.File;
import java.util.Locale;

import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * @author Dodgers
 *
 */
public class FileHandler {
	
	
	@Transformer
	public Message<String> processthefile(Message<File> csvFile) {
		
		String filepath = csvFile.getPayload().getAbsolutePath();
		String filename = csvFile.getPayload().getName();

		System.out.println(" FilePath :: " + filepath);	
		
		final Message<String> message = MessageBuilder.withPayload(filepath)
				.setHeader(FileHeaders.FILENAME,      filename)
				.build();
		return message;
	}
	
}
