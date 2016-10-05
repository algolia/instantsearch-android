package com.algolia.instantsearch.views;

import com.algolia.instantsearch.InstantSearchTest;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RefinementListTest extends InstantSearchTest {
    @Test(expected = IllegalStateException.class)
    public void shouldParseEmptyThrow() {
        String input = "";
        RefinementList.parseSortOrder(input);
    }

    @Test
    public void shouldParseNullToNull() {
        final ArrayList<String> output = RefinementList.parseSortOrder(null);
        Assert.assertEquals("Parsing a null string should result in a null List.", null, output);
    }

    @Test
    public void shouldParseValidString() {
        List<String> inputs = Arrays.asList(
                RefinementList.SORT_ISREFINED,
                RefinementList.SORT_NAME_ASC,
                RefinementList.SORT_NAME_DESC,
                RefinementList.SORT_COUNT_ASC,
                RefinementList.SORT_COUNT_DESC);
        for (String input : inputs) {
            ArrayList<String> output = RefinementList.parseSortOrder(input);
            Assert.assertEquals(String.format("Parsing a single valid value %s should result in a singletonList.", input), 1, output.size());
            Assert.assertTrue("The resulting list should contain the given sortOrder.", input.equals(output.get(0)));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldParseInvalidStringThrow() throws IllegalStateException {
        final String input = "invalidOption";
        RefinementList.parseSortOrder(input);
    }

    @Test
    public void shouldParseEmptyArrayToEmpty() {
        final ArrayList<String> output = RefinementList.parseSortOrder("[]");
        Assert.assertEquals("Parsing an empty array should result in an empty List.", 0, output.size());
    }

    @Test
    public void shouldParseValidArray() {
        final ArrayList<String> output = RefinementList.parseSortOrder("['count:asc', 'name:asc']");
        Assert.assertEquals("Parsing an array with two valid sortOrders should result in a List of two values.", 2, output.size());
        Assert.assertTrue("The output's first value should be 'count:asc'.", "count:asc".equals(output.get(0)));
        Assert.assertTrue("The output's second value should be 'name:asc'.", "name:asc".equals(output.get(1)));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldParseInvalidArrayThrow() throws IllegalStateException {
        final String input = "[foo, bar]";
        RefinementList.parseSortOrder(input);
    }
}