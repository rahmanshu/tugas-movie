package com.olins.movie.ui.fragments.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.olins.movie.ui.activities.BaseActivity;
import com.olins.movie.ui.fragments.BaseFragment;
import com.olins.movie.R;
import com.olins.movie.data.api.bean.DetailMovie;
import com.olins.movie.data.api.response.DetailMovieResponse;
import com.olins.movie.data.sqlite.SqliteDBHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

public class DetailMovieFragment extends BaseFragment {

    public static final String TAG = DetailMovieFragment.class.getSimpleName();

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvRated)
    TextView tvRated;

    @BindView(R.id.tvRelease)
    TextView tvRelease;

    @BindView(R.id.ivDetail)
    ImageView ivDetail;

    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @BindView(R.id.lnProgressBar)
    LinearLayout lnProgressBar;

    @BindView(R.id.lnDismissBar)
    RelativeLayout lnDismissBar;

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    @BindView(R.id.lnConnectionError)
    LinearLayout lnConnectionError;

    DetailMoviePresenter presenter;
    @State
    DetailMovieResponse response;

    public DetailMovie movie;
    String imdbId;
    public SqliteDBHelper db;

    @Override
    protected int getLayout() {
        return R.layout.fragment_detail_movie;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SqliteDBHelper(getActivity());
        presenter = new DetailMoviePresenter(this);
        imdbId = getArguments().getString("imdbId");
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.getDetailMovie(imdbId);
    }

    public static void showFragment(BaseActivity sourceActivity, String imdbId) {
        if (!sourceActivity.isFragmentNotNull(TAG)) {
            FragmentTransaction fragmentTransaction = sourceActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit, R.anim.popup_enter, R.anim.popup_exit);
            DetailMovieFragment fragment = new DetailMovieFragment();
            Bundle bundle = new Bundle();
            bundle.putString("imdbId", imdbId);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, fragment, TAG);
            fragmentTransaction.commit();
        }
    }

    public void showProgressBar(){
        pbLoading.setVisibility(View.VISIBLE);
        lnConnectionError.setVisibility(View.GONE);
        lnProgressBar.setVisibility(View.VISIBLE);
        lnDismissBar.setVisibility(View.GONE);
    }

    public void dismissProgressBar(){
        lnProgressBar.setVisibility(View.GONE);
        lnDismissBar.setVisibility(View.VISIBLE);
    }

    public void connectionError() {
        lnProgressBar.setVisibility(View.VISIBLE);
        lnDismissBar.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        lnConnectionError.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvTryAgain)
    void retryConnection(){
        presenter.getDetailMovie(imdbId);
    }

    public void loadList(){
        tvTitle.setText(response.getTitle());
        tvRated.setText(response.getRated());
        tvRelease.setText("Release Date : " + response.getReleased());
        tvDesc.setText(response.getWriter());
        Picasso.with(getActivity()).load(response.getPoster())
                .fit()
                .into(ivDetail);
    }

    @OnClick(R.id.btnFav)
    void addFavorite(){
        if(response != null){
            addItemToLocalStorage(response.getTitle(), response.getYear(), response.getImdbId(), response.getType(), response.getPoster());
        }

    }

    public void addItemToLocalStorage(String title, String year, String imdbid, String type, String poster) {
        long id = db.insertItem(title, year, imdbid, type, poster);
        com.olins.movie.data.sqlite.MyFavorite n = db.getItemProduct(id);
        if (n != null) {
            Toast.makeText(getActivity(), "Data berhasil dijadikan favorit!",
                    Toast.LENGTH_LONG).show();
        }
    }


}
