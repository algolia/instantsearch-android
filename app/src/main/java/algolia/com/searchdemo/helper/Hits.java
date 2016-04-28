package algolia.com.searchdemo.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.algolia.search.saas.AlgoliaException;

import java.util.Collection;
import java.util.List;

import algolia.com.searchdemo.R;

public class Hits extends ListView {

    private final Integer hitsPerPage;

    private ArrayAdapter<String> adapter;

    public Hits(Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBox, 0, 0);
        initAdapter();
        try {
            hitsPerPage = styledAttributes.getInt(R.styleable.Hits_hitsPerPage, AlgoliaHelper.HITS_PER_PAGE);
            Log.e("PLN", "init: submit(" + false + "), hitsPerPage(" + hitsPerPage + ").");

        } finally {
            styledAttributes.recycle();
        }
    }

    public Integer getHitsPerPage() {
        return hitsPerPage;
    }

    private void initAdapter() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        setAdapter(adapter);
    }

    public void clear() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void add(Collection<String> results) {
        adapter.addAll(results);
        adapter.notifyDataSetChanged();
    }

    public void replace(Collection<String> values) {
        adapter.clear();
        add(values);
    }
}