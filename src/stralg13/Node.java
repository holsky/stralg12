package stralg13;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {
	Map<Tuple, Node> edges = new HashMap<Tuple, Node>();
	Node parent = null;
	
	public Node() {
		this(null);
	}
	
	public Node(Node parent) {
		this.parent = parent;
	}
	
	public void addEdge(int startIndex, int endIndex) {
		edges.put(new Tuple(startIndex, endIndex), new Node(this));
	}
	
	
	
	public boolean equals(Node otherNode) {
		
	    if (edges.size() != otherNode.edges.size())
	    	return false;
	    for (Map.Entry<Tuple, Node> edge : edges.entrySet()) {
	        if (otherNode.edges.containsKey(edge.getKey())) {
	            boolean result = edges.get(edge.getKey()).equals(otherNode.edges.get(edge.getKey()));
	            if (!result) return false;
	        } else
	            return false;
	    }
	    return true;
	}
	
}
