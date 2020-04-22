package com.olins.movie.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.olins.movie.ui.fragments.item.DetailMovieFragment;
import com.olins.movie.R;

import butterknife.BindView;

public class DetailMovieActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    String imdbId = "";

    public static void startActivity(BaseActivity sourceActivity, String imdbId) {
        Intent intent = new Intent(sourceActivity, DetailMovieActivity.class);
        intent.putExtra("imdbId", imdbId);
        sourceActivity.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail_movie;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
        imdbId = getIntent().getStringExtra("imdbId");
        DetailMovieFragment.showFragment(this, imdbId);
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        title.setText("Detail Movie");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, ListOfItemActivity.class));
        finish();
    }

}
