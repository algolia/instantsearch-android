package com.algolia.instantsearch.ui.views;

import com.algolia.instantsearch.InstantSearchTest;
import com.algolia.instantsearch.core.model.FacetValue;

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
        assertThat("Parsing a null string should result in a null List.", output, nullValue());
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
            assertThat(String.format("Parsing a single valid value %s should result in a singletonList.", input), output, hasSize(1));
            assertThat("The resulting list should contain the given sortOrder.", output.get(0), is(input));
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
        assertThat("Parsing an empty array should result in an empty List.", output, hasSize(0));
    }

    @Test
    public void shouldParseValidArray() {
        final ArrayList<String> output = RefinementList.parseSortOrder("['count:asc', 'name:asc']");
        assertThat("Parsing an array with two valid sortOrders should result in a List of two values.", output, hasSize(2));
        assertThat("The output's first value should be 'count:asc'.", output.get(0), is("count:asc"));
        assertThat("The output's second value should be 'name:asc'.", output.get(1), is("name:asc"));
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