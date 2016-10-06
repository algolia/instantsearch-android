/*
 * Copyright (c) 2015 Algolia
 * http://www.algolia.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.algolia.instantsearch.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import com.algolia.instantsearch.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Renders HTML-like attributed strings into `Spannable` instances suitable for display.
 */
public class Highlighter {
    private static Highlighter defaultHighlighter;

    private final Pattern pattern;

    /**
     * Get the default highlighter.
     *
     * @return an Highlighter matching anything between {@code <em>} and {@code </em>}.
     */
    public static Highlighter getDefault() {
        if (defaultHighlighter == null) {
            defaultHighlighter = new Highlighter();
        }
        return defaultHighlighter;
    }

    /**
     * Set the default highlighter to highlight the captured group of the given RegExp regexp.
     *
     * @param regexp a capturing RegExp regexp to find and capture the part to highlight.
     */
    public static void setDefault(@NonNull final String regexp) {
        defaultHighlighter = new Highlighter(regexp);
    }

    /**
     * Set the default highlighter to highlight anything between {@code prefixTag} and {@code postfixTag}.
     *
     * @param prefixTag  the String that is inserted before a highlighted part of a result.
     * @param postfixTag the String that is inserted after a highlighted part of a result.
     */
    public static void setDefault(final String prefixTag, final String postfixTag) {
        defaultHighlighter = new Highlighter(prefixTag, postfixTag);
    }

    /**
     * Constructor for a custom highlighter, using a custom regexp.
     *
     * @param regexp a capturing RegExp regexp to find and capture the part to highlight.
     */
    public Highlighter(String regexp) {
        this.pattern = Pattern.compile(regexp);
    }

    /**
     * Constructor for a custom highlighter, using prefix and postfix tags.
     *
     * @param prefixTag  the String that is inserted before a highlighted part of a result.
     * @param postfixTag the String that is inserted after a highlighted part of a result.
     */
    public Highlighter(String prefixTag, String postfixTag) {
        this(prefixTag + "(.*?)" + postfixTag);
    }

    private Highlighter() {
        this("<em>", "</em>");
    }

    /**
     * Render a highlighted result's attribute using a color resource.
     *
     * @param result        {@link JSONObject} describing a hit.
     * @param attributeName name of the attribute to be highlighted.
     * @param colorId       a resource Id referencing a color.
     * @param context       a {@link Context} to get resources from.
     * @return a {@link Spannable} with the highlighted text.
     */
    @Nullable
    public Spannable renderHighlightColor(@NonNull JSONObject result, String attributeName, @ColorRes int colorId, @NonNull Context context) {
        return renderHighlightColor(getHighlightedAttribute(result, attributeName), colorId, context);
    }

    /**
     * Render a highlighted result's attribute using the default highlighting color resource.
     *
     * @param result        {@link JSONObject} describing a hit.
     * @param attributeName name of the attribute to be highlighted.
     * @param context       a {@link Context} to get resources from.
     * @return a {@link Spannable} with the highlighted text.
     */
    @Nullable
    public Spannable renderHighlightColor(@NonNull JSONObject result, String attributeName, @NonNull Context context) {
        return renderHighlightColor(getHighlightedAttribute(result, attributeName), getColor(context, R.color.colorHighlighting));
    }

    /**
     * Render a highlighted text using the default highlighting color resource.
     *
     * @param markupString a string to highlight.
     * @param colorId      a resource Id referencing a color.
     * @param context      a {@link Context} to get resources from.
     * @return a {@link Spannable} with the highlighted text.
     */
    @Nullable
    public Spannable renderHighlightColor(String markupString, @NonNull Context context) {
        return renderHighlightColor(markupString, getColor(context, R.color.colorHighlighting));
    }

    /**
     * Render a highlighted text using a color resource.
     *
     * @param markupString a string to highlight.
     * @param colorId      a resource Id referencing a color.
     * @param context      a {@link Context} to get resources from.
     * @return a {@link Spannable} with the highlighted text.
     */
    @Nullable
    public Spannable renderHighlightColor(String markupString, @ColorRes int colorId, @NonNull Context context) {
        return renderHighlightColor(markupString, getColor(context, colorId));
    }

    /**
     * Render a highlighted result's attribute using a packed color int.
     *
     * @param result        {@link JSONObject} describing a hit.
     * @param attributeName name of the attribute to be highlighted.
     * @param color         a color integer, see {@link android.graphics.Color}.
     * @return a {@link Spannable} with the highlighted text.
     */
    @Nullable
    public Spannable renderHighlightColor(@NonNull JSONObject result, String attributeName, @ColorInt int color) {
        return renderHighlightColor(getHighlightedAttribute(result, attributeName), color);
    }

    /**
     * Render a highlighted text using a packed color int.
     *
     * @param markupString a string to highlight.
     * @param color        a color integer, see {@link android.graphics.Color}.
     * @return a {@link Spannable} with the highlighted text.
     */
    @Nullable
    public Spannable renderHighlightColor(@Nullable String markupString, @ColorInt int color) {
        if (markupString == null) {
            return null;
        }

        SpannableStringBuilder result = new SpannableStringBuilder();
        Matcher matcher = pattern.matcher(markupString);
        int posIn = 0; // current position in input string
        int posOut = 0; // current position in output string

        // For each highlight:
        while (matcher.find()) {
            // Append text before.
            result.append(markupString.substring(posIn, matcher.start()));
            posOut += matcher.start() - posIn;

            // Append highlighted text.
            String highlightString = matcher.group(1);
            result.append(highlightString);
            final BackgroundColorSpan span;
            span = new BackgroundColorSpan(color);
            result.setSpan(span, posOut, posOut + highlightString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            posOut += highlightString.length();
            posIn = matcher.end();
        }
        // Append text after.
        result.append(markupString.substring(posIn));
        return result;
    }

    /**
     * Get the highlighted version of an attribute, if there is one.
     *
     * @param result        {@link JSONObject} describing a hit.
     * @param attributeName the name of the attribute to return highlighted.
     * @return the highlighted version of this attribute if there is one, else the raw attribute.
     */
    public static String getHighlightedAttribute(@NonNull JSONObject result, String attributeName) {
        final JSONObject highlightResult = result.optJSONObject("_highlightResult");
        if (highlightResult != null) {
            JSONObject highlightAttribute = highlightResult.optJSONObject(attributeName);
            if (highlightAttribute != null) {
                String highlightedValue = highlightAttribute.optString("value");
                if (highlightedValue != null) {
                    return highlightedValue;
                }
            } else { //Maybe it is an array?
                final JSONArray array = highlightResult.optJSONArray(attributeName);
                if (array != null) {
                    StringBuilder builder = new StringBuilder();
                    final int length = array.length();

                    for (int i = 0; i < length; i++) {
                        final String elementValue = ((JSONObject) array.opt(i)).optString("value");
                        builder.append(elementValue);
                        if (i + 1 < length) {
                            builder.append(", ");
                        }
                    }
                    return builder.toString();
                }
            }
        }
        return null;
    }

    private
    @ColorInt
    int getColor(@NonNull Context context, @ColorRes int colorId) {
        final int colorHighlighting;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorHighlighting = context.getResources().getColor(colorId, context.getTheme());
        } else {
            colorHighlighting = context.getResources().getColor(colorId);
        }
        return colorHighlighting;
    }
}