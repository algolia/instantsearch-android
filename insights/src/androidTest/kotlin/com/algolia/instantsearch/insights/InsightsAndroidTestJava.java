package com.algolia.instantsearch.insights;

import android.content.Context;
import android.os.Build;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.algolia.instantsearch.insights.event.Event;
import com.algolia.instantsearch.insights.event.EventObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.Q)
public class InsightsAndroidTestJava {

    private Context context = InstrumentationRegistry.getContext();
    private Insights.Configuration configuration = new Insights.Configuration(5000, 5000);

    @Test
    public void testInitShouldFail() {
        try {
            Insights.shared("index");
        } catch (Exception exception) {
            assertEquals(exception.getClass(), InsightsException.IndexNotRegistered.class);
        }
    }

    @Test
    public void testInitShouldWork() {
        Insights insights = InsightsKt.register(context, "testApp", "testKey", "index", configuration);
        Insights insightsShared = Insights.shared();
        assertNotNull("shared Insights should have been registered", insightsShared);
        Event.Click click = new Event.Click(
                "",
                "",
                0,
                new EventObjects.IDs(),
                "",
                new ArrayList<Integer>()
        );
        assertEquals(insights, insightsShared);
        insightsShared.track(click);
    }
}
