package com.algolia.instantsearch.insights;

import android.content.Context;
import android.os.Build;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.algolia.search.model.IndexName;
import com.algolia.search.model.insights.EventName;
import com.algolia.search.model.insights.UserToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Collections;

import static com.algolia.instantsearch.insights.util.WorkerManagerKt.setupWorkManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.Q)
public class InsightsAndroidTestJava {

    private Context context = ApplicationProvider.getApplicationContext();
    private Insights.Configuration configuration = new Insights.Configuration(5000, 5000);

    @Before
    public void init() {
        setupWorkManager();
    }

    @Test
    public void testInitShouldFail() {
        try {
            IndexName index = new IndexName("index");
            Insights.shared(index);
        } catch (Exception exception) {
            assertEquals(exception.getClass(), InsightsException.IndexNotRegistered.class);
        }
    }

    @Test
    public void testInitShouldWork() {
        IndexName index = new IndexName("index");
        Insights insights = Insights.register(context, "testApp", "testKey", index, configuration);
        insights.setUserToken(new UserToken("foobarbaz"));
        Insights insightsShared = Insights.shared();
        assertNotNull("shared Insights should have been registered", insightsShared);
        assertEquals(insights, insightsShared);

        insightsShared.clickedObjectIDs(
            new EventName("eventName"),
            Collections.emptyList(),
            0L
        );
    }
}
