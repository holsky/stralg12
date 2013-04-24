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
		root.addEdgeAndNewNode(0, string.length() - 1);
		Node headOfIplus1 = null;
		Node headOfI = root;

		for (int i = 0; i < string.length() - 1; ++i) {
			if (headOfI.equals(root)) {
				headOfIplus1 = slowscan(root, i + 1);
				headOfI.addEdgeAndNewNode(i + 1, string.length() - 1);
			} else {

			}

			headOfI = headOfIplus1;
		}
	}

	public boolean equals(SuffixTree otherTree) {
		return string.equals(otherTree.string) && root.equals(otherTree.root);
	}

	Node slowscan(Node startNode, int startIndex) {
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edgeEqualsString(startIndex, edge.getKey())) {
				return slowscan(startNode.edges.get(edge.getKey()), startIndex
						+ edge.getKey().second - edge.getKey().first);
			}
			if (edgeStartsWithString(startIndex, edge.getKey())) {
				return slowscan(startNode.edges.get(edge.getKey()), startIndex
						+ edge.getKey().second - edge.getKey().first);
			}
		}
		return startNode;
	}

	private boolean edgeEqualsString(int startIndex, Tuple key) {
		if (startIndex + key.second - key.first > string.length() - 1)
			return false;
		return string.substring(startIndex, key.second - key.first).equals(
				string.substring(key.first, key.second));
	}

	boolean edgeStartsWithString(int startIndex, Tuple tuple) {
		return string.substring(startIndex).startsWith(
				string.substring(tuple.first, tuple.second));
	}

}
