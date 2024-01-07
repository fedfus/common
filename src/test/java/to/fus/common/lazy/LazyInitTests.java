package to.fus.common.lazy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import to.fus.common.collections.LazyInitList;
import to.fus.common.collections.LazyInitSet;

import java.util.*;
import java.util.function.Supplier;

class LazyInitTests {
    private static int NO_INIT_COUNTER = 0;

    @BeforeEach
    void init() {
        NO_INIT_COUNTER = 0;
    }

    @Test
    void lazyInitMapTest() {
        LazyInitMap map = new LazyInitMap(initLazyMap());
        Assertions.assertEquals(0, NO_INIT_COUNTER);
        map.size();
        Assertions.assertNotEquals(0, NO_INIT_COUNTER);
    }

    @Test
    void lazyInitSetTest() {
        LazyInitSet<Integer> set = new LazyInitSet<>(initLazySet());
        Assertions.assertEquals(0, NO_INIT_COUNTER);
        set.size();
        Assertions.assertNotEquals(0, NO_INIT_COUNTER);
    }

    @Test
    void lazyInitListTest() {
        LazyInitList<Integer> list = new LazyInitList<>(initLazyList());
        Assertions.assertEquals(0, NO_INIT_COUNTER);
        list.size();
        Assertions.assertNotEquals(0, NO_INIT_COUNTER);
    }

    private Supplier<? extends Set<Integer>> initLazySet() {
        return () -> {
            Set<Integer> set = new HashSet<>();
            set.add(1);
            set.add(2);
            set.add(3);
            NO_INIT_COUNTER++;
            return set;
        };
    }

    private Supplier<? extends Map<String, Integer>> initLazyMap() {
        return () -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("1", 1);
            map.put("2", 2);
            map.put("3", 3);
            NO_INIT_COUNTER++;
            return map;
        };
    }

    private Supplier<? extends List<Integer>> initLazyList() {
        return () -> {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            list.add(2);
            list.add(3);
            NO_INIT_COUNTER++;
            return list;
        };
    }

}
