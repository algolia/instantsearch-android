package com.algolia.instantsearch.helpers;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides Search Suggestions through a ContentProvider.
 * <p>
 * Your subclass must provide
 * {@link QuerySuggestionsContentProvider#getIndex() an index} and specify {@link QuerySuggestionsContentProvider#getLimit() a limit}.
 */
public abstract class QuerySuggestionsContentProvider extends ContentProvider {

    public static final String[] COLUMN_NAMES = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA};

    /**
     * Returns an Index to search suggestions within.
     */
    protected abstract Index getIndex();

    /**
     * Returns the maximum number of suggestions to display.
     */
    protected abstract int getLimit();

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(COLUMN_NAMES);
        final String query = uri.getLastPathSegment().toLowerCase();

        try {
            SearchResults results = new SearchResults(getIndex().searchSync(new Query(query).setHitsPerPage(getLimit())));
            for (int i = 0; i < results.hits.length(); i++) {
                JSONObject hit = results.hits.getJSONObject(i);
                final String suggestion = hit.getString("query");
                if (!suggestion.equalsIgnoreCase(query)) {
                    cursor.addRow(new Object[]{hit.getString("objectID").hashCode(), suggestion, suggestion});
                }
            }
            return cursor;
        } catch (AlgoliaException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

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
}
