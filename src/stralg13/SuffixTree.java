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

		for (int i = 0; i < string.length() - 1; ++i) {
			if (headOfI.equals(root)) {
				SlowScanResult scresult = slowscan(root, i + 1, string.length()); 
				
				if (scresult.isAnEdge()) {
					headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
					headOfIplus1.addEdgeAndNewNode((i + 1) + scresult.index, string.length());
					heads[i + 1] = i + 1 + scresult.index; 
				} else {
					headOfI.addEdgeAndNewNode(i + 1, string.length());
					headOfIplus1 = root;
					heads[i + 1] = 0;
				}
				
			} else {
				Tuple parentToHeadEdge = null; // v
				for (Map.Entry<Tuple, Node> edge : headOfI.parent.edges.entrySet()) {
					if (edge.getValue() == headOfI) {
						parentToHeadEdge = edge.getKey();
						break;
					}
				}
				SlowScanResult fastScanResult = null; // w (edge or node)
				if (headOfI.parent == root) {
					if (parentToHeadEdge.first + 1 < parentToHeadEdge.second)
						fastScanResult = fastscan(root, parentToHeadEdge.first + 1, parentToHeadEdge.second); 
				} else {
					fastScanResult = fastscan(headOfI.parent.suffixLink, parentToHeadEdge.first, parentToHeadEdge.second);
				}
				
				Node w = null;
				if (fastScanResult == null || fastScanResult.isANode()) {
					if (fastScanResult == null)
						w = root;
					else
						w = fastScanResult.node;

					SlowScanResult scresult = slowscan(w, heads[i], string.length());
					if (scresult.isAnEdge()) {
						headOfIplus1 = scresult.node.splitEdgeAndReturnNewNode(scresult.edge, scresult.index);
					} else {
						headOfIplus1 = scresult.node; 
					}
					heads[i + 1] = scresult.index - (i+1);
				} else if (fastScanResult.isAnEdge()) {
					headOfIplus1 = fastScanResult.node.splitEdgeAndReturnNewNode(fastScanResult.edge, fastScanResult.index);
					heads[i + 1] = fastScanResult.index;
					w = headOfIplus1;
				}
				headOfI.suffixLink = w;
				headOfIplus1.addEdgeAndNewNode(i + 1 + heads[i + 1], string.length());
				
				//headOfIplus1.addEdgeAndNewNode((i + 1) + fastScanResult.index, string.length() - 1);
			}

			headOfI = headOfIplus1;
		}
	}

	public boolean equals(SuffixTree otherTree) {
		return string.equals(otherTree.string) && root.equals(otherTree.root);
	}

	SlowScanResult slowscan(Node startNode, int startIndex, int endIndex) {
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edgeEqualsString(startIndex, edge.getKey())) {
				return slowscan(startNode.edges.get(edge.getKey()), startIndex
						+ edge.getKey().second - edge.getKey().first, endIndex);
			}
			if (edgeStartsWithString(startIndex, edge.getKey())) {
				return SlowScanResult.makeEdgeResult(startNode, edge.getKey(),
						getOccurenceOnEdge(startIndex, endIndex, edge.getKey()));
			}
		}
		return SlowScanResult.makeNodeResult(startNode, startIndex);
	}
	
	SlowScanResult fastscan(Node startNode, int startIndex, int endIndex) {
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

}
