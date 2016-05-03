package com.algolia.searchdemo;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.searchdemo.helper.AlgoliaHelper;
import com.algolia.searchdemo.helper.Hits;
import com.algolia.searchdemo.helper.SearchBox;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.searchBox)
    SearchBox searchBox;

    @BindView(R.id.hits)
    Hits hits;

    private AlgoliaHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        helper = new AlgoliaHelper("9V0VSE2N4Z", "7c4db7c9e62611b1eb078a220044f546", "contacts");
        helper.setActivity(this);
        View emptyView = findViewById(android.R.id.empty);
        if (emptyView == null) {
            throw new RuntimeException("You have to provide an empty view.");
        }
        hits.setEmptyView(emptyView);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    hits.clear();
                } else {
                    search(newText);
                }
                return true;
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        helper.search(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    List<String> results = helper.parseResults(content);
                    hits.replace(results);
                    hits.smoothScrollToPosition(0);
                }
            }
        });
    }
}
