package com.iyoho.social.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iyoho.social.R;
import com.yoho.glide.GlideImageLoader;

import java.util.List;

/**
 * Created by ab053167 on 2017/12/6.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<String> mDatas;
    private Context context;
    private static OnItemClickRVListener onItemClickListener;
    public interface OnItemClickRVListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickRVListener listener){
        this.onItemClickListener=listener;
    }
    public RecyclerViewAdapter(Context context,List<String>mDatas){
        this.mDatas=mDatas;
        this.context=context;
    }
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_img,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,(int)v.getTag());
                }
            });
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        GlideImageLoader.create(holder.imageView).loadImage(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView=view.findViewById(R.id.rectangleImgView);
        }
    }

}
