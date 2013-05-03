package stralg13;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Finder {
	private String input;
	private String pattern;
	
	public Finder(String input, String pattern) {
		this.input = input;
		this.pattern = pattern;
	}
	
	public List<Integer> search() {
		SuffixTree tree = new SuffixTree(input);
		
		return findNode(tree.root, 0, 0);
	}
	
	public List<Integer> findNode(Node startNode, int startNodeIndex, int patternIndex) {
		List<Integer> results = new ArrayList<Integer>();
		
		if (patternIndex >= pattern.length()) {
			findLeaves(startNode, results);
			return results;
		}
		
		for (Map.Entry<Tuple, Node> edge : startNode.edges.entrySet()) {
			if (edge.getKey().first >= input.length()) {
				if (patternIndex >= pattern.length()) {
					findLeaves(startNode, results);
					return results;
				}
				continue;
			}
			String inputSubstring;
			if (edge.getKey().first == edge.getKey().second - 1)
				inputSubstring = String.valueOf(input.charAt(edge.getKey().first));
			else
				inputSubstring = input.substring(edge.getKey().first, edge.getKey().second - 1);
			
			if (pattern.substring(patternIndex)
					.startsWith(inputSubstring) || inputSubstring.startsWith(pattern.substring(patternIndex))) {
				results.addAll(findNode(edge.getValue(),
								edge.getKey().second,
								patternIndex + edge.getKey().second - edge.getKey().first));
			}
		}
		
		return results;
	}
	
	private void findLeaves(Node node, List<Integer> result) {
		if (node.edges.isEmpty())  {
			result.add(node.leafIndex);
		} else {
			for (Map.Entry<Tuple, Node> edge : node.edges.entrySet()) {
				findLeaves(edge.getValue(), result);
			}
		}
	}

	public int[] combineArrays(int[] array1, int[] array2) {
		if (array1.length == 0)
			return array2;
		if (array2.length == 0)
			return array1;
		int[] array1and2 = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, array1and2, 0, array1.length);
		System.arraycopy(array2, 0, array1and2, array1.length, array2.length);
		return array1and2;
	}
}
