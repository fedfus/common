package to.fus.common.range;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import to.fus.common.range.utils.Intervals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangeUtilsTest {

	@Test
	@DisplayName("test creazione range 1-100")
	void testCreazione() {
		RangeBuilder<Long> builder = new RangeBuilder<>(Intervals.of(1L, 100L, x -> ++x, x -> --x));
		RangeUtils<Long> rangeUtils = builder.createRangeUtils();
		assertTrue(rangeUtils.hasAvailableValues());
		assertEquals(1L, rangeUtils.getFirstAvailableValue());
		assertTrue(rangeUtils.isInRange(100L));
	}

	@Test
	@DisplayName("test definizione errata range")
	void testCreazioneErrata() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new RangeBuilder<>(Intervals.of(1, -1, x -> ++x, x -> --x)));
	}

	@Test
	@DisplayName("test gestione range")
	void testRange() {
		RangeBuilder<Integer> builder = new RangeBuilder<>(Intervals.of(25, 29, x -> ++x, x -> --x));
		RangeUtils<Integer> rangeUtils = builder.createRangeUtils();
		Assertions.assertEquals(26, rangeUtils.getElementFromRange(26));
		Assertions.assertEquals(25, rangeUtils.getElementFromRange(26));
		Assertions.assertEquals(27, rangeUtils.getFirstAvailableValue());
	}

	@Test
	@DisplayName("no more space test")
	void testNoMoreSpace() {
		RangeUtils<Character> rangeUtils = new RangeBuilder<>(Intervals.of('a', 'a', x -> ++x, x -> --x)).createRangeUtils();
		Assertions.assertEquals('a', rangeUtils.getElementFromRange('a'));
		Assertions.assertNull(rangeUtils.getFirstAvailableValue());
	}

}
