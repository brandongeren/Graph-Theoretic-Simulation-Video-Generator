package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;

public class MaxDeltaConvergence extends AbstractConvergenceCriteria{

	public MaxDeltaConvergence(ArrayList<Node> beforeLayout, ArrayList<Node> afterLayout, GraphFactory factory) {
		super(beforeLayout, afterLayout, factory);
	}

	public MaxDeltaConvergence(GraphFactory factory) {
		this.beforeLayout = null;
		this.afterLayout = null;
		this.factory = factory;
	}
	
	@Override
	// compute the maximum change in coordinates from one layout to the next
	public float change() {
		int numNodes = beforeLayout.size();
		float distance = 0;
		float maxDistance = 0;

		for(int i = 0; i < numNodes; i++) {
			Node newNode = beforeLayout.get(i);
			distance = this.getDistance(beforeLayout.get(i).x(), afterLayout.get(i).x(), beforeLayout.get(i).y(), afterLayout.get(i).y());
			if(distance > maxDistance) {
				maxDistance = distance;
			}
		}
		return maxDistance;		
	}

	@Override
	public String getConvergenceType() {
		return "Max Delta Convergence";
	}

}
