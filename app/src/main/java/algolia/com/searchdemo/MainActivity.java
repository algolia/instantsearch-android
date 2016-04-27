package algolia.com.searchdemo;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ListActivity {

    @BindView(android.R.id.list)
    ListView listView;

    @BindView(R.id.searchBox)
    SearchView searchBox;

    @BindArray(R.array.data)
    String[] dataList;

    private int itemStyleResourceId = android.R.layout.simple_list_item_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        resetList();
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchBox.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchBox.setIconifiedByDefault(false);

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
        ArrayList<String> matchingList = new ArrayList<>();

        for (String item : dataList) {
            if (levenshtein(item, query) < 2) {
                matchingList.add(item);
            }
        }
        getListView().setAdapter(new ArrayAdapter<>(this, itemStyleResourceId, matchingList));
    }

    private void resetList() {
        setListAdapter(new ArrayAdapter<>(this, itemStyleResourceId, dataList));
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
