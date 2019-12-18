package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;


public class HealthReportActivity extends Activity {
	@ViewInject(value = R.id.title_tv, parentId = R.id.healthreport_title)
	private TextView titleTv;
	@ViewInject(value = R.id.right_btn_igv, parentId = R.id.healthreport_title)
	private ImageView shareIgv;

	@ViewInject(R.id.healthreport_scl)
	private ScrollView scrollView;

	@ViewInject(value = R.id.healthreport_date, parentId = R.id.healthreport_basic_info)
	private TextView healthReport_date;
	@ViewInject(value = R.id.hr_startdate, parentId = R.id.healthreport_basic_info)
	private TextView startDate_tv;
	@ViewInject(value = R.id.hr_enddate, parentId = R.id.healthreport_basic_info)
	private TextView endDate_tv;
	@ViewInject(value = R.id.hr_sex, parentId = R.id.healthreport_basic_info)
	private TextView sex_tv;
	@ViewInject(value = R.id.hr_height, parentId = R.id.healthreport_basic_info)
	private TextView height_tv;
	@ViewInject(value = R.id.hr_age, parentId = R.id.healthreport_basic_info)
	private TextView age_tv;
	@ViewInject(value = R.id.hr_weight, parentId = R.id.healthreport_basic_info)
	private TextView weight_tv;

	@ViewInject(R.id.prchart)
	private LinearLayout PRLayout;
	@ViewInject(R.id.bpchart)
	private LinearLayout BPLayout;
	@ViewInject(R.id.calchart)
	private LinearLayout CALLayout;
	@ViewInject(R.id.bp_ll)
	private LinearLayout bpLL;
	@ViewInject(R.id.advicell)
	private LinearLayout ADVICELL;

	@ViewInject(R.id.pr_data_analysis)
	private TextView PrDataAnalysis;
	@ViewInject(R.id.bp_data_analysis)
	private TextView BpDataAnalysis;
	@ViewInject(R.id.cal_data_analysis)
	private TextView CalDataAnalysis;

	@ViewInject(R.id.life_analysis)
	private TextView LifeAdvice;
	@ViewInject(R.id.data_monitor_analysis)
	private TextView DataMonitorAdvive;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
//	private GoogleApiClient client;


	@OnClick({R.id.back_igv})
	private void onClick(View view) {
		switch (view.getId()) {
			case R.id.back_igv:
				finish();
				break;

			default:
				break;
		}
	}

	private Context context;

	private GraphicalView prViewChart, bpViewChart, calViewChart;// 用于显示现行统计图
	private XYSeries prseries, bpseries, calseries;// XY数据集
	private XYMultipleSeriesDataset prDataset, bpDataset, calDataset;// XY轴数据集


	private XYMultipleSeriesRenderer prXYRenderer, bpXYRenderer, calXYRenderer;// 线�?统计图主描绘�?
	private int prX, calX;// X数据集大小，横轴
	private int prYmin, prYmax, // 脉搏最值
			psYmax, // 收缩压最高值
			pdYmin, //舒张压最低值
			calYmax;// 卡路里最值
	// Y数据集大小，纵轴
	private int age;
	private int sex;
	private int height;
	private int weight;
	private int prXlabel, prYlabel, bpXlabel, bpYlabel, calXlabel, calYlabel;

	private String prXtitle;

	private float[] tmpPrData, tmpPsData, tmpPdData, tmpCalData, tmpDataCopy;
	private int tmpPrdataCount = 1;
	private int tmpbpdataCount = 1;
	private int calMax, calMin;
//	private String dateTime;

	IprecareThread  getReportAna;
	String customerId;
	private String jWotchModel;

	String dateHr[],dateBp[], dateCal[];

	int jsonSizeHr, jsonSizeBp,jsonSizeCal;
	boolean isBp = false;
	boolean isCal = false;
	boolean isPr = false;

	int Tyear, Tmonth, Tday;
	String calValue;

	public SharedPreferences sp;
	public Editor editor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_health_report);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ViewUtils.inject(this);

		titleTv.setText(getResources().getString(R.string.health_report));

//		shareIgv.setImageResource(R.drawable.share);
//		shareIgv.setVisibility(View.VISIBLE);
//		shareIgv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				Bitmap bitmap = getBitmapByView(scrollView);
////				BitmapDrawable bd = new BitmapDrawable(bitmap);
//
//				showShare();
//			}
//		});

		prXtitle = getResources().getString(R.string.date);


		context = this;


		sp = this.getSharedPreferences(AppConstant.USER_sp_name,
				Context.MODE_WORLD_READABLE);
		int id = sp.getInt(AppConstant.USER_customerId, 0);
		customerId = String.valueOf(id);
//

		age = sp.getInt(AppConstant.USER_age, 0);
		sex = sp.getInt(AppConstant.USER_sex, 0);

		age_tv.setText(age + "");
		if (sex == 0) {
			sex_tv.setText(getResources().getString(R.string.male));
		} else if (sex == 1) {
			sex_tv.setText(getResources().getString(R.string.female));
		}

		healthReport_date.setText(getResources().getString(R.string.date) + ":"
				+ GetDate.lastDay());
		startDate_tv.setText(GetDate.lastMonth());
		endDate_tv.setText(GetDate.lastDay());

		height = sp.getInt(AppConstant.USER_height, 0);
		weight = sp.getInt(AppConstant.USER_weight, 0);
		height_tv.setText(height + "");
		weight_tv.setText(weight + "");

		getReportAna();

//		if (jWotchModel.equals(AppConstant.JWOTCHMODEL_032)) {
//			bpLL.setVisibility(View.GONE);
//			BpDataAnalysis.setVisibility(View.GONE);
//		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected XYMultipleSeriesRenderer buildRenderer(int color,
													 PointStyle style, boolean fill, int XLabel, int YLabel, int Ymin,
													 int Ymax, int X) {// 设置图表中曲线本身的样式，包括颜色�?点的大小以及线的粗细�?
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer r = new XYSeriesRenderer();// 对点的描绘进行设�?

		if (isBp) {
			r = new XYSeriesRenderer();// 对点的描绘进行设�?
			r.setColor(Color.BLUE);
			r.setPointStyle(PointStyle.TRIANGLE);
			r.setFillPoints(fill);
			r.setLineWidth(3);
			r.setDisplayChartValues(true);
			r.setDisplayChartValuesDistance(10);
			renderer.addSeriesRenderer(r);

			r = new XYSeriesRenderer();// 对点的描绘进行设�?
			r.setColor(Color.RED);
			r.setPointStyle(PointStyle.SQUARE);
			r.setFillPoints(fill);
			r.setLineWidth(3);
			r.setDisplayChartValues(true);
			r.setDisplayChartValuesDistance(10);
			renderer.addSeriesRenderer(r);
		}
		if (isPr) {
			r = new XYSeriesRenderer();// 对点的描绘进行设�?
			r.setColor(Color.MAGENTA);
			r.setPointStyle(PointStyle.DIAMOND);
			r.setFillPoints(fill);
			r.setLineWidth(3);
			r.setDisplayChartValues(true);
			r.setDisplayChartValuesDistance(10);
//			XYSeriesRenderer.FillOutsideLine filll = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW);
//			// fill.setColor(Color.YELLOW);
//			r.addFillOutsideLine(filll);//
			renderer.addSeriesRenderer(r);

			r = new XYSeriesRenderer();// 对点的描绘进行设�?
			r.setColor(Color.RED);
			r.setPointStyle(PointStyle.POINT);
			r.setFillPoints(fill);
			r.setLineWidth(2);
			r.setDisplayChartValues(false);

			renderer.addSeriesRenderer(r);

			r = new XYSeriesRenderer();// 对点的描绘进行设�?
			r.setColor(Color.RED);
			r.setPointStyle(PointStyle.POINT);
			r.setFillPoints(fill);
			r.setLineWidth(2);
			r.setDisplayChartValues(false);

			renderer.addSeriesRenderer(r);
//			renderer.setDisplayValues(true);
		}
		if(isCal){
			r.setColor(color);
			r.setPointStyle(style);
			r.setFillPoints(fill);
//			r.setLineWidth(2);
//			r.setDisplayChartValuesDistance(10);
			renderer.addSeriesRenderer(r);
		}
		renderer.setShowGrid(true);// 显示表格
		renderer.setGridColor(Color.GRAY);// 据说绿色代表健康色调，不过我比较喜欢灰色,表格线颜�?
		renderer.setMarginsColor(Color.WHITE);// 设置空白区的颜色
		renderer.setApplyBackgroundColor(true);// 是否采用背景色，false下默认chart背景色为黑色，默认false
		renderer.setBackgroundColor(Color.WHITE);
		// mXYRenderer.setAxesColor(Color.BLACK);//坐标轴颜�?
		// mXYRenderer.setLabelsColor(Color.WHITE);//坐标名称以及标题颜色
		renderer.setXLabelsColor(Color.BLACK);// 设置X轴刻度颜�?
		renderer.setYLabelsColor(0, Color.BLACK);// 设置Y轴刻度颜�?
		renderer.setXLabels(XLabel);// X轴格�?
		renderer.setYLabels(YLabel);// Y轴格�?
		renderer.setMargins(new int[]{20, 30, 20, 20});// 上，左，下，�?

		renderer.setShowLegend(true);// 不显示图�?
		renderer.setZoomEnabled(false);
		renderer.setPanEnabled(true, false);// 允许左右拖动,但不允许上下拖动.
		renderer.setClickEnabled(false);
		renderer.setInScroll(true);
		renderer.setShowAxes(true);
		renderer.setLabelsTextSize(18);
		renderer.setLegendTextSize(15); // 设置图例文本大小
		renderer.setChartTitle(" ");
		renderer.setXTitle(prXtitle);
		renderer.setYTitle("");
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(7);
		renderer.setYAxisMin(Ymin);
		renderer.setYAxisMax(Ymax);
		renderer.setAxesColor(Color.BLACK);
		renderer.setLabelsColor(Color.BLACK);
		renderer.setFitLegend(true);
		renderer.setPointSize(5);
		renderer.setYLabelsAlign(Align.RIGHT);

		return renderer;
	}


	private void getReportAna() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==0x133){
					String result=getReportAna.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject json=JSONObject.fromObject(result);
						int code=json.getInt("code");
						if(code==200){
							String data=json.getString("Data");
							JSONObject jsonData=JSONObject.fromObject(data);

							String arrHr=jsonData.getString("ChartHR");
							JSONArray jsonArrHr=JSONArray.fromObject(arrHr);
							jsonSizeHr=jsonArrHr.size();

							if(jsonSizeHr>0){
								tmpPrData = new float[jsonSizeHr];
								dateHr = new String[jsonSizeHr];

								for (int l = 0; l < jsonSizeHr; l++) {
									JSONObject jsonRec = jsonArrHr
											.getJSONObject(l);
//								Log.v("健康报告","心率日期："+jsonRec.getString("TestDate"));
									dateHr[l] = jsonRec.getString("TestDate")
											.substring(5, 10);
//								Log.v("健康报告","心率日期："+dateHr[l]);
									tmpPrData[l] = Integer
											.parseInt(jsonRec.getString("HR"));
								}

								tmpDataCopy = tmpPrData.clone();
								Arrays.sort(tmpDataCopy);

								prYmin = (int) tmpDataCopy[0];
								prYmax = (int) tmpDataCopy[tmpDataCopy.length - 1];

								prDataset = new XYMultipleSeriesDataset(); // 创建�?��数据集的实例，这个数据集将被用来创建图表

								prX = jsonSizeHr;
								prXlabel = jsonSizeHr;
								prYlabel = 10;
								updatePRChart();
								isPr = true;
								prXYRenderer = buildRenderer(Color.rgb(176, 10, 176),
										PointStyle.DIAMOND, true, prXlabel, prYlabel, prYmin>10?prYmin-10:0, prYmax+10,
										prX);
//							int r = 1;
								prXYRenderer.addXTextLabel(0, " ");
								for (int i = 1; i < jsonSizeHr+1; i++) {
//								if (i < jsonSizeHr) {
									prXYRenderer.addXTextLabel(i, dateHr[i-1]);
//								}
								}
								prXYRenderer.setXLabels(0);

								prViewChart = ChartFactory.getLineChartView(context, prDataset,
										prXYRenderer);// 通过ChartFactory生成图表
								PRLayout.addView(prViewChart, new LayoutParams(
										LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));// 将图表添加到布局中去

								isPr = false;
							}





							String arrBp=jsonData.getString("ChartBP");
							JSONArray jsonArrBp=JSONArray.fromObject(arrBp);
							jsonSizeBp=jsonArrBp.size();

							if(jsonSizeBp>0){
								tmpPsData = new float[jsonSizeBp];
								tmpPdData = new float[jsonSizeBp];
								dateBp =new String[jsonSizeBp];

								for (int l = 0; l < jsonSizeBp; l++) {
									JSONObject jsonRec = jsonArrBp
											.getJSONObject(l);
									dateBp[l] = jsonRec.getString("TestDate")
											.substring(5, 10);

									tmpPsData[l] = Integer
											.parseInt(jsonRec.getString("PS"));
									tmpPdData[l] = Integer
											.parseInt(jsonRec.getString("PD"));
								}



//							int j = 0;

								tmpDataCopy = tmpPsData.clone();
								Arrays.sort(tmpDataCopy);

//							psYmin = (int) tmpDataCopy[0];
								psYmax = (int) tmpDataCopy[tmpDataCopy.length - 1];

								tmpDataCopy = tmpPdData.clone();
								Arrays.sort(tmpDataCopy);

								pdYmin = (int) tmpDataCopy[0];
//							pdYmax = (int) tmpDataCopy[tmpDataCopy.length - 1];


								bpDataset = new XYMultipleSeriesDataset(); // 创建�?��数据集的实例，这个数据集将被用来创建图表
								// 将点集添加到这个数据集中

								updateBPChart();

								isBp = true;
								bpXYRenderer = buildRenderer(Color.RED, PointStyle.SQUARE, true,
										prXlabel, prYlabel, pdYmin-10>0?pdYmin-10:0, psYmax+10, prX);
//							r = 1;
								bpXYRenderer.addXTextLabel(0, " ");
								for (int i = 1; i < jsonSizeBp+1; i++) {
//								if (i < jsonSizeBp) {
									bpXYRenderer.addXTextLabel(i, dateBp[i - 1]);
//									r = r + jsonSizeBp / 7 + 1;
//								}
								}
								bpXYRenderer.setXLabels(0);

								bpViewChart = ChartFactory.getLineChartView(context, bpDataset,
										bpXYRenderer);// 通过ChartFactory生成图表
								BPLayout.addView(bpViewChart, new LayoutParams(
										LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));// 将图表添加到布局中去

								isBp = false;
							}




							String arrCal=jsonData.getString("ChartCalorie");

							JSONArray jsonArrCal=JSONArray.fromObject(arrCal);

							jsonSizeCal = jsonArrCal.size();

							if(jsonSizeCal>0){
								tmpCalData = new float[jsonSizeCal];
								dateCal = new String[jsonSizeCal];
								for (int l = 0; l < jsonSizeCal; l++) {
									JSONObject jsonRec = jsonArrCal
											.getJSONObject(l);

									dateCal[l] = jsonRec.getString("TestDate").substring(5,
											10);
									tmpCalData[l] = Float.parseFloat(jsonRec
											.getString("Calorie"));
								}

								tmpDataCopy = tmpCalData.clone();
								Arrays.sort(tmpDataCopy);

//								calYmin = (int) tmpDataCopy[0];
								calYmax = (int) tmpDataCopy[tmpDataCopy.length - 1];

								calDataset = new XYMultipleSeriesDataset(); // 创建�?��数据集的实例，这个数据集将被用来创建图表

								calX = jsonSizeCal;
								calXlabel = jsonSizeCal;
								calYlabel = 10;

								updateCALChart();

								isCal = true;
								calXYRenderer = buildRenderer(Color.GREEN, PointStyle.POINT, true,
										calXlabel, calYlabel, 0, calYmax + 100, calX);
//							int a = 1;
								calXYRenderer.addXTextLabel(0, " ");
								for (int i = 1; i < jsonSizeCal+1; i++) {
//								if (a < jsonSizeCal) {
									calXYRenderer.addXTextLabel(i, dateCal[i - 1]);
//									a = a + jsonSizeCal / 7 + 1;
//								}
								}
								calXYRenderer.setXLabels(0);

								calViewChart = ChartFactory.getBarChartView(context, calDataset,
										calXYRenderer, Type.DEFAULT);// 通过ChartFactory生成图表
								CALLayout.addView(calViewChart, new LayoutParams(
										LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));// 将图表添加到布局中去

								isCal = false;
							}



							String anaHr=jsonData.getString("HRInfo");
							PrDataAnalysis.setText(convertN(anaHr));

							String anaBp=jsonData.getString("BPInfo");
							BpDataAnalysis.setText(convertN(anaBp));

							String anaCal=jsonData.getString("CalorieInfo");
							CalDataAnalysis.setText(convertN(anaCal));

							String anaLife=jsonData.getString("LifeInfo");
							LifeAdvice.setText(convertN(anaLife));

							String anaDataMonitor=jsonData.getString("AdviceInfo");
							DataMonitorAdvive.setText(convertN2(anaDataMonitor));


						}else{
							UiUtil.showToast(context,json.getString("message"));
						}
					}
				}
			}
		};

		getReportAna=new IprecareThread(ApiConstant.GETHEALTHREPORT,handler,context);
//		getReportAna.setCustomerId(customerId);
//		getReportAna.setStartTime(GetDate.lastMonth());
//		getReportAna.setEndTime(GetDate.lastDay());
//		String lan= Locale.getDefault().getLanguage();
//		if(lan.equals("zh")){
//			getReportAna.setCulture("zh-cn");
//			ADVICELL.setVisibility(View.VISIBLE);
//		}else{
//			getReportAna.setCulture("en-us");
//			ADVICELL.setVisibility(View.GONE);
//		}
//        getReportAna.start();

	}

	private void updatePRChart() {// 主要工作是每�?000ms刷新整个统计�?
		// prDataset.removeSeries(prseries);// 移除数据集中旧的点集
		// prseries.clear();// 点集先清空，为了做成新的点集而准�?
		prseries = new XYSeries(getResources().getString(R.string.bp_cur));// 这个类用来放置曲线上的所有点，是�?��点的集合，根据这些点画出曲线
		for (int k = 1; k < jsonSizeHr + 1; k++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获�?
			float y = tmpPrData[k - 1];
			prseries.add(k, y);
		}
		prDataset.addSeries(prseries);// 在数据集中添加新的点�?

		prseries = new XYSeries(getResources().getString(R.string.normal_up));
		for (int t = 1; t < jsonSizeHr + 1; t++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获�?
			prseries.add(t, 100);
		}
		prDataset.addSeries(prseries);// 在数据集中添加新的点�?

		prseries = new XYSeries(getResources().getString(R.string.normal_down));
		float prMIN = 55;
		if (age < 60) {
			prMIN = 60;
		}
		for (int t = 1; t < jsonSizeHr + 1; t++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获�?
			prseries.add(t, prMIN);
		}
		prDataset.addSeries(prseries);// 在数据集中添加新的点�?

		// prViewChart.invalidate();// 视图更新，没有这�?��，曲线不会呈现动�?
	}

	private void updateBPChart() {// 主要工作是每�?000ms刷新整个统计�?
		// bpDataset.removeSeries(bpseries);
		bpseries = new XYSeries(getResources().getString(R.string.ps));
		// 移除数据集中旧的点集
		// bpseries.clear();// 点集先清空，为了做成新的点集而准�?

		for (int k = 1; k < jsonSizeBp + 1; k++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获�?
			float y = tmpPsData[k - 1];
			bpseries.add(k, y);
		}
		bpDataset.addSeries(bpseries);// 在数据集中添加新的点�?

		bpseries = new XYSeries(getResources().getString(R.string.pd));
		for (int t = 1; t < jsonSizeBp + 1; t++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获�?
			float yt = tmpPdData[t - 1];
			bpseries.add(t, yt);
		}
		bpDataset.addSeries(bpseries);// 在数据集中添加新的点�?

		// bpViewChart.invalidate();// 视图更新，没有这�?��，曲线不会呈现动�?
	}

	private void updateCALChart() {// 主要工作是每�?000ms刷新整个统计�?
		calseries = new XYSeries(getResources().getString(R.string.calorie));// 这个类用来放置曲线上的所有点，是�?��点的集合，根据这些点画出曲线
		for (int k = 1; k < jsonSizeCal + 1; k++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获�?
			float y = tmpCalData[k - 1];
			calseries.add(k, y);
		}
		calDataset.addSeries(calseries);// 在数据集中添加新的点�?
		// calViewChart.invalidate();// 视图更新，没有这�?��，曲线不会呈现动�?
	}

	private String convertDate(String str) {
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 截取scrollview的屏
	 **/
	public static Bitmap getBitmapByView(ScrollView scrollView) {
		int h = 0;
		Bitmap bitmap = null;
		// 获取listView实际高度
		int jInt = 0;
		jInt = scrollView.getChildCount();
		for (int i = 0; i < jInt; i++) {
			h += scrollView.getChildAt(i).getHeight();
			// scrollView.getChildAt(i).setBackgroundResource(R.drawable.bg3);
		}
		jInt = 0;
		// 创建对应大小的bitmap
		bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
				Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		scrollView.draw(canvas);
		// 测试输出
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("/sdcard/health_report_screen.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (null != out) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		return bitmap;
	}



	private String convertN(String s){
		s=s.replace("[","").replace("]","").replaceAll("\"","");
		String[] ss=s.split("n");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<ss.length-1;i++){
			ss[i]=ss[i].replace("\\", "\n");
			sb.append(ss[i]);
		}
		sb.append(ss[ss.length-1]);
		return sb.toString();
	}
	private String convertN2(String s){
		s=s.replace("[","").replace("]","").replaceAll("\",\"","").replaceAll("\"","");
		String[] ss=s.split("n");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<ss.length-1;i++){
			ss[i]=ss[i].replace("\\", "\n");
			sb.append(ss[i]);
		}
		sb.append(ss[ss.length-1]);
		return sb.toString();
	}

}
