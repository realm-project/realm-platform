package net.objectof.model.query.fallback;

public abstract class BiPredicate<T, U> {
	
	public abstract boolean test(T t, U u) throws UnsupportedOperationException;
	
	
	public BiPredicate<T, U> and(final BiPredicate<? super T, ? super U> other) {
		return new BiPredicate<T, U>() {

			@Override
			public boolean test(T t, U u) throws UnsupportedOperationException {
				return BiPredicate.this.test(t, u) && other.test(t, u);
			}
		};
	}

	public BiPredicate<T, U> or(final BiPredicate<? super T, ? super U> other) {
		return new BiPredicate<T, U>() {

			@Override
			public boolean test(T t, U u) throws UnsupportedOperationException {
				return BiPredicate.this.test(t, u) || other.test(t, u);
			}
		};
	}
	
	public BiPredicate<T, U> negate() {
		return new BiPredicate<T, U>() {

			@Override
			public boolean test(T t, U u) throws UnsupportedOperationException {
				return !BiPredicate.this.test(t, u);
			}};
	}
	

}
