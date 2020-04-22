package com.olins.movie.ui.fragments.item;
import com.olins.movie.data.api.response.DetailMovieResponse;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailMoviePresenter {

    private DetailMovieFragment fragment;

    public DetailMoviePresenter(DetailMovieFragment fragment) {
        this.fragment = fragment;
    }

    void getDetailMovie(String imdbId){
        fragment.showProgressBar();
        fragment.getApi().getDetailMovie("a04e4108",
                imdbId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DetailMovieResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fragment.connectionError();
                    }

                    @Override
                    public void onNext(DetailMovieResponse response) {
                        if (response != null) {
                            fragment.response = response;
                            System.out.println("=========>>> " + response.getActors());
                            //fragment.movie = response;
                            fragment.loadList();
                            fragment.dismissProgressBar();
                        } else {
                            fragment.dismissProgressBar();
                        }
                    }
                });
    }

}
