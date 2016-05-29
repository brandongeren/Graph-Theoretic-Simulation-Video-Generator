package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;

public abstract class AbstractConvergenceCriteria implements ConvergenceCriteria{
	public ArrayList<Node> beforeLayout;
	public ArrayList<Node> afterLayout;
	public GraphFactory factory;
	
	
	public AbstractConvergenceCriteria(ArrayList<Node> beforeLayout, ArrayList<Node> afterLayout, GraphFactory factory) {
		this.beforeLayout = beforeLayout;
		this.afterLayout = afterLayout;
		this.factory = factory;
	}
	
	public AbstractConvergenceCriteria() {
		beforeLayout = null;
		afterLayout = null;
	}

	// compute the distance between two points
	/** TODO: check if casting from double to float can cause problems **/
	public float getDistance(float x1, float x2, float y1, float y2) {
		return (float) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}

	public ArrayList<Node> getBeforeLayout() {
		return beforeLayout;
	}

	public void setBeforeLayout(ArrayList<Node> beforeLayout) {
		this.beforeLayout = beforeLayout;
	}

	public ArrayList<Node> getAfterLayout() {
		return afterLayout;
	}

	public void setAfterLayout(ArrayList<Node> afterLayout) {
		this.afterLayout = afterLayout;
	}
	

}
