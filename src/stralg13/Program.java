package stralg13;

import java.util.Map;

public class Program {
	public static void main(String [] args) {
		SuffixTree tree = new SuffixTree("abaababa");
		
		System.out.println(String.format("Tree size %d", ObjectSizeFetcher.getObjectSize(tree)));
		int a = 1;
		System.out.println(String.format("Int size %d", ObjectSizeFetcher.getObjectSize(a)));
		
		printNodeSize(tree.root);
    }
	
	public static void printNodeSize(Node node) {
		System.out.println(ObjectSizeFetcher.getObjectSize(node));
		for (Map.Entry<Tuple, Node> edge : node.edges.entrySet()) {
			printNodeSize(edge.getValue());
		}
	}
}
