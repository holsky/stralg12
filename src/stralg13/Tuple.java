package stralg13;

public class Tuple {
	public int first, second;

	public Tuple(int first, int second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + first;
		result = prime * result + second;
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
		Tuple other = (Tuple) obj;
		if (first != other.first)
			return false;
		if (second != other.second)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Tuple [first=" + first + ", second=" + second + "]";
	}
	
	
}
