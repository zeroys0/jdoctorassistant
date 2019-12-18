package com.jxj.jdoctorassistant.main.sports;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.model.Gps;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.PositionUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.ProgressBarViewScale;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ToolsSportsMoveTrailGMActivity extends Activity 
//implements OnMapReadyCallback 
{

//    @ViewInject(R.id.title_tv)
//    TextView titileName;
//
//    @ViewInject(R.id.sports_jwotch_record_gm_calorie_tv)
//    TextView calorieTv;
//    @ViewInject(R.id.sports_jwotch_record_gm_time_tv)
//    TextView timeTv;
//    @ViewInject(R.id.sports_jwotch_record_gm_distance_tv)
//    TextView distanceTv;
////    @ViewInject(R.id.scrollview)
////    ScrollView scrollview;
//
//    @ViewInject(R.id.sport_jwotch_record_gm_pbvs)
//    ProgressBarViewScale pBVS;
//
//    private int customerId, sportId, desCalorie;
//    private SharedPreferences sp;
//    private Context context;
//    private JSONArray jsonArrayGps = null;
//
//    private List<LatLng> points = new ArrayList<LatLng>();
//
//
//    double spLatitude;
//    double spLongtitude;
//
//    Double splat;
//    Double splng;
//
//    private JAssistantAPIThread getSportThread;
//    private Handler handler_getSport;
//
//    @OnClick({ R.id.back_igv, R.id.right_btn_igv })
//    public void click(View v) {
//        switch (v.getId()) {
//            case R.id.back_igv:
//                loaded();
//                break;
//            case R.id.right_btn_igv:
////			share();
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
////		SDKInitializer.initialize(getApplicationContext());
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置禁止横屏
//        setContentView(R.layout.activity_sports_movetrail_gm);
//        ViewUtils.inject(this);
//
//
//
//        MapFragment mapFragment =
//                (MapFragment) getFragmentManager().findFragmentById(R.id.sports_map);
//        mapFragment.getMapAsync(this);
//
//        context = this;
////		titileOtherName.setBackground(getResources().getDrawable(
////				R.drawable.share));
//        // TODO Auto-generated method stub
//        titileName.setText("运动轨迹");
//        sp = getSharedPreferences(AppConstant.USER_sp_name,
//                context.MODE_WORLD_READABLE);
//        customerId = sp.getInt(AppConstant.USER_customerId, 0);
////		desCalorie = sp.getInt("desCalorie", 0);
//
//        Bundle bundle = this.getIntent().getExtras();
//		/* 获取Bundle中的数据，注意类型和key */
//        sportId = bundle.getInt("sportId");
//        calorieTv.setText(bundle.getString("calorie") + "kcal");
//        timeTv.setText(bundle.getString("totaltime") + "s");
//        distanceTv.setText(bundle.getString("distance") + "m");
//
//        float f = Float.valueOf(bundle.getString("calorie"));
//
//        int sex=sp.getInt(AppConstant.USER_sex, 0);
//        int age=sp.getInt(AppConstant.USER_age, 18);
//        int height=sp.getInt(AppConstant.USER_height, 170);
//        int weight=sp.getInt(AppConstant.USER_weight, 60);
//
//        int desCal=getDesCal(sex, age, height, weight);
//
//        int i =(int) (f*100/desCal);
//        pBVS.setMax(100);
//        if(i<100){
//            pBVS.setProgress(100-i);
//        }else{
//            pBVS.setProgress(0);
//        }
//        listSetData();
//
//        // 界面加载时添加绘制图层
//        if (jsonArrayGps != null) {
//            points.clear();
//            getPoints();
//
//
//
//        } else {
//
//        }
//    }
//
//    private int getDesCal(int sex,int age,int height,int weight){
//        int desCalorie=1000;
//
//        if (sex == 0) {
//            desCalorie = (int) (529.4
//                    + 5
//                    * height
//                    - 3
//                    * weight - 1.6 * age);
//        } else {
//            desCalorie = (int) (346.2
//                    + 7.2
//                    * height
//                    - 2.8
//                    * weight - 0.8 * age);
//        }
//        return desCalorie;
//    }
//
//    private void listSetData() {
//        handler_getSport = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                // TODO Auto-generated method stub
//                super.handleMessage(msg);
//            }
//        };
//        getSportThread = new JAssistantAPIThread(
//                ApiConstant.GETSPORTUNITLIST, handler_getSport, context);
//        getSportThread.setCustomerId(String.valueOf(customerId));
//        getSportThread.setId(sportId);
//        getSportThread.start();
//
//        try {
//            getSportThread.join();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        String result = getSportThread.getResult();
//        if (UiUtil.isResultSuccess(context, result)) {
//            try {
//                JSONObject j = JSONObject.fromObject(result);
//
//                if (j.getInt("code") == 200) {
//                    String mResult = j.getString("SportUnitList");
//                    jsonArrayGps = JSONArray.fromObject(mResult);
//                }
//
//                else if (j.getInt("code") == 204) {
//                    jsonArrayGps = null;
//                    UiUtil.showToast(context, j.getString("message"));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//        }
//    }
//
//
//    public void getPoints() {
//        LatLng sourceLatLng = null;
//        LatLng desLatLng = null;
//        for (int i = 0; i < jsonArrayGps.size(); i++) {
//            JSONObject gpsjson = jsonArrayGps.getJSONObject(i);
//            int gpsStatus = gpsjson.getInt("GPSStatus");
//
//            if (gpsStatus == 1) {
//                spLatitude = Double.valueOf(gpsjson.getString("Latitude"));
//                spLongtitude = Double.valueOf(gpsjson.getString("Longitude"));
//
//                Gps googleGps= PositionUtil.gps84_To_Gcj02(spLatitude, spLongtitude);
////
//                desLatLng = new LatLng(googleGps.getWgLat(),googleGps.getWgLon());
////                CoordinateConverter converter = new CoordinateConverter();
////                converter.from(CoordinateConverter.CoordType.GPS);
////                // 待转换坐标
////                sourceLatLng = null;
////                sourceLatLng = new LatLng(spLatitude, spLongtitude);
////                converter.coord(sourceLatLng);
////                desLatLng = null;
////                desLatLng = converter.convert();
//                points.add(desLatLng);
//            }
//        }
//
//    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
////        LatLng MELBOURNE = new LatLng(37.35, -122.0);
////        Marker melbourne = googleMap.addMarker(new MarkerOptions()
////                .position(MELBOURNE)
////                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//
//
//        PolylineOptions rectOptions = new PolylineOptions();
//
//        for (int i=0;i<points.size();i++){
//            rectOptions.add(points.get(i));
//
//        }
//
////                .add(new LatLng(37.35, -122.0))
////                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
////                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
////                .add(new LatLng(37.35, -122.2));  // Same longitude, and 16km to the south
////                .add(new LatLng(37.35, -122.0)); // Closes the polyline.
//
//        rectOptions.width(8).color(Color.RED);
//// Get back the mutable Polyline
////        Polyline polyline = googleMap.addPolyline(rectOptions);
//
//
////        Gps googleGps= PositionUtil.gps84_To_Gcj02(spLatitude, spLongtitude);
////
////        LatLng latLng = new LatLng(googleGps.getWgLat(),googleGps.getWgLon());
//
////        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
////
////
////        if (locationManager != null) {
////
////            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
////                googleMap.setMyLocationEnabled(true);
////            }else{
////                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
////                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////                    googleMap.setMyLocationEnabled(true);
////                }
////            }
////
////        }
//
//
//
//
//
//        LatLng startLatLng = points.get(0);
//        MarkerOptions startMarker = new MarkerOptions()
//                .position(startLatLng)
////                .title("Melbourne")
////                .snippet("Population: 4,137,400")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.movearea_start));
//
//        LatLng endLatLng = points.get(points.size()-1);
//        MarkerOptions endMarker = new MarkerOptions()
//                .position(endLatLng)
////                .title("Melbourne")
////                .snippet("Population: 4,137,400")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.movearea_end));
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 16));
//        googleMap.addMarker(startMarker);
//        googleMap.addMarker(endMarker);
//
//        googleMap.addPolyline(rectOptions);
//    }
}
