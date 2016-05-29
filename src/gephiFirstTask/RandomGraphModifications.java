package gephiFirstTask;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.gephi.graph.api.Node;
import org.jcodec.api.awt.SequenceEncoder;

public class RandomGraphModifications {
	
	
	public static void main(String[] args) throws IOException {
		// this program could use some refining
		// refine the definition of neighbor so that it only involves outward edges

		// green commented out lines are part of a less efficient version of this code which involves more i/o
		/** blue commented out lines are optional changes **/
		int numModifications = 60;
		GraphMaker graphMaker = new GraphMaker();
		graphMaker.importGraph("JavaNoViz.gexf");
		graphMaker.forceAtlas(1);
		// int modificationDigits = GraphMaker.numDigits(numModifications);
		String userHome = System.getProperty("user.home");
		String separator = System.getProperty("file.separator");
		SequenceEncoder encoder = new SequenceEncoder(new File(userHome + separator + "Movies" + separator + "modifications" + "." + "mp4"));

		for(int i = 0; i < numModifications; i++) {
			Node randNode = graphMaker.pickRandomNode();
			graphMaker.removeRandomEdge(randNode);
			graphMaker.addEdge(graphMaker.pickRandomEdge(randNode));
			graphMaker.forceAtlas(1);
			graphMaker.filterByDegree(1);
			graphMaker.rankColorByDegree();
			/** graphMaker.rankColorByDegree(new Color(0xFFFFF), new Color(0xFFFFFF)); **/
			graphMaker.initPreviewModel(false, Color.ORANGE, new Float(0.1f)); //  set the boolean to true if you want node labels
			// graphMaker.exportPNG(String.format("data/random%0" + modificationDigits + "d", i), 1600, 2560);
			graphMaker.encodeFrame(encoder);
		}
		
	    // make an mp4 out of the png files made
	    // GraphMaker.makeMovie("modifications", "mp4", "data", "random", "png", 60);
		encoder.finish();
	    System.out.println("Video saved to user's Movies directory");
	}
	
}
