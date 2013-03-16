package generics;

/**
 * Represents a pair of two data types
 * 
 * @author Alex
 * 
 * @param <L>
 *            The left data type
 * @param <R>
 *            The right data type
 */
public class MutablePair<L, R> {

	private L left;
	private R right;

	public MutablePair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}
	
	public void setLeft(L left) {
		this.left = left;
	}

	public void setRight(R right) {
		this.right = right;
	}

	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof ImmutablePair))
			return false;
		ImmutablePair<?, ?> pairo = (ImmutablePair<?, ?>) o;
		return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
	}

}
