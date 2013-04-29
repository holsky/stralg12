package stralg13;

public class SlowScanResult {
	
	Node node;
	Tuple edge;
	int index;
	
	public static SlowScanResult makeNodeResult(Node node, int occurenceIndex) {
		return new SlowScanResult(node, null, occurenceIndex);
	}
	
	public static SlowScanResult makeEdgeResult(Node node, Tuple edge, int occurenceIndex) {
		return new SlowScanResult(node, edge, occurenceIndex);
	}
	
	private SlowScanResult(Node node, Tuple edge, int index) {
		super();
		this.node = node;
		this.edge = edge;
		this.index = index;
	}
	
	public boolean isANode() {
		return node != null && edge == null && index == 0;
	}
	
	public boolean isAnEdge() {
		return node != null && edge != null && index != 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edge == null) ? 0 : edge.hashCode());
		result = prime * result + index;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SlowScanResult other = (SlowScanResult) obj;
		if (edge == null) {
			if (other.edge != null)
				return false;
		} else if (!edge.equals(other.edge))
			return false;
		if (index != other.index)
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SlowScanResult [node=" + node + ", edge=" + edge + ", index="
				+ index + "]";
	}
	
	
	
}
