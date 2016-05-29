package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;

public class SuperEdge extends SuperGraphElementAbstract{
	
	private ArrayList<Edge> edges;
	
	public SuperEdge(String id) {
		super(id);
		this.edges = new ArrayList<Edge>();
	}

	public SuperEdge(String id, GraphFactory gf, ArrayList<Edge> edges) {
		super(id, gf);
		this.edges = edges;
	}
	
	public SuperEdge(String id, ArrayList<Edge> edges) {
		super(id);
		this.edges = edges;
	}

	public SuperEdge(String id, GraphFactory gf) {
		super(id, gf);
		this.edges = new ArrayList<Edge>();
	}
	
	@Override
	public int getWeight() {
		return this.edges.size();
	}
	
	public Edge toEdge(Node source, Node dest) {
		Edge asEdge = this.gf.newEdge(source, dest);
		asEdge.getTable().addColumn(weight, Integer.class);
		asEdge.setAttribute(weight, this.getWeight());
		return asEdge;
	}

	
	
	

}
