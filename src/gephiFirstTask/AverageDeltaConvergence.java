package gephiFirstTask;

import java.util.ArrayList;

import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;

public class AverageDeltaConvergence extends AbstractConvergenceCriteria{
	
	public AverageDeltaConvergence(ArrayList<Node> beforeLayout, ArrayList<Node> afterLayout, GraphFactory factory) {
		super(beforeLayout, afterLayout, factory);
	}
	
	public AverageDeltaConvergence(GraphFactory factory) {
		this.beforeLayout = null;
		this.afterLayout = null;
		this.factory = factory;
	}

	@Override
	// computes the average change in x and y between the two layouts
	public float change() {
		int numNodes = beforeLayout.size();
		float distance = 0;
		float totalDistance = 0;
		
		for(int i = 0; i < numNodes; i++) {						
			distance = this.getDistance(beforeLayout.get(i).x(), afterLayout.get(i).x(), beforeLayout.get(i).y(), afterLayout.get(i).y());
			totalDistance += distance;
		}
		System.out.println(totalDistance / numNodes);
		return totalDistance / numNodes;
	}

	@Override
	public String getConvergenceType() {
		return "Average Delta Convergence";
	}

}
