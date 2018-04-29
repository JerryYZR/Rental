package com.example.track_rental;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

/**
 * Created by 叶泽锐 on 2018/4/24.
 */

public class AvaliableTenantAdapter extends RecyclerView.Adapter<AvaliableTenantAdapter.ViewHolder>{

    private OnItemClickListener mOnItemClickListener = null;

    private List<TenantTable> mTenantList;
    private List<Double> mpriceList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View avaliableView;
        TextView name, address, price;
        RatingBar ratingBar;
        LinearLayout linear_sendOrNot;

        public ViewHolder(View view) {
            super(view);
            avaliableView = view;
            name = (TextView) itemView.findViewById(R.id.tvName);
            address = (TextView) itemView.findViewById(R.id.tvAddress);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar_avaliable);
            linear_sendOrNot = (LinearLayout) itemView.findViewById(R.id.linear_sendOrNot);
        }
    }

    public AvaliableTenantAdapter(List<TenantTable> mTenantList, List<Double> mpriceList) {
        this.mTenantList = mTenantList;
        this.mpriceList = mpriceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tenant, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.avaliableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                TenantTable tenantTable = mTenantList.get(position);
//                Toast.makeText(v.getContext(), "you clicked view " + tenantTable.getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TenantTable tenantTable = mTenantList.get(position);
        holder.name.setText(tenantTable.getStoreName());
        holder.address.setText(tenantTable.getAddress());
        holder.price.setText(mpriceList.get(position) + "");
        holder.ratingBar.setRating(tenantTable.getEvaluation());
        if(tenantTable.getSendOrNot()){
            holder.linear_sendOrNot.setVisibility(View.VISIBLE);
        }else{
            holder.linear_sendOrNot.setVisibility(View.GONE);
        }


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,holder.name, position);
                }
            });
        }
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, TextView textView, int position);
    }

    @Override
    public int getItemCount() {
        return mTenantList.size();
    }
}
