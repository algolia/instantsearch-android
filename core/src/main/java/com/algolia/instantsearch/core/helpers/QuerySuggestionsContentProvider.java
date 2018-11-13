package com.algolia.instantsearch.core.helpers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provides Search Suggestions through a ContentProvider.
 * <p>
 * Your subclass must provide
 * {@link QuerySuggestionsContentProvider#initIndex()}  an index} and specify {@link QuerySuggestionsContentProvider#getLimit() a limit}.
 */
public abstract class QuerySuggestionsContentProvider extends ContentProvider {

    public static final String[] COLUMN_NAMES = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA};

    private Index index;
    private boolean shouldReturnHighlightResult;

    /**
     * Returns an Index to search suggestions within.
     */
    protected abstract Index initIndex();

    /**
     * Returns the maximum number of suggestions to display.
     */
    protected abstract int getLimit();

    /**
     * If {@code true}, the suggestion will return the highlighting output to be used at display in your custom {@code suggestionRowLayout}.
     * This value will be sent to its TextView identified as <b>{@code @android:id/text1}</b> through {@link android.widget.TextView#setText(CharSequence) setText}.
     */
    protected boolean shouldReturnHighlightResult() {
        return false;
    }

    @Override
    public boolean onCreate() {
        index = initIndex();
        shouldReturnHighlightResult = shouldReturnHighlightResult();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(COLUMN_NAMES);
        final String query = uri.getLastPathSegment().toLowerCase();

        try {
            SearchResults results = new SearchResults(index.searchSync(new Query(query).setHitsPerPage(getLimit()).setAttributesToHighlight("query")));
            for (int i = 0; i < results.hits.length(); i++) {
                JSONObject hit = results.hits.getJSONObject(i);
                final String suggestion = hit.getString("query");
                if (!suggestion.equalsIgnoreCase(query)) {
                    String displaySuggestion = shouldReturnHighlightResult ? getHighlightedSuggestion(hit) : suggestion;
                    cursor.addRow(new Object[]{hit.getString("objectID").hashCode(), displaySuggestion, suggestion});
                }
            }
            return cursor;
        } catch (AlgoliaException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHighlightedSuggestion(JSONObject hit) throws JSONException {
        String displaySuggestion;
        final String highlightResult = hit.getJSONObject("_highlightResult").getJSONObject("query").getString("value");
        displaySuggestion = Highlighter.getDefault().inverseHighlight(highlightResult)
                .replace("<em>", "<b>").replace("</em>", "</b>");
        return displaySuggestion;
    }

    // region ContentProvider Stubs
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    //endregion
}
