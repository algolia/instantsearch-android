/*
 * Initially made by Android-WTF: android-wtf.com/2013/06/how-to-easily-traverse-any-view-hierarchy-in-android/
 * License: "I haven’t applied a licence to it yet. But you are free to use it for any kind of project,
 * open source or commercial. A link to this page in source code would be great, though."
 */
package com.algolia.instantsearch.ui.utils;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Iterates on views in a layout.
 */
final public class LayoutViews {
    @NonNull public static List<View> findByTag(@NonNull ViewGroup root, Object tag) {
        FinderByTag finderByTag = new FinderByTag(tag);
        LayoutTraverser.build(finderByTag).traverse(root);
        return finderByTag.getViews();
    }

    /**
     * Gets a List of Views matching a given class.
     *
     * @param rootView  the root View to traverse.
     * @param classType the class to find.
     * @param <T>       the class to find.
     * @return a List of every matching View encountered.
     */
    @NonNull public static <T> List<T> findByClass(@NonNull View rootView, Class<T> classType) {
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(android.R.id.content);
        return viewGroup == null ? new ArrayList<T>() : findByClass(viewGroup, classType);
    }

    /**
     * Gets a List of Views matching a given class.
     *
     * @param root      the root ViewGroup to traverse.
     * @param classType the class to find.
     * @param <T>       the class to find.
     * @return a List of every matching View encountered.
     */
    @NonNull public static <T> List<T> findByClass(@NonNull ViewGroup root, Class<T> classType) {
        FinderByClass<T> finderByClass = new FinderByClass<>(classType);
        LayoutTraverser.build(finderByClass)
                .traverse(root);
        return finderByClass.getViews();
    }

    /**
     * Gets a List of Views from a ViewGroup.
     *
     * @param root      the root ViewGroup to traverse.
     * @return a List of every View encountered.
     */
    @NonNull public static List<View> findAny(@NonNull ViewGroup root) {
        FinderAny finder = new FinderAny();
        LayoutTraverser.build(finder)
                .traverse(root);
        return finder.getViews();
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

    private static class FinderAny implements LayoutTraverser.Processor {
        @NonNull
        private final List<View> views;

        private FinderAny() {
            views = new ArrayList<>();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void process(@NonNull View view) {
            views.add(view);
        }

        @NonNull public List<View> getViews() {
            return views;
        }
    }
}
