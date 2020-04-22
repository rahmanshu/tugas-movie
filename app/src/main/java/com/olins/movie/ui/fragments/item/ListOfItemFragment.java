package com.olins.movie.ui.fragments.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.olins.movie.ui.activities.BaseActivity;
import com.olins.movie.ui.adapters.ItemProductAdapter;
import com.olins.movie.ui.fragments.BaseFragment;
import com.olins.movie.R;
import com.olins.movie.data.api.bean.Movie;
import com.olins.movie.data.api.response.MovieResponse;
import com.olins.movie.data.sqlite.SqliteDBHelper;
import com.olins.movie.utils.EndlessOnScrollListener;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;


public class ListOfItemFragment extends BaseFragment implements ItemProductAdapter.AdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = ListOfItemFragment.class.getSimpleName();

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

    @BindView(R.id.tvSearch)
    TextView tvSearch;

    @BindView(R.id.etSearch)
    TextView etSearch;

    @BindView(R.id.sSearch)
    Spinner sSearch;

    @BindView(R.id.tvConnection)
    TextView tvConnection;

    @BindView(R.id.lnConnectionError)
    LinearLayout lnConnectionError;

    @State
    MovieResponse response;

    public List<Movie> listMovies;
    ListOfItemPresenter presenter;
    public SqliteDBHelper db;
    private String categorySearch = "";
    private String s = "batman", y = "", i = "";

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_item;
    }

    @Override
    public void onResume(){
        super.onResume();

        swipe_container.setOnRefreshListener(this);
        swipe_container.post(new Runnable() {
            @Override
            public void run() {
                swipe_container.setRefreshing(true);
                presenter.getListMovie(s, y, i);
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ListOfItemPresenter(this);
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
            fragmentTransaction.replace(R.id.container, new ListOfItemFragment(), TAG);
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
        tvConnection.setVisibility(View.GONE);
    }

    public void connectionError() {
        lnProgressBar.setVisibility(View.VISIBLE);
        lnDismissBar.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        lnConnectionError.setVisibility(View.VISIBLE);
        tvConnection.setText(R.string.connection_error);
    }

    public void noData() {
        lnProgressBar.setVisibility(View.VISIBLE);
        lnDismissBar.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        lnConnectionError.setVisibility(View.VISIBLE);
    }

    public void loadList(){
        rv.setAdapter(new ItemProductAdapter(getActivity(), listMovies, this));
    }

    @OnClick(R.id.tvTryAgain)
    void retryConnection(){
        presenter.getListMovie(s, y, i);
    }

    @Override
    public void onRefresh() {
        presenter.getListMovie(s, y, i);
    }

    @Override
    public void onMethodCallback(Movie movie) {
        addItemToLocalStorage(movie.getTitle(), movie.getYear(), movie.getImdbId(), movie.getType(), movie.getPoster());
    }

    public void addItemToLocalStorage(String title, String year, String imdbid, String type, String poster) {
        long id = db.insertItem(title, year, imdbid, type, poster);
        com.olins.movie.data.sqlite.MyFavorite n = db.getItemProduct(id);
        if (n != null) {
            Toast.makeText(getActivity(), "Data berhasil dijadikan favorit!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.tvSearch)
    void doSearchMovie(){
        categorySearch =  sSearch.getSelectedItem().toString();
        if(categorySearch.toLowerCase().equals("title")){
            if(etSearch.getText().toString().equals("")){
               s = "batman";
            }else{
               s = etSearch.getText().toString();
            }
            y = ""; i = "";
        }else if(categorySearch.toLowerCase().equals("year")){
            if(etSearch.getText().toString().equals("")){
                s = "batman";
            }else{
                y = etSearch.getText().toString();
            }
        }else{
            if(etSearch.getText().toString().equals("")){
                s = "batman";
            }else{
                i = etSearch.getText().toString();
            }
        }
        presenter.getListMovie(s, y, i);
    }

    private EndlessOnScrollListener scrollData(String page) {
        return new EndlessOnScrollListener() {
            @Override
            public void onLoadMore() {
                presenter.getListMovie(s, y, i);
            }
        };
    }

}
