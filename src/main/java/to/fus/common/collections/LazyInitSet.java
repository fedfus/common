package to.fus.common.collections;

import lombok.NonNull;
import org.apache.commons.collections4.set.AbstractSetDecorator;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author F.Fusto
 *
 * @param <E>
 */
public class LazyInitSet<E> extends AbstractSetDecorator<E> implements Serializable {

	private static final long serialVersionUID = 3846152162804021329L;

	@SuppressWarnings("rawtypes")
	private static final Set NO_INIT = Collections.emptySet();

	private final transient Supplier<? extends Set<E>> supplier;

	@SuppressWarnings("unchecked")
	public LazyInitSet(@NonNull Supplier<? extends Set<E>> supplier) {
		super(NO_INIT);
		this.supplier = supplier;
	}

	// -----------------------------------------------------------------------

	/*
	 * inizializzazione al primo accesso del set tramite Supplier definito da costruttore
	 * (non-Javadoc)
	 * @see org.apache.commons.collections4.set.AbstractSetDecorator#decorated()
	 */
	@Override
	protected Set<E> decorated() {
		Set<E> result = super.decorated();
		if (result == NO_INIT) {
			synchronized (this) {
				// effettuo un ulteriore controllo
				result = super.decorated();
				if (result == NO_INIT) {
					result = supplier.get();
					super.setCollection(result);
				}
			}
		}
		return result;
	}

	/**
	 * consente di ricaricare i valori forzando il reload dal supplier al prossimo accesso al decorator.
	 */
	@SuppressWarnings("unchecked")
	public final void reload() {
		super.setCollection(NO_INIT);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.collections4.set.AbstractSetDecorator#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

}
