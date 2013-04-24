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
	
	public void addEdgeAndNewNode(int startIndex, int endIndex) {
		edges.put(new Tuple(startIndex, endIndex), new Node(this));
	}
	
	public void addEdgeAndNode(Tuple edge, Node node) {
		edges.put(edge, node);
	}
	
	void splitEdge (Tuple edge, int index) {
		Node childNode = edges.get(edge);
		edges.remove(edge);
		addEdgeAndNewNode(edge.first, index);
		edges.get(new Tuple(edge.first, index))
			.addEdgeAndNode(new Tuple(index, edge.second), childNode);
		
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
