package algolia.com.searchdemo;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.listeners.SearchListener;

import org.json.JSONObject;

import java.util.List;

import algolia.com.searchdemo.helper.AlgoliaHelper;
import algolia.com.searchdemo.helper.Hits;
import algolia.com.searchdemo.helper.SearchBox;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.searchBox)
    SearchBox searchBox;

    @BindView(R.id.hits)
    Hits hits;

    private ArrayAdapter<String> adapter;
    private AlgoliaHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        resetList();
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
                    resetList();
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
        helper.search(query, new SearchListener() {
                    @Override
                    public void searchResult(Index index, Query query, JSONObject jsonResults) {
                        List<String> results = helper.parseResults(jsonResults);
                        adapter.clear();
                        adapter.addAll(results);
                        adapter.notifyDataSetChanged();

                        hits.smoothScrollToPosition(0);
                    }

                    @Override
                    public void searchError(Index index, Query query, AlgoliaException e) {
                        Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }


    private void resetList() {
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            hits.setAdapter(adapter);
        }
        adapter.clear();
        adapter.notifyDataSetChanged();
    }
}
