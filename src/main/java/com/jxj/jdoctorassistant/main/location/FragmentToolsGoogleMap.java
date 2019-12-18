package com.jxj.jdoctorassistant.main.location;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.model.Gps;
import com.jxj.jdoctorassistant.util.PositionUtil;
import com.lidroid.xutils.ViewUtils;

@SuppressLint("NewApi")
public class FragmentToolsGoogleMap extends Fragment {

	private Context context;
	private static String MAP_URL;
	private WebView webView;
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_tools_googlemap,
				container, false);
		ViewUtils.inject(this, view);
		context = getActivity().getApplicationContext();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initviews();
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void initviews() {
		sp = getActivity().getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);

		float lat=sp.getFloat("latitude", 0);
		float lng=sp.getFloat("longtitude", 0);
		
//		Gps gps=new Gps(lat, lng);
		Gps googleGps=PositionUtil.gps84_To_Gcj02(lat, lng);
		
		MAP_URL = "http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
				+ googleGps.getWgLat() + ","
				+ googleGps.getWgLon();
		webView = (WebView) getView().findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(MAP_URL);

	}

}
