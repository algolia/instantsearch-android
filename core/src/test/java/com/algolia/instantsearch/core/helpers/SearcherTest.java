package com.algolia.instantsearch.core.helpers;

import com.algolia.instantsearch.Helpers;
import com.algolia.instantsearch.InstantSearchTest;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.NumericRefinement;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.Client;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import java.util.Locale;

import androidx.annotation.NonNull;

public class SearcherTest extends InstantSearchTest {
    @NonNull private Client initClient() {
        return new Client(Helpers.app_id, Helpers.api_key);
    }

    @NonNull
    private Searcher initSearcher() {
        final Client client = initClient();
        return Searcher.create(client.getIndex(Helpers.safeIndexName("test")));
    }

    @After
    public void tearDown() {
        Searcher.destroyAll();
    }

    @Test(expected = IllegalStateException.class)
    public void instantiatesDuplicateNoVariantThrows() {
        final String indexName = "asd";
        final Client client = initClient();
        Searcher.create(client.getIndex(indexName));
        Searcher.create(client.getIndex(indexName));
    }

    @Test public void instantiatesAccordingToVariant() {
        final String indexName = "asd";
        final Client client = initClient();
        Searcher searcher = Searcher.create(client.getIndex(indexName), "foo");
        Searcher searcher2 = Searcher.create(client.getIndex(indexName), "foo");
        Assert.assertSame("Two searchers with same indexNames and variants should be the same instance.", searcher, searcher2);

        searcher = Searcher.create(client.getIndex(indexName));
        searcher2 = Searcher.create(client.getIndex(indexName + "_foo"));
        Assert.assertNotSame("Two searchers with different indexNames and no variants should be different instance.", searcher, searcher2);

        searcher = Searcher.create(client.getIndex(indexName), "variant");
        searcher2 = Searcher.create(client.getIndex("bar"), "variant");
        Assert.assertNotSame("Two searchers with different indexNames and same variants should be different instance.", searcher, searcher2);
        Assert.assertSame("A second searcher with the same variant should replace the first one", searcher2, Searcher.get("variant"));


        searcher = Searcher.create(client.getIndex(indexName), "variant1");
        searcher2 = Searcher.create(client.getIndex(indexName), "variant2");
        Assert.assertNotSame("Two searchers with the same indexName but different variants should be different instances.", searcher, searcher2);

        searcher = Searcher.create(client.getIndex(indexName), "variant1");
        searcher2 = Searcher.create(client.getIndex("bar"), "variant2");
        Assert.assertNotSame("Two searchers with different indexNames and variants should be different instances.", searcher, searcher2);
    }

    @Test(expected = IllegalStateException.class)
    public void instantiatesMissingVariant() {
        final String indexName = "asd";
        final Client client = initClient();
        Searcher.create(client.getIndex(indexName));
        Searcher.create(client.getIndex(indexName));
        Assert.fail("Instantiating two searchers with the same indexName and no variants should throw.");
    }

    @Test
    public void hasHitsFalseIfEmpty() {
        boolean output = Searcher.hasHits(null);
        //noinspection ConstantConditions warning means test passes for IDE ;)
        Assert.assertFalse("hasHits should return false on null input", output);
    }

    @Test
    public void hasHitsFalseIfNoHits() throws JSONException {
        JSONObject input = new JSONObject("{'name':'foo'}");
        boolean output = Searcher.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with no hits", output);
    }

    @Test
    public void hasHitsFalseIfNullHitsArray() throws JSONException {
        JSONObject input = new JSONObject("{'hits':null}");
        boolean output = Searcher.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with null hits array", output);
    }

    @Test
    public void hasHitsFalseIfNullHits() throws JSONException {
        JSONObject input = new JSONObject("{'hits':[null, null]}");
        boolean output = Searcher.hasHits(input);
        Assert.assertFalse("hasHits should return false on input with null hits", output);
    }

    @Test
    public void hasHitsTrueIfHits() throws JSONException {
        JSONObject input = new JSONObject("{'hits':[{'name':'foo'}]}");
        boolean output = Searcher.hasHits(input);
        Assert.assertTrue("hasHits should return true on input with some hits", output);
    }

    @Test
    public void canCancelPendingRequests() {
        Searcher searcher = initSearcher();
        final AlgoliaResultsListener resultListener = new AlgoliaResultsListener() {
            @Override
            public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
                Assert.fail("The request should have been cancelled.");
            }
        };
        searcher.registerResultListener(resultListener);
        searcher.search();
        Assert.assertTrue("There should be a pending request", searcher.hasPendingRequests());
        searcher.cancelPendingRequests();
    }

    @Test
    public void numericRefinements() {
        Searcher searcher = initSearcher();
        final NumericRefinement r = new NumericRefinement("attribute", NumericRefinement.OPERATOR_EQ, 42);
        final NumericRefinement r2 = new NumericRefinement("attribute", NumericRefinement.OPERATOR_NE, 42);
        final String formattedValue = String.format(Locale.US, "%f", 42f);

        searcher.addNumericRefinement(r);
        Assert.assertEquals("There should be a numeric refinement for attribute", r, searcher.getNumericRefinement(r.attribute, r.operator));
        Assert.assertEquals("Query numericFilters should represent the refinement", "[\"attribute=" + formattedValue + "\"]", searcher.getQuery().getNumericFilters().toString());

        searcher.removeNumericRefinement(r);
        Assert.assertEquals("This numeric refinement should have been removed.", null, searcher.getNumericRefinement(r.attribute, r.operator));
        Assert.assertEquals("Query numericFilters should be empty after removal", "[]", searcher.getQuery().getNumericFilters().toString());

        searcher.addNumericRefinement(r);
        searcher.addNumericRefinement(r2);
        Assert.assertEquals("Query numericFilters should represent both refinements", "[\"attribute=" + formattedValue + "\",\"attribute!=" + formattedValue + "\"]", searcher.getQuery().getNumericFilters().toString());

        searcher.removeNumericRefinement(r.attribute);
        Assert.assertEquals("Both numeric refinements for this attribute should have been removed", null, searcher.getNumericRefinement(r.attribute, r.operator));
        Assert.assertEquals("Query numericFilters should be empty after removal", "[]", searcher.getQuery().getNumericFilters().toString());

        searcher.addNumericRefinement(r);
        searcher.addNumericRefinement(r2);
        searcher.removeNumericRefinement(r.attribute, r.operator);
        Assert.assertEquals("The numeric refinement for this attribute/operator pair should have been removed", null, searcher.getNumericRefinement(r.attribute, r.operator));
        Assert.assertEquals("The numeric refinement for this attribute but other operator should have been kept", r2, searcher.getNumericRefinement(r2.attribute, r2.operator));
    }

    @SuppressWarnings("deprecation")
    // deprecated facetFilters are used on purpose for filters managed programmatically
    @Test
    public void facetRefinements() {
        final Searcher searcher = initSearcher();
        searcher.addFacetRefinement("attribute", "foo");
        Assert.assertEquals("facetFilters should represent the refinement", "[\"attribute:foo\"]", searcher.getQuery().getFacetFilters().toString());
        Assert.assertTrue("hasFacetRefinement should return true for attribute/foo", searcher.hasFacetRefinement("attribute", "foo"));

        searcher.removeFacetRefinement("attribute", "foo");
        Assert.assertEquals("facetFilters should not contain the refinement after removeFacetRefinement()", "[]", searcher.getQuery().getFacetFilters().toString());
        Assert.assertFalse("hasFacetRefinement should return false for attribute/foo", searcher.hasFacetRefinement("attribute", "foo"));

        searcher.updateFacetRefinement("attribute", "foo", true);
        Assert.assertEquals("facetFilters should represent again the refinement", "[\"attribute:foo\"]", searcher.getQuery().getFacetFilters().toString());

        searcher.updateFacetRefinement("attribute", "foo", false);
        Assert.assertEquals("facetFilters should not contain the refinement after updateFacetRefinement(false)", "[]", searcher.getQuery().getFacetFilters().toString());

        searcher.addFacetRefinement("attribute", "foo");
        searcher.addFacetRefinement("attribute", "bar");
        Assert.assertTrue("facetFilters should contain the first refinement", searcher.getQuery().getFacetFilters().toString().contains("attribute:foo"));
        Assert.assertTrue("facetFilters should contain the second refinement", searcher.getQuery().getFacetFilters().toString().contains("attribute:bar"));

        searcher.removeFacetRefinement("attribute", "foo");
        Assert.assertFalse("facetFilters should not contain the first refinement anymore", searcher.getQuery().getFacetFilters().toString().contains("attribute:foo"));
        Assert.assertTrue("facetFilters should still contain the second refinement", searcher.getQuery().getFacetFilters().toString().contains("attribute:bar"));

        searcher.addFacetRefinement("attribute", "foo");
        searcher.addFacetRefinement("other", "baz");
        searcher.clearFacetRefinements("attribute");
        Assert.assertFalse("facetFilters should have no more refinements on attribute", searcher.getQuery().getFacetFilters().toString().contains("attribute"));
        Assert.assertTrue("facetFilters should still contain the other attribute's refinement", searcher.getQuery().getFacetFilters().toString().contains("other:baz"));
    }

    @Test
    public void addRemoveFacet() {
        final Searcher searcher = initSearcher();

        // add one facet
        searcher.addFacet("foo");
        String[] facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should contain one facet.", 1, facets.length);
        Assert.assertEquals("The query's facet should be `foo`.", "foo", facets[0]);

        // add a facet twice
        searcher.addFacet("foo");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should still contain one facet.", 1, facets.length);
        Assert.assertEquals("The query's facet should still be `foo`.", "foo", facets[0]);

        // remove one facet requested twice
        searcher.removeFacet("foo");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should still contain one facet.", 1, facets.length);
        Assert.assertEquals("The query's facet should still be `foo`.", "foo", facets[0]);

        // add a second facet
        searcher.addFacet("bar");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should contain two facets.", 2, facets.length);
        Assert.assertEquals("The query's second facet should be `bar`.", "bar", facets[1]);

        // remove a facet
        searcher.removeFacet("foo");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should now contain one facet.", 1, facets.length);
        Assert.assertEquals("The query's facet should now be `bar`.", "bar", facets[0]);


        // add several facets
        searcher.addFacet("foo", "baz");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should now contain three facets.", 3, facets.length);

        // remove several facets
        searcher.removeFacet("foo", "bar", "baz");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should now contain no facets.", 0, facets.length);

        // delete a facet
        searcher.addFacet("foo").addFacet("foo");
        searcher.deleteFacet("foo");
        facets = searcher.getQuery().getFacets();
        Assert.assertEquals("The query should now contain no facets.", 0, facets.length);
    }

}
