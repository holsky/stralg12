package stralg13;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class TandemRepeatsFinder {

	String string;
	SuffixTree tree;
	public int[] leafToDFS;
	public int[] dfsToLeaf;
	int dfsIteration = 0;
	
	public TandemRepeatsFinder(String string) {
		this.string = string;
		tree = new SuffixTree(string);
		leafToDFS = new int[string.length() + 1];
		dfsToLeaf = new int[string.length() + 1];
	}

	public void createDFSOrdering() {
		visitNodesDFSOrdering(tree.root);
	}
	
	public Tuple visitNodesDFSOrdering(Node node) {
		if (node.edges.size() == 0) {
			leafToDFS[node.leafIndex] = dfsIteration;
			dfsToLeaf[dfsIteration] = node.leafIndex;
			dfsIteration++;
			return new Tuple(dfsIteration - 1, dfsIteration - 1);
		}
		
		Tuple leafList = new Tuple(string.length() + 1, -1);
		Tuple largestChild = new Tuple(0,-1);
		
		for (Tuple edge : getSortedEdges(node)) {
			Tuple childLeafList = (visitNodesDFSOrdering(node.edges.get(edge)));
			if (childLeafList.first < leafList.first) {
				leafList.first = childLeafList.first;
			}
			if (childLeafList.second > leafList.second) {
				leafList.second = childLeafList.second;
			}
			if (childLeafList.size() > largestChild.size() ) {
				largestChild = childLeafList;
			}
		}
		node.largeList = largestChild;
		node.leafList = leafList;
		return node.leafList;
	}
	
	private SortedSet<Tuple> getSortedEdges(Node node) {
		SortedSet<Tuple> sortedEdges = new TreeSet<Tuple>(new Comparator<Tuple> () {
			@Override
			public int compare(Tuple o1, Tuple o2) {
				return tree.string.charAt(o1.first) - tree.string.charAt(o2.first);
			}

		});
		
		for (Entry<Tuple, Node> edge : node.edges.entrySet()) {
			sortedEdges.add(edge.getKey());
		}
		return sortedEdges;
	}
}
