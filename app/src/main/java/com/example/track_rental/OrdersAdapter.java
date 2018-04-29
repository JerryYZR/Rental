package com.example.track_rental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 叶泽锐 on 2018/4/9.
 */

public class OrdersAdapter extends ArrayAdapter<OrderTable> {

    private int resourceId;
    private OrdersAdapter.ViewHolder viewHolder;
    private OrderTable orderTable;


    public OrdersAdapter(Context context, int textViewResourceId,
                         List<OrderTable> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        orderTable = getItem(position);

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new OrdersAdapter.ViewHolder();
            viewHolder.car_type = (TextView) view.findViewById (R.id.car_type);
            viewHolder.model_number = (TextView) view.findViewById (R.id.model_number);
            viewHolder.car_status = (TextView)view.findViewById(R.id.car_status);
            viewHolder.oDate = (TextView)view.findViewById(R.id.oDate);
            viewHolder.dDate = (TextView) view.findViewById (R.id.dDate);
            viewHolder.expense = (TextView)view.findViewById(R.id.expense);
            viewHolder.address = (TextView)view.findViewById(R.id.o_address);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (OrdersAdapter.ViewHolder) view.getTag();
        }

        viewHolder.car_type.setText(orderTable.getCarType());
        viewHolder.model_number.setText(orderTable.getModelNumber());
        if(orderTable.getFinishOrNot()){
            viewHolder.car_status.setText("已完成");
        }else{
            viewHolder.car_status.setText("未完成");
        }

        String[] temp1 = orderTable.getOriginTime().getDate().split(" ");
        String[] temp2 = orderTable.getEndTime().getDate().split(" ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd日HH:mm");
        viewHolder.oDate.setText(temp1[0]);
        viewHolder.dDate.setText(temp2[0]);
        viewHolder.expense.setText(orderTable.getPrice() + "");
        viewHolder.address.setText(orderTable.getStoreName());

        return view;
    }

    class ViewHolder {
        TextView car_type;
        TextView model_number;
        TextView car_status;
        TextView oDate;
        TextView dDate;
        TextView expense;
        TextView address;
    }

}