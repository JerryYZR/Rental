package com.example.track_rental;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PoiAddressActivity extends AppCompatActivity {

    private RecyclerView rvType;
    private StickyListHeadersListView listView;


    private ArrayList<TenantTable> goodsList = new ArrayList<TenantTable>();
    private ArrayList<TenantTable> typeList = new ArrayList<TenantTable>();
    private SparseIntArray groupSelect;

    private TenantAdapter myAdapter;
    private TypeAdapter typeAdapter;

    private NumberFormat nf;
    private Handler mHanlder;

    private String city = "杭州";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_poi_address);

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

        Intent intent1 = this.getIntent();
        city = intent1.getStringExtra("city");


        Bmob.initialize(this, "f2866fc6b4319ea05ddc197f51b43994");

        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mHanlder = new Handler(getMainLooper());
        groupSelect = new SparseIntArray();

        getData();

    }

    public void getData(){

        //获取数据
        BmobQuery<TenantTable> bmobQuery = new BmobQuery<TenantTable>();
        //不指定查找具体的哪一项则为查找全部数据
        // bmobQuery.addQueryKeys("title");

//        BmobQuery<TenantTable> que1 = new BmobQuery<TenantTable>();
//        que1.addWhereEqualTo("price", 300);
        BmobQuery<TenantTable> que2 = new BmobQuery<TenantTable>();
        que2.addWhereEqualTo("city", city);

//        List<BmobQuery<TenantTable>> andQuerys = new ArrayList<BmobQuery<TenantTable>>();
//
//        andQuerys.add(que1);
//        andQuerys.add(que2);
//        //查询符合整个and条件的人
//        BmobQuery<TenantTable> query = new BmobQuery<TenantTable>();
//        query.and(andQuerys);

        que2.findObjects(new FindListener<TenantTable>() {
            @Override
            public void done(List<TenantTable> object, BmobException e) {
                if(e==null){
                    TenantTable item = null;
                    int temp = 0;
                    int i=0;
                    int j=0;
                    ArrayList<TenantTable> tempList = new ArrayList<TenantTable>();
                    ArrayList<Integer> intList = new ArrayList<Integer>();
                    for (TenantTable tenantTable : object) {
                        item = new TenantTable(tenantTable.getStoreId(),tenantTable.getStoreName(), tenantTable.getPhoneNumber()
                                , tenantTable.getAreaId(),tenantTable.getEvaluation(), tenantTable.getAddress()
                                , tenantTable.getCity(), tenantTable.getArea(), tenantTable.getSendOrNot());
                        tempList.add(item);
                        goodsList.add(item);
                    }

                    for (i = goodsList.size()-1; i >=0; i--) {
                        for (j = typeList.size()-1; j >=0; j--){
                            if(typeList.get(j).getAreaId() == goodsList.get(i).getAreaId()){
                                temp = 1;
                                break;
                            }
                        }
                        if(temp == 0){
                            typeList.add(goodsList.get(i));
                        }
                    }

                    if(goodsList.size() !=0){
                        //initData();
                        //由于它是一个线程，所以adapter的初始化要放在线程内，否则就会由于未读取到东西而出错
                        initView();
                    }


                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }

            }
        });
    }

    private void initData(){

        TenantTable item = null;

        item = new TenantTable("1","丁桥中豪四季公馆店", "15967186973", 1, 5, "江干区丁桥长虹路118号","杭州", "江干区", true);
        goodsList.add(item);
        typeList.add(item);


    }


    private void initView(){
        rvType = (RecyclerView) findViewById(R.id.typeRecyclerView);

        listView = (StickyListHeadersListView) findViewById(R.id.itemListView);

        rvType.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter = new TypeAdapter(this,typeList);
        rvType.setAdapter(typeAdapter);
        rvType.addItemDecoration(new DividerDecoration(this));

        myAdapter = new TenantAdapter(goodsList,this);
        listView.setAdapter(myAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                TenantTable item = goodsList.get(firstVisibleItem);
                if(typeAdapter.selectTypeId != item.getAreaId()) {
                    typeAdapter.selectTypeId = item.getAreaId();
                    typeAdapter.notifyDataSetChanged();
                    rvType.smoothScrollToPosition(getSelectedGroupPosition(item.getAreaId()));
                }
            }
        });

    }



    //根据类别Id获取属于当前类别的数量
    public int getSelectedGroupCountByTypeId(int typeId){
        return groupSelect.get(typeId);
    }
    //根据类别id获取分类的Position 用于滚动左侧的类别列表
    public int getSelectedGroupPosition(int typeId){
        for(int i=0;i<typeList.size();i++){
            if(typeId==typeList.get(i).getAreaId()){
                return i;
            }
        }
        return 0;
    }

    public void onTypeClicked(int typeId){
        listView.setSelection(getSelectedPosition(typeId));
    }

    private int getSelectedPosition(int typeId){
        int position = 0;
        for(int i=0;i<goodsList.size();i++){
            if(goodsList.get(i).getAreaId() == typeId){
                position = i;
                break;
            }
        }
        return position;
    }


}
