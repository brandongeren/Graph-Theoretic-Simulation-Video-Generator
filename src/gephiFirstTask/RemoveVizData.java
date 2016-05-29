package gephiFirstTask;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoveVizData {

	public static void main(String[] args) throws IOException {
		String filename = "Java";
		String fileExtension = ".gexf";
		Scanner sc = new Scanner(new File(filename + fileExtension));
		/**
		 * 
		 * 
		 * List<String> lines = Arrays.asList("The first line", "The second line");
		   Path file = Paths.get("the-file-name.txt");
		   Files.write(file, lines, Charset.forName("UTF-8"));
		   Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		 */
		ArrayList<String> lines = new ArrayList<String>();
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(!line.contains("<viz")) {
				lines.add(line);
			}
		}
		
		Path file = Paths.get(filename + "NoViz" + fileExtension);
		Files.write(file, lines, Charset.forName("UTF-8"));
		sc.close();
	}
}
