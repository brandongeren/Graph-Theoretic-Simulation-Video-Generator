package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.Node;

public interface ConvergenceCriteria {
	
	// this method assumes that all of the nodes remain in the same place in the array after an iteration of the layout algorithm
	// also assumes that beforeLayout and afterLayout will be the same size (no new nodes are added)
	public float change();
	
	// returns a string saying what kind of convergence the object is
	public String getConvergenceType();
	
	// compute the distance between two points
	public float getDistance(float x1, float x2, float y1, float y2);
	
	// getters and setters
	public ArrayList<Node> getBeforeLayout();
	public void setBeforeLayout(ArrayList<Node> beforeLayout);
	public ArrayList<Node> getAfterLayout();
	public void setAfterLayout(ArrayList<Node> afterLayout);
	
	
}
