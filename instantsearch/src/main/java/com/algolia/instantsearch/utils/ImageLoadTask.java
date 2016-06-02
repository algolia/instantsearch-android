package com.algolia.instantsearch.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.algolia.instantsearch.model.Errors;

import java.io.IOException;
import java.net.URL;

public class ImageLoadTask extends AsyncTask<String, Void, Bitmap>{
    private final ImageView view;
    private Bitmap bitmap;

    public ImageLoadTask(ImageView imageView) {
        view = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalStateException(Errors.IMAGELOAD_INVALID_URL);
        }
        try {
            URL url = new URL(params[0]);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        view.setImageBitmap(bitmap);
    }
}
