package com.algolia.instantsearch.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;


public class Stats extends android.support.v7.widget.AppCompatTextView implements AlgoliaWidget {
    // TODO: Autohidecontainer, maybe other useful attrs? https://community.algolia.com/instantsearch.js/documentation/#stats
    /** The default template, only shown when there is no error. */
    public static final String DEFAULT_TEMPLATE = "{nbHits} results found in {processingTimeMS} ms";

    private String resultTemplate;
    private String errorTemplate;
    private final boolean autoHide;

    /**
     * Constructs a new Stats with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public Stats(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Stats, 0, 0);
        final TypedArray widgetAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Widget, 0, 0);
        try {
            resultTemplate = styledAttributes.getString(R.styleable.Stats_resultTemplate);
            if (resultTemplate == null) {
                resultTemplate = DEFAULT_TEMPLATE;
            }
            errorTemplate = styledAttributes.getString(R.styleable.Stats_errorTemplate);
            autoHide = widgetAttributes.getBoolean(R.styleable.Widget_autoHide, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    public void initWithSearcher(@NonNull Searcher searcher) {
    }

    @Override
    public void onReset() {
        setVisibility(GONE);
    }

    @Override
    public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        if (autoHide && results.nbHits == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            setText(applyTemplate(resultTemplate, results));
        }
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
                .replace("{query}", String.valueOf((query.getQuery())))
                .replace("{error}", String.valueOf((error.getLocalizedMessage())));
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
