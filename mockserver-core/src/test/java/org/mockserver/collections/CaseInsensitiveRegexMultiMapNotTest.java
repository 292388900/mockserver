package org.mockserver.collections;

import org.junit.Test;
import org.mockserver.model.NottableString;

import java.util.Arrays;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockserver.model.NottableString.not;
import static org.mockserver.model.NottableString.string;
import static org.mockserver.model.NottableString.strings;

/**
 * @author jamesdbloom
 */
public class CaseInsensitiveRegexMultiMapNotTest {

    @Test
    public void shouldSupportRemovingAllValues() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(strings("one_one", "one_two", "one_three"), circularMultiMap.removeAll(string("one")));
        assertNull(circularMultiMap.removeAll(string("three")));

        // then
        assertEquals(1, circularMultiMap.size());
        // - should have correct keys
        assertFalse(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertFalse(circularMultiMap.containsValue("one_two"));
        assertFalse(circularMultiMap.containsValue("one_three"));
        assertTrue(circularMultiMap.containsValue("two"));
        assertEquals(strings("two"), circularMultiMap.values());
        // - should have correct values per key
        assertNull(circularMultiMap.get("one"));
        assertEquals(string("two"), circularMultiMap.get("two"));
        assertEquals(0, circularMultiMap.getAll("one").size());
        assertEquals(strings("two"), circularMultiMap.getAll("two"));
    }

    @Test
    public void shouldSupportNotRemovingAllValues() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(strings("two"), circularMultiMap.removeAll(not("one")));
        assertEquals(strings("one_one", "one_two", "one_three"), circularMultiMap.removeAll(not("three")));

        // then
        assertEquals(0, circularMultiMap.size());
        // - should have correct keys
        assertFalse(circularMultiMap.containsKey("one"));
        assertFalse(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), empty());
    }

    @Test
    public void shouldSupportRemovingAValue() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(string("one_one"), circularMultiMap.remove(string("one")));
        assertNull(circularMultiMap.remove(string("three")));

        // then
        assertEquals(2, circularMultiMap.size());
        // - should have correct keys
        assertTrue(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "one", "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertTrue(circularMultiMap.containsValue("one_two"));
        assertTrue(circularMultiMap.containsValue("one_three"));
        assertTrue(circularMultiMap.containsValue("two"));
        assertEquals(strings("one_two", "one_three", "two"), circularMultiMap.values());
        // - should have correct values per key
        assertEquals(string("one_two"), circularMultiMap.get("one"));
        assertEquals(string("two"), circularMultiMap.get("two"));
        assertEquals(strings("one_two", "one_three"), circularMultiMap.getAll("one"));
        assertEquals(strings("two"), circularMultiMap.getAll("two"));
    }

    @Test
    public void shouldSupportNotRemovingAValue() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(string("two"), circularMultiMap.remove(not("one")));
        assertEquals(string("one_one"), circularMultiMap.remove(not("three")));

        // then
        assertEquals(2, circularMultiMap.size());
        // - should have correct keys
        assertTrue(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "one", "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertTrue(circularMultiMap.containsValue("one_two"));
        assertTrue(circularMultiMap.containsValue("one_three"));
        assertFalse(circularMultiMap.containsValue("two"));
        assertEquals(strings("one_two", "one_three"), circularMultiMap.values());
    }

    @Test
    public void shouldSupportRemovingAValueWithRegex() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(string("one_one"), circularMultiMap.remove(string("o[a-z]{2}")));
        assertNull(circularMultiMap.remove(string("t[a-z]{3}")));

        // then
        assertEquals(2, circularMultiMap.size());
        // - should have correct keys
        assertTrue(circularMultiMap.containsKey("o.*"));
        assertTrue(circularMultiMap.containsKey("T[a-z]{2}"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "one", "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertTrue(circularMultiMap.containsValue("one_two"));
        assertTrue(circularMultiMap.containsValue("one_three"));
        assertTrue(circularMultiMap.containsValue("two"));
        assertEquals(strings("one_two", "one_three", "two"), circularMultiMap.values());
        // - should have correct values per key
        assertEquals(string("one_two"), circularMultiMap.get(".*n.*"));
        assertEquals(string("two"), circularMultiMap.get(".*o"));
        assertEquals(strings("one_two", "one_three"), circularMultiMap.getAll("one"));
        assertEquals(strings("two"), circularMultiMap.getAll("two"));
    }

    @Test
    public void shouldSupportNotRemovingAValueWithRegex() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(string("two"), circularMultiMap.remove(not("o[a-z]{2}")));
        assertEquals(string("one_one"), circularMultiMap.remove(not("t[a-z]{3}")));

        // then
        assertEquals(2, circularMultiMap.size());
        // - should have correct keys
        assertTrue(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "one", "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertTrue(circularMultiMap.containsValue("one_two"));
        assertTrue(circularMultiMap.containsValue("one_three"));
        assertFalse(circularMultiMap.containsValue("two"));
        assertEquals(strings("one_two", "one_three"), circularMultiMap.values());
    }

    @Test
    public void shouldSupportRemovingAValueWithCaseInsensitivity() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(string("one_one"), circularMultiMap.remove(string("ONE")));
        assertNull(circularMultiMap.remove(string("three")));

        // then
        assertEquals(2, circularMultiMap.size());
        // - should have correct keys
        assertTrue(circularMultiMap.containsKey("oNE"));
        assertTrue(circularMultiMap.containsKey("Two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "one", "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertTrue(circularMultiMap.containsValue("one_two"));
        assertTrue(circularMultiMap.containsValue("one_three"));
        assertTrue(circularMultiMap.containsValue("two"));
        assertEquals(strings("one_two", "one_three", "two"), circularMultiMap.values());
        // - should have correct values per key
        assertEquals(string("one_two"), circularMultiMap.get("oNe"));
        assertEquals(string("two"), circularMultiMap.get("twO"));
        assertEquals(strings("one_two", "one_three"), circularMultiMap.getAll("one"));
        assertEquals(strings("two"), circularMultiMap.getAll("two"));
    }

    @Test
    public void shouldSupportNotRemovingAValueWithCaseInsensitivity() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(string("two"), circularMultiMap.remove(not("ONE")));
        assertEquals(string("one_one"), circularMultiMap.remove(not("three")));

        // then
        assertEquals(2, circularMultiMap.size());
        // - should have correct keys
        assertTrue(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "one", "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertTrue(circularMultiMap.containsValue("one_two"));
        assertTrue(circularMultiMap.containsValue("one_three"));
        assertFalse(circularMultiMap.containsValue("two"));
        assertEquals(strings("one_two", "one_three"), circularMultiMap.values());
    }

    @Test
    public void shouldSupportRemovingAllValuesWithRegex() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(strings("one_one", "one_two", "one_three"), circularMultiMap.removeAll(string("o[a-z]{2}")));
        assertNull(circularMultiMap.removeAll(string("t[a-z]{3}")));

        // then
        assertEquals(1, circularMultiMap.size());
        // - should have correct keys
        assertFalse(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertFalse(circularMultiMap.containsValue("one_two"));
        assertFalse(circularMultiMap.containsValue("one_three"));
        assertTrue(circularMultiMap.containsValue("two"));
        assertEquals(strings("two"), circularMultiMap.values());
        // - should have correct values per key
        assertNull(circularMultiMap.get("one"));
        assertEquals(string("two"), circularMultiMap.get("two"));
        assertEquals(0, circularMultiMap.getAll("one").size());
        assertEquals(strings("two"), circularMultiMap.getAll("two"));
    }

    @Test
    public void shouldSupportNotRemovingAllValuesWithRegex() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(strings("two"), circularMultiMap.removeAll(not("o[a-z]{2}")));
        assertEquals(strings("one_one", "one_two", "one_three"), circularMultiMap.removeAll(not("t[a-z]{3}")));

        // then
        assertEquals(0, circularMultiMap.size());
        // - should have correct keys
        assertFalse(circularMultiMap.containsKey("one"));
        assertFalse(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), empty());
    }

    @Test
    public void shouldSupportRemovingAllValuesWithCaseInsensitivity() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(strings("one_one", "one_two", "one_three"), circularMultiMap.removeAll(string("ONE")));
        assertNull(circularMultiMap.removeAll(string("three")));

        // then
        assertEquals(1, circularMultiMap.size());
        // - should have correct keys
        assertFalse(circularMultiMap.containsKey("one"));
        assertTrue(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), containsInAnyOrder((Object) "two"));
        // - should have correct values
        assertFalse(circularMultiMap.containsValue("one_one"));
        assertFalse(circularMultiMap.containsValue("one_two"));
        assertFalse(circularMultiMap.containsValue("one_three"));
        assertTrue(circularMultiMap.containsValue("two"));
        assertEquals(strings("two"), circularMultiMap.values());
        // - should have correct values per key
        assertNull(circularMultiMap.get("one"));
        assertEquals(string("two"), circularMultiMap.get("two"));
        assertEquals(0, circularMultiMap.getAll("one").size());
        assertEquals(strings("two"), circularMultiMap.getAll("two"));
    }

    @Test
    public void shouldSupportNotRemovingAllValuesWithCaseInsensitivity() {
        // given
        CaseInsensitiveRegexMultiMap circularMultiMap = new CaseInsensitiveRegexMultiMap();
        circularMultiMap.put("one", "one_one");
        circularMultiMap.put("one", "one_two");
        circularMultiMap.put("one", "one_three");
        circularMultiMap.put("two", "two");

        // when
        assertEquals(strings("two"), circularMultiMap.removeAll(not("ONE")));
        assertEquals(strings("one_one", "one_two", "one_three"), circularMultiMap.removeAll(not("three")));

        // then
        assertEquals(0, circularMultiMap.size());
        // - should have correct keys
        assertFalse(circularMultiMap.containsKey("one"));
        assertFalse(circularMultiMap.containsKey("two"));
        assertThat(circularMultiMap.keySet(), empty());
    }
}
