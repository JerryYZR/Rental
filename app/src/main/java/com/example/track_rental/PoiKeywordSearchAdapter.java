package com.example.track_rental;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 叶泽锐 on 2018/4/3.
 */

public class PoiKeywordSearchAdapter extends RecyclerView.Adapter<PoiKeywordSearchAdapter.MyViewHolder> {

    private DataApplication myapplication;
    List<PoiAddressBean> poiAddressBean;
    Context mContext;
    public PoiKeywordSearchAdapter(Context context, List<PoiAddressBean> poiAddressBean) {
        this.poiAddressBean  = poiAddressBean;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_poi_keyword_search, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final PoiAddressBean poiAddressBean = this.poiAddressBean.get(position);
        holder.tv_detailAddress.setText(poiAddressBean.getDetailAddress());
        holder.tv_content.setText(poiAddressBean.getText());
        holder.ll_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PoiKeywordSearchActivity)mContext).setDetailAddress(poiAddressBean.getDetailAddress());
                ((PoiKeywordSearchActivity)mContext).setmEndLat(Double.parseDouble(poiAddressBean.getLat()));
                ((PoiKeywordSearchActivity)mContext).setmEndLng(Double.parseDouble(poiAddressBean.getLong()));
                //给句目标的经纬度
                //myapplication.setLat(Double.parseDouble(poiAddressBean.getLat()));
                //myapplication.setLon(Double.parseDouble(poiAddressBean.getLong()));
                //Log.d("PoiKeywordSearch", poiAddressBean.getLat());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (poiAddressBean != null) {
            return poiAddressBean.size();
        } else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_content;
        TextView     tv_detailAddress;
        LinearLayout ll_item_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_detailAddress = (TextView) itemView.findViewById(R.id.tv_detailAddress);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ll_item_layout = (LinearLayout) itemView.findViewById(R.id.ll_item_layout);
        }
    }
}
