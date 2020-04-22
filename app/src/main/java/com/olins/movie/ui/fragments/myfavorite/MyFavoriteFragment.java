package com.olins.movie.ui.fragments.myfavorite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.olins.movie.ui.activities.BaseActivity;
import com.olins.movie.ui.adapters.MyFavoriteAdapter;
import com.olins.movie.ui.fragments.BaseFragment;
import com.olins.movie.R;
import com.olins.movie.data.api.bean.Movie;
import com.olins.movie.data.api.bean.MyFavorite;
import com.olins.movie.data.api.response.MovieResponse;
import com.olins.movie.data.sqlite.SqliteDBHelper;
import com.olins.movie.utils.EndlessOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

public class MyFavoriteFragment extends BaseFragment implements MyFavoriteAdapter.AdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MyFavoriteFragment.class.getSimpleName();

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.lnProgressBar)
    LinearLayout lnProgressBar;

    @BindView(R.id.lnDismissBar)
    RelativeLayout lnDismissBar;

    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    @BindView(R.id.lnConnectionError)
    LinearLayout lnConnectionError;

    @BindView(R.id.tvConnection)
    TextView tvConnection;

    @BindView(R.id.tvTryAgain)
    TextView tvTryAgain;

    @State
    MovieResponse response;

    public List<Movie> listMovies;

    List<com.olins.movie.data.sqlite.MyFavorite> itemProductList = new ArrayList<>();
    MyFavoritePresenter presenter;
    public List<MyFavorite> itemList = new ArrayList<>();
    public SqliteDBHelper db;

    @Override
    protected int getLayout() {
        return R.layout.fragment_myfavorite;
    }

    @Override
    public void onResume(){
        super.onResume();
        swipe_container.setOnRefreshListener(this);
        swipe_container.post(new Runnable() {
            @Override
            public void run() {
                swipe_container.setRefreshing(true);
                presenter.getMyFavorite();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MyFavoritePresenter(this);
        db = new SqliteDBHelper(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 1 : 1;
            }
        });
        rv.setLayoutManager(layoutManager);
        rv.addOnScrollListener(scrollData(""));
    }

    public static void showFragment(BaseActivity sourceActivity) {
        if (!sourceActivity.isFragmentNotNull(TAG)) {
            FragmentTransaction fragmentTransaction = sourceActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.popup_enter, R.anim.popup_exit, R.anim.popup_enter, R.anim.popup_exit);
            fragmentTransaction.replace(R.id.container, new MyFavoriteFragment(), TAG);
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
        tvTryAgain.setVisibility(View.VISIBLE);
    }

    public void noData() {
        lnProgressBar.setVisibility(View.VISIBLE);
        lnDismissBar.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        lnConnectionError.setVisibility(View.VISIBLE);
        tvConnection.setText("No data available!");
        tvTryAgain.setVisibility(View.GONE);
    }

    public void loadList(){
        rv.setAdapter(new MyFavoriteAdapter(getActivity(), itemList, this));
    }

    @OnClick(R.id.tvTryAgain)
    void retryConnection(){
        presenter.getMyFavorite();
    }

    @Override
    public void onRefresh() {
        presenter.getMyFavorite();
    }

    @Override
    public void onMethodCallback(MyFavorite movie) {
        validation(movie);
    }

    private EndlessOnScrollListener scrollData(String page) {
        return new EndlessOnScrollListener() {
            @Override
            public void onLoadMore() {
                presenter.getMyFavorite();
            }
        };
    }

    private void validation(final MyFavorite movie){

        new MaterialDialog.Builder(getActivity())
                .iconRes(R.mipmap.ic_remove)
                .backgroundColor(Color.WHITE)
                .title("Konfirmasi")
                .titleColor(Color.BLACK)
                .content("Yakin akan dihapus ?")
                .contentColor(Color.GRAY)
                .positiveText("Hapus")
                .positiveColor(Color.BLUE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        db.deleteItem(Integer.valueOf(movie.getId()));
                        presenter.getMyFavorite();
                    }
                })
                .negativeText("Batal")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .show();
    }

}
