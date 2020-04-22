package com.olins.movie.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.olins.movie.data.api.bean.ItemList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemProductResponse  extends GenericResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<ItemList> data = new ArrayList<>();

    public List<ItemList> getData() {
        return data;
    }

    public void setData(List<ItemList> data) {
        this.data = data;
    }
}
