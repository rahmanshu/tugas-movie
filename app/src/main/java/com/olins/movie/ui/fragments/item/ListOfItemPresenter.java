package com.olins.movie.ui.fragments.item;
import android.view.View;

import com.google.gson.Gson;
import com.olins.movie.data.api.response.MovieResponse;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ListOfItemPresenter {

    private ListOfItemFragment fragment;

    public ListOfItemPresenter(ListOfItemFragment fragment) {
        this.fragment = fragment;
    }

    void getListMovie(String s, String y, String i){
        fragment.showProgressBar();
        fragment.getApi().getListItem("a04e4108", "a",
                 s,y,i)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fragment.connectionError();
                    }

                    @Override
                    public void onNext(MovieResponse response) {
                        if (response != null) {
                            if(response.getResponse() != null){
                                try {
                                    if (response.getResponse().toLowerCase().equals("true")) {
                                        fragment.response = response;
                                        fragment.listMovies = response.getData();
                                        fragment.loadList();
                                        fragment.dismissProgressBar();
                                        fragment.swipe_container.setRefreshing(false);
                                    } else {
                                        if (response.getResponse().toLowerCase().equals("false")) {
                                            fragment.tvConnection.setVisibility(View.VISIBLE);
                                            fragment.tvConnection.setText("No data available!");
                                            fragment.noData();
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            fragment.dismissProgressBar();
                            fragment.swipe_container.setRefreshing(false);
                        }
                        fragment.swipe_container.setRefreshing(false);
                    }
                });
    }


}
