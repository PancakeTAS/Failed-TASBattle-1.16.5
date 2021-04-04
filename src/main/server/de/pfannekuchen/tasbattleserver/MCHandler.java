package de.pfannekuchen.tasbattleserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

public class MCHandler {

	public MCHandler(File server, File copy) throws IOException {
		if (server.exists()) FileUtils.deleteDirectory(server); /* Delete existing server */
		FileUtils.copyDirectory(copy, server);
		System.out.println("Server installed");
		ProcessBuilder run = new ProcessBuilder("java -jar paper.jar nogui".split(" "));
		run.directory(server);
		Process p = run.start();
		
		/* Wait until server is done */
		BufferedReader stream = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = stream.readLine();
		while (p.isAlive()) {
			if (line.contains("Done")) {
				System.out.println(line);
				break;
			}
			line = stream.readLine();
		}
		
	}
	
}
