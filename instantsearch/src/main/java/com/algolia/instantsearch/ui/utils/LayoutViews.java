package com.algolia.instantsearch.ui.utils;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper to iterate on views in a layout.
 * <a href="android-wtf.com/2013/06/how-to-easily-traverse-any-view-hierarchy-in-android/">Initially made by Android-WTF</a><br />
 * License: "I haven’t applied a licence to it yet. But you are free to use it for any kind of project,
 * open source or commercial. A link to this page in source code would be great, though."
 */
final public class LayoutViews {
    @NonNull public static List<View> findByTag(ViewGroup root, Object tag) {
        FinderByTag finderByTag = new FinderByTag(tag);
        LayoutTraverser.build(finderByTag).traverse(root);
        return finderByTag.getViews();
    }

    @NonNull public static <T> List<T> findByClass(@NonNull View rootView, Class<T> classType) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(android.R.id.content);
        return findByClass(viewGroup, classType);
    }

    @NonNull public static <T> List<T> findByClass(ViewGroup root, Class<T> classType) {
        FinderByClass<T> finderByClass = new FinderByClass<>(classType);
        LayoutTraverser.build(finderByClass)
                .traverse(root);
        return finderByClass.getViews();
    }

    private static class FinderByTag implements LayoutTraverser.Processor {
        private final Object searchTag;
        private final List<View> views = new ArrayList<>();

        private FinderByTag(Object searchTag) {
            this.searchTag = searchTag;
        }

        @Override
        public void process(@NonNull View view) {
            Object viewTag = view.getTag();

            if (viewTag != null && viewTag.equals(searchTag)) {
                views.add(view);
            }
        }

        @NonNull private List<View> getViews() {
            return views;
        }
    }

    private static class FinderByClass<T> implements LayoutTraverser.Processor {
        private final Class<T> type;
        @NonNull
        private final List<T> views;

        private FinderByClass(Class<T> type) {
            this.type = type;
            views = new ArrayList<>();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void process(@NonNull View view) {
            if (type.isAssignableFrom(view.getClass())) {
                views.add((T) view);
            }
        }

        @NonNull public List<T> getViews() {
            return views;
        }
    }
}
