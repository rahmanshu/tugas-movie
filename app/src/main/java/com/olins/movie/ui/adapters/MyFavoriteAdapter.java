package com.olins.movie.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olins.movie.ui.activities.BaseActivity;
import com.olins.movie.ui.activities.DetailMovieActivity;
import com.olins.movie.R;
import com.olins.movie.data.api.bean.MyFavorite;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.MyFavoriteHolder> {

    private Context context;
    private List<MyFavorite> list;
    private AdapterCallback mAdapterCallback;

    public MyFavoriteAdapter(Context context, List<MyFavorite> list, AdapterCallback callback){
        this.context = context;
        this.list = list;
        this.mAdapterCallback = callback;
    }

    @NonNull
    @Override
    public MyFavoriteAdapter.MyFavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_favorite, parent, false);
        MyFavoriteAdapter.MyFavoriteHolder myFavoriteAdapterHolder = new MyFavoriteAdapter.MyFavoriteHolder(itemView);
        return myFavoriteAdapterHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavoriteAdapter.MyFavoriteHolder holder, int position) {
        final MyFavorite item = list.get(position);
        holder.itemView.setTag(item);
        holder.tvTitle.setText(item.getTitle());
        holder.tvType.setText(item.getType());
        holder.tvYear.setText(item.getYear());
        Picasso.with(context).load(item.getPoster())
                .fit()
                .into(holder.ivProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailMovieActivity.startActivity((BaseActivity) context, item.getImdbid());
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallback.onMethodCallback(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyFavoriteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvType)
        TextView tvType;

        @BindView(R.id.tvYear)
        TextView tvYear;

        @BindView(R.id.ivProduct)
        ImageView ivProduct;

        @BindView(R.id.ivRemove)
        ImageView ivRemove;

        public MyFavoriteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface AdapterCallback {
        void onMethodCallback(MyFavorite movie);
    }

}
