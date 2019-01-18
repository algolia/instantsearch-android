package com.algolia.instantsearch.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.algolia.instantsearch.R;
import com.algolia.instantsearch.core.events.QueryTextChangeEvent;
import com.algolia.instantsearch.ui.databinding.BindingHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Provides a user input for search queries that are directly sent to the Algolia engine.
 */
public class SearchBox extends SearchView {
    /**
     * Constructs a new SearchBox with the given context's theme.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public SearchBox(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Constructs a new SearchBox with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public SearchBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        EventBus.getDefault().register(this); // SearchBox needs to react to QueryTextChangeEvent before being attached

        if (attrs != null) {
            final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBox, 0, 0);

            try {
                setIconifiedByDefault(false); // By default, don't collapse SearchBox
                for (int i = 0; i < attrs.getAttributeCount(); i++) {
                    if ("iconifiedByDefault".equals(attrs.getAttributeName(i))) {
                        setIconifiedByDefault(attrs.getAttributeBooleanValue(i, false)); // Unless iconifiedByDefault is set
                    }
                }

                if (styledAttributes.getBoolean(R.styleable.SearchBox_autofocus, false)) {
                    setFocusable(true);
                    setIconified(false);
                    requestFocusFromTouch();
                }
                setSubmitButtonEnabled(styledAttributes.getBoolean(R.styleable.SearchBox_submitButtonEnabled, false));
            } finally {
                styledAttributes.recycle();
            }
            BindingHelper.setVariantForView(this, attrs);
        }
    }

    /**
     * Disables fullscreen keyboard display in landscape mode. <b>This only works on {@literal >= 4.1} devices.</b>
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void disableFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setImeOptions(getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }
    }

    /**
     * Enables fullscreen keyboard display in landscape mode. <b>This only works on {@literal >= 4.1} devices.</b>
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public void enableFullScreen() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setImeOptions(getImeOptions() & EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }
    }

    @Subscribe
    public void onQueryEvent(QueryTextChangeEvent event) {
        if (!this.equals(event.origin)) {
            setQuery(event.query, false);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

}