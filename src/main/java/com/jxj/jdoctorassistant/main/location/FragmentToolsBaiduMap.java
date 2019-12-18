package com.jxj.jdoctorassistant.main.location;
//
//import java.io.IOException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.AppConstant;

@SuppressLint("NewApi")
public class FragmentToolsBaiduMap extends Fragment implements
		OnGetGeoCoderResultListener {

	private Context context;
//	private String customerId;
	private SharedPreferences sp;
//	private Editor editor;
	public BaiduMap baiduMap = null;
	// 定位相关声明
	public LocationClient locationClient = null;

	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	boolean isFirstLoc = true;// 是否首次定位

	double spLatitude;
	double spLongtitude;
	@Bind(R.id.address_tv)
	TextView addressTv;
	@Bind(R.id.bmapView)
	MapView mapView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(getActivity().getApplicationContext());
		View view = inflater.inflate(R.layout.fragment_tools_baidumap, container, false);
		ButterKnife.bind(this, view);
		context = getActivity().getApplicationContext();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initviews();
	}

	private void initviews() {
		sp = getActivity().getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
//		editor = sp.edit();
//		customerId = sp.getString(AppConstant.USER_customerId,null);

		spLatitude = sp.getFloat("latitude", 0);
		spLongtitude = sp.getFloat("longtitude", 0);

		addressTv.setText(getLocationAddress(spLatitude, spLongtitude));

		baiduMap = mapView.getMap();

		locationClient = new LocationClient(getActivity()
				.getApplicationContext()); // 实例化LocationClient类

		// baiduMap.setMyLocationEnabled(true);
		baiduMap.setMyLocationEnabled(true);
		// locationClient = new LocationClient(getApplicationContext()); //
		// 实例化LocationClient类
		locationClient.registerLocationListener(myListener); // 注册监听函数
		setLocationOption(); // 设置定位参数

		// mapView.refreshDrawableState();
		mapView.onResume();
		locationClient.start();
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
	}

	// 三个状态实现地图生命周期管理
	@Override
	public void onDestroy() {
		// 退出时销毁定位
		locationClient.stop();
		baiduMap.setMyLocationEnabled(false);
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
		mSearch.destroy();
		mapView = null;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mapView == null)
				return;

			LatLng sourceLatLng = new LatLng(spLatitude, spLongtitude);

			// 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
			CoordinateConverter converter = new CoordinateConverter();
			// converter.from(CoordType.COMMON);
			converter.from(CoordType.GPS);
			// sourceLatLng待转换坐标
			converter.coord(sourceLatLng);
			LatLng desLatLng = converter.convert();

			location.setLatitude(desLatLng.latitude);
			location.setLongitude(desLatLng.longitude);

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			baiduMap.setMyLocationData(locData); // 设置定位数据

			if (isFirstLoc) {
				// isFirstLoc = false;

				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());

				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLngZoom(ll, 18); // 设置地图中心点以及缩放级别
				// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				baiduMap.animateMapStatus(u);
			}
		}
	};

	/**
	 * 设置定位参数
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02

		option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息

		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

		locationClient.setLocOption(option);
	}

	private String getLocationAddress(double lat, double lng) {
		String add = "";
		String addPro = " ";
		Geocoder geoCoder = new Geocoder(getActivity().getBaseContext(),
				Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(lat, lng, 1);
			if (addresses.size() != 0) {
				Address address = addresses.get(0);

				if (address.getAdminArea().equals(address.getLocality())) {
					addPro = address.getAdminArea();
				} else {
					addPro = address.getAdminArea() + " "
							+ address.getLocality();
				}

				// System.out.println("1*省"+address.getAdminArea());
				// System.out.println("2*市"+address.getLocality());
				int maxLine = address.getMaxAddressLineIndex();
				if (maxLine >= 2) {
					add = address.getAddressLine(1) + " "
							+ address.getAddressLine(2);
				} else {
					add = address.getAddressLine(1);
				}
			} else {
				add = getResources().getString(R.string.address_data_req_fail);
			}

		} catch (IOException e) {
			add = getResources().getString(R.string.map_exception);
			e.printStackTrace();
		}
		return addPro + " " + add;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

}
