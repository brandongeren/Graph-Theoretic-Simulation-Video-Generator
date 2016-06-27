package gephiFirstTask;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jcodec.api.awt.SequenceEncoder;


public class TraceReader {
	private SequenceEncoder encoder;
	private GraphMaker gm;
	private ConfigReader cr;
	private double videoTime = 0;
	private double traceTime = 0;
	private int frameCounter = 0;
	
	public TraceReader(SequenceEncoder encoder, ConfigReader cr, GraphMaker gm) {
		this.encoder = encoder;
		this.cr = cr;
		this.gm = gm;
	}
	
	public GraphMaker getGraphMaker() {
		return gm;
	}

	public void setGraphMaker(GraphMaker gm) {
		this.gm = gm;
	}

	public SequenceEncoder getEncoder() {
		return encoder;
	}
	
	public void setEncoder(SequenceEncoder encoder) {
		this.encoder = encoder;
	}
		
	public ConfigReader getCr() {
		return cr;
	}

	public void setCr(ConfigReader cr) {
		this.cr = cr;
	}

	public void readTraceFile() throws IOException {
		// make a scanner to read every line of the trace file
		Scanner traceSc = new Scanner(new File(cr.getTraceFile()));
				
		// read the trace file line by line, doing exactly as the trace file says
		while (traceSc.hasNextLine()) {
			// System.out.println(lineCounter);
			String line = traceSc.nextLine();
			Scanner lineSc = new Scanner(line);
			// System.out.println(line);
			lineSc.useDelimiter(": ");
			
			
			if (lineSc.hasNext()) {
				traceTime = lineSc.nextDouble();
				
				// use a while loop to check the trace time. if it's too big, it belongs at a later point in the video
				// also encodes frames into the video
				checkTime();
				
				
				/** parse the other stuff that's going on **/
				String query = lineSc.next();
				long t1 = System.currentTimeMillis();
				parseQuery(query);
				long t2 = System.currentTimeMillis();
				// System.out.println("Parse Query: " + (t2 - t1));

				
				
				
				
			}
			lineSc.close();
		
		}
		gm.encodeFrame(encoder);
		gm.exportPNG("test", 1080, 1920);
		gm.exportGraphFile("testingBS.gexf");
		traceSc.close();
	}
	
	// encode frames if the time is correct
	public void checkTime() throws IOException {
		double timePerFrame = cr.getTimePerFrame();
		// System.out.println("Video time: " + videoTime);
		// System.out.println("Trace time: " + traceTime);
		// System.out.println("Frame counter: " + frameCounter);
		while (videoTime + timePerFrame < traceTime) {
			if (videoTime < timePerFrame) {
				String workaround = "workaroundGraph.gexf";
				gm.exportGraphFile(workaround);
				gm = new GraphMaker();
				gm.importGraph(workaround);
				
				
				gm.forceAtlas(50);
			}
			frameCounter++;
			
			long t1 = System.currentTimeMillis();
			gm.forceAtlas(1);
			long t2 = System.currentTimeMillis();
			// System.out.println("Yifan Hu: " + (t2 - t1));
			long t3 = System.currentTimeMillis();
			gm.encodeFrame(encoder);
			// gm.exportGraphFile(String.format("graph/frame%05d.gexf", frameCounter));
			gm.exportPNG(String.format("video/frame%05d", frameCounter), 1080, 1920);
			long t4 = System.currentTimeMillis();
			// System.out.println("Encode frame: " + (t4 - t3));
			videoTime += timePerFrame;
		}
	
	}
	
	
	// read the query string and perform the proper action
	public void parseQuery(String query) {
		Scanner sc = new Scanner(query);
		
		if (sc.hasNext()) {
			String action = sc.next();
			action.toLowerCase();
			String target = sc.next();
			target.toLowerCase();
			switch (action) {
				case "add":			
					switch (target) {
						case "edge":
							// add edge edgeLayerName sourceLabel targetLabel
							String edgeLayerName = sc.next();
							String sourceLabel = sc.next();
							String targetLabel = sc.next();
							
							/** TODO: include the edgeLayer in this **/
							if (gm.getGraph().hasNode(sourceLabel) && gm.getGraph().hasNode(targetLabel)) {
								gm.addEdge(sourceLabel, targetLabel);
							}
							
							break;
						case "node":
							// add node nodeLabel
							String nodeLabel = sc.next();
							gm.addNode(nodeLabel);
							break;
					}
					break;
				case "delete": case "remove":
					switch (target) {
						case "edge":
							// remove edge sourceLabel targetLabel
							String sourceLabel = sc.next();
							String targetLabel = sc.next();
							if (gm.getGraph().hasNode(sourceLabel) && gm.getGraph().hasNode(targetLabel)) {
								gm.removeEdge(sourceLabel, targetLabel);
							}
							break;
						case "node":
							// remove node nodeLabel
							String nodeLabel = sc.next();
							gm.removeNode(nodeLabel);
							break;
					}
					break;
				// alter node nodeID propertyName newValue
				case "alter": case "change": case "modify":
					// if alter edge is added as a command, then a switch statement on the target will be needed
					String nodeLabel = sc.next();
					String propertyName = sc.next();					
					
					break;
				default:
					sc.close();
					throw new RuntimeException("Trace file syntax error: \"" + action + "\" is not an acceptable query");
			}
		}
		sc.close();
	}
	
}
