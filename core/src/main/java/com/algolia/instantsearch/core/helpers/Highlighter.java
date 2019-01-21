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

package com.algolia.instantsearch.core.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;

import com.algolia.instantsearch.core.R;
import com.algolia.instantsearch.core.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Renders HTML-like attributed strings into {@link Spannable} instances suitable for display.
 */
public class Highlighter {
    private static Highlighter defaultHighlighter;

    /**
     * The pattern used for matching a part to highlight in a string.
     */
    private final Pattern pattern;
    /**
     * The prefixTag used, if any.
     */
    private final String prefixTag;
    /**
     * The postfixTag used, if any.
     */
    private final String postfixTag;

    /**
     * Gets the default highlighter.
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
     * Sets the default highlighter to highlight the captured group of the given RegExp regexp.
     *
     * @param regexp a capturing RegExp regexp to find and capture the part to highlight.
     */
    public static void setDefault(@NonNull final String regexp) {
        defaultHighlighter = new Highlighter(regexp);
    }

    /**
     * Sets the default highlighter to highlight anything between {@code prefixTag} and {@code postfixTag}.
     *
     * @param prefixTag  the String that is inserted before a highlighted part of a result.
     * @param postfixTag the String that is inserted after a highlighted part of a result.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public static void setDefault(final String prefixTag, final String postfixTag) {
        defaultHighlighter = new Highlighter(prefixTag, postfixTag);
    }

    /**
     * Constructs a custom highlighter, using a custom regexp.
     *
     * @param regexp a capturing RegExp regexp to find and capture the part to highlight.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Highlighter(String regexp) {
        this.pattern = Pattern.compile(regexp);
        final String[] splits = regexp.split("[()]");
        prefixTag = splits.length > 0 ? splits[0] : null;
        postfixTag = splits.length > 0 ? splits[splits.length - 1] : null;
    }

    /**
     * Constructs a custom highlighter, using prefix and postfix tags.
     *
     * @param prefixTag  the String that is inserted before a highlighted part of a result.
     * @param postfixTag the String that is inserted after a highlighted part of a result.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Highlighter(String prefixTag, String postfixTag) {
        this(prefixTag + "(.*?)" + postfixTag);
    }

    /**
     * Constructs the default highlighter, using {@code <em>} and {@code </em>} tags.
     */
    private Highlighter() {
        this("<em>", "</em>");
    }

    /**
     * Sets the input to highlight.
     *
     * @param result    a JSONObject containing our attribute.
     * @param attribute the attribute to highlight.
     * @return a {@link Styler} to specify the style before rendering.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Styler setInput(@NonNull JSONObject result, @NonNull String attribute) {
        return setInput(result, attribute, false);
    }

    /**
     * Sets the input to highlight.
     *
     * @param result    a {@link JSONObject} describing a hit.
     * @param attribute the attribute to highlight.
     * @param inverted  if {@code true}, the highlighting will be inverted.
     * @return a {@link Styler} to specify the style before rendering.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Styler setInput(@NonNull JSONObject result, @NonNull String attribute, boolean inverted) {
        final String highlightedAttribute = getHighlightedAttribute(result, attribute, inverted, false);
        return setInput(highlightedAttribute);
    }

    /**
     * Sets the input to highlight.
     *
     * @param result    a {@link JSONObject} describing a hit.
     * @param attribute the attribute to highlight.
     * @param inverted  if {@code true}, the highlighting will be inverted.
     * @return a {@link Styler} to specify the style before rendering.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Styler setInput(@NonNull JSONObject result, @NonNull String attribute,
                           @Nullable String prefix, @Nullable String suffix,
                           boolean inverted, boolean snippetted) {
        final String highlightedAttribute = getHighlightedAttribute(result, attribute, inverted, snippetted);
        prefix = prefix == null ? "" : prefix;
        suffix = suffix == null ? "" : suffix;
        return setInput(prefix + highlightedAttribute + suffix);
    }

    /**
     * Sets the input to highlight.
     *
     * @param markupString a String to highlight according to this highlighter's {@link #pattern}.
     * @return a {@link Styler} to specify the style before rendering.
     */
    @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
    public Styler setInput(@NonNull String markupString) {
        return new Styler(markupString);
    }

    /**
     * Lets you specify a highlighting style. You will get a Styler by calling setInput on your Highlighter.
     */
    public class Styler {
        /**
         * The input that will be highlighted on {@link Renderer#render() render}.
         */
        @NonNull
        private String markupString;

        public Styler(@NonNull String markupString) {
            this.markupString = markupString;
        }

        /**
         * Sets the color for rendering, using the {@link R.color#colorHighlighting default color}.
         *
         * @param context a Context to get the color from.
         * @return a {@link Renderer} that can render the highlight.
         */
        @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
        public Renderer setStyle(@NonNull Context context) {
            return setStyle(getColor(context, R.color.colorHighlighting));
        }

        /**
         * Sets the color for rendering, using a color resource.
         *
         * @param colorRes a {@link ColorRes color resource}.
         * @param context  a Context to get the color from.
         * @return a {@link Renderer} that can render the highlight.
         */
        @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
        public Renderer setStyle(@ColorRes int colorRes, Context context) {
            return setStyle(getColor(context, colorRes));
        }

        /**
         * Sets the color for rendering, using a packed color int.
         *
         * @param colorInt a {@link ColorRes packed color int}.
         * @return a {@link Renderer} that can render the highlight.
         */
        @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
        public Renderer setStyle(@ColorInt int colorInt) {
            return new Renderer(markupString, false, colorInt);
        }

        /**
         * Whether to use bold to highlight instead of a color.
         *
         * @param isBold if {@code true}, the highlight will be applied using {@link Typeface#BOLD}.
         * @return a {@link Renderer} that can render the highlight.
         */
        @SuppressWarnings({"WeakerAccess", "unused"}) // For library users
        public Renderer setStyle(boolean isBold) {
            return new Renderer(markupString, true, 0);
        }

    }

    /**
     * Renders the highlight. You will get a Styler by calling setInput then setStyle on your Highlighter.
     */
    public class Renderer {
        /**
         * The color which will be used for highlighting.
         */
        private @ColorInt
        int color;
        /**
         * The input that will be highlighted on {@link Renderer#render() render}.
         */
        @NonNull
        private final String markupString;
        /**
         * If {@code true}, highlighting will be done using bold.
         */
        private boolean bold;

        Renderer(@NonNull String markupString, boolean bold, int colorInt) {
            this.markupString = markupString;
            this.bold = bold;
            this.color = colorInt;
        }

        /**
         * Renders the highlight, resetting the {@link #color}/{@link #bold boldness} and the {@link Styler#markupString}.
         *
         * @return a Spannable highlighted with the appropriate {@link ParcelableSpan spans}.
         */
        @NonNull
        public Spannable render() {
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
                ParcelableSpan span = bold ? new StyleSpan(Typeface.BOLD) :
                        color != 0 ? new BackgroundColorSpan(color) : new StyleSpan(Typeface.NORMAL);
                result.setSpan(span, posOut, posOut + highlightString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                posOut += highlightString.length();
                posIn = matcher.end();
            }
            // Append text after.
            result.append(markupString.substring(posIn));
            return result;
        }

    }

    /**
     * Inverts the highlighting for a given string.
     *
     * @param text a String matching this highlighter's {@link #pattern}.
     * @return the input, with highlights inverted.
     */
    @Nullable
    public String inverseHighlight(@Nullable String text) {
        if (text == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        Matcher matcher = pattern.matcher(text);
        final Pattern spaces = Pattern.compile("\\s");
        int posIn = 0; // current position in input string

        // For each highlight:
        while (matcher.find()) {
            String match = matcher.group(1);
            final String before = text.substring(posIn, matcher.start());
            posIn = matcher.end();
            final Matcher spacesBefore = spaces.matcher(before);

            if (spacesBefore.matches()) {

                // Append text before, highlighted.
                result.append(prefixTag);
                result.append(before);
                result.append(postfixTag);
            } else {
                // Append text before, highlighted.
                result.append(prefixTag);
                result.append(before);
                result.append(postfixTag);
            }

            // Append matched text, without highlight.
            result.append(match);
        }
        // Append text after, highlighted.
        result.append(prefixTag);
        final String after = text.substring(posIn);
        result.append(after);
        result.append(postfixTag);
        return result.toString().replace(prefixTag + postfixTag, "");
    }

    /**
     * Gets the highlighted version of an attribute, if there is one.
     *
     * @param result     {@link JSONObject} describing a hit.
     * @param attribute  the name of the attribute to return highlighted.
     * @param inverted   if {@code true}, the highlighting will be inverted.
     * @param snippetted if {@code true}, the snippetted value of the attribute will be used for highlighting.
     * @return the highlighted version of this attribute if there is one, else the raw attribute.
     */
    private String getHighlightedAttribute(@NonNull JSONObject result, String attribute,
                                           boolean inverted, boolean snippetted) {
        final JSONObject highlightResult = result.optJSONObject((snippetted ? "_snippet" : "_highlight") + "Result");
        if (highlightResult != null) {
            HashMap<String, String> highlightAttribute = JSONUtils.getMapFromJSONPath(highlightResult, attribute);
            if (highlightAttribute != null) {
                String highlightedValue = highlightAttribute.get("value");
                if (inverted) {
                    highlightedValue = inverseHighlight(highlightedValue);
                }
                if (highlightedValue != null) {
                    return highlightedValue;
                }
            } else { //Maybe it is an array?
                final JSONArray array = highlightResult.optJSONArray(attribute);
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
        return JSONUtils.getStringFromJSONPath(result, attribute);
    }

    @ColorInt
    private int getColor(@NonNull Context context, @ColorRes int colorId) {
        final int colorHighlighting;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorHighlighting = context.getResources().getColor(colorId, context.getTheme());
        } else {
            //noinspection deprecation
            colorHighlighting = context.getResources().getColor(colorId);
        }
        return colorHighlighting;
    }
}
