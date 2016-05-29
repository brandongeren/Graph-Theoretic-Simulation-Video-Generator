package gephiFirstTask;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

import org.gephi.graph.api.Column;
import org.gephi.graph.api.ColumnIterable;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDirectionDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class Playground {
	public static void main(String[] args) throws IOException {
	
		GraphMaker gm = new GraphMaker();
		gm.importGraph("lookAtMe.gexf");
		
		HashSet<String> propertySet = new HashSet<String>();
		propertySet.add("degree");
		propertySet.add("outdegree");

		
		Projection firstProj = new Projection(propertySet, gm);
		GraphMaker newGM = firstProj.generateGraph();
		newGM.forceAtlas(100);
		newGM.yifanHu(1);
		newGM.initPreviewModel(true, new Color(0x69D2E7), 0.2f);
		newGM.exportGraphFile("projection.gexf");
		
		
		
		
		
	}
}
