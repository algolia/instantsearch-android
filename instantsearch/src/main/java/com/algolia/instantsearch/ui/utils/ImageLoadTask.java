package com.algolia.instantsearch.ui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.algolia.instantsearch.model.Errors;

import java.io.IOException;
import java.net.URL;

public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
    private final BitmapListener listener;

    private final ImageView imageView;
    private Bitmap bitmap;
    private String url;

    public ImageLoadTask(BitmapListener listener, ImageView imageView) {
        this.listener = listener;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(@NonNull String... params) {
        if (params.length != 1) {
            throw new IllegalStateException(Errors.IMAGELOAD_INVALID_URL);
        }
        try {
            url = params[0];
            bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            Log.e("Algolia|ImageLoadTask", "Error loading image with url `" + url + "`: " + e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        listener.onResult(url, bitmap, imageView);
    }

    public interface BitmapListener {
        void onResult(String url, Bitmap bitmap, ImageView imageView);
    }
}
