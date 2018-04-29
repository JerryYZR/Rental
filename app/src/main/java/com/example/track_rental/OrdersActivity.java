package com.example.track_rental;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OrdersActivity extends AppCompatActivity {

    private List<OrderTable> itemList1 = new ArrayList<OrderTable>();
    private List<OrderTable> itemList2 = new ArrayList<OrderTable>();

    private DataApplication dataapplication;

    public String UserId;

    private DrawerLayout mDrawerLayout;

    private TextView finish, Notfinish;
    private ListView listView2, listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_orders);

        //得到UserId这个整个app的全局变量
        dataapplication = (DataApplication) getApplication();
        UserId = dataapplication.getId();


        //用户界面
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_order);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_location:
                        startActivity(new Intent(OrdersActivity.this, LBSActivity.class));
                        Toast.makeText(OrdersActivity.this, "导航", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_instructor:
                        Toast.makeText(OrdersActivity.this, "租车操作指南", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_criterion:
                        Toast.makeText(OrdersActivity.this, "租车收费标准", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_money:
                        Toast.makeText(OrdersActivity.this, "我的押金", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(OrdersActivity.this, "常用联系人", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_order:
                        Toast.makeText(OrdersActivity.this, "我的订单", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_register:
                        startActivity(new Intent(OrdersActivity.this, VerifyActivity.class));
                        break;
                    default:
                }
                //mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //顶部menu
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


        finish = (TextView) findViewById(R.id.finish);
        Notfinish = (TextView) findViewById(R.id.Notfinish);
        listView1 = (ListView) findViewById(R.id.order_list);
        listView2 = (ListView) findViewById(R.id.order_list2);

        Notfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notfinish.setTextColor(Color.parseColor("#fbff5500"));
                finish.setTextColor(Color.parseColor("#ffffff"));
                listView1.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.GONE);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish.setTextColor(Color.parseColor("#fbff5500"));
                Notfinish.setTextColor(Color.parseColor("#ffffff"));
                listView2.setVisibility(View.VISIBLE);
                listView1.setVisibility(View.GONE);
            }
        });

        Bmob.initialize(this, "f2866fc6b4319ea05ddc197f51b43994");
        getData();
    }


    private void getData(){

        BmobQuery<OrderTable> query = new BmobQuery<OrderTable>();
        query.addWhereEqualTo("clientPhoneNumber", UserId);

        query.findObjects(new FindListener<OrderTable>() {
            @Override
            public void done(List<OrderTable> object, BmobException e) {
                if(e==null){

                    OrderTable item = null;

                    for (OrderTable orderTable : object) {
                        item = new OrderTable(orderTable.getStoreId(),orderTable.getStoreName(), orderTable.getPrice()
                                , orderTable.getClientPhoneNumber(),orderTable.getOriginTime(), orderTable.getEndTime()
                                , orderTable.getOrderEvaluation(), orderTable.getFinishOrNot(), orderTable.getModelNumber(),orderTable.getCarType());
                        if(item.getFinishOrNot()){
                            itemList2.add(item);
                        }else{
                            itemList1.add(item);
                        }
                    }

                    if(itemList1 == null){

                    }else{
                        //适配器，listview
                        OrdersAdapter adapter = new OrdersAdapter(OrdersActivity.this, R.layout.order_item, itemList1);
                        listView1.setAdapter(adapter);
                    }

                    if(itemList2 == null){

                    }else{
                        //适配器，listview
                        OrdersAdapter adapter = new OrdersAdapter(OrdersActivity.this, R.layout.order_item, itemList2);
                        listView2.setAdapter(adapter);
                    }



                }else{
                    Log.i("bmob","失败1："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });


    }
}
