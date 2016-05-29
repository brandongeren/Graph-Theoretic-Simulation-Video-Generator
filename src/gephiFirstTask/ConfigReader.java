package gephiFirstTask;

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
	 */
	
	private String filename;
	private int height;
	private int width;
	private String graphFile;
	private String traceFile;
	private String outputFile;
	private String nodeSizeProperty;

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
	
	
	// use switch statements to check for each of these properties
}
