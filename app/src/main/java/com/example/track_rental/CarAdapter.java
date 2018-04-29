package com.example.track_rental;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 叶泽锐 on 2018/4/4.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder>{

    private OnItemClickListener mOnItemClickListener = null;

    private List<Car> mCarList;

    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    private View view;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View CarView;
        TextView CarName;
        LinearLayout CarLinear;

        public ViewHolder(View view) {
            super(view);
            CarView = view;
            CarName = (TextView) view.findViewById(R.id.Car_name);
            CarLinear = (LinearLayout) view.findViewById(R.id.linear_car);
        }
    }

    public CarAdapter(List<Car> CarList) {
        mCarList = CarList;

        isClicks = new ArrayList<Boolean>();
        for(int i = 0;i<mCarList.size();i++){
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.CarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    //点击改变颜色
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Car car = mCarList.get(position);
        holder.CarName.setText(car.getName());



        holder.itemView.setTag(holder.CarName);
        if(isClicks.get(position)){
            holder.CarLinear.setBackgroundResource(R.drawable.background_car_style2);
            holder.CarName.setTextColor(Color.parseColor("#ffffff"));
        }else{
            holder.CarLinear.setBackgroundResource(R.drawable.background_car_style);
            holder.CarName.setTextColor(Color.parseColor("#000000"));
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
                    mOnItemClickListener.onItemClick(holder.itemView,holder.CarName, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCarList.size();
    }

}
