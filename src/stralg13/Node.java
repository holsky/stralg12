package stralg13;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {
	Map<Tuple, Node> edges = new HashMap<Tuple, Node>();
	Node parent = null;
	Node suffixLink = null;
	
	public Node() {
		this(null);
	}
	
	public Node(Node parent) {
		this.parent = parent;
	}
	
	public Node addEdgeAndNewNode(int startIndex, int endIndex) {
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edges == null) ? 0 : edges.keySet().hashCode());
		return result;
	}
	
	public boolean equals(Node otherNode) {
		
	    if (edges.size() != otherNode.edges.size())
	    	return false;
	    
	    if (suffixLink == null) {
			if (otherNode.suffixLink != null)
				return false;
		} else if (suffixLink.hashCode() != otherNode.suffixLink.hashCode())
			return false;
	    
	    for (Map.Entry<Tuple, Node> edge : edges.entrySet()) {
	        if (otherNode.edges.containsKey(edge.getKey())) {
	            boolean result = edges.get(edge.getKey()).equals(otherNode.edges.get(edge.getKey()));
	            if (!result)
	            	return false;
	        } else
	            return false;
	    }
	    return true;
	}
	

	
}
