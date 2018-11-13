package com.algolia.instantsearch.ui.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    private RefinementList refinementList;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refinementList = new RefinementList(this);
    }

    public RefinementList getRefinementList() {
        return refinementList;
    }
}
