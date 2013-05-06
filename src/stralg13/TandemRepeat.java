package stralg13;

public class TandemRepeat implements Comparable<TandemRepeat>{

	int index;
	int length;
	boolean branching;
	
	
	public TandemRepeat(int index, int length, boolean branching) {
		super();
		this.index = index;
		this.length = length;
		this.branching = branching;
	}
	@Override
	public String toString() {
		return "TandemResult [index=" + index + ", length=" + length + ", branching=" + branching + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 111;
		int result = 1;
		result = prime * result + index;
		result = prime * result + length;
		result = prime * result + (branching ? 1 : 0);
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
		TandemRepeat other = (TandemRepeat) obj;
		if (index != other.index)
			return false;
		if (length != other.length)
			return false;
		if (branching != other.branching)
			return false;
		return true;
	}
	@Override
	public int compareTo(TandemRepeat o) {
		if (index != o.index)
			return index - o.index;
		return length - o.length;
	}
	
	
}
