package gephiFirstTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class PropertySet {
	// TODO: change usedValues to a collection
	// TODO: come up with better names than name and names, as in, make a distinction
	private String name;
	private Collection<String> names;
	private LinkedHashSet<String> usedValues;
	
	// one property as input
	public PropertySet(String name, Collection<String> usedValues) {
		this.name = name;
		this.usedValues = new LinkedHashSet<String>(usedValues);
		this.names = new ArrayList<String>();
		names.add(name);
	}
	
	// two or more properties
	public PropertySet(Collection<String> names, Collection<String> usedValues, String delimiter) {
		this("", usedValues);
		PropertyNameGenerator png = new PropertyNameGenerator(null, names, delimiter);
		this.setName(png.combinedPropertyName());
		this.names = names;
	}
	
	// TODO: make " && " a variable somewhere public and reference it everywhere
	public PropertySet(Collection<String> names, Collection<String> usedValues) {
		this(names, usedValues, " && ");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LinkedHashSet<String> getUsedValues() {
		return usedValues;
	}
	
	public void setUsedValues(Collection<String> usedValues) {
		this.usedValues = new LinkedHashSet<String>(usedValues);
	}
	
	public Collection<String> getNames() {
		return names;
	}
	
	public void setNames(Collection<String> names) {
		this.names = names;
	}

}
