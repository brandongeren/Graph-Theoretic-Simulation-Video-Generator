package gephiFirstTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigReader {
	// GEXF file location
	// trace file location
	// output file location (movie file of some sort)
	// resolution (height and width)
	// primary layer for layout purposes
	// which property governs node size
	// amount of time per frame
	
	
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
	 delta: 2.0
	 
	 Comments: anything after ## will be treated as a comment
	 
	 */
	
	private String filename;
	private int height;
	private int width;
	private String graphFile;
	private String traceFile;
	private String outputFile;
	private String nodeSizeProperty;
	private String primaryLayer;
	private double timePerFrame;

	public ConfigReader(String filename) {
		this.filename = filename;
		
		//default values for some of the parameters
		this.width = 1920;
		this.height = 1080;
		this.timePerFrame = 1.0;
		String userHome = System.getProperty("user.home");
		String separator = System.getProperty("file.separator");
		this.outputFile = userHome + separator + "Movies" + separator + "movie" + "." + "mp4";
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
	
	public double getTimePerFrame() {
		return timePerFrame;
	}
	
	public void setTimePerFrame(double timePerFrame) {
		this.timePerFrame = timePerFrame;
	}

	public void readFromFile() throws FileNotFoundException {
		Scanner fileSc = new Scanner(new File(filename));
		while (fileSc.hasNextLine()) {
			String line = fileSc.nextLine();
			Scanner lineSc = new Scanner(line);
			lineSc.useDelimiter(": ");
			if (lineSc.hasNext()) {
				String tag = lineSc.next();
				tag.toLowerCase();
				String value = lineSc.next();
				value = removeComment(value);
				value = value.trim();
				switch (tag) {
					case "height": 
						this.setHeight(Integer.parseInt(value));
						break;
					case "width":
						this.setWidth(Integer.parseInt(value));
						break;
					case "graphFile": case "graph":
						this.setGraphFile(value);
						break;
					case "outputFile": case "output":
						this.setOutputFile(value);
						break;
					case "traceFile": case "trace":
						this.setTraceFile(value);
						break;
					case "nodeSizeProperty":
						this.setNodeSizeProperty(value);
						break;
					case "primaryLayer":
						this.setPrimaryLayer(value);
						break;
					case "delta": case "timePerFrame":
						this.setTimePerFrame(Double.parseDouble(value));
						break;
					default: break;
				}

			}
			lineSc.close();
		}

		fileSc.close();
	}
	
	public String toString() {
		String toString = "";
				
		String newline = "\n";
		toString += "File Name: " + filename + newline;
		toString += "Height: " + height + newline;
		toString += "Width: " + width + newline;
		toString += "Graph File Location: " + graphFile + newline;
		toString += "Trace File Location: " + traceFile + newline;
		toString += "Output File Location: " + outputFile + newline;
		toString += "Node Size Property:" + nodeSizeProperty + newline;
		toString += "Primary Layer: " + primaryLayer + newline;
		toString += "Time Per Frame: " + timePerFrame + newline;
		
		return toString;
	}
	
	public String removeComment(String in) {
		String comment = "##";
		int idx = in.indexOf(comment);
		if (idx != -1) {
			in = in.substring(0, idx);
		}
		
		return in;
	}
}
