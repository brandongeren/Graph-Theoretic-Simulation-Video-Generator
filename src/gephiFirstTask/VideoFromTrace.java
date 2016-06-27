package gephiFirstTask;

import java.io.File;
import java.io.IOException;

import org.jcodec.api.awt.SequenceEncoder;

public class VideoFromTrace {
	
	public static void main(String[] args) throws IOException {
		// args[0] is the location of the config file
		ConfigReader cr = new ConfigReader(args[0]);
		// ConfigReader cr = new ConfigReader("config.txt");
		cr.readFromFile();
		GraphMaker gm = new GraphMaker();
		String graphFile = cr.getGraphFile();
		SequenceEncoder encoder = new SequenceEncoder(new File(cr.getOutputFile()));

		TraceReader tr = new TraceReader(encoder, cr, gm);
		tr.readTraceFile();
				
		
		encoder.finish();
		System.out.println("Video saved to " + cr.getOutputFile());
	}
}
