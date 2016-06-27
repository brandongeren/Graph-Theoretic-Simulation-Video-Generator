package gephiFirstTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FixTraceFile {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("/Users/brandongeren/Documents/ElasticSearchandGephiInterface/GephiBeginnings/trace.txt"));
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			Scanner lineSc = new Scanner(line);
			if (lineSc.hasNextInt()) {
//				System.out.println(lineSc.nextInt());
//				System.out.println(lineSc.next());
				int time = lineSc.nextInt();
				String next = lineSc.next();
				String restOfLine = line.substring(line.indexOf(next));
				line = time + ".0: " + restOfLine;
			}
			
			lineSc.close();
			System.out.println(line);
			
		}
		scan.close();
	}
}
