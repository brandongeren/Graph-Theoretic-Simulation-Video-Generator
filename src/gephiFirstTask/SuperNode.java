package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.Column;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;
import org.gephi.graph.impl.ColumnStore;
import org.gephi.graph.impl.GraphStore;
import org.gephi.graph.impl.NodeImpl;

public class SuperNode extends SuperGraphElementAbstract {
	private ArrayList<Node> nodes;	

	public SuperNode(String id) {
		super(id);
		this.nodes = new ArrayList<Node>();
	}

	public SuperNode(String id, GraphFactory gf, ArrayList<Node> nodes) {
		super(id, gf);
		this.nodes = nodes;
	}
	
	public SuperNode(String id, ArrayList<Node> nodes) {
		super(id);
		this.nodes = nodes;
	}

	public SuperNode(String id, GraphFactory gf) {
		super(id, gf);
		this.nodes = new ArrayList<Node>();
	}

	
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public int getWeight() {
		return this.nodes.size();
	}
	
	public Node toNode() {
		Node asNode = gf.newNode(this.id);
		asNode.getTable().addColumn(weight, Integer.class);
		asNode.setAttribute(weight, this.getWeight());
		return asNode;
	}
	
	
}
