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
import com.android.liumeng.zhihudaily.model.ListItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liumeng on 2015/9/11.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {
    private LayoutInflater inflater;
    private List<ListItem> items;
    private RecyclerClickListner clickListener;

    public ListAdapter(Context context, List<ListItem> items) {
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(List<ListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setOnClickListener(RecyclerClickListner listener) {
        this.clickListener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CustomViewHolder holder;
        if (viewType == ListItem.DATE) {
            View view = inflater.inflate(R.layout.daily_item_date_layout, parent, false);
            holder = new CustomViewHolder(view, viewType);
        } else {
            View view = inflater.inflate(R.layout.daily_item_layout, parent, false);
            holder = new CustomViewHolder(view, clickListener, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ListItem item = items.get(position);
        if (item.type == ListItem.ITEM) {
            DailyItem daily = (DailyItem) item.item;
            ImageLoader.getInstance().displayImage(daily.images.get(0), holder.image);
            holder.title.setText(daily.title);
            if (daily.multipic) {
                holder.imageMulti.setVisibility(View.VISIBLE);
            } else {
                holder.imageMulti.setVisibility(View.GONE);
            }
        } else if (item.type == ListItem.DATE) {
            holder.date.setText((String) item.item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView date;
        ImageView image;
        TextView title;
        ImageView imageMulti;
        RecyclerClickListner clickListener;
        int viewType;

        public CustomViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            date = (TextView) itemView.findViewById(R.id.daily_item_date);
        }

        public CustomViewHolder(View itemView, RecyclerClickListner clickListener, int viewType) {
            super(itemView);
            this.clickListener = clickListener;
            this.viewType = viewType;
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