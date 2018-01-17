package com.algolia.instantsearch.ui.views;

import com.algolia.instantsearch.InstantSearchTest;
import com.algolia.instantsearch.model.FacetValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RefinementListTest extends InstantSearchTest {
    private RefinementList mRefinementList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityController<TestActivity> activityController = Robolectric.buildActivity(TestActivity.class).create().start();
        mRefinementList = activityController.get().getRefinementList();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldParseEmptyThrow() {
        String input = "";
        RefinementList.parseSortOrder(input);
    }

    @Test
    public void shouldParseNullToNull() {
        final ArrayList<String> output = RefinementList.parseSortOrder(null);
        //noinspection ConstantConditions warning means test passes for IDE ;)
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

    @Test
    public void shouldSortWithLessValuesThanLimit() {
        //Given one facet value, and a RefinementList with limit=10
        final RefinementList.FacetAdapter facetAdapter = (RefinementList.FacetAdapter) mRefinementList.getAdapter();
        facetAdapter.addAll(Collections.singletonList(new FacetValue("foo", 1)));

        // Expect successful sort and one facet stored
        facetAdapter.sort(mRefinementList.getSortComparator());
        assertThat(facetAdapter.getCount(), is(1));
    }

    @Test
    public void shouldSortWithMoreValuesThanLimit() {
        //Given two facet value, and a RefinementList with limit=1
        final RefinementList.FacetAdapter facetAdapter = (RefinementList.FacetAdapter) mRefinementList.getAdapter();
        facetAdapter.addAll(Arrays.asList(new FacetValue("foo", 1), new FacetValue("bar", 2)));
        mRefinementList.setLimit(1);

        // Expect successful sort and one facet stored
        facetAdapter.sort(mRefinementList.getSortComparator());
        assertThat(facetAdapter.getCount(), is(1));
    }
}