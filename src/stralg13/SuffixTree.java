package stralg13;

import java.util.Map;

public class SuffixTree {
	public static final String STRING_END = "~";
	Node root;
	String string;
	int[] tailStart;
	int iteration = 0;

	Node headOfIplus1 = null;
	Node headOfI = null;

	int originalStart = 0;
	int originalEnd = 0;

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
		headOfIplus1 = null;
		headOfI = root;
		tailStart = new int[string.length()];
		tailStart[0] = 0;

		for (iteration = 0; iteration < string.length() - 2; ++iteration) {
			if (headOfI.equals(root)) {
				headOfIplus1 = scanAddNewNodeAndEdge(root, iteration + 1, string.length());
			} else {
				Tuple parentToHeadEdge = findParentEdge(headOfI);
				
				int startIndex = parentToHeadEdge.first;
				int endIndex = parentToHeadEdge.second;
				Node startNode = headOfI.parent;
				if (startNode == root) {
					startIndex++;
					if (startNode == root && startIndex >= endIndex) {
						headOfIplus1 = scanAddNewNodeAndEdge(root, iteration + 1,
								string.length());
						headOfI.suffixLink = root;
					} else {
						fastscanThenMaybeSlowscan(root, startIndex, endIndex);
					}
				} else {
					startNode = startNode.suffixLink;
					fastscanThenMaybeSlowscan(startNode, startIndex, endIndex);
				}
			}

			headOfI = headOfIplus1;
		}
		Node leaf = root.addEdgeAndNewNode(string.length() - 1, string.length());
		leaf.leafIndex = string.length() - 1;

	}

	public int getDepth(Node node) {
		int sum = 0;
		Node current = node;
		while (current.parent != null) {
			Tuple edge = findParentEdge(current);
			sum += edge.second - edge.first;
			current = current.parent;
		}
		return sum;
	}
	
	protected Tuple findParentEdge(Node headOfI) {
		for (Map.Entry<Tuple, Node> edge : headOfI.parent.edges.entrySet()) {
			if (edge.getValue().equals(headOfI)) {
				return edge.getKey();
			}
		}
		return null;
	}

	public boolean equals(SuffixTree otherTree) {
		return string.equals(otherTree.string) && root.equals(otherTree.root);
	}

	void fastscanThenMaybeSlowscan(Node startNode, int startIndex, int endIndex) {

		if (startIndex < endIndex) {
			for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
				if (shouldContinueDown(startIndex, endIndex, edge.getKey())) {
					fastscanThenMaybeSlowscan(startNode.edges.get(edge.getKey()), startIndex
							+ edge.getKey().second - edge.getKey().first,
							endIndex);
					return;
				}
				if (shouldSplitEdge(startIndex, edge.getKey())) {
					int index = getSplitIndex(startIndex, endIndex,
							edge.getKey());
					Node head = startNode.splitEdgeAndReturnNewNode(
							edge.getKey(), index);

					Node leaf = head.addEdgeAndNewNode(iteration  + 1 + getDepth(head),
							string.length());
					leaf.leafIndex = iteration + 1;
						
					headOfIplus1 = head;
					headOfI.suffixLink = head;

					tailStart[iteration + 1] = iteration + 1  + getDepth(head);
					return;
				}
			}
		}

		headOfIplus1 = scanAddNewNodeAndEdge(startNode, tailStart[iteration],
				string.length());
		headOfI.suffixLink = startNode;
	}

	Node scanAddNewNodeAndEdge(Node startNode, int startIndex, int endIndex) {
		if (startIndex < endIndex) {
			for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
				if (shouldContinueDown(startIndex, endIndex, edge.getKey())) {
					return scanAddNewNodeAndEdge(startNode.edges.get(edge.getKey()),
							startIndex + edge.getKey().second
									- edge.getKey().first, endIndex);
				}
				if (shouldSplitEdge(startIndex, edge.getKey())) {
					int headIndex = getSplitIndex(startIndex, endIndex,
							edge.getKey());
					Node head = startNode.splitEdgeAndReturnNewNode(
							edge.getKey(), headIndex);

					Node leaf = head.addEdgeAndNewNode(iteration + 1  + getDepth(head), string.length());
					leaf.leafIndex = iteration + 1;
					
					headOfIplus1 = head;
					//headOfI.suffixLink = startNode;
					
					tailStart[iteration + 1] = iteration + 1  + getDepth(head);
					return head;
				}
			}
		}
		tailStart[iteration + 1] = startIndex;
		Node leaf = startNode.addEdgeAndNewNode(startIndex, string.length());
		leaf.leafIndex = iteration + 1;
		return startNode;
	}

	private boolean shouldContinueDown(int startIndex, int endIndex, Tuple key) {
		if ((startIndex + (key.second - key.first)) > string.length() - 1)
			return false;

		return string.substring(startIndex, endIndex).startsWith(
				string.substring(key.first, key.second));
	}

	boolean shouldSplitEdge(int startIndex, Tuple tuple) {
		return string.charAt(tuple.first) == string.charAt(startIndex);
	}

	int getSplitIndex(int startIndex, int endIndex, Tuple tuple) {
		int i = 0;
		for (; i < tuple.second - tuple.first && i < endIndex - startIndex; ++i) {
			if (string.charAt(startIndex + i) != string.charAt(tuple.first + i))
				break;
		}
		return tuple.first + i;

	}

	@Override
	public String toString() {
		return string + "\n" + root.toString();
	}

}
