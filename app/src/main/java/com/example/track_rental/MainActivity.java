package com.example.track_rental;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mob.MobSDK;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity{



    private ImageView animationIV;
    private AnimationDrawable animationDrawable;
    private DrawerLayout mDrawerLayout;

    private TextView time1, time2, day_num;
    private TextView city_get, city_return;
    private TextView store_get, store_return;

    private String originDate, destinationDate;
    private String modelNumber, carType;
    private Integer origin_month_num, origin_day_num;
    private Integer destination_month_num, destination_day_num;

    private Button query;

    private List<Car> carList = new ArrayList<Car>();
    private List<Car> styleList1 = new ArrayList<Car>();
    private List<Car> styleList2 = new ArrayList<Car>();
    private List<Car> styleList3 = new ArrayList<Car>();
    private List<Car> doorList = new ArrayList<Car>();


    private DataApplication dataapplication;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_order);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_location:
                        startActivity(new Intent(MainActivity.this, LBSActivity.class));
                        Toast.makeText(MainActivity.this, "导航", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_instructor:
                        Toast.makeText(MainActivity.this, "租车操作指南", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_criterion:
                        Toast.makeText(MainActivity.this, "租车收费标准", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_money:
                        Toast.makeText(MainActivity.this, "我的押金", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this, "常用联系人", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_order:
                        startActivity(new Intent(MainActivity.this, OrdersActivity.class));
                        break;
                    case R.id.nav_register:
                        startActivity(new Intent(MainActivity.this, VerifyActivity.class));
                        break;
                    default:
                }
                //mDrawerLayout.closeDrawers();
                return true;
            }
        });


        //顶部菜单栏
        animationIV = (ImageView) findViewById(R.id.img_loading);
        animationIV.setImageResource(R.drawable.loading);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();

        TopBar topBar = (TopBar) findViewById(R.id.topbar);
        topBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();//左边按钮实现的功能逻辑
            }

            @Override
            public void OnRightButtonClick() {
                //右边按钮实现的功能逻辑
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });


        time1 = (TextView) findViewById(R.id.time1);
        time2 = (TextView) findViewById(R.id.time2);
        day_num = (TextView) findViewById(R.id.day_num);
        city_get = (TextView) findViewById(R.id.city_get);
        city_return = (TextView) findViewById(R.id.city_return);
        store_get = (TextView) findViewById(R.id.store_get);
        store_return = (TextView) findViewById(R.id.store_return);
        query = (Button) findViewById(R.id.query);




        //车型和车种的点击事件
        initCars();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.car_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        CarAdapter adapter = new CarAdapter(carList);

        final RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.style_recyclerview);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager1);
        final CarAdapter adapter1 = new CarAdapter(styleList1);
        final CarAdapter adapter2 = new CarAdapter(styleList2);
        final CarAdapter adapter3 = new CarAdapter(styleList3);

        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.door_ornot_recyclerview);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(layoutManager2);
        CarAdapter adapter4 = new CarAdapter(doorList);

        //点击变红，且改变车型的adapter所包含的list
        adapter.setOnItemClickLitener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Car car = carList.get(position);
                switch (car.getName().toString()){
                    case "叉车":
                        recyclerView1.setAdapter(adapter1);
                        break;
                    case "吊车":
                        recyclerView1.setAdapter(adapter2);
                        break;
                    case "起重机":
                        recyclerView1.setAdapter(adapter3);
                        break;
                }
                carType = car.getName();
                Toast.makeText(view.getContext(), "you clicked view " + car.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter1.setOnItemClickLitener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Car car = styleList1.get(position);
                modelNumber = car.getName();
                Toast.makeText(view.getContext(), "you clicked view " + car.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter2.setOnItemClickLitener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Car car = styleList2.get(position);
                modelNumber = car.getName();
                Toast.makeText(view.getContext(), "you clicked view " + car.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter3.setOnItemClickLitener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Car car = styleList3.get(position);
                modelNumber = car.getName();
                Toast.makeText(view.getContext(), "you clicked view " + car.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter4.setOnItemClickLitener(new CarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                Car car = doorList.get(position);
                Toast.makeText(view.getContext(), "you clicked view " + car.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter4);


        //事件选取器
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日HH:mm");// HH:mm:ss
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");// HH:mm:ss
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        time1.setText(simpleDateFormat.format(date));
        time2.setText(simpleDateFormat.format(date));
        origin_month_num = Integer.parseInt(simpleDateFormat2.format(date));
        origin_day_num = Integer.parseInt(simpleDateFormat3.format(date));
        destination_month_num = Integer.parseInt(simpleDateFormat2.format(date));
        destination_day_num = Integer.parseInt(simpleDateFormat3.format(date));

        //判断有几天
        time_judge();

        //参考https://blog.csdn.net/u013148839/article/details/52191183
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=   new DateDialog(MainActivity.this,"获取当前时间日期", DateDialog.MODE_1, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime, Integer month_num, Integer day_num) {
                        Toast.makeText(MainActivity.this,dateTime,Toast.LENGTH_LONG).show();
                        originDate = dateTime;
                        origin_month_num = month_num;
                        origin_day_num = day_num;
                        time1.setText(dateTime);

                        time_judge();
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog=   new DateDialog(MainActivity.this,"获取当前时间日期", DateDialog.MODE_1, new DateDialog.InterfaceDateDialog() {
                    @Override
                    public void getTime(String dateTime, Integer month_num, Integer day_num) {
                        Toast.makeText(MainActivity.this,dateTime,Toast.LENGTH_LONG).show();
                        destinationDate = dateTime;
                        destination_month_num = month_num;
                        destination_day_num = day_num;
                        time2.setText(dateTime);

                        time_judge();
                    }
                } );
                dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateDialog.show();
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, QueryResultActivity.class);
                intent1.putExtra("city",city_get.getText());
                intent1.putExtra("modelNumber",modelNumber);
                intent1.putExtra("carType",carType);
                intent1.putExtra("originTime",time1.getText());
                intent1.putExtra("endTime",time2.getText());
                startActivity(intent1);
            }
        });

        dataapplication = (DataApplication) getApplication();

        city_get.setText(dataapplication.getCity());
        city_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchCityActivity.class);
                intent.putExtra("1_2","1");
                startActivity(intent);
            }
        });

        city_return.setText(dataapplication.getCity2());
        city_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, SearchCityActivity.class);
                intent2.putExtra("1_2","2");
                startActivity(intent2);
            }
        });

        store_get.setText(dataapplication.getStore1());
        store_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, PoiKeywordSearchActivity.class);
                intent3.putExtra("2_1","1");
                startActivity(intent3);
            }
        });

        store_return.setText(dataapplication.getStore2());
        store_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, PoiKeywordSearchActivity.class);
                intent4.putExtra("2_1","2");
                startActivity(intent4);
            }
        });
    }

    //几个list的初始化
    private void initCars() {
        for (int i = 0; i < 1; i++) {
            Car racing_bike = new Car("叉车");
            carList.add(racing_bike);
            Car truck = new Car("吊车");
            carList.add(truck);
            Car private_car = new Car("起重机");
            carList.add(private_car);
        }
        Car item = null;
        item = new Car("2t");
        styleList2.add(item);
        item = new Car("3t");
        styleList1.add(item);
        styleList2.add(item);
        item = new Car("4t");
        styleList3.add(item);
        item = new Car("5t");
        styleList1.add(item);
        item = new Car("8t");
        styleList3.add(item);
        item = new Car("10t");
        styleList1.add(item);
        item = new Car("15t");
        styleList1.add(item);


        item = new Car("送车上门");
        doorList.add(item);
        item = new Car("上门取车");
        doorList.add(item);
    }


    private void time_judge(){
        Integer temp = 0;
        if(destination_month_num < origin_month_num){
            temp = temp + (destination_month_num+12-origin_month_num)*60 + (destination_day_num-origin_day_num);
        }else{
            temp = temp + (destination_month_num-origin_month_num)*60 + (destination_day_num-origin_day_num);
        }
        if(temp < 0){
            time2.setText(time1.getText());
            day_num.setText(String.valueOf(0));
        }else{
            day_num.setText(String.valueOf(temp));
        }
    }

    @Override
    protected void onResume() {
        dataapplication = (DataApplication) getApplication();
        city_get.setText(dataapplication.getCity());
        city_return.setText(dataapplication.getCity2());
        store_get.setText(dataapplication.getStore1());
        store_return.setText(dataapplication.getStore2());
        super.onResume();
    }
}
