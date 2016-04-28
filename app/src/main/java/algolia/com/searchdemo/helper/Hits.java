package algolia.com.searchdemo.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

import com.algolia.search.saas.AlgoliaException;

import algolia.com.searchdemo.R;

public class Hits extends ListView {

    private final Integer hitsPerPage;

    public Hits(Context context, AttributeSet attrs) throws AlgoliaException {
        super(context, attrs);
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBox, 0, 0);

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
}