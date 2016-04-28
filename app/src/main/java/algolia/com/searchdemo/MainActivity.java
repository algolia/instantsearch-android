package algolia.com.searchdemo;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.listeners.SearchListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algolia.com.searchdemo.helper.AlgoliaHelper;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ListActivity {

    @BindView(R.id.searchBox)
    SearchBox searchBox;

    @BindArray(R.array.data)
    String[] dataList;

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

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchBox.setIconifiedByDefault(false);
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("PLN", "Submit!");
                Toast.makeText(MainActivity.this, "Submit!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("PLN", "Change!");
                Toast.makeText(MainActivity.this, "Change!", Toast.LENGTH_SHORT).show();
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

                        getListView().smoothScrollToPosition(0);
                    }

                    @Override
                    public void searchError(Index index, Query query, AlgoliaException e) {
                        Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        );
//
//        ArrayList<String> matchingList = new ArrayList<>();
//
//        for (String item : dataList) {
//            if (levenshtein(item, query) < 2) {
//                matchingList.add(item);
//            }
//        }
//
//        getListView().setAdapter(new ArrayAdapter<>(this, itemStyleResourceId, matchingList));
    }


    private void resetList() {
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(Arrays.asList(dataList)));
            setListAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(dataList);
            adapter.notifyDataSetChanged();
        }
    }

    private int levenshtein(String a, String b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null || a.length() == 0) {
            return b.length();
        }
        if (b == null || b.length() == 0) {
            return a.length();
        }

        if (a.charAt(0) == b.charAt(0)) {
            return levenshtein(a.substring(1), b.substring(1));
        }

        int distanceA = levenshtein(a, b.substring(1));
        int distanceB = levenshtein(a.substring(1), b.substring(1));
        int distanceC = levenshtein(a.substring(1), b);

        return 1 + Math.min(distanceA, Math.min(distanceB, distanceC));
    }
}
