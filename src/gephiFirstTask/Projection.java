package gephiFirstTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.gephi.graph.api.Column;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.Table;

public class Projection {	
	private PropertySet ps;
	private GraphMaker gm;
	private final String WEIGHT = "weight";
	private final String DEFAULT_DELIMITER = " && ";
	private final String LABEL = "label";
	
	
	// TODO: add constructors which include delimiters
	public Projection(PropertySet ps, GraphMaker gm) {
		this.ps = ps;
		this.gm = gm;
	}
	
	
	public Projection(Collection<String> properties, String name, GraphMaker gm) {
		this(new PropertySet(name, properties), gm);
	}

	private Projection() {
		this.ps = null;
		this.gm = null;
	}
	
	public Projection(Collection<String> properties, GraphMaker gm) {
		this();
		DirectedGraph graph = gm.getGraph();
		ArrayList<Node> nodes = new ArrayList<Node>(graph.getNodes().toCollection());
		LinkedHashSet<String> values = new LinkedHashSet<String>();
		// TODO: add integrity check to see if there are actually nodes in this graph
		Node initNode = null;
		
		PropertyNameGenerator png = new PropertyNameGenerator(initNode, properties);
		
		for (Node node: nodes) {
			values.add(getValuesSetNode(png, node));
		}
		
		PropertySet ps = new PropertySet(properties, values);
		this.setPS(ps);
		this.setGM(gm);
	}
	
	public PropertySet getPS() {
		return ps;
	}
	
	public void setPS(PropertySet ps) {
		this.ps = ps;
	}
	
	public GraphMaker getGM() {
		return gm;
	}
	
	public void setGM(GraphMaker gm) {
		this.gm = gm;
	}

	public GraphMaker generateGraph(String delimiter) {
		GraphMaker newGM = new GraphMaker();		
		
		// TODO: make an adjacency matrix for edges between different bins in this propertySet
		
		LinkedHashSet<String> valuesSet = ps.getUsedValues();
		ArrayList<String> values = new ArrayList<String>(valuesSet);
		
		
		int valueSize = values.size();
		
		// adjMatrix is an adjacency matrix for the edges of the new graph
		// if unfamiliar with the concept of adjacency matrix, look it up. it is a common graph theory idea.
		// the first index represents which new node an edge comes from, the second index represents where it goes to
		// the int value in the adjMatrix represents how many times an edge of this type occurs
		int[][] adjMatrix = new int[valueSize][valueSize];
		DirectedGraph oldGraph = gm.getGraph();
		ArrayList<Node> oldNodes = new ArrayList<Node>(oldGraph.getNodes().toCollection());

		// make nodes out of the value strings, then give them a weight column and initialize it to 0
		int i = 1;
		for (String id: values) {
			newGM.addNode(id);
			Node newNode = newGM.getGraph().getNode(id);
			
			//only add the column if it's the first run through. otherwise, addColumn throws an error
			if (i == 1) {
				newNode.getTable().addColumn(WEIGHT, Integer.class);
				i++;
			}
			newNode.setAttribute(WEIGHT, 0);
			newNode.setAttribute(LABEL, id);
		}

		
		PropertyNameGenerator png = new PropertyNameGenerator(null, this.ps.getNames(), delimiter);
		DirectedGraph newGraph = newGM.getGraph();
		
		// make sure the weights of the new nodes are correct
		Node currentNode = null;
		for (Node oldNode: oldNodes) {
			String nodeKey = getValuesSetNode(png, oldNode);
			currentNode = newGraph.getNode(nodeKey);
			incrementNodeWeight(currentNode);
		}
		
		ArrayList<Edge> oldEdges = new ArrayList<Edge>(oldGraph.getEdges().toCollection());
						
		// increment the weights in the adjacency matrix
		for (Edge edge: oldEdges) {
			Node source = edge.getSource();
			Node target = edge.getTarget();
			String sourceName = getValuesSetNode(png, source);
			String targetName = getValuesSetNode(png, target);
			int sourceNameIdx = values.indexOf(sourceName);
			int targetNameIdx = values.indexOf(targetName);
			adjMatrix[sourceNameIdx][targetNameIdx]++;
		}
		
		// create the edges from the adjacency matrix
		for (int srcIdx = 0; srcIdx < valueSize; srcIdx++) {
			for (int tgtIdx = 0; tgtIdx < valueSize; tgtIdx++) {
				int adjacentWeight = adjMatrix[srcIdx][tgtIdx];
				// the edge only exists if the adjMatrix entry is greater than 0
				// only make an edge if it exists
				if (adjacentWeight > 0) {
					Node newSource = newGraph.getNode(values.get(srcIdx));
					Node newTarget = newGraph.getNode(values.get(tgtIdx));
					Edge newEdge = newGM.addEdge(newSource, newTarget);
					newEdge.setAttribute(WEIGHT, (double) adjacentWeight);
					newGM.addEdge(newEdge);
				}
			}
		}				
		return newGM;
	}
	
	private String getValuesSetNode(PropertyNameGenerator png, Node node) {
		png.setNode(node);
		return png.combinedPropertyValue();
	}
	
	public GraphMaker generateGraph() {
		return generateGraph(DEFAULT_DELIMITER);
	}
	
	// increase the weight of this node by 1
	public void incrementNodeWeight(Node node) {
		Integer weight = (int) node.getAttribute(WEIGHT);
		weight++;
		node.setAttribute(WEIGHT, weight);
	}
}
