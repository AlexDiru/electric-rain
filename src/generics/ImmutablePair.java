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
public class ImmutablePair<L, R> {

	private final L left;
	private final R right;

	public ImmutablePair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
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
