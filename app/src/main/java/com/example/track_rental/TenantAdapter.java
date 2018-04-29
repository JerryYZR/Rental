package com.example.track_rental;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class TenantAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<TenantTable> dataList;
    private PoiAddressActivity mContext;
    private NumberFormat nf;
    private LayoutInflater mInflater;

    public TenantAdapter(ArrayList<TenantTable> dataList, PoiAddressActivity mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_header_view, parent, false);
        }
        ((TextView) (convertView)).setText(dataList.get(position).getArea());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).getAreaId();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_tenant, parent, false);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        TenantTable item = dataList.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder {
        private TextView name, address, price;
        private TenantTable item;

        public ItemViewHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.tvName);
            address = (TextView) itemView.findViewById(R.id.tvAddress);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
        }

        public void bindData(TenantTable item) {
            this.item = item;
            name.setText(item.getStoreName());
            address.setText(item.getAddress());
        }
    }
}
