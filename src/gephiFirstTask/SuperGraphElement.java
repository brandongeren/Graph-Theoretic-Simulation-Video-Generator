package gephiFirstTask;

import org.gephi.graph.api.GraphFactory;

public interface SuperGraphElement {
	public String getId();

	public void setId(String id);

	public GraphFactory getGf();

	public void setGf(GraphFactory gf);
	
	public int getWeight();

}
