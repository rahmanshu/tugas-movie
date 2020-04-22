package com.olins.movie.ui.fragments.myfavorite;

import android.view.View;

import com.olins.movie.data.api.bean.MyFavorite;

import java.util.ArrayList;

public class MyFavoritePresenter {

    private MyFavoriteFragment fragment;

    public MyFavoritePresenter(MyFavoriteFragment fragment) {
        this.fragment = fragment;
    }


    void getMyFavorite(){
        fragment.showProgressBar();
        fragment.itemList = new ArrayList<>();
        fragment.itemProductList = fragment.db.getAllItemProduct();
        if(fragment.itemProductList != null && fragment.itemProductList.size() > 0){
            for(int i = 0; i < fragment.itemProductList.size(); i++){
                MyFavorite itemList = new MyFavorite();
                itemList.setId(String.valueOf(fragment.itemProductList.get(i).getId()));
                itemList.setTitle(fragment.itemProductList.get(i).getTitle());
                itemList.setYear(fragment.itemProductList.get(i).getYear());
                itemList.setImdbid(fragment.itemProductList.get(i).getImdbid());
                itemList.setType(fragment.itemProductList.get(i).getType());
                itemList.setPoster(fragment.itemProductList.get(i).getPoster());
                itemList.setTimestamp(fragment.itemProductList.get(i).getTimestamp());
                fragment.itemList.add(itemList);
            }
        }
        fragment.loadList();
        fragment.dismissProgressBar();
        fragment.swipe_container.setRefreshing(false);

        if(fragment.itemProductList.size() == 0){
            fragment.noData();
        }

    }


}
