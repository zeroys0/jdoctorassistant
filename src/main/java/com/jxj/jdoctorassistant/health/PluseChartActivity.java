package com.jxj.jdoctorassistant.health;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import net.sf.json.JSONObject;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PluseChartActivity extends Activity {
	@ViewInject(value= R.id.title_tv, parentId=R.id.pluse_chart_title)
	TextView titleTv;
	
	@ViewInject(R.id.datedata_chart)
	private LinearLayout datedataChartLL;

	@ViewInject(R.id.pluse_info_ll)
	private LinearLayout pluseInfoLL;
	
	@ViewInject(R.id.dd_testdate)
	private TextView ddTestDate;
	@ViewInject(R.id.dd_pr)
	private TextView ddPR;
	@ViewInject(R.id.dd_ps)
	private TextView ddPS;
	@ViewInject(R.id.dd_pd)
	private TextView ddPD;
	@ViewInject(R.id.dd_sv)
	private TextView ddSV;
	@ViewInject(R.id.dd_co)
	private TextView ddCO;
	@ViewInject(R.id.dd_tpr)
	private TextView ddTPR;
	@ViewInject(R.id.dd_ac)
	private TextView ddAC;
	
	@OnClick({R.id.back_igv})
	private void onClick(View view){
		switch (view.getId()) {
		case R.id.back_igv:
			finish();
			break;

		default:
			break;
		}
	}
	


	JAssistantAPIThread getPriDataThread;
	Context context;
	private XYSeries series;// XY数据点
	private XYMultipleSeriesDataset mDataset;// XY轴数据集
	private GraphicalView mViewChart;// 用于显示现行统计图
	private XYMultipleSeriesRenderer mXYRenderer;// 线性统计图主描绘器
	private Handler handler;
	boolean Tag = true;
	private int Ymin = 0;
	private int Ymax = 0;
	private String title = " ";
	private String dataId;
	private String result = null;
	private int[] resultData = null;
	private int[] resultDataCopy = null;
	int len = 0;
	int t = 1;
	private Timer timer = new Timer();// 定时器
	private TimerTask task;// 任务


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pluse_chart);
		ViewUtils.inject(this);
		context = this;

		titleTv.setText(getResources().getString(R.string.detailed_info));



		Bundle bundle = this.getIntent().getExtras();
		/* 获取Bundle中的数据，注意类型和key */
		String jsonSendStr = bundle.getString("pluseInfo");
        String jwotchModel=bundle.getString("jwotchModel");

		JSONObject jsonSend = JSONObject.fromObject(jsonSendStr);

		ddPS.setText(jsonSend.getString("PS")           +"\n\n"+
				getResources().getString(R.string.ps)   +"\n"  +
				"mmHg");
		ddPD.setText(jsonSend.getString("PD")           +"\n\n"+
				getResources().getString(R.string.pd)   +"\n"  +
				"mmHg");
		if(jwotchModel.equals(AppConstant.JWOTCHMODEL_041)){
			ddPR.setText(jsonSend.getString("HR")			+"\n\n"+
					getResources().getString(R.string.pr)   +"\n"  +
					getResources().getString(R.string.pr_unit));
			ddTestDate.setText(getResources().getString(R.string.test_time) + ":"
					+ jsonSend.getString("TestTime"));
			dataId = jsonSend.getString("Id");
			pluseInfoLL.setVisibility(View.GONE);
			int stype=jsonSend.getInt("SType");
			ddAC.setVisibility(View.GONE);
			if(stype==1){
				ddAC.setText(getResources().getString(R.string.stype) 	+"\n"  +
						getResources().getString(R.string.manual));
			}else{
				ddAC.setText(getResources().getString(R.string.stype) 	+"\n"  +
						getResources().getString(R.string.auto));
			}

		}else{
			ddTestDate.setText(getResources().getString(R.string.test_time) + ":"
					+ jsonSend.getString("TestDate"));
			ddPR.setText(jsonSend.getString("PR")			+"\n\n"+
					getResources().getString(R.string.pr)   +"\n"  +
					getResources().getString(R.string.pr_unit));
			ddSV.setText(jsonSend.getString("SV")			+"\n\n"+
					getResources().getString(R.string.sv) 	+"\n"  +
					"ml");
			ddCO.setText(jsonSend.getString("CO")           +"\n\n"+
					getResources().getString(R.string.co) 	+"\n"  +
					"ml");
			ddTPR.setText(jsonSend.getString("TPR")         +"\n\n"+
					getResources().getString(R.string.tpr)  +"\n"  +
					"ml");
			ddAC.setText(jsonSend.getString("AC")           +"\n\n"+
					getResources().getString(R.string.ac) 	+"\n"  +
					"ml");
			dataId = jsonSend.getString("DataId");
		}


		drawChart();

	}

	void drawChart() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			}
		};

		getPriDataThread = new JAssistantAPIThread(
				ApiConstant.GETPRIMITIVEDATA, handler, context);
		getPriDataThread.setDataId(dataId);
		try {
			getPriDataThread.start();
			getPriDataThread.join();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = null;
		result = getPriDataThread.getResult();
		if (UiUtil.isResultSuccess(context, result)) {

			try {
				len = result.length() / 4;
				resultData = new int[len];
				for (int i = 0; i < len; i++) {
					resultData[i] = Integer.parseInt(
							result.substring(i * 4, (i + 1) * 4), 16);
					System.out.println("resultdata " + i + ":" + resultData[i]);
				}
				resultDataCopy = resultData.clone();
				Arrays.sort(resultDataCopy);
				Ymin = resultDataCopy[0];
				Ymax = resultDataCopy[resultDataCopy.length - 1];
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(
						this,
						getResources()
								.getString(R.string.pr_data_ana_exception),
						Toast.LENGTH_SHORT).show();
			}
			context = getApplicationContext();// 获取上下文对象
			// 这里获得xy_chart的布局，下面会把图表画在这个布局里面
			series = new XYSeries(title);// 这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线

			mDataset = new XYMultipleSeriesDataset(); // 创建一个数据集的实例，这个数据集将被用来创建图表
			mDataset.addSeries(series);// 将点集添加到这个数据集中

			int color = Color.BLUE;// 设置线条颜色
			PointStyle style = PointStyle.CIRCLE;// 设置外观周期性显示

			mXYRenderer = buildRenderer(color, style, true);
			mXYRenderer.setShowGrid(true);// 显示表格
			mXYRenderer.setGridColor(Color.BLACK);// 据说绿色代表健康色调，不过我比较喜欢灰色,表格线颜色
			mXYRenderer.setMarginsColor(Color.WHITE);// 设置空白区的颜色
			mXYRenderer.setApplyBackgroundColor(true);// 是否采用背景色，false下默认chart背景色为黑色，默认false
			mXYRenderer.setBackgroundColor(Color.WHITE);// 设置chart的背景颜色，不包括空白区
			// mXYRenderer.setAxesColor(Color.BLACK);//坐标轴颜色
			// mXYRenderer.setLabelsColor(Color.WHITE);//坐标名称以及标题颜色
			mXYRenderer.setXLabelsColor(Color.WHITE);// 设置X轴刻度颜色
			mXYRenderer.setYLabelsColor(0, Color.WHITE);// 设置Y轴刻度颜色
			mXYRenderer.setXLabels(20);
			mXYRenderer.setYLabels(10);
			mXYRenderer.setYLabelsAlign(Align.RIGHT);// 右对齐
			mXYRenderer.setShowLegend(false);// 不显示图例
			mXYRenderer.setZoomEnabled(false);
			mXYRenderer.setPanEnabled(true, false);
			mXYRenderer.setClickEnabled(false);

			setChartSettings(mXYRenderer, title, " ", " ", 0, len / 3 + 1,
					Ymin, Ymax, Color.BLACK, Color.GREEN);
			mViewChart = ChartFactory.getLineChartView(context, mDataset,
					mXYRenderer);// 通过ChartFactory生成图表

			datedataChartLL.addView(mViewChart, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));// 将图表添加到布局中去
			t = 1;
			gif();

			// updateChart();

		}

	}

	protected XYMultipleSeriesRenderer buildRenderer(int color,
			PointStyle style, boolean fill) {// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(color);
		r.setPointStyle(style);
		r.setFillPoints(fill);
		r.setLineWidth(3);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {// 设置主描绘器的各项属性，详情可阅读官方API文档
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setMargins(new int[] { 20, 20, 0, 20 });
	}

	private void updateChart(int i) {// 主要工作是每隔1000ms刷新整个统计图
		int ii = resultData.length;
		mDataset.removeSeries(series);// 移除数据集中旧的点集
		series.clear();// 点集先清空，为了做成新的点集而准备

		for (int k = 0; k < ii / 3; k++) {// 实际项目中这些数据最好是由线程搞定，可以从WebService中获取
			int y = resultData[k + (ii * t) / 3];
			series.add(k, y);
		}

		mDataset.addSeries(series);// 在数据集中添加新的点集
		mViewChart.invalidate();// 视图更新，没有这一步，曲线不会呈现动态
	}

	void gif() {
		handler = new Handler() {// 简单的通过Handler+Task形成一个定时任务，从而完成定时更新图表的功能
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Log.i("qiuzhping", "Handler handleMessage");
					updateChart(t); // 刷新图表,handler的作用是将此方法并入主线程，在非主线程是不能修改UI的
					super.handleMessage(msg);
				}
			}
		};

		task = new TimerTask() {// 定时器
			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;// 设置标志
				handler.sendMessage(message);
				Log.i("qiuzhping", t + " ");
				t++;
				if (t > 2) {
					t = 1;
				}
			}
		};

		timer.schedule(task, 500, 1000);// 运行时间和间隔都是1000ms
	}

	@Override
	public void onDestroy() {
		if (timer != null) {// 当结束程序时关掉Timer
			timer.cancel();
			Log.i("qiuzhping", "onDestroy timer cancel");
			super.onDestroy();
		}
	}

}
