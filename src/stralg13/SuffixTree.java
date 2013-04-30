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
		int headIndexFromStringStart[] = new int[string.length()];
		int tailStart[] = new int[string.length()];
		headIndexFromStringStart[0] = 0;
		tailStart[0] = 0;

		for (int i = 0; i < string.length() -2; ++i) {
			if (headOfI.equals(root)) {
				//scan for suffix(i+1)
				ScanResult scresult = slowscan(root, i + 1, string.length()); 
				
				//if it is an edge, split that edge
				if (scresult.isAnEdge()) {
					headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
					headOfIplus1.addEdgeAndNewNode((i + 1) + scresult.index, string.length());
					//where the tail(i+1) begins relative to the string
					//scresult.index would be relative to the suffix
					tailStart[i + 1] = i + 1 + scresult.index;
					headIndexFromStringStart[i + 1] = scresult.index;
				//if it is a node, then it must be added to root
				//since the head(i) is the root, head(i+1) is the root too,
				// because head(i) is a prefix of head(i+1) 
				} else {
					root.addEdgeAndNewNode(i + 1, string.length());
					headOfIplus1 = root;
					//where tail(i+1) begins relative to the string
					headIndexFromStringStart[i + 1] = 0;
					headIndexFromStringStart[i + 1] = 0;
				}
				
			} else {
				Tuple parentToHeadEdge = findParentEdge(headOfI); // v
				
				//here we find whatever the root of the tree head(i) is in
				ScanResult subtreeContainingHeadOfIPlus1 = findW(headOfI, parentToHeadEdge);
				
				if (subtreeContainingHeadOfIPlus1.isAnEdge()) {
					
					headOfIplus1 = subtreeContainingHeadOfIPlus1.node
							.splitEdgeAndReturnNewNode(subtreeContainingHeadOfIPlus1.edge, subtreeContainingHeadOfIPlus1.index);
					//where head(i) ends, relative to start of string
					headIndexFromStringStart[i + 1] = subtreeContainingHeadOfIPlus1.index;
					//where the tail(i+1) begins, relative to the suffix
					//add index, because that is how far the suffix is already covered by the edge
					//subtract 1, because index is exclusive
					tailStart[i + 1] = i+1 + subtreeContainingHeadOfIPlus1.index-1;
					//if w is an edge, we take the result of the split as suffix link
					//which is headOfIplus1
					headOfI.suffixLink = headOfIplus1;
				} else if (subtreeContainingHeadOfIPlus1.isANode()) {

					ScanResult scresult = slowscan(subtreeContainingHeadOfIPlus1.node,
				//			subtreeContainingHeadOfIPlus1.index,
							tailStart[i],
							string.length());
					if (scresult.isAnEdge()) {
						headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
						//where the tail(i+1) begins, relative to start of the string
						tailStart[i + 1] = i+1 + scresult.index;
						//where head(i+1) ends, relative to start of string
						headIndexFromStringStart[i + 1] =  scresult.index;
					} else {
						headOfIplus1 = scresult.node;
						//where head(i+1) ends, relative to start of string
						headIndexFromStringStart[i + 1] = scresult.index;
						//where the tail(i+1) begins, relative to start of the string
						tailStart[i + 1] = i+1 + scresult.index;
					}
					//if w is a node, we can just put it as suffix link
					headOfI.suffixLink = subtreeContainingHeadOfIPlus1.node;
				}
				
				
				//i+1 is the distance that it must in all cases have 
				//heads[i+1] is the distance we are already down the tree
				headOfIplus1.addEdgeAndNewNode(tailStart[i+1], string.length());
				
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
	
	ScanResult slowscan(Node startNode, int startIndex, int endIndex) {
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edgeEqualsString(startIndex, edge.getKey())) {
				return slowscan(startNode.edges.get(edge.getKey()), startIndex
						+ edge.getKey().second - edge.getKey().first, endIndex);
			}
			if (edgeStartsWithString(startIndex, edge.getKey())) {	
				return ScanResult.makeEdgeResult(startNode, edge.getKey(),
						getOccurenceOnEdgeRelativeToEdgeStart(startIndex, endIndex, edge.getKey()));
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

		return string
				.substring(startIndex, startIndex + key.second - key.first)
				.equals(string.substring(key.first, key.second));
	}

	boolean edgeStartsWithString(int startIndex, Tuple tuple) {
		return string.charAt(tuple.first) == string.charAt(startIndex);
	}

	int getOccurenceOnEdgeRelativeToEdgeStart(int startIndex, int endIndex, Tuple tuple) {
		//i is the occurence relative to the beginning of the string
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
