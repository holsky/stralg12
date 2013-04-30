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
		root.addEdgeAndNewNode(0, string.length());
		root.suffixLink = root;
		Node headOfIplus1 = null;
		Node headOfI = root;
		int heads[] = new int[string.length()];
		heads[0] = 0;

		for (int i = 0; i < string.length() -2; ++i) {
			if (headOfI.equals(root)) {
				ScanResult scresult = slowscan(root, i + 1, string.length()); 
				
				if (scresult.isAnEdge()) {
					headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
					headOfIplus1.addEdgeAndNewNode((i + 1) + scresult.index, string.length());
					heads[i + 1] = scresult.index; 
				} else {
					headOfI.addEdgeAndNewNode(i + 1, string.length());
					headOfIplus1 = root;
					heads[i + 1] = 0;
				}
				
			} else {
				Tuple parentToHeadEdge = findParentEdge(headOfI); // v
				
				//here we find whatever the root of the tree head(i) is in
				ScanResult subtreeContainingHeadOfIPlus1 = findW(headOfI, parentToHeadEdge);
				
				if (subtreeContainingHeadOfIPlus1.isAnEdge()) {
					
					headOfIplus1 = subtreeContainingHeadOfIPlus1.node
							.splitEdgeAndReturnNewNode(subtreeContainingHeadOfIPlus1.edge, subtreeContainingHeadOfIPlus1.index);
					//have to substract fastScanResult.edge.first
					//because it is the length (in indices) from the path to the head(i+1) node
					heads[i + 1] = subtreeContainingHeadOfIPlus1.index - subtreeContainingHeadOfIPlus1.edge.first;
				} else if (subtreeContainingHeadOfIPlus1.isANode()) {

					ScanResult scresult = slowscanWithIndex(subtreeContainingHeadOfIPlus1.node,
							subtreeContainingHeadOfIPlus1.index,
							heads[i],
							string.length());
					if (scresult.isAnEdge()) {
						headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
						heads[i + 1] = scresult.index;
					} else {
						headOfIplus1 = scresult.node;
						heads[i + 1] = scresult.index;
					}
					//
					
				}
				headOfI.suffixLink = subtreeContainingHeadOfIPlus1.node;
				
				//i+1 is the distance that it must in all cases have 
				//heads[i+1] is the distance we are already down the tree
				headOfIplus1.addEdgeAndNewNode(i + 1 + heads[i + 1], string.length());
				
				//headOfIplus1.addEdgeAndNewNode((i + 1) + fastScanResult.index, string.length() - 1);
			}
		
			headOfI = headOfIplus1;
		}
		root.addEdgeAndNewNode(string.length() - 1, string.length());		
		
	}

	protected ScanResult findW(Node headOfI, Tuple parentToHeadEdge) {
		if (headOfI.parent == root ) {
			if (parentToHeadEdge.first + 1 < parentToHeadEdge.second) {
				return fastscan(root, parentToHeadEdge.first + 1, parentToHeadEdge.second);
			} else { 
				return ScanResult.makeNodeResult(root, 0);
			}
		} else {
			return fastscan(headOfI.parent.suffixLink, parentToHeadEdge.first, parentToHeadEdge.second);
		}		
	}

	protected Tuple findParentEdge(Node headOfI) {
		for (Map.Entry<Tuple, Node> edge : headOfI.parent.edges.entrySet()) {
			if (edge.getValue() == headOfI) {
				return edge.getKey();
			}
		}
		return null;
	}

	public boolean equals(SuffixTree otherTree) {
		return string.equals(otherTree.string) && root.equals(otherTree.root);
	}

	ScanResult slowscanWithIndex(Node startNode, int startNodeIndex, int startIndex, int endIndex) {
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edgeEqualsString(startIndex, edge.getKey())) {
				return slowscanWithIndex(startNode.edges.get(edge.getKey()),
						edge.getKey().second, 
						startIndex + edge.getKey().second - edge.getKey().first,
						endIndex);
			}
			if (edgeStartsWithString(startIndex, edge.getKey())) {
				return ScanResult.makeEdgeResult(startNode, edge.getKey(),
						getOccurenceOnEdge(startIndex, endIndex, edge.getKey()));
			}
		}
		return ScanResult.makeNodeResult(startNode, startNodeIndex);
	}
	
	ScanResult slowscan(Node startNode, int startIndex, int endIndex) {
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edgeEqualsString(startIndex, edge.getKey())) {
				return slowscan(startNode.edges.get(edge.getKey()), startIndex
						+ edge.getKey().second - edge.getKey().first, endIndex);
			}
			if (edgeStartsWithString(startIndex, edge.getKey())) {
				return ScanResult.makeEdgeResult(startNode, edge.getKey(),
						getOccurenceOnEdge(startIndex, endIndex, edge.getKey()));
			}
		}
		return ScanResult.makeNodeResult(startNode, startIndex);
	}
	
	ScanResult fastscan(Node startNode, int startIndex, int endIndex) {
		return slowscan(startNode, startIndex, endIndex);
	}

	private boolean edgeEqualsString(int startIndex, Tuple key) {
		if ((startIndex + (key.second - key.first)) > string.length() - 1)
			return false;

		if (key.first - key.second == 0)
			return string.charAt(key.first) == string.charAt(startIndex);

		return string
				.substring(startIndex, startIndex + key.second - key.first)
				.equals(string.substring(key.first, key.second));
	}

	boolean edgeStartsWithString(int startIndex, Tuple tuple) {
		return string.charAt(tuple.first) == string.charAt(startIndex);
	}

	int getOccurenceOnEdge(int startIndex, int endIndex, Tuple tuple) {
		int i = 0;
		for (; i < tuple.second - tuple.first && i < endIndex - startIndex; ++i) {
			if (string.charAt(startIndex + i) != string.charAt(tuple.first + i))
				break;
		}
		return tuple.first + i;
		//return string.indexOf(string.substring(startIndex, string.length() - 1)) + 1;
	}
	
	@Override
	public String toString() {
		return string + "\n" + root.toString();
	}

}
