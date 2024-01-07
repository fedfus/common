/**
 * @author F.Fusto - 87000968
 * 
 *         7 giu 2019
 */
package to.fus.common.concurrent;

import lombok.Getter;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.function.Supplier;

/**
 * @author F.Fusto - 87000968
 *
 *         7 giu 2019
 * 
 */
public class LazyInit<T> extends LazyInitializer<T> {

	@Getter
	private volatile boolean initialized = false;
	private final Supplier<T> initSupplier;

	/**
	 * 
	 */
	public LazyInit(Supplier<T> initializer) {
		this.initSupplier = initializer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.lang3.concurrent.LazyInitializer#initialize()
	 */
	@Override
	protected T initialize() throws ConcurrentException {
		return initSupplier.get();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.lang3.concurrent.LazyInitializer#get()
	 */
	@Override
	public T get() throws ConcurrentException {
		T elem = super.get();
		initialized = true;
		return elem;
	}

}
