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
			if (i == string.length () - 1 - 1) {
				root.addEdgeAndNewNode(i +1, i+1);
			}
			if (headOfI.equals(root)) {
				SlowScanResult scresult = slowscan(root, i +1 ); 
				
				if (scresult.isAnEdge()) {
					headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
					headOfIplus1.addEdgeAndNewNode((i + 1) + scresult.index, string.length() - 1);
				} else {
					headOfI.addEdgeAndNewNode(i + 1, string.length() - 1);
					headOfIplus1 = root;
				}
				
			} else {

			}

			headOfI = headOfIplus1;
		}
	}

	public boolean equals(SuffixTree otherTree) {
		return string.equals(otherTree.string) && root.equals(otherTree.root);
	}

	SlowScanResult slowscan(Node startNode, int startIndex) {
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edgeEqualsString(startIndex, edge.getKey())) {
				return slowscan(startNode.edges.get(edge.getKey()), startIndex
						+ edge.getKey().second - edge.getKey().first);
			}
			if (edgeStartsWithString(startIndex, edge.getKey())) {
				return SlowScanResult.makeEdgeResult(startNode, edge.getKey(),
						getOccurenceOnEdge(startIndex, edge.getKey()));
			}
		}
		return SlowScanResult.makeNodeResult(startNode);
	}

	private boolean edgeEqualsString(int startIndex, Tuple key) {
		if ((startIndex + (key.second - key.first)) > string.length() - 1)
			return false;
		if (key.first - key.second == 0)
			return false;
		return string.substring(startIndex, startIndex + key.second - key.first).equals(
				string.substring(key.first, key.second));
	}

	boolean edgeStartsWithString(int startIndex, Tuple tuple) {
		if (startIndex == string.length() - 1) 
			return false;
		return string.substring(tuple.first, tuple.second).startsWith(
				string.substring(startIndex, string.length() - 1));
	}
	
	int getOccurenceOnEdge(int startIndex, Tuple tuple) {
		return string.indexOf(string.substring(startIndex, string.length() - 1)) + 1;
	}

}
