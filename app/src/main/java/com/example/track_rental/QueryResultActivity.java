package com.example.track_rental;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.wrappers.PaySDKWrapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class QueryResultActivity extends AppCompatActivity {

    private List<CarTable> carList = new ArrayList<CarTable>();
    private List<TenantTable> avaliableList1 = new ArrayList<TenantTable>();
    private List<TenantTable> avaliableList2 = new ArrayList<TenantTable>();
//    private List<TenantTable> avaliableList3 = new ArrayList<TenantTable>();

    private String city = "";
    private String modelNumber = "";
    private String carType = "";
    private String originTime = "";
    private String endTime = "";
    private List<Double> priceList = new ArrayList<Double>();
    private List<Double> priceList2 = new ArrayList<Double>();

    private boolean temp = true;
    private boolean temp2 = true;


    private TextView sort_price, sort_evaluation;
    private TextView info_city, info_origintime, info_endtime, info_cartype, info_modelnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_query_result);


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
        city = intent1.getStringExtra("city");
        carType = intent1.getStringExtra("carType");
        modelNumber = intent1.getStringExtra("modelNumber");
        originTime = intent1.getStringExtra("originTime");
        endTime = intent1.getStringExtra("endTime");


        sort_price = (TextView) findViewById(R.id.sort_price);
        sort_evaluation = (TextView) findViewById(R.id.sort_evaluation);
        info_cartype = (TextView) findViewById(R.id.info_cartype);
        info_city = (TextView) findViewById(R.id.info_city);
        info_endtime = (TextView) findViewById(R.id.info_endtime);
        info_modelnumber = (TextView) findViewById(R.id.info_modelnumber);
        info_origintime = (TextView) findViewById(R.id.info_origintime);


        info_cartype.setText(carType);
        info_modelnumber.setText(modelNumber);
        info_city.setText(city);
        info_origintime.setText(originTime);
        info_endtime.setText(endTime);


        sort_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_price.setTextColor(Color.parseColor("#ff3c00"));
                sort_evaluation.setTextColor(Color.parseColor("#ffffff"));
                if(temp){
                    temp = false;
                    orderByPriceInverted();
                    sort_price.setText("价格由高到低∧");
                }else{
                    temp = true;
                    orderByPricePositive();
                    sort_price.setText("价格由低到高∨");
                }
                sort_View();
            }
        });

        sort_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_price.setTextColor(Color.parseColor("#ffffff"));
                sort_evaluation.setTextColor(Color.parseColor("#ff3c00"));
                if(temp){
                    temp = false;
                    orderByEvaluationInverted();
                    sort_evaluation.setText("评价由高到低∧");
                }else{
                    temp = true;
                    orderByEvaluationPositive();
                    sort_evaluation.setText("评价由低到高∨");
                }
                sort_View();
            }
        });


        getData();

//        initCar();
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_car_type);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new CarTypeAdapter(carList);
//
//        recyclerView.setAdapter(adapter);


        //init();

    }

    private void initCar() {
        for (int i = 0; i < 1; i++) {
            CarTable item = null;
            item = new CarTable("3t", "吊车", "1");
            carList.add(item);
            item = new CarTable("3t", "起重机", "1");
            carList.add(item);
            item = new CarTable("3t", "叉车", "1");
            carList.add(item);
        }
    }

    private void init() {
        for (int i = 0; i < 1; i++) {
            TenantTable item = null;
            item = new TenantTable("1","丁桥中豪四季公馆店", "15967186973", 1, 5, "江干区丁桥长虹路118号","杭州", "江干区", true);
            avaliableList1.add(item);
            item = new TenantTable("1","丁桥中豪四季公馆店", "15967186973", 1, 5, "江干区丁桥长虹路118号","杭州", "江干区", true);
            avaliableList1.add(item);
            item = new TenantTable("1","丁桥中豪四季公馆店", "15967186973", 1, 5, "江干区丁桥长虹路118号","杭州", "江干区", true);
            avaliableList1.add(item);
        }
    }

    private void getData(){

        BmobQuery<CarTable> que1 = new BmobQuery<CarTable>();
        que1.addWhereEqualTo("modelNumber", modelNumber);
        BmobQuery<CarTable> que2 = new BmobQuery<CarTable>();
        que2.addWhereEqualTo("carType", carType);

        List<BmobQuery<CarTable>> andQuerys = new ArrayList<BmobQuery<CarTable>>();

        andQuerys.add(que1);
        andQuerys.add(que2);
        //查询符合整个and条件的人
        BmobQuery<CarTable> query = new BmobQuery<CarTable>();
        query.and(andQuerys);

        query.findObjects(new FindListener<CarTable>() {
            @Override
            public void done(List<CarTable> object, BmobException e) {
                if(e==null){

                    ArrayList<String> tempList = new ArrayList<String>();
                    for (CarTable carTable : object) {
                        tempList.add(carTable.getCarId());
                    }

                    for(int i=0; i<tempList.size(); i++){
                        BmobQuery<AvaliableCar> que3 = new BmobQuery<AvaliableCar>();
                        que3.addWhereEqualTo("carId", tempList.get(i));
                        BmobQuery<AvaliableCar> que4 = new BmobQuery<AvaliableCar>();
                        que4.addWhereEqualTo("city", city);

                        List<BmobQuery<AvaliableCar>> andQuerys2 = new ArrayList<BmobQuery<AvaliableCar>>();

                        andQuerys2.add(que3);
                        andQuerys2.add(que4);
                        //查询符合整个and条件的人
                        BmobQuery<AvaliableCar> query2 = new BmobQuery<AvaliableCar>();
                        query2.and(andQuerys2);

                        Log.i("bmob","carId："+ tempList.get(i));
                        query2.findObjects(new FindListener<AvaliableCar>() {
                            @Override
                            public void done(List<AvaliableCar> object, BmobException e) {
                                if(e==null){

                                    ArrayList<String> tempList2 = new ArrayList<String>();
                                    for (AvaliableCar avaliableCar : object) {

                                        priceList.add(avaliableCar.getPrice());
                                        Log.i("bmob","price："+ String.valueOf(avaliableCar.getPrice()));

                                        BmobQuery<TenantTable> query5 = new BmobQuery<TenantTable>();
                                        query5.addWhereEqualTo("storeId", avaliableCar.getStoreId());

                                        query5.findObjects(new FindListener<TenantTable>() {
                                            @Override
                                            public void done(List<TenantTable> object, BmobException e) {
                                                if(e==null){

                                                    TenantTable item = null;

                                                    for (TenantTable tenantTable : object) {
                                                        item = new TenantTable(tenantTable.getStoreId(),tenantTable.getStoreName(), tenantTable.getPhoneNumber()
                                                                , tenantTable.getAreaId(),tenantTable.getEvaluation(), tenantTable.getAddress()
                                                                , tenantTable.getCity(), tenantTable.getArea(),tenantTable.getSendOrNot());
                                                        avaliableList1.add(item);
                                                    }

                                                    if(avaliableList1 == null){

                                                    }else{
                                                        //init();
                                                        initView();
                                                    }

                                                }else{
                                                    Log.i("bmob","失败3："+e.getMessage()+","+e.getErrorCode());
                                                }
                                            }
                                        });
                                    }

                                }else{
                                    Log.i("bmob","失败2："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }

                }else{
                    Log.i("bmob","失败1："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });


    }


    private void initView(){

        avaliableList2 = avaliableList1;
        priceList2 = priceList;
        orderByPricePositive();

        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_avaliablecar);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager((layoutManager1));
        AvaliableTenantAdapter adapter1 = new AvaliableTenantAdapter(avaliableList2, priceList2);
        adapter1.setOnItemClickLitener(new AvaliableTenantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                TenantTable tenantTable = avaliableList2.get(position);
                Intent intent = new Intent(QueryResultActivity.this, TakeOrderActivity.class);
                intent.putExtra("carType",carType);
                intent.putExtra("modelNumber",modelNumber);
                intent.putExtra("storeName",tenantTable.getStoreName());
                intent.putExtra("originTime",originTime);
                intent.putExtra("endTime",endTime);
                intent.putExtra("price",priceList2.get(position)+"");
                intent.putExtra("storeId",tenantTable.getStoreId());
                startActivity(intent);
                Toast.makeText(view.getContext(), "you clicked view " + tenantTable.getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView1.setAdapter(adapter1);
    }

    private void orderByPricePositive(){
        //https://blog.csdn.net/Crystal_Zero/article/details/52022767
        //这里的筛选参考这个博客的

        // TODO Auto-generated method stub
        Map<Double, TenantTable> map = new TreeMap<Double, TenantTable>();

        for(int i=0; i<priceList2.size(); i++){
            map.put(priceList2.get(i), avaliableList2.get(i));
        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<Double, TenantTable>> list = new ArrayList<Map.Entry<Double, TenantTable>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Double, TenantTable>>() {
            //升序排序
            public int compare(Map.Entry<Double, TenantTable> o1,
                               Map.Entry<Double, TenantTable> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });


        priceList2.clear();
        avaliableList2.clear();
        for(Map.Entry<Double, TenantTable> mapping:list){
            priceList2.add(mapping.getKey());
            avaliableList2.add(mapping.getValue());
        }
    }

    private void orderByEvaluationPositive(){
        // TODO Auto-generated method stub
        Map<Float, Double> map = new TreeMap<Float, Double>();
        Map<Float, TenantTable> map1 = new TreeMap<Float, TenantTable>();


        for(int i=0; i<avaliableList2.size(); i++){
            map1.put(avaliableList2.get(i).getEvaluation(), avaliableList2.get(i));
        }
        for(int j=0; j<avaliableList2.size(); j++){
            map.put(avaliableList2.get(j).getEvaluation(), priceList2.get(j));
        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<Float, Double>> list = new ArrayList<Map.Entry<Float, Double>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Float, Double>>() {
            //升序排序
            public int compare(Map.Entry<Float, Double> o1,
                               Map.Entry<Float, Double> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        //这里将map.entrySet()转换成list
        List<Map.Entry<Float, TenantTable>> list2 = new ArrayList<Map.Entry<Float, TenantTable>>(map1.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list2,new Comparator<Map.Entry<Float, TenantTable>>() {
            //升序排序
            public int compare(Map.Entry<Float, TenantTable> o1,
                               Map.Entry<Float, TenantTable> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });


        priceList2.clear();
        avaliableList2.clear();
        for(Map.Entry<Float, Double> mapping:list){
            priceList2.add(mapping.getValue());
        }

        for(Map.Entry<Float, TenantTable> mapping:list2){
            avaliableList2.add(mapping.getValue());
        }
    }

    private void orderByPriceInverted(){
        //https://blog.csdn.net/Crystal_Zero/article/details/52022767
        //这里的筛选参考这个博客的

        // TODO Auto-generated method stub
        Map<Double, TenantTable> map = new TreeMap<Double, TenantTable>();

        for(int i=0; i<priceList2.size(); i++){
            map.put(priceList2.get(i), avaliableList2.get(i));
        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<Double, TenantTable>> list = new ArrayList<Map.Entry<Double, TenantTable>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Double, TenantTable>>() {
            //升序排序
            public int compare(Map.Entry<Double, TenantTable> o1,
                               Map.Entry<Double, TenantTable> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });


        priceList2.clear();
        avaliableList2.clear();
        for(Map.Entry<Double, TenantTable> mapping:list){
            priceList2.add(mapping.getKey());
            avaliableList2.add(mapping.getValue());
        }
    }

    private void orderByEvaluationInverted(){
        // TODO Auto-generated method stub
        Map<Float, Double> map = new TreeMap<Float, Double>();
        Map<Float, TenantTable> map1 = new TreeMap<Float, TenantTable>();


        for(int i=0; i<avaliableList2.size(); i++){
            map1.put(avaliableList2.get(i).getEvaluation(), avaliableList2.get(i));
        }
        for(int j=0; j<avaliableList2.size(); j++){
            map.put(avaliableList2.get(j).getEvaluation(), priceList2.get(j));
        }

        //这里将map.entrySet()转换成list
        List<Map.Entry<Float, Double>> list = new ArrayList<Map.Entry<Float, Double>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<Float, Double>>() {
            //升序排序
            public int compare(Map.Entry<Float, Double> o1,
                               Map.Entry<Float, Double> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });

        //这里将map.entrySet()转换成list
        List<Map.Entry<Float, TenantTable>> list2 = new ArrayList<Map.Entry<Float, TenantTable>>(map1.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list2,new Comparator<Map.Entry<Float, TenantTable>>() {
            //升序排序
            public int compare(Map.Entry<Float, TenantTable> o1,
                               Map.Entry<Float, TenantTable> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });


        priceList2.clear();
        avaliableList2.clear();
        int temp = 0;
        for(Map.Entry<Float, Double> mapping:list){
            priceList2.add(mapping.getValue());
        }

        temp = 0;
        for(Map.Entry<Float, TenantTable> mapping:list2){
            avaliableList2.add(mapping.getValue());
        }
    }


    private void sort_View(){

        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_avaliablecar);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager((layoutManager1));
        AvaliableTenantAdapter adapter1 = new AvaliableTenantAdapter(avaliableList2, priceList2);
        adapter1.setOnItemClickLitener(new AvaliableTenantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TextView textView, int position) {
                TenantTable tenantTable = avaliableList2.get(position);
                Intent intent = new Intent(QueryResultActivity.this, TakeOrderActivity.class);
                intent.putExtra("carType",carType);
                intent.putExtra("modelNumber",modelNumber);
                intent.putExtra("storeName",tenantTable.getStoreName());
                intent.putExtra("originTime",originTime);
                intent.putExtra("endTime",endTime);
                intent.putExtra("price",priceList2.get(position)+"");
                intent.putExtra("storeId",tenantTable.getStoreId());
                startActivity(intent);
                Toast.makeText(view.getContext(), "you clicked view " + tenantTable.getStoreName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView1.setAdapter(adapter1);
    }
}
