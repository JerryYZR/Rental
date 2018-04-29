package com.example.track_rental;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 叶泽锐 on 2018/4/24.
 */

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.ViewHolder>{

    private List<CarTable> mcarList;

    private OnItemClickListener mOnItemClickListener = null;

    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    private View view;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View carView;
        TextView carName;

        public ViewHolder(View view) {
            super(view);
            carView = view;
            carName = (TextView) view.findViewById(R.id.car_type);
        }
    }

    public CarTypeAdapter(List<CarTable> avaliableList) {
        mcarList = avaliableList;
        isClicks = new ArrayList<Boolean>();
        for(int i = 0;i<mcarList.size();i++){
            if(i==0){
                isClicks.add(true);
            }
            isClicks.add(false);
        }
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, TextView textView, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avaliable_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.carView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CarTable carTable = mcarList.get(position);
        holder.carName.setText(carTable.getCarType());

        holder.itemView.setTag(holder.carName);
        if(isClicks.get(position)){
            holder.itemView.setBackgroundColor(Color.WHITE);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
//        if(addDevice.isFirst()) {
//            viewHolder.viewSpace.setVisibility(View.GONE);
//        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i = 0; i <isClicks.size();i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(holder.itemView,holder.carName, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mcarList.size();
    }
}
