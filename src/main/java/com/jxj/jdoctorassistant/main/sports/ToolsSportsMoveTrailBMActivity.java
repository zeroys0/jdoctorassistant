package com.jxj.jdoctorassistant.main.sports;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.ProgressBarViewScale;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ToolsSportsMoveTrailBMActivity extends Activity {
	@ViewInject(R.id.title_tv)
	TextView titileName;

	@ViewInject(R.id.sports_jwotch_record_calorie_tv)
	TextView calorieTv;
	@ViewInject(R.id.sports_jwotch_record_time_tv)
	TextView timeTv;
	@ViewInject(R.id.sports_jwotch_record_distance_tv)
	TextView distanceTv;
	@ViewInject(R.id.scrollview)
	ScrollView scrollview;

	@ViewInject(R.id.sport_jwotch_record_pbvs)
	ProgressBarViewScale pBVS;

	private int  sportId, desCalorie;
	private String customerId;
	private int uId;
	private SharedPreferences sp;
	private Context context;
	private JSONArray jsonArrayGps = null;
	public MapView mMapView = null;
	public BaiduMap mBaiduMap = null;
	private boolean isTest=false;

	public LocationClient locationClient = null;

	// 自定义图标
	BitmapDescriptor mCurrentMarker = null;

	boolean isFirstLoc = true;// 是否首次定位

	double spLatitude;
	double spLongtitude;

//	Double splat;
//	Double splng;
	private List<LatLng> points = new ArrayList<LatLng>();

	private JAssistantAPIThread getSportThread;
	private Handler handler_getSport;

	@OnClick({ R.id.back_igv, R.id.right_btn_igv })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.right_btn_igv:
//			share();
			break;

		default:
			break;
		}
	}

	// 第三方分享

//	private void share() {
//
//
//
//		// 获取当前屏幕截图
////		UiUtil.showToast(context, "111");
//		// 获取windows中最顶层的view
//		View view = this.getWindow().getDecorView();
//
////		getBitmapByView(mMapView);
//
////		showShare();
//
////		view.buildDrawingCache();
////		// 获取状态栏高度
////		Rect rect = new Rect();
////		view.getWindowVisibleDisplayFrame(rect);
////		int statusBarHeights = rect.top;
////		Display display = this.getWindowManager().getDefaultDisplay();
////		// 获取屏幕宽和高
////		int widths = display.getWidth();
////		int heights = display.getHeight();
////		// 允许当前窗口保存缓存信息
////		view.setDrawingCacheEnabled(true);
////		// 去掉状态栏
////		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0,
////				statusBarHeights, widths, heights - statusBarHeights);
////		// 销毁缓存信息
////		view.destroyDrawingCache();
////
////		// 保存在指定的文件夹
////		// 判断sd卡是否存在
////		if (Environment.getExternalStorageState().equals(
////				Environment.MEDIA_MOUNTED)) {
////
////			// 创建一个文件夹对象，赋值为外部存储器的目录
////			File sdcardDir = Environment.getExternalStorageDirectory();
////			// 得到一个路径，内容是sdcard的文件夹路径和名字
////			String path = sdcardDir.getPath() + "/jWotchHelper/ScreenShot";
////			File dir = new File(path);
////
////			// 判断文件夹是否存在，不存在则创建
////			if (!dir.exists()) {
////				dir.mkdir();
////			}
////			File file = new File(dir, "MoveTrail.jpeg");
////			try {
////				FileOutputStream fos = new FileOutputStream(file);
////				if (fos != null) {
////					// 第一参数是图片格式，第二个是图片质量，第三个是输出流
////					bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
////					// 用完关闭
////					fos.flush();
////					fos.close();
////				}
////			} catch (FileNotFoundException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////
////		}
//
//	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置禁止横屏
		setContentView(R.layout.activity_sports_movetrail_bm);
		ViewUtils.inject(this);
		context = this;
//		titileOtherName.setBackground(getResources().getDrawable(
//				R.drawable.share));
		// TODO Auto-generated method stub
		titileName.setText("运动轨迹");
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		isTest=sp.getBoolean(AppConstant.Is_test,false);

		Bundle bundle = this.getIntent().getExtras();
		/* 获取Bundle中的数据，注意类型和key */
		sportId = bundle.getInt("sportId");
		calorieTv.setText(bundle.getString("calorie") + "kcal");
		timeTv.setText(bundle.getString("totaltime") + "s");
		distanceTv.setText(bundle.getString("distance") + "m");

		float f = Float.valueOf(bundle.getString("calorie"));

		int sex=sp.getInt(AppConstant.USER_sex, 0);
		int age=sp.getInt(AppConstant.USER_age, 18);
		int height=sp.getInt(AppConstant.USER_height, 170);
		int weight=sp.getInt(AppConstant.USER_weight, 60);

		int desCal=getDesCal(sex, age, height, weight);

		int i =(int) (f*100/desCal);
		pBVS.setMax(100);
		if(i<100){
			pBVS.setProgress(100-i);
		}else{
			pBVS.setProgress(0);
		}
		listSetData();

		mMapView = (MapView) this.findViewById(R.id.lmapView); // 获取地图控件引用
		mBaiduMap = mMapView.getMap();

		// 界面加载时添加绘制图层
		if (jsonArrayGps != null) {
			points.clear();
			getPoints();

			locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类

			mBaiduMap.setMyLocationEnabled(true);
			locationClient.registerLocationListener(myListener); // 注册监听函数
			setLocationOption();

			mBaiduMap.setMapStatus(MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().zoom(18).build()));

			drawLine();

		} else {
			mMapView = (MapView) this.findViewById(R.id.lmapView); // 获取地图控件引用
			mBaiduMap = mMapView.getMap();
		}
	}

	private int getDesCal(int sex,int age,int height,int weight){
		int desCalorie=1000;

		if (sex == 0) {
			desCalorie = (int) (529.4
					+ 5
					* height
					- 3
					* weight - 1.6 * age);
		} else {
			desCalorie = (int) (346.2
					+ 7.2
					* height
					- 2.8
					* weight - 0.8 * age);
		}
		return desCalorie;
	}

	private void listSetData() {
		handler_getSport = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			}
		};
		getSportThread = new JAssistantAPIThread(
				ApiConstant.GETSPORTUNITLIST, handler_getSport, context);
		getSportThread.setuId(uId);
		getSportThread.setCustomerId(String.valueOf(customerId));
		getSportThread.setId(sportId);
		getSportThread.start();

		try {
			getSportThread.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String result = getSportThread.getResult();
		if(isTest){
			UiUtil.showToast(context,result);
		}

		if (UiUtil.isResultSuccess(context, result)) {
			try {
				JSONObject j = JSONObject.fromObject(result);

				if (j.getInt("code") == 200) {
					String mResult = j.getString("Data");
					jsonArrayGps = JSONArray.fromObject(mResult);
				}else{
					jsonArrayGps=null;
					showDialog(j.getString("message"));
				}



			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private void showDialog(String string) {

		View viewNull = LayoutInflater.from(context)
				.inflate(R.layout.view_null, null);

		android.app.AlertDialog mAlertDialog = new android.app.AlertDialog.Builder(context)
				.setTitle(string)
				.setView(viewNull)
				.setNegativeButton(getResources().getString(R.string.ok),new ok())
				.create();

		mAlertDialog.show();

	}

	private class ok implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			finish();
		}

	}



	public void getPoints() {
		LatLng sourceLatLng = null;
		LatLng desLatLng = null;
		for (int i = 0; i < jsonArrayGps.size(); i++) {
			JSONObject gpsjson = jsonArrayGps.getJSONObject(i);
			int gpsStatus = gpsjson.getInt("GPSStatus");

			if (gpsStatus == 1) {
				spLatitude = Double.valueOf(gpsjson.getString("Latitude"));
				spLongtitude = Double.valueOf(gpsjson.getString("Longitude"));
				CoordinateConverter converter = new CoordinateConverter();
				converter.from(CoordinateConverter.CoordType.GPS);
				// 待转换坐标
				sourceLatLng = null;
				sourceLatLng = new LatLng(spLatitude, spLongtitude);
				converter.coord(sourceLatLng);
				desLatLng = null;
				desLatLng = converter.convert();
				points.add(desLatLng);
			}
		}

	}

	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置

			if (location == null || mMapView == null)
				return;
			if (points.size() < 1)
				return;

			location.setLatitude(points.get(0).latitude);
			location.setLongitude(points.get(0).longitude);

			// 定义Maker坐标点
						LatLng point = new LatLng(points.get(0).latitude,
								points.get(0).longitude);
						// 构建Marker图标
						BitmapDescriptor bitmap = BitmapDescriptorFactory
								.fromResource(R.drawable.movearea_start);
						// 构建MarkerOption，用于在地图上添加Marker
						OverlayOptions option = new MarkerOptions().position(point).icon(
								bitmap);
						// 在地图上添加Marker，并显示
						mBaiduMap.addOverlay(option);

						int len = points.size() - 1;
						LatLng pointend = new LatLng(points.get(len).latitude,
								points.get(len).longitude);
						// 构建Marker图标
						BitmapDescriptor bitmapend = BitmapDescriptorFactory
								.fromResource(R.drawable.movearea_end);
						// 构建MarkerOption，用于在地图上添加Marker
						OverlayOptions optionend = new MarkerOptions().position(pointend)
								.icon(bitmapend);
						// 在地图上添加Marker，并显示
						mBaiduMap.addOverlay(optionend);

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();

			mBaiduMap.setMyLocationData(locData); // 设置定位数据

			if (isFirstLoc) {
				// isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory
						.newLatLngZoom(ll, 16); // 起作用的，设置地图中心点以及缩放级别
				mBaiduMap.animateMapStatus(u);
			}
		}
	};

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(15000); // 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息

		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

		locationClient.setLocOption(option);
		locationClient.start();
	}

	public void drawLine() {
		try {
			// 构建用户绘制多边形的Option对象
			OverlayOptions ooPolyline = new PolylineOptions().width(10)
					.color(0xAAFF0000).points(points);
			mBaiduMap.addOverlay(ooPolyline);

			locationClient.requestLocation();

		} catch (Exception e) {
			e.printStackTrace();
			showDialog(getResources().getString(R.string.ma_ll_data_exception) + "！");
		}
	}


//	/**
//	 * 截取scrollview的屏
//	 * **/
//	public static Bitmap getBitmapByView(ScrollView scrollView) {
//		int h = 0;
//		Bitmap bitmap = null;
//		// 获取listView实际高度
//		int jInt = 0;
//		jInt = scrollView.getChildCount();
//		for (int i = 0; i < jInt; i++) {
//			h += scrollView.getChildAt(i).getHeight();
//			// scrollView.getChildAt(i).setBackgroundResource(R.drawable.bg3);
//		}
//		jInt = 0;
//		// 创建对应大小的bitmap
//		bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
//				Bitmap.Config.ARGB_8888);
//		final Canvas canvas = new Canvas(bitmap);
//		scrollView.draw(canvas);
//		// 测试输出
//		FileOutputStream out = null;
//		try {
//			out = new FileOutputStream("/sdcard/move_trail_screen.png");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			if (null != out) {
//				bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
//				out.flush();
//				out.close();
//			}
//		} catch (IOException e) {
//			// TODO: handle exception
//		}
//		return bitmap;
//	}


	public static Bitmap getBitmapByView(View v)
	{

		        String
		 fname = "/sdcard/jWotchHelper/move_trail_screen.png";

		        View view = v.getRootView();

		        view.setDrawingCacheEnabled(true);

		        view.buildDrawingCache();

		        Bitmap
		 bitmap = view.getDrawingCache();

		        if(bitmap!= null)
		 {



		          try{

		            FileOutputStream out = new FileOutputStream(fname);

		            bitmap.compress(Bitmap.CompressFormat.PNG,100,
		 out);


		          }catch(Exception e) {

		            e.printStackTrace();

		          }

		        }
				return bitmap;

		      }

//	private void showShare() {
//
//		 ShareSDK.initSDK(this);
//		 OnekeyShare oks = new OnekeyShare();
//		 //关闭sso授权
//		 oks.disableSSOWhenAuthorize();
//
//		// 分享时Notification的图标和文字 2.5.9以后不调用这个方法
//		// oks.setNotification(R.drawable.ic_launcher,getResources().getString(R.string.hphr));
//		 // title标题，印象笔记�?邮箱、信息�?微信、人人网和QQ空间使用
//		 oks.setTitle(getResources().getString(R.string.ma_trail_map));
//		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		 oks.setTitleUrl("http://android.myapp.com/myapp/detail.htm?apkName=com.jxj.healthambassador");
//		 // text是分享文本，�?��平台都需要这个字�?
//		 oks.setText(getResources().getString(R.string.ma_trail_map));
//
//		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参�?
//		 oks.setImagePath("/sdcard/jWotchHelper/move_trail_screen.png");//确保SDcard下面存在此张图片
//		 // url仅在微信（包括好友和朋友圈）中使�?
//		// oks.setUrl("/sdcard/health_report_screen.png");
//		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment(getResources().getString(R.string.ma_trail_map));
//		 // site是分享此内容的网站名称，仅在QQ空间使用
//		 oks.setSite(getResources().getString(R.string.iparecare));
//		 oks.show(this);
//
//
//	}



}
