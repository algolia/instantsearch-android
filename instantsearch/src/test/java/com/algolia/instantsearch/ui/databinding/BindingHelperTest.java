package com.algolia.instantsearch.ui.databinding;

import android.app.Activity;
import android.view.View;

import com.algolia.instantsearch.InstantSearchTest;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.HashMap;

public class BindingHelperTest extends InstantSearchTest
{
    private View view = new View(Robolectric.setupActivity(Activity.class));
    private String attr = "foo";
    private String value = "bar";
    private String prefix = "http://";
    private String suffix = ".jpg";

    @Before
    public void setUp() {
        view.setId(42);
        BindingHelper.bindAttribute(view, attr, null, null, null, prefix, suffix);
    }

    @Test
    public void isAlreadyMapped() {
        BindingHelper.bindAttribute(view, attr, null);
        HashMap<Integer, String> bindings = BindingHelper.getBindings(null);
        Assert.assertTrue("There should only be one binding", bindings.size() == 1);
    }

    @Test
    public void getFullAttribute() {
        String fullAttribute = BindingHelper.getFullAttribute(view, attr);
        Assert.assertTrue("Attribute should be prefixed", fullAttribute.startsWith(prefix));
        Assert.assertTrue("Attribute should still be there", fullAttribute.contains(attr));
        Assert.assertTrue("Attribute should be suffixed", fullAttribute.endsWith(suffix));
    }
}
