package stralg13;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Node {
	Map<Tuple, Node> edges = new HashMap<Tuple, Node>();
	Node parent = null;
	Node suffixLink = null;
	
	Tuple leafList;
	Tuple largeList;
	
	int leafIndex = 0;
	
	public Node() {
		this(null);
	}
	
	public Node(Node parent) {
		this.parent = parent;
	}
	
	public Node addEdgeAndNewNode(int startIndex, int endIndex) {
		return addEdgeAndNewNode(startIndex, endIndex, -1);
	}
	
	public Node addEdgeAndNewNode(int startIndex, int endIndex, int iteration) {
		Node node = new Node(this);
		edges.put(new Tuple(startIndex, endIndex), node);
		return node;
	}
	
	public void addEdgeAndNode(Tuple edge, Node node) {
		edges.put(edge, node);
	}
		
	Node splitEdgeAndReturnNewNode (Tuple edge, int index) {
		Node childNode = edges.get(edge);
		edges.remove(edge);
		
		Node newNode = addEdgeAndNewNode(edge.first, index);
		childNode.parent = newNode;
		newNode.addEdgeAndNode(new Tuple(index, edge.second), childNode);
		
		return newNode;
		
	}
	

	@Override
	public int hashCode() {
		final int prime = 4095;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.keySet().hashCode());
		result += prime *  leafIndex;
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node otherNode = (Node) obj;
	    if (edges.size() != otherNode.edges.size())
	    	return false;
	    
	    /*if (suffixLink == null) {
			if (otherNode.suffixLink != null)
				return false;
		} else if (suffixLink.hashCode() != otherNode.suffixLink.hashCode())
			return false;
	    */
	    if (leafIndex != otherNode.leafIndex)
	    	return false;
	    
	    for (Map.Entry<Tuple, Node> edge : edges.entrySet()) {
	        if (otherNode.edges.containsKey(edge.getKey())) {
	            boolean result = edge.getValue().equals(otherNode.edges.get(edge.getKey()));
	            if (!result)
	            	return false;
	        } else
	            return false;
	    }
	    return true;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (Tuple edge : edges.keySet()) {
			sb.append("[" + edge.first + "," + edge.second + "] = ");
			sb.append(edges.get(edge));
			sb.append(",");
		}
		sb.append("}");
		
		return sb.toString();
	}
	

	
}
