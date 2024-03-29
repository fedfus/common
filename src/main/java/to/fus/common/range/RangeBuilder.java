package to.fus.common.range;

import to.fus.common.range.dto.RangeDTO;
import to.fus.common.range.interfaces.Interval;
import to.fus.common.range.interfaces.Manipulator;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @param <T>
 *
 * @author federico - Jun 22, 2019
 */
public class RangeBuilder<T> {

	private final T rangeStart;
	private T lastValue = null;
	private T expected;
	private RangeDTO<T> last;

	private boolean primoElaborato;

	private List<RangeDTO<T>> availableRange;
	@Getter
	private Manipulator<T> manipulator;
	@Getter
	private Comparator<T> comparator;

	///////////////////////////////////////////////////////
	///////////////////// COSTRUTTORE /////////////////////
	///////////////////////////////////////////////////////

	/**
	 * @param rangeStart
	 * 		range start
	 * @param rangeEnd
	 * 		range end
	 * @param manipulator
	 * 		manipulator
	 * @param comparator
	 * 		comparator
	 */
	public RangeBuilder(@NonNull T rangeStart, @NonNull T rangeEnd, @NonNull Manipulator<T> manipulator,
			@NonNull Comparator<T> comparator) {
		if (comparator.compare(rangeStart, rangeEnd) > 0) {
			throw new IllegalArgumentException("rangeStart must be lower or equals than rangeEnd");
		}
		this.rangeStart = rangeStart;
		this.manipulator = manipulator;
		this.comparator = comparator;
		expected = rangeStart;
		availableRange = new ArrayList<>();
		last = new RangeDTO<>(rangeStart, rangeEnd, comparator);
		primoElaborato = true;
	}

	/**
	 * @param interval
	 * 		interval
	 * @param manipulator
	 * 		manipulator
	 */
	public RangeBuilder(Interval<T> interval, Manipulator<T> manipulator, Comparator<T> comparator) {
		this(interval.getStart(), interval.getEnd(), manipulator, comparator);
	}

	/**
	 * @param interval
	 * 		interval
	 */
	public <I extends Interval<T> & Manipulator<T> & Comparator<T>> RangeBuilder(I interval) {
		this(interval, interval, interval);
	}

	///////////////////////////////////////////////////////
	////////////////// METODI PUBBLICI ////////////////////
	///////////////////////////////////////////////////////

	/**
	 * @param item
	 * 		item
	 *
	 * @throws Exception
	 * 		exception
	 */
	public void addItem(T item) {
		if (item != null) {
			if (primoElaborato && comparator.compare(rangeStart, item) > 0) {
				// nel caso in cui si vada ad aggiungere un item minore di rangeStart,
				// questo viene utilizzato come valore atteso
				expected = item;
				primoElaborato = false;
			}
			if (comparator.compare(expected, item) != 0) {
				// se il valore atteso e' diverso da quello corrente
				if (comparator.compare(item, lastValue) == 0) {
					// controllo se e' uguale al precedente gia' elaborato. Se si, allora si tratta di duplicato
					return;
				}
				// altrimenti lo aggiungo nel range
				availableRange.add(new RangeDTO<>(expected, manipulator.decrease(item), comparator));
			}
			lastValue = item;
			expected = manipulator.increase(item);
			last.setRangeStart(expected);
		}
	}

	/**
	 * @param item
	 * 		item
	 * @param function
	 * 		function
	 */
	public <X> void addItem(X item, Function<X, T> function) {
		addItem(function.apply(item));
	}

	/**
	 * @param items
	 * 		items
	 */
	public void addItems(List<T> items) {
		for (T item : items) {
			addItem(item);
		}
	}

	/**
	 * @param items
	 * 		items
	 * @param function
	 * 		function
	 */
	public <X> void addItems(List<X> items, Function<X, T> function) {
		for (X item : items) {
			addItem(item, function);
		}
	}

	/**
	 * @param transformer
	 * 		transformer
	 *
	 * @return available range
	 */
	public <X> List<Range<X>> getAvailableRange(Function<T, X> transformer, Comparator<X> comparator) {
		return getAvailableRangeDTO().stream().map(rdto -> Range
				.between(transformer.apply(rdto.getRangeStart()), transformer.apply(rdto.getRangeEnd()), comparator))
				.collect(Collectors.toList());
	}

	/**
	 * @return available range
	 */
	public List<Range<T>> getAvailableRange() {
		return getAvailableRange(Function.identity(), comparator);
	}

	/**
	 * @param transformer
	 * 		transformer
	 * @param manipulator
	 * 		manipulator
	 * @param comparator
	 * 		comparator
	 *
	 * @return range
	 */
	public <X> RangeUtils<X> createRangeUtils(Function<T, X> transformer, Manipulator<X> manipulator,
			Comparator<X> comparator) {
		return new RangeUtils<>(getAvailableRange(transformer, comparator), manipulator, comparator);
	}

	/**
	 * @return range utils
	 */
	public RangeUtils<T> createRangeUtils() {
		return new RangeUtils<>(getAvailableRange(), this.manipulator, this.comparator);
	}

	///////////////////////////////////////////////////////
	////////////////// METODI PRIVATI /////////////////////
	///////////////////////////////////////////////////////

	/**
	 * @return the availableRange
	 */
	private List<RangeDTO<T>> getAvailableRangeDTO() {
		if (comparator.compare(last.getRangeStart(), last.getRangeEnd()) <= 0) {
			availableRange.add(last);
		}
		return availableRange;
	}
}
