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
import android.util.Log;

import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class QuerySuggestionsContentProvider extends ContentProvider {

    public static final String[] COLUMN_NAMES = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA};

    protected abstract Index getIndex();

    /**
     * Returns the maximum number of usggestions to display.
     */
    protected abstract int getLimit();

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("QSCP", "query called with uri " + uri);
        MatrixCursor cursor = new MatrixCursor(COLUMN_NAMES);

        try {
            SearchResults results = new SearchResults(getIndex().searchSync(new Query(uri.getLastPathSegment().toLowerCase()).setHitsPerPage(getLimit())));
            final int nbHits = results.hits.length();
            Log.e("QSCP", "query found " + nbHits + " hits.");
            for (int i = 0; i < nbHits; i++) {
                JSONObject hit = results.hits.getJSONObject(i);
                final String hitQuery = hit.getString("query");
                cursor.addRow(new Object[]{hit.getString("objectID").hashCode(), hitQuery, hitQuery});
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
