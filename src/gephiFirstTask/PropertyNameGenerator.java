package gephiFirstTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.gephi.graph.api.Node;

public class PropertyNameGenerator {
	private Node node;
	private ArrayList<String> propertySet;
	private String delimiter;
	
	public PropertyNameGenerator(Node node, Collection<String> propertySet, String delimiter) {
		this.node = node;
		HashSet<String> setOfProperties = new HashSet<String>(propertySet.size());
		this.delimiter = delimiter;
		
		//  check the integrity of the data
		for (String property: propertySet) {
			
			if (property.contains(delimiter)) {
				throw new RuntimeException("Property name may not contain the string \"" + delimiter + "\".");
			}
			
			if (!setOfProperties.add(property)) {
				throw new RuntimeException("The propertySet provided contained duplicate elements. The string \"" + property + "\" appeared more than once.");
			}
		}
		this.propertySet = new ArrayList<String>(propertySet);
	}
	
	//  create a PropertyNameGenerator instance with the default delimiter of " && "
	public PropertyNameGenerator(Node node, Collection<String> propertySet) {
		this(node, propertySet, " && ");
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public ArrayList<String> getPropertySet() {
		return propertySet;
	}

	public void setPropertySet(Collection<String> propertySet) {
		this.propertySet = new ArrayList<String>(propertySet);
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public String combinedPropertyName() {
		return combineStrings(true);
	}
	
	public String combinedPropertyValue() {
		return combineStrings(false);
	}
	
	// use the delimiter to generate a new string which represents the name of a new node
	private String combineStrings(boolean isName) {
		String combinedString = "";
		int propertySetSize = propertySet.size();
		for (int i = 0; i < propertySetSize - 1; i++) {
			String currentProp = propertySet.get(i);
			if (isName) combinedString += currentProp;
			
			else combinedString += node.getAttribute(currentProp).toString();

			combinedString += delimiter;
		}
		
		String lastProp = propertySet.get(propertySetSize - 1);
		if (isName) combinedString += lastProp;
		
		else combinedString += node.getAttribute(lastProp).toString();
		
		return combinedString;
	}
}
