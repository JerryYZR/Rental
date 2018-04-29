package com.example.track_rental;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;


//借鉴https://blog.csdn.net/alpha58/article/details/57079874
public class PoiKeywordSearchActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {


    private RecyclerView mRecyclerView;
    private EditText mEt_keyword;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private DataApplication dataapplication;
    private TextView commit;

    private double mEndLng = 0.0;
    private double mEndLat = 0.0;

    private String city_2_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_poi_keyword_search);

        initView();
        initListener();
        initData();

        Intent intent = this.getIntent();
        city_2_1 = intent.getStringExtra("2_1");

        commit = (TextView) findViewById(R.id.commit_search);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataapplication = (DataApplication) getApplication();
                if(dataapplication.getChoose() == true){
                    dataapplication.setOrigin(mEt_keyword.getText().toString());
                    dataapplication.setLat1(mEndLat);
                    dataapplication.setLon1(mEndLng);
                }else{
                    dataapplication.setDestination(mEt_keyword.getText().toString());
                    dataapplication.setLat(mEndLat);
                    dataapplication.setLon(mEndLng);
                }

                finish();
            }
        });


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

    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEt_keyword = (EditText) findViewById(R.id.et_keyword);
    }

    private void initListener() {
        mEt_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = String.valueOf(charSequence);
                if ("".equals(keyWord)) {
                    ToastUtil.show(PoiKeywordSearchActivity.this,"请输入搜索关键字");
                    return;
                } else {
                    doSearchQuery();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        //不输入城市名称有些地方搜索不到
        query = new PoiSearch.Query(keyWord, "", "浙江");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        //这里没有做分页加载了,默认给50条数据
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;
                    ArrayList<PoiAddressBean> data = new ArrayList<PoiAddressBean>();//自己创建的数据集合
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    for(PoiItem item : poiItems){
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();

                        String title = item.getTitle();
                        String text = item.getSnippet();
                        String provinceName = item.getProvinceName();
                        String cityName = item.getCityName();
                        String adName = item.getAdName();
                        data.add(new PoiAddressBean(String.valueOf(lon), String.valueOf(lat), title, text,provinceName,
                                cityName,adName));
                    }
                    PoiKeywordSearchAdapter adapter = new PoiKeywordSearchAdapter(PoiKeywordSearchActivity.this,data);
                    mRecyclerView.setAdapter(adapter);
                }
            } else {
                ToastUtil.show(PoiKeywordSearchActivity.this,
                        "no result");
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }


    /**
     * 设置详情地址
     * @param detailAddress
     */
    public void setDetailAddress(String detailAddress) {
        mEt_keyword.setText(detailAddress);
    }

    public void setmEndLat(double mEndLat) {
        this.mEndLat = mEndLat;
    }

    public void setmEndLng(double mEndLng) {
        this.mEndLng = mEndLng;
    }
}
