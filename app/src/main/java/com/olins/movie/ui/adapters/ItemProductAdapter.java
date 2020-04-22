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
import com.olins.movie.data.api.bean.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemProductAdapter extends RecyclerView.Adapter<ItemProductAdapter.ItemProductHolder> {

    private Context context;
    private List<Movie> list;
    private AdapterCallback mAdapterCallback;

    public ItemProductAdapter(Context context, List<Movie> list, AdapterCallback callback){
        this.context = context;
        this.list = list;
        this.mAdapterCallback = callback;
    }

    @NonNull
    @Override
    public ItemProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        ItemProductHolder itemProductHolder = new ItemProductHolder(itemView);
        return itemProductHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemProductHolder holder, int position) {
        final Movie item = list.get(position);
        holder.itemView.setTag(item);
        holder.tvTitle.setText(item.getTitle());
        holder.tvType.setText(item.getType());
        holder.tvYear.setText(item.getYear());
        Picasso.with(context).load(item.getPoster())
                .fit()
                .placeholder(R.mipmap.ic_placeholder)
                .into(holder.ivProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailMovieActivity.startActivity((BaseActivity) context, item.getImdbId());
            }
        });

        holder.ivMyFavorite.setOnClickListener(new View.OnClickListener() {
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

    public static class ItemProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvType)
        TextView tvType;

        @BindView(R.id.tvYear)
        TextView tvYear;

        @BindView(R.id.ivProduct)
        ImageView ivProduct;

        @BindView(R.id.ivMyFavorite)
        ImageView ivMyFavorite;

        public ItemProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface AdapterCallback {
        void onMethodCallback(Movie movie);
    }

}
