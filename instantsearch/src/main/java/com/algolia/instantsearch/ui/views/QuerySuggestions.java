package com.algolia.instantsearch.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helpers.Highlighter;
import com.algolia.instantsearch.model.SearchResults;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuerySuggestions extends Hits {

    /** Default maximum amount of suggestions to display. */
    private static final int DEFAULT_COUNT = 3;

    /** If true, the suggestions will use inverted highlighting to highlight individual differences. */
    private boolean highlighting;
    /** Maximum amount of suggestions to display. */
    private int count;
    /** The query for the current suggestion. */
    private String query;

    /**
     * Constructs a new QuerySuggestions with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public QuerySuggestions(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.QuerySuggestions, 0, 0);
        try {
            highlighting = styledAttributes.getBoolean(R.styleable.QuerySuggestions_highlighting, true);
            count = styledAttributes.getInt(R.styleable.QuerySuggestions_count, DEFAULT_COUNT);
        } finally {
            styledAttributes.recycle();
        }

        if (infiniteScrollListener != null) {
            removeOnScrollListener(infiniteScrollListener);
        }

        if (layoutId == 0) {
            layoutId = R.layout.layout_suggestion;
        }

        //TODO: Auto onclick -> suggest query
//        setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override public void onItemClick(RecyclerView recyclerView, int position, View v) {
//                searchBox.searchView.setQuery(v!!.findViewById<TextView>(R.id.suggestion_query).text, true)
//        });
    }

    @Override
    protected void addHits(@Nullable SearchResults results, boolean isReplacing) {
        if (results != null) {
            query = results.query;
            if (results.nbHits > count) {
                JSONArray hits = new JSONArray();
                for (int i = 0; i < count; i++) {
                    hits.put(results.hits.optJSONObject(i));
                }
                results.hits = hits;
            }
        }
        super.addHits(results, true);
    }

    @Nullable @Override
    protected Spannable getHighlightedAttribute(@NonNull JSONObject hit, @NonNull View view, @NonNull String attribute, @Nullable String attributeValue) {
        if (highlighting) {
            return Highlighter.getDefault().setInput(hit, attribute, query.length() > 0).setStyle(true).render();
        } else {
            return new SpannableString(attributeValue);
        }
    }
}
