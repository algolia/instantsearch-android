package com.algolia.instantsearch.ui.utils;

import android.app.SearchableInfo;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

/**
 * A facade to handle both {@link SearchView android.widget.SearchView} and {@link androidx.appcompat.widget.SearchView} transparently.
 */
@SuppressWarnings({"WeakerAccess", "unused"}) // For library users
public class SearchViewFacade {
    private static final String ERROR_NO_SEARCHVIEW = "A SearchViewFacade should have at least one SearchView reference.";
    private SearchView searchView;
    private androidx.appcompat.widget.SearchView supportView;

    /**
     * Constructs a facade from a {@link SearchView android.widget.SearchView}.
     *
     * @param searchView the {@code SearchView} to facade.
     */
    public SearchViewFacade(SearchView searchView) {
        this.searchView = searchView;
    }

    /**
     * Constructs a facade from a {@link androidx.appcompat.widget.SearchView}.
     *
     * @param searchView the {@code SearchView} to facade.
     */
    public SearchViewFacade(androidx.appcompat.widget.SearchView searchView) {
        supportView = searchView;
    }

    public SearchViewFacade(@NonNull Menu menu, int id) {
        try {
            searchView = (SearchView) menu.findItem(id).getActionView();
        } catch (ClassCastException e) {
            supportView = (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(id));
        }
    }

    /**
     * Returns the SearchView. (either {@link SearchView android.widget.SearchView} or {@link androidx.appcompat.widget.SearchView}).
     *
     * @return the actual SearchView wrapped by this facade.
     */
    public Object getSearchView() {
        if (searchView != null) {
            return searchView;
        } else if (supportView != null) {
            return supportView;
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    /**
     * Called when this view wants to give up focus. If focus is cleared
     * {@link View#onFocusChanged(boolean, int, android.graphics.Rect)} is called.
     * <p>
     * <strong>Note:</strong> When a View clears focus the framework is trying
     * to give focus to the first focusable View from the top. Hence, if this
     * View is the first from the top that can take focus, then all callbacks
     * related to clearing focus will be invoked after which the framework will
     * give focus to this view.
     * </p>
     */
    public void clearFocus() {
        if (searchView != null) {
            searchView.clearFocus();
        } else if (supportView != null) {
            supportView.clearFocus();
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Look for a child view with the given id.  If this view has the given
     * id, return this view.
     *
     * @param id The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    @Nullable public View findViewById(int id) {
        if (searchView != null) {
            return searchView.findViewById(id);
        } else if (supportView != null) {
            return supportView.findViewById(id);
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    /**
     * Returns the context the view is running in, through which it can
     * access the current theme, resources, etc.
     *
     * @return The view's Context.
     */
    @NonNull public Context getContext() {
        if (searchView != null) {
            return searchView.getContext();
        } else if (supportView != null) {
            return supportView.getContext();
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    /**
     * Returns the query string currently in the text field.
     *
     * @return the query string
     */
    @NonNull public CharSequence getQuery() {
        if (searchView != null) {
            return searchView.getQuery();
        } else if (supportView != null) {
            return supportView.getQuery();
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    /**
     * Sets a query string in the text field and optionally submits the query as well.
     *
     * @param query  the query string. This replaces any query text already present in the
     *               text field.
     * @param submit whether to submit the query right now or only update the contents of
     *               text field.
     */
    public void setQuery(CharSequence query, boolean submit) {
        if (searchView != null) {
            searchView.setQuery(query, submit);
        } else if (supportView != null) {
            supportView.setQuery(query, submit);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Sets the SearchableInfo for this SearchView. Properties in the SearchableInfo are used
     * to display labels, hints, suggestions, create intents for launching search results screens
     * and controlling other affordances such as a voice button.
     *
     * @param searchableInfo a SearchableInfo can be retrieved from the SearchManager, for a specific
     *                       activity or a global search provider.
     */
    public void setSearchableInfo(SearchableInfo searchableInfo) {
        if (searchView != null) {
            searchView.setSearchableInfo(searchableInfo);
        } else if (supportView != null) {
            supportView.setSearchableInfo(searchableInfo);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Sets the default or resting state of the search field. If {@code true}, a single search icon is
     * shown by default and expands to show the text field and other buttons when pressed. Also,
     * if the default state is iconified, then it collapses to that state when the close button
     * is pressed. Changes to this property will take effect immediately.
     * <p>The default value is true.</p>
     *
     * @param iconified whether the search field should be iconified by default
     */
    public void setIconifiedByDefault(boolean iconified) {
        if (searchView != null) {
            searchView.setIconifiedByDefault(iconified);
        } else if (supportView != null) {
            supportView.setIconifiedByDefault(iconified);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param listener the listener object that receives callbacks when the user performs
     *                 actions in the SearchView such as clicking on buttons or typing a query.
     */
    public void setOnQueryTextListener(@NonNull final SearchView.OnQueryTextListener listener) {
        if (searchView != null) {
            searchView.setOnQueryTextListener(listener);
        } else if (supportView != null) {
            supportView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String query) {
                    return listener.onQueryTextSubmit(query);
                }

                @Override public boolean onQueryTextChange(String newText) {
                    return listener.onQueryTextChange(newText);
                }
            });
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Sets a listener for user actions within the SearchView.
     *
     * @param listener the listener object that receives callbacks when the user performs
     *                 actions in the SearchView such as clicking on buttons or typing a query.
     */
    public void setOnQueryTextListener(@NonNull final androidx.appcompat.widget.SearchView.OnQueryTextListener listener) {
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String query) {
                    return listener.onQueryTextSubmit(query);
                }

                @Override public boolean onQueryTextChange(String newText) {
                    return listener.onQueryTextChange(newText);
                }
            });
        } else if (supportView != null) {
            supportView.setOnQueryTextListener(listener);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Sets a listener to inform when the user closes the SearchView.
     *
     * @param listener the listener to call when the user closes the SearchView.
     */
    public void setOnCloseListener(@NonNull final SearchView.OnCloseListener listener) {
        if (searchView != null) {
            searchView.setOnCloseListener(listener);
        } else if (supportView != null) {
            supportView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
                @Override public boolean onClose() {
                    return listener.onClose();
                }
            });
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    /**
     * Sets a listener to inform when the user closes the SearchView.
     *
     * @param listener the listener to call when the user closes the SearchView.
     */
    public void setOnCloseListener(@NonNull final androidx.appcompat.widget.SearchView.OnCloseListener listener) {
        if (searchView != null) {
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override public boolean onClose() {
                    return listener.onClose();
                }
            });
        } else if (supportView != null) {
            supportView.setOnCloseListener(listener);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }
}
