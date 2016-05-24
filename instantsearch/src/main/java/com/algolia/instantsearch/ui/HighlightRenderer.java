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
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import com.algolia.instantsearch.model.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Renders HTML-like attributed strings into `Spannable` instances suitable for display.
 */
public class HighlightRenderer {
    // NOTE: This pattern is not bullet-proof (most notably against nested tags), but it is
    // sufficient for our purposes.
    private static final Pattern HIGHLIGHT_PATTERN = Pattern.compile("<em>([^<]*)</em>");

    public static Spannable renderHighlightColor(Result result, String attributeName, @ColorRes int colorId, Context context) {
        return renderHighlightColor(result.get(attributeName, true), colorId, context);
    }

    public static Spannable renderHighlightColor(String markupString, @ColorRes int colorId, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return renderHighlightColor(markupString, context.getResources().getColor(colorId, context.getTheme()));
        } else { //noinspection deprecation
            return renderHighlightColor(markupString, context.getResources().getColor(colorId));
        }
    }

    public static Spannable renderHighlightColor(Result result, String attributeName, @ColorInt int color) {
        return renderHighlightColor(result.get(attributeName, true), color);
    }

    public static Spannable renderHighlightColor(String markupString, @ColorInt int color) {
        SpannableStringBuilder result = new SpannableStringBuilder();
        Matcher matcher = HIGHLIGHT_PATTERN.matcher(markupString);
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

    public static String removeHighlight(String attribute) {
        return attribute.replace("<em>", "").replace("</em>", "");
    }
}
