package com.algolia.instantsearch;

import com.algolia.instantsearch.helpers.Highlighter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.regex.Pattern;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

public class HighlighterTest {
    private Highlighter highlighter;

    @Before
    public void setup() {
        Highlighter.setDefault("<em>", "</em>");
        highlighter = Highlighter.getDefault();
    }

    @Test
    public void testGet() {
        Highlighter h = Highlighter.getDefault();
        assertSame("The default highlighter should be a singleton", highlighter, h);
    }

    @Test
    public void testSetTags() {
        Highlighter.setDefault("foo", "bar");
        Highlighter h = Highlighter.getDefault();
        assertNotSame("The default highlighter should have changed", highlighter, h);
        final String pattern = ((Pattern) Whitebox.getInternalState(Highlighter.getDefault(), "pattern")).pattern();
        assertTrue(String.format("The pattern \"%s\" doesn't match the given tags.", pattern), pattern.startsWith("foo") && pattern.endsWith("bar"));
    }

    @Test
    public void testInverseHighlighting() {
        String highlightString = "<em>Foo</em>Bar <em>Baz</em>";
        String inverseHighlight = highlighter.inverseHighlight(highlightString);
        assertTrue("Should not highlight begin", inverseHighlight.startsWith("Foo"));
        assertTrue("Should not highlight end", inverseHighlight.endsWith("Baz"));
        assertTrue("Should not highlight spaces", inverseHighlight.contains(" Baz"));
        assertTrue("Should highlight Bar", inverseHighlight.contains("<em>Bar</em>"));
    }

}
