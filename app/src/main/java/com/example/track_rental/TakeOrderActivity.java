package com.example.track_rental;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class TakeOrderActivity extends AppCompatActivity {


    private String modelNumber = "";
    private String carType = "";
    private String originTime = "";
    private String endTime = "";
    private String storeName = "";
    private String price = "";
    private String storeId = "";
    private BmobDate origin;
    private BmobDate end;

    private TextView info_cartype, info_origintime, info_endtime, info_price, info_modelnumber, info_storename, take_order;

    private DataApplication dataapplication;

    public String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_order);

        //得到UserId这个整个app的全局变量
        dataapplication = (DataApplication) getApplication();
        UserId = dataapplication.getId();


        TopBar topBar = (TopBar) findViewById(R.id.topbar);
        topBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();//左边按钮实现的功能逻辑
            }

            @Override
            public void OnRightButtonClick() {
            }
        });

        Bmob.initialize(this, "f2866fc6b4319ea05ddc197f51b43994");

        Intent intent1 = this.getIntent();
        carType = intent1.getStringExtra("carType");
        modelNumber = intent1.getStringExtra("modelNumber");
        originTime = intent1.getStringExtra("originTime");
        endTime = intent1.getStringExtra("endTime");
        storeName= intent1.getStringExtra("storeName");
        price = intent1.getStringExtra("price");
        storeId = intent1.getStringExtra("storeId");


        String temp[] = originTime.split("月");
        String temp1[] = temp[1].split("日");
        String temp2[] = endTime.split("月");
        String temp3[] = temp2[1].split("日");

        //将时间转化为BmobDate类型
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        Date time1 = new Date();
        try {
            time = formatter.parse("2017-" + temp[0]+ "-" +temp1[0] + " " + temp1[1]+":00");
            time = formatter.parse("2017-" + temp2[0]+ "-" +temp3[0] + temp3[1]+":00");
            //time = formatter.parse("2017-" + temp[0]+ "-" +temp1[0] + " " + temp1[1]+":00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        origin = new BmobDate(time);
        end = new BmobDate(time1);

        info_cartype = (TextView) findViewById(R.id.info_cartype_take);
        info_endtime = (TextView) findViewById(R.id.info_endtime_take);
        info_modelnumber = (TextView) findViewById(R.id.info_modelnumber_take);
        info_origintime = (TextView) findViewById(R.id.info_origintime_take);
        info_storename = (TextView) findViewById(R.id.info_store_take);
        info_price = (TextView) findViewById(R.id.info_price_take);

        info_modelnumber.setText(modelNumber);
        info_cartype.setText(carType);
        info_origintime.setText(originTime);
        info_endtime.setText(endTime);
        info_storename.setText(storeName);
        info_price.setText(price);


        take_order = (TextView) findViewById(R.id.take_order);
        take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

    }

    private void addData(){
        OrderTable orderTable = new OrderTable(storeId, storeName, Double.parseDouble(price),
                UserId, origin, end, 5, false, "3t", "吊车");

        List<BmobObject> userBeans = new ArrayList<BmobObject>();
        userBeans.add(orderTable);

        orderTable.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Toast.makeText(TakeOrderActivity.this, "订单提交成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TakeOrderActivity.this, OrdersActivity.class));
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
