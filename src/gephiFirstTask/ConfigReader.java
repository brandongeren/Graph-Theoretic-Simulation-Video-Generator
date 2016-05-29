package gephiFirstTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigReader {
	// GEXF file location
	// output file location (movie file of some sort)
	// resolution
	// primary layer for layout purposes
	// which property governs node size
	
	
	/** 
	 * Config file syntax
	 (in any order) 
	 (caps on tags do not matter) (tags are things like input, output, etc)
	 graphFile: projection.gexf
	 traceFile: /Users/brandongeren/Documents/trace.txt
	 outputFile: /Users/brandongeren/Movies/movie.mp4
	 height: 1080
	 width: 1920
	 nodeSizeProperty: HCV:1
	 primaryLayer: Sex
	 */
	
	private String filename;
	private int height;
	private int width;
	private String graphFile;
	private String traceFile;
	private String outputFile;
	private String nodeSizeProperty;
	private String primaryLayer;

	public ConfigReader(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getGraphFile() {
		return graphFile;
	}

	public String getTraceFile() {
		return traceFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public String getNodeSizeProperty() {
		return nodeSizeProperty;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setGraphFile(String graphFile) {
		this.graphFile = graphFile;
	}

	public void setTraceFile(String traceFile) {
		this.traceFile = traceFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setNodeSizeProperty(String nodeSizeProperty) {
		this.nodeSizeProperty = nodeSizeProperty;
	}

	public String getPrimaryLayer() {
		return primaryLayer;
	}

	public void setPrimaryLayer(String primaryLayer) {
		this.primaryLayer = primaryLayer;
	}

	public void readFromFile(String filename) throws FileNotFoundException {
		Scanner fileSc = new Scanner(new File(filename));
		while (fileSc.hasNextLine()) {
			String line = fileSc.nextLine();
			Scanner lineSc = new Scanner(line);
			lineSc.useDelimiter(": ");
			String tag = lineSc.next();
			tag.toLowerCase();
			System.out.println(tag);
			switch (tag) {
				case "height": 
					this.setHeight(lineSc.nextInt());
					break;
				case "width":
					this.setWidth(lineSc.nextInt());
					break;
				case "graphFile":
					this.setGraphFile(lineSc.next());
					break;
				case "outputFile":
					this.setOutputFile(lineSc.next());
					break;
				case "traceFile":
					this.setTraceFile(lineSc.next());
					break;
				case "nodeSizeProperty":
					this.setNodeSizeProperty(lineSc.next());
					break;
				case "primaryLayer":
					this.setPrimaryLayer(lineSc.next());
					break;
				default: break;
			}
			lineSc.close();
		}

		fileSc.close();
	}
	
	// use switch statements to check for each of these properties
}
