package stralg13;

import java.util.Map;

public class SuffixTree {
	public static final String STRING_END = "$";
	Node root;
	String string;	
	int[] tailStart;
	
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
		tailStart = new int[string.length()];
		tailStart[0] = 0;

		
		for (int iteration = 0; iteration < string.length() - 2; ++iteration) {
			if (headOfI.equals(root)) {
				headOfIplus1 = slowscan2(root, iteration + 1,
						string.length(), iteration);
			} else {
				Tuple parentToHeadEdge = findParentEdge(headOfI);
				
				ScanResult subtreeContainingHeadOfIPlus1 = null;
				if (headOfI.parent == root) {
					if (parentToHeadEdge.first + 1 < parentToHeadEdge.second) {
						subtreeContainingHeadOfIPlus1 = slowscan(root,
								parentToHeadEdge.first + 1,
								parentToHeadEdge.second);
					} else {
						subtreeContainingHeadOfIPlus1 = ScanResult
								.makeNodeResult(root, 0);
					}
				} else {
					subtreeContainingHeadOfIPlus1 = slowscan3(
							headOfI.parent.suffixLink, parentToHeadEdge.first,
							parentToHeadEdge.second, iteration);
				}
				if (subtreeContainingHeadOfIPlus1.isAnEdge()) {
					headOfIplus1 = subtreeContainingHeadOfIPlus1.node;
					headOfI.suffixLink = headOfIplus1;
	
				} else if (subtreeContainingHeadOfIPlus1.isANode()) {

					headOfIplus1 = slowscan2(
							subtreeContainingHeadOfIPlus1.node,
							tailStart[iteration], string.length(), iteration);
					headOfI.suffixLink = subtreeContainingHeadOfIPlus1.node;
				}
			}

			headOfI = headOfIplus1;
		}
		root.addEdgeAndNewNode(string.length() - 1, string.length());

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
		// check for this, might happen if called recursively
		if (startIndex < endIndex) {
			for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
				if (edgeEqualsString(startIndex, edge.getKey())) {
					return slowscan(startNode.edges.get(edge.getKey()),
							startIndex + edge.getKey().second
									- edge.getKey().first, endIndex);
				}
				if (edgeStartsWithString(startIndex, edge.getKey())) {
					return ScanResult.makeEdgeResult(
							startNode,
							edge.getKey(),
							getOccurenceOnEdgeRelativeToEdgeStart(startIndex,
									endIndex, edge.getKey()));
				}
			}
		}
		return ScanResult.makeNodeResult(startNode, startIndex);
	}

	ScanResult slowscan3(Node startNode, int startIndex, int endIndex, int iteration) {
		if (startIndex < endIndex) {
			for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
				if (edgeEqualsString(startIndex, edge.getKey())) {
					return slowscan3(startNode.edges.get(edge.getKey()),
							startIndex + edge.getKey().second
									- edge.getKey().first, endIndex, iteration);
				}
				if (edgeStartsWithString(startIndex, edge.getKey())) {
					int index = getOccurenceOnEdgeRelativeToEdgeStart(
							startIndex, endIndex, edge.getKey());
					Node head = startNode
							.splitEdgeAndReturnNewNode(edge.getKey(),
									index);
					int tailIndex = getOccurenceOnEdge(startIndex, endIndex,
							edge.getKey());
					head.addEdgeAndNewNode(iteration + tailIndex,
							string.length());
					
					tailStart[iteration + 1] = iteration + tailIndex;
					
					return ScanResult.makeEdgeResult(
							head,
							edge.getKey(),
							getOccurenceOnEdgeRelativeToEdgeStart(startIndex,
									endIndex, edge.getKey()));
				}
			}
		}
		return ScanResult.makeNodeResult(startNode, startIndex);
	}

	
	Node slowscan2(Node startNode, int startIndex, int endIndex, int iteration) {
		if (startIndex < endIndex) {
			for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
				if (edgeEqualsString(startIndex, edge.getKey())) {
					return slowscan2(startNode.edges.get(edge.getKey()),
							startIndex + edge.getKey().second
									- edge.getKey().first, endIndex, iteration);
				}
				if (edgeStartsWithString(startIndex, edge.getKey())) {
					int headIndex = getOccurenceOnEdgeRelativeToEdgeStart(
							startIndex, endIndex, edge.getKey());
					Node head = startNode.splitEdgeAndReturnNewNode(
							edge.getKey(), headIndex);
					int tailIndex = getOccurenceOnEdge(startIndex, endIndex,
							edge.getKey());
					head.addEdgeAndNewNode(tailIndex, string.length());
					tailStart[iteration + 1] = tailIndex;
					
					return head;
				}
			}
		}
		tailStart[iteration + 1] = startIndex;
		startNode.addEdgeAndNewNode(startIndex, string.length());
		return startNode;
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

	int getOccurenceOnEdgeRelativeToEdgeStart(int startIndex, int endIndex,
			Tuple tuple) {
		// i is the occurence relative to the beginning of the string
		int i = 0;
		for (; i < tuple.second - tuple.first && i < endIndex - startIndex; ++i) {
			if (string.charAt(startIndex + i) != string.charAt(tuple.first + i))
				break;
		}
		return tuple.first + i;
		// return string.indexOf(string.substring(startIndex, string.length() -
		// 1)) + 1;
	}

	int getOccurenceOnEdge(int startIndex, int endIndex, Tuple tuple) {
		// i is the occurence relative to the beginning of the string
		int i = 0;
		for (; i < tuple.second - tuple.first && i < endIndex - startIndex; ++i) {
			if (string.charAt(startIndex + i) != string.charAt(tuple.first + i))
				break;
		}
		return startIndex + i;
		// return string.indexOf(string.substring(startIndex, string.length() -
		// 1)) + 1;
	}

	@Override
	public String toString() {
		return string + "\n" + root.toString();
	}

}
