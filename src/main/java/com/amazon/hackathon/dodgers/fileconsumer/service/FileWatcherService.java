package com.amazon.hackathon.dodgers.fileconsumer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;

@EnableAutoConfiguration
@SpringApplicationConfiguration
@SpringBootApplication
//@ContextConfiguration(locations="file:src/main/resources/context.xml", inheritLocations=true)
@ImportResource({"classpath:context.xml"})
public class FileWatcherService {

	public static void main(String[] args) {
		SpringApplication.run(FileWatcherService.class, args);
	}

}
