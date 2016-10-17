package com.algolia.instantsearch.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;


public class Stats extends TextView implements AlgoliaWidget {
    // TODO: Autohidecontainer, maybe other useful attrs? https://community.algolia.com/instantsearch.js/documentation/#stats
    public static final String DEFAULT_TEMPLATE = "{nbHits} results found in {processingTimeMS} ms";

    private String resultTemplate;
    private String errorTemplate;

    public Stats(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, com.algolia.instantsearch.R.styleable.Stats, 0, 0);
        try {
            resultTemplate = styledAttributes.getString(com.algolia.instantsearch.R.styleable.Stats_resultTemplate);
            if (resultTemplate == null) {
                resultTemplate = DEFAULT_TEMPLATE;
            }
            errorTemplate = styledAttributes.getString(com.algolia.instantsearch.R.styleable.Stats_errorTemplate);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    public void setSearcher(@NonNull Searcher searcher) {
    }

    @Override
    public void onReset() {
        setVisibility(GONE);
    }

    @Override
    public void onResults(SearchResults results, boolean isLoadingMore) {
        setVisibility(VISIBLE);
        setText(applyTemplate(resultTemplate, results));
    }

    private String applyTemplate(@NonNull String template, @NonNull SearchResults results) {
        return template
                .replace("{hitsPerPage}", String.valueOf(results.hitsPerPage))
                .replace("{processingTimeMS}", String.valueOf(results.processingTimeMS))
                .replace("{nbHits}", String.valueOf(results.nbHits))
                .replace("{nbPages}", String.valueOf(results.nbPages))
                .replace("{page}", String.valueOf(results.page))
                .replace("{query}", String.valueOf(results.query));
    }

    private String applyTemplate(@NonNull String template, @NonNull Query query, @NonNull AlgoliaException error) {
        return template
                .replace("{query}", query.getQuery())
                .replace("{error}", error.getLocalizedMessage());
    }

    @Override
    public void onError(Query query, AlgoliaException error) {
        if (errorTemplate == null) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            setText(applyTemplate(errorTemplate, query, error));
        }
    }
}
