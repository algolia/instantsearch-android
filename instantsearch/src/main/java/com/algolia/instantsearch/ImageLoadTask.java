package com.algolia.instantsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class ImageLoadTask extends AsyncTask<String, Void, Bitmap>{
    private final ImageView view;
    Bitmap bitmap;

    public ImageLoadTask(ImageView imageView) {
        view = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params.length != 1) {
            throw new RuntimeException("There should only be one url per image.");
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
