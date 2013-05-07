package stralg13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class TandemRepeatFinder {

	protected String string;
	protected SuffixTree tree;
	protected int[] leafToDFS;
	protected int[] dfsToLeaf;
	protected int dfsIteration = 0;

	protected SortedSet<TandemRepeat> repeats;
	private int branchingRepeats = 0;
	private int nonBranchingRepeats = 0;

	TandemRepeatFinder(String string) {
		this.string = string;
		tree = new SuffixTree(string);
		leafToDFS = new int[string.length() + 1];
		dfsToLeaf = new int[string.length() + 1];

		repeats = new TreeSet<TandemRepeat>();

		visitNodesInitialize(tree.root);
		visitNodesFindRepeats(tree.root, 0);
		findNonBranchingRepeats();
	}

	public static void findTandemRepeatsReportOnlyCount(String file, PrintStream out) throws Exception {
		new TandemRepeatFinder(readFile(file)).reportCounts(out);
	}
	
	void reportCounts(PrintStream out) {
		out.print(branchingRepeats + " " + nonBranchingRepeats + "\n");
	}

	public static void findTandemRepeats(String file, PrintStream out) throws Exception {
		new TandemRepeatFinder(readFile(file)).reportRepeats(out);
	}
	
	public void reportRepeats(PrintStream out) {
		String template = "(%d,%d,2) %s\n";
		for (TandemRepeat repeat : repeats) {
			out.print(String.format(template, repeat.index, repeat.length, repeat.branching ? "branching" : "non-branching"));
		}
		out.print(branchingRepeats + " " + nonBranchingRepeats + "\n");
	}
	
	static String readFile  (String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}

	void findNonBranchingRepeats() {
		List<TandemRepeat> nonbranchingRepeats = new LinkedList<TandemRepeat>();
		for (TandemRepeat repeat : repeats) {
			for (int i = 1; repeat.index - i >= 0; i++) {
				if (string.charAt(repeat.index - i) == string
						.charAt(repeat.index - i + repeat.length * 2)) {
					nonbranchingRepeats.add(new TandemRepeat(repeat.index - i,
							repeat.length, false));
					nonBranchingRepeats++;
				} else {
					break;
				}
			}
		}

		repeats.addAll(nonbranchingRepeats);

	}

	void visitNodesFindRepeats(Node node, int depth) {
		if (node.edges.isEmpty())
			return;

		for (Map.Entry<Tuple, Node> edge : node.edges.entrySet()) {
			visitNodesFindRepeats(edge.getValue(), depth + edge.getKey().size());
		}
		for (int i = node.leafList.first; i <= node.leafList.second; i++) {
			if (i == node.largeList.first) {
				i = node.largeList.second;
				continue;
			}
			if (dfsToLeaf[i] + depth >= 0
					&& dfsToLeaf[i] + depth <= string.length()) {
				if (node.leafList.includes(leafToDFS[dfsToLeaf[i] + depth])) {
					if (isBranchingRepeat(dfsToLeaf[i], depth)) {
						boolean notAlreadyPresent = repeats.add(new TandemRepeat(dfsToLeaf[i], depth, true));
						if (notAlreadyPresent)
							branchingRepeats++;
					}
				}
			}
			if (dfsToLeaf[i] - depth >= 0
					&& dfsToLeaf[i] - depth <= string.length()) {
				if (node.leafList.includes(leafToDFS[dfsToLeaf[i] - depth])) {
					if (isBranchingRepeat(dfsToLeaf[i] - depth, depth)) {
						boolean notAlreadyPresent = repeats.add(new TandemRepeat(dfsToLeaf[i] - depth,
								depth, true));
						if (notAlreadyPresent)
							branchingRepeats++;
					}
				}
			}
		}
	}

	boolean isBranchingRepeat(int i, int depth) {
		if (i + 2 * depth >= string.length())
			return false;
		return string.charAt(i) != string.charAt(i + 2 * depth);
	}

	

	Tuple visitNodesInitialize(Node node) {
		if (node.edges.isEmpty()) {
			leafToDFS[node.leafIndex] = dfsIteration;
			dfsToLeaf[dfsIteration] = node.leafIndex;
			dfsIteration++;
			return new Tuple(dfsIteration - 1, dfsIteration - 1);
		}

		Tuple leafList = new Tuple(string.length() + 1, -1);
		Tuple largestChild = new Tuple(0, -1);

		for (Map.Entry<Tuple,Node> edge : node.edges.entrySet()) {
			Tuple childLeafList = visitNodesInitialize(edge.getValue());
			if (childLeafList.first < leafList.first) {
				leafList.first = childLeafList.first;
			}
			if (childLeafList.second > leafList.second) {
				leafList.second = childLeafList.second;
			}
			if (childLeafList.size() > largestChild.size()) {
				largestChild = childLeafList;
			}
		}
		node.largeList = largestChild;
		node.leafList = leafList;
		return node.leafList;
	}

	public SortedSet<TandemRepeat> getRepeats() {
		return repeats;
	}

	public int getBranchingRepeats() {
		return branchingRepeats;
	}

	public int getNonBranchingRepeats() {
		return nonBranchingRepeats;
	}
	
	
}
