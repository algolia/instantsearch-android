package com.algolia.instantsearch.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.events.ResetEvent;
import com.algolia.instantsearch.core.model.AlgoliaErrorListener;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class Stats extends AppCompatTextView implements AlgoliaResultsListener, AlgoliaErrorListener {
    /**
     * The default template, only shown when there is no error.
     */
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

    // region Lifecycle
    @Subscribe
    public void onReset(ResetEvent event) {
        setVisibility(GONE);
    }

    @Override
    public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        if (isEmptyView()) {
            if (results.nbHits != 0) setVisibility(GONE);
            else showResultTemplate(results);
        } else {
            if (autoHide && results.nbHits == 0) setVisibility(GONE);
            else showResultTemplate(results);
        }
    }

    @Override
    public void onError(@NonNull Query query, @NonNull AlgoliaException error) {
        if (errorTemplate == null) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            setText(applyTemplate(errorTemplate, query, error));
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }
    // endregion Lifecycle

    // region Helpers
    private void showResultTemplate(@NonNull SearchResults results) {
        setVisibility(VISIBLE);
        setText(applyTemplate(resultTemplate, results));
    }

    private Spanned applyTemplate(@NonNull String template, @NonNull SearchResults results) {
        final String templated = template
                .replace("{hitsPerPage}", String.valueOf(results.hitsPerPage))
                .replace("{processingTimeMS}", String.valueOf(results.processingTimeMS))
                .replace("{nbHits}", String.valueOf(results.nbHits))
                .replace("{nbPages}", String.valueOf(results.nbPages))
                .replace("{page}", String.valueOf(results.page))
                .replace("{query}", String.valueOf(results.query));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(templated, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(templated);
        }
    }

    private Spanned applyTemplate(@NonNull String template, @NonNull Query query, @NonNull AlgoliaException error) {
        final String templated = template
                .replace("{query}", String.valueOf((query.getQuery())))
                .replace("{error}", String.valueOf((error.getLocalizedMessage())));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(templated, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(templated);
        }
    }

    private boolean isEmptyView() {
        return getId() == android.R.id.empty;
    }

    // endregion
}
