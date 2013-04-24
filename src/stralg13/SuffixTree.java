package stralg13;

import java.util.Map;

public class SuffixTree {
	public static final String STRING_END = "$";
	Node root;
	String string;
	
	public SuffixTree(String string) {
		this.string = string + STRING_END;
				
		makeSuffixTree();
	}
	
	public SuffixTree() {
		root = new Node();
	}

	public void makeSuffixTree() {
		root = new Node();
		root.addEdge(0, string.length() - 1);
	}
	
	public boolean equals(SuffixTree otherTree) {
		return string.equals(otherTree.string) &&
				root.equals(otherTree.root);
	}
	
	boolean compareString(int startIndex, Tuple tuple)
	{
	    return string.substring(tuple.first, tuple.second  - tuple.first)
	            .equals(string.substring(startIndex, tuple.second - tuple.first));
	}

	Node slowscan(Node startNode, int startIndex)
	{
	    for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
	        if (compareString(startIndex, edge.getKey())) {
	           return children[key]->slowscan(startIndex + key.second - key.first);
	        }
	    }
	    return this;
	}
	
}
