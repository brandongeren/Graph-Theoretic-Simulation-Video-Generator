package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;

public abstract class SuperGraphElementAbstract implements SuperGraphElement{
	protected String id; 
	protected GraphFactory gf;
	protected final String weight = "Weight";

	
	public SuperGraphElementAbstract(String id, GraphFactory gf) {
		this.id = id;
		this.gf = gf;
	}
	
	public SuperGraphElementAbstract(String id) {
		this.id = id;
		this.gf = null;
	}


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GraphFactory getGf() {
		return this.gf;
	}

	public void setGf(GraphFactory gf) {
		this.gf = gf;
	}
	
	public int getWeight() {
		return 0;
	}
	
}
