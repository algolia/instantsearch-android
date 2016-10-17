package com.algolia.instantsearch.ui.utils;

import android.app.SearchableInfo;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

public class SearchViewFacade {
    private static final String ERROR_NO_SEARCHVIEW = "A SearchViewFacade should have at least one SearchView reference.";
    private SearchView searchView;
    private android.support.v7.widget.SearchView supportView;

    public SearchViewFacade(SearchView searchView) {
        this.searchView = searchView;
    }

    public SearchViewFacade(android.support.v7.widget.SearchView searchView) {
        supportView = searchView;
    }

    public SearchViewFacade(@NonNull Menu menu, int id) {
        try {
            searchView = (SearchView) menu.findItem(id).getActionView();
        } catch (ClassCastException e) {
            supportView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(id));
        }
    }

    public void setQuery(CharSequence query, boolean submit) {
        if (searchView != null) {
            searchView.setQuery(query, submit);
        } else if (supportView != null) {
            supportView.setQuery(query, submit);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    public void clearFocus() {
        if (searchView != null) {
            searchView.clearFocus();
        } else if (supportView != null) {
            supportView.clearFocus();
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    public void setSearchableInfo(SearchableInfo searchableInfo) {
        if (searchView != null) {
            searchView.setSearchableInfo(searchableInfo);
        } else if (supportView != null) {
            supportView.setSearchableInfo(searchableInfo);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    public void setIconifiedByDefault(boolean iconified) {
        if (searchView != null) {
            searchView.setIconifiedByDefault(iconified);
        } else if (supportView != null) {
            supportView.setIconifiedByDefault(iconified);
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    @Nullable public View findViewById(int id) {
        if (searchView != null) {
            return searchView.findViewById(id);
        } else if (supportView != null) {
            return supportView.findViewById(id);
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    @NonNull public Context getContext() {
        if (searchView != null) {
            return searchView.getContext();
        } else if (supportView != null) {
            return supportView.getContext();
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    @NonNull public CharSequence getQuery() {
        if (searchView != null) {
            return searchView.getQuery();
        } else if (supportView != null) {
            return supportView.getQuery();
        }
        throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
    }

    public void setOnQueryTextListener(@NonNull final SearchView.OnQueryTextListener listener) {
        if (searchView != null) {
            searchView.setOnQueryTextListener(listener);
        } else if (supportView != null) {
            supportView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
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

    public void setOnQueryTextListener(@NonNull final android.support.v7.widget.SearchView.OnQueryTextListener listener) {
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

    public void setOnCloseListener(@NonNull final SearchView.OnCloseListener listener) {
        if (searchView != null) {
            searchView.setOnCloseListener(listener);
        } else if (supportView != null) {
            supportView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
                @Override public boolean onClose() {
                    return listener.onClose();
                }
            });
        } else {
            throw new IllegalStateException(ERROR_NO_SEARCHVIEW);
        }
    }

    public void setOnCloseListener(@NonNull final android.support.v7.widget.SearchView.OnCloseListener listener) {
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
