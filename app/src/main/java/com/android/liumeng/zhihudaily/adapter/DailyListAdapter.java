package com.android.liumeng.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liumeng.zhihudaily.R;
import com.android.liumeng.zhihudaily.listener.RecyclerClickListner;
import com.android.liumeng.zhihudaily.model.DailyItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liumeng on 2015/9/6.
 */
public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<DailyItem> items;
    private RecyclerClickListner clickListener;

    public DailyListAdapter(Context context, List<DailyItem> items) {
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(List<DailyItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnClickListener(RecyclerClickListner listener) {
        this.clickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.daily_item_layout, parent, false);
        return new MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DailyItem item = items.get(position);
        ImageLoader.getInstance().displayImage(item.images.get(0), holder.image);
        holder.title.setText(item.title);
        if (item.multipic) {
            holder.imageMulti.setVisibility(View.VISIBLE);
        } else {
            holder.imageMulti.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView image;
        TextView title;
        ImageView imageMulti;
        RecyclerClickListner clickListener;

        public MyViewHolder(View itemView, RecyclerClickListner clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            image = (ImageView) itemView.findViewById(R.id.daily_item_image);
            title = (TextView) itemView.findViewById(R.id.daily_item_title);
            imageMulti = (ImageView) itemView.findViewById(R.id.daily_item_image_multi);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (clickListener != null) {
                clickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}
