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
	List<Integer> iterationsVisited = new ArrayList<Integer>();
	
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
		node.addIteration(iteration);
		return node;
	}
	
	public void addEdgeAndNode(Tuple edge, Node node) {
		edges.put(edge, node);
	}
	
	Node splitEdgeAndReturnNewNode (Tuple edge, int index) {
		return splitEdgeAndReturnNewNode(edge, index, -1);
	}
	
	Node splitEdgeAndReturnNewNode (Tuple edge, int index, int iteration) {
		Node childNode = edges.get(edge);
		edges.remove(edge);
		
		Node newNode = addEdgeAndNewNode(edge.first, index, iteration);
		childNode.parent = newNode;
		newNode.iterationsVisited.addAll(childNode.iterationsVisited);
		newNode.addEdgeAndNode(new Tuple(index, edge.second), childNode);
		
		return newNode;
		
	}
	
	public void addIteration(int i) {
		if (!iterationsVisited.contains(i + 1))
			iterationsVisited.add(i + 1);
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
