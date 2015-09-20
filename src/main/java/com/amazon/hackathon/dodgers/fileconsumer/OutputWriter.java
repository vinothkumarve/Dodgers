package com.amazon.hackathon.dodgers.fileconsumer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.classmate.util.ResolvedTypeCache.Key;

public class OutputWriter {
	

	@Value("${outputDir}")
	public String outputDir;
	public String writeResult(HashMap<String, String> result) {
		
		System.out.println(result.toString());
		String filename = outputDir + File.separator + "console.txt";
		File outdir = new File(outputDir);
		List<String> output = new ArrayList<String>();
		result.entrySet().stream().forEach(key -> {
			output.add(key.getKey() + " : " + result.get(key));
		});
		if(!outdir.exists())
			outdir.mkdirs();
		File file = new File(filename);
		try {
			FileUtils.writeLines(file, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return filename;
		
	}

}
