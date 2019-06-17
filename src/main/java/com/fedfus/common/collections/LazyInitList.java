package com.fedfus.common.collections;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.collections4.list.AbstractSerializableListDecorator;

import lombok.NonNull;

/**
 * @author F.Fusto
 * @param <E>
 */
public class LazyInitList<E> extends AbstractSerializableListDecorator<E> {

	private static final long serialVersionUID = -7234122450366672660L;

	@SuppressWarnings("rawtypes")
	private static final List NO_INIT = Collections.emptyList();

	private final Supplier<? extends List<E>> supplier;

	@SuppressWarnings("unchecked")
	public LazyInitList(@NonNull Supplier<? extends List<E>> supplier) {
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
	protected List<E> decorated() {
		List<E> result = super.decorated();
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

}
