package com.jxj.jdoctorassistant.health;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ChartDataListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;

import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.DateDialog;
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

import java.util.Arrays;

public class ChartActivity extends Activity {

    private Context context;
    private int type = 1;
    private int n = 1;

    private SharedPreferences sp;
    private int jsonsize;
    private String customerId,jwotchModel;
    private int uerId;
    private JAssistantAPIThread getDataThread;
    private Handler handler_getData;
    private String[] date;
    private float[] calorieData, psData, pdData, prData;
    private float calorieYMax, psYMax, pdYMin, prYMax, prYMin;
    private JSONArray jsonarray;
    private XYSeries series;
    private XYMultipleSeriesDataset dataset;
    private GraphicalView chartView;

    @ViewInject(R.id.calorie_chart_ll)
    private LinearLayout chartLL;

    private int ChartType;
    private DateDialog dialog;

    @ViewInject(R.id.calorie_radiogroup)
    RadioGroup chartRg;
    @ViewInject(R.id.calorieweek_btn)
    RadioButton weekRb;
    @ViewInject(R.id.caloriemonth_btn)
    RadioButton monthRb;
    @ViewInject(R.id.calorieself_btn)
    RadioButton selfRb;
    @ViewInject(R.id.calorie_date_tv)
    TextView calorieDateTv;
    @ViewInject(R.id.calorie_startdate_tv)
    TextView startDateTv;
    @ViewInject(R.id.calorie_enddate_tv)
    TextView endDateTv;
    @ViewInject(R.id.calorie_start_tv)
    TextView dataStartTv;
    @ViewInject(R.id.calorie_end_tv)
    TextView dataEndTv;

    @ViewInject(R.id.calories_save_btn)
    TextView calorieSaveTv;

    @ViewInject(R.id.calorie_left_img)
    ImageView calorieLeftImg;
    @ViewInject(R.id.calorie_right_img)
    ImageView calorieRightImg;

    @ViewInject(R.id.calorie_recode_lv)
    ListView listview;
    private ChartDataListAdapter adapter;

    @OnClick({R.id.chat_back_igb})
    public void Click(View v) {
        switch (v.getId()) {
            case R.id.chat_back_igb:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chart);
        context = this;
        ViewUtils.inject(this);
        sp = getSharedPreferences(AppConstant.USER_sp_name,
                MODE_PRIVATE);
        initviews();
    }

    private void initviews() {

        Intent intent = getIntent();
        ChartType = intent.getIntExtra("type", 0);


        customerId = sp.getString(AppConstant.USER_customerId, "");
//        System.out.println("customerId:"+customerId);
        uerId=sp.getInt(AppConstant.ADMIN_userId,0);
        jwotchModel=sp.getString(AppConstant.USER_jwotchModel,"");

        setDate(1);
        getResult();
        chartRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.calorieweek_btn:
                        weekRb.setTextColor(getResources().getColor(
                                R.color.login_loginBtn_bgColor));
                        monthRb.setTextColor(getResources().getColor(
                                R.color.white));
                        selfRb.setTextColor(getResources().getColor(
                                R.color.white));
                        dataStartTv.setText(getResources().getString(
                                R.string.pre_week));
                        dataEndTv.setText(getResources().getString(
                                R.string.next_week));
                        type = 1;
                        n = 1;
                        setDate(type);
                        calorieSaveTv.setVisibility(View.GONE);
                        updateChart();
                        getResult();
                        break;

                    case R.id.caloriemonth_btn:
                        monthRb.setTextColor(getResources().getColor(
                                R.color.login_loginBtn_bgColor));
                        weekRb.setTextColor(getResources().getColor(
                                R.color.white));
                        selfRb.setTextColor(getResources().getColor(
                                R.color.white));
                        dataStartTv.setText(getResources().getString(
                                R.string.pre_month));
                        dataEndTv.setText(getResources().getString(
                                R.string.next_month));
                        type = 2;
                        n = 1;
                        setDate(type);
                        calorieSaveTv.setVisibility(View.GONE);

                        updateChart();
                        getResult();

                        break;
                    case R.id.calorieself_btn:
                        selfRb.setTextColor(getResources().getColor(
                                R.color.login_loginBtn_bgColor));
                        weekRb.setTextColor(getResources().getColor(
                                R.color.white));
                        monthRb.setTextColor(getResources().getColor(
                                R.color.white));
                        dataStartTv.setText(getResources()
                                .getString(R.string.start));
                        dataEndTv.setText(getResources().getString(R.string.end));
                        type = 3;
                        n = 1;
                        setDate(type);
                        calorieSaveTv.setVisibility(View.VISIBLE);
                        updateChart();

                        adapter.setJsonarray(null, ChartType);
                        adapter.notifyDataSetChanged();
                        break;

                    default:
                        break;
                }
            }
        });

        calorieLeftImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                n++;

                if (type == 1) {
                    startDateTv.setText(GetDate.getDateBefore(7 * n));
                    endDateTv.setText(GetDate.getDateBefore(7 * (n - 1)));
                    updateChart();
                    getResult();

                } else if (type == 2) {
                    startDateTv.setText(GetDate.getDateBefore(30 * n));
                    endDateTv.setText(GetDate
                            .getDateBefore(30 * (n - 1)));
                    updateChart();
                    getResult();

                } else if (type == 3) {
                    Handler handler=new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.what==AppConstant.MSG_DATEDIALOG){
                                startDateTv.setText(dialog.getDate());
                            }
                        }
                    };
                    dialog = new DateDialog(context,handler);
                    dialog.setDate();
                }

            }
        });

        calorieRightImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                n--;
                if (type == 1) {
                    startDateTv.setText(GetDate.getDateBefore(7 * n));
                    endDateTv.setText(GetDate.getDateBefore(7 * (n - 1)));
                    updateChart();
                    getResult();

                } else if (type == 2) {
                    startDateTv.setText(GetDate.getDateBefore(30 * n));
                    endDateTv.setText(GetDate
                            .getDateBefore(30 * (n - 1)));
                    updateChart();
                    getResult();

                } else if (type == 3) {
                    Handler handler=new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            // TODO Auto-generated method stub
                            if(msg.what==AppConstant.MSG_DATEDIALOG){
                                endDateTv.setText(dialog.getDate());
                            }
                        }
                    };

                    dialog = new DateDialog(context,handler);
                    dialog.setDate();
                }
            }
        });

        calorieSaveTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getResult();
            }
        });

        dataStartTv.setText(getResources().getString(R.string.pre_week));
        dataEndTv.setText(getResources().getString(R.string.next_week));

        adapter = new ChartDataListAdapter(context);
        listview.setAdapter(adapter);

    }

    private void setDate(int type) {
        switch (type) {
            case 1:
                startDateTv.setText(GetDate.lastWeek());
                endDateTv.setText(GetDate.lastDay());
                break;
            case 2:
                startDateTv.setText(GetDate.lastMonth());
                endDateTv.setText(GetDate.lastDay());
                break;
            case 3:
                startDateTv.setText(getResources().getString(
                        R.string.left_right_arr));
                endDateTv.setText(getResources().getString(
                        R.string.select_date));
                break;

            default:
                break;
        }
    }

    private void getResult() {

        handler_getData = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.what == 0x133) {
                    String result = getDataThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {

                        if (result.length() < 1) {
                            UiUtil.showToast(context,
                                    getResources()
                                            .getString(R.string.show_date));

                        } else {
                            drawChart(result);
                        }

                    }
                }

            }

            ;
        };

        switch (ChartType) {
            case AppConstant.CHART_CAL:
                getDataThread = new JAssistantAPIThread(
                        ApiConstant.GETCALORIE_WEEKORMONTH, handler_getData,
                        context);
                break;
            case AppConstant.CHART_HR:
                if(jwotchModel.equals(AppConstant.JWOTCHMODEL_032)){
                    getDataThread = new JAssistantAPIThread(
                            ApiConstant.GETHEARTRATE_WEEKORMONTH, handler_getData,
                            context);
                }else if(jwotchModel.equals(AppConstant.JWOTCHMODEL_041)){
                    getDataThread = new JAssistantAPIThread(
                            ApiConstant.GETHEARTRATE_WEEKORMONTH, handler_getData,
                            context);
                }else{
                    getDataThread = new JAssistantAPIThread(
                            ApiConstant.GETCUSTOMERDATA_WEEKORMONTH, handler_getData,
                            context);
                }

                break;
            case AppConstant.CHART_BP:
                if(jwotchModel.equals(AppConstant.JWOTCHMODEL_041)){
                    getDataThread = new JAssistantAPIThread(
                            ApiConstant.GETBLOODPRESSURE_WEEKORMONTH, handler_getData,
                            context);
                }else{
                    getDataThread = new JAssistantAPIThread(
                            ApiConstant.GETCUSTOMERDATA_WEEKORMONTH, handler_getData,
                            context);
                }

                break;

            default:
                break;
        }

        getDataThread.setCustomerId(customerId);
        getDataThread.setuId(uerId);
        getDataThread.setStartTime(startDateTv.getText().toString()
                .trim());
        getDataThread.setEndTime(endDateTv.getText().toString().trim());
        getDataThread.start();

    }

    // 开始绘图
    private void drawChart(String result) {

        jsonarray = null;
        jsonsize = 0;
        date = null;

        // 数据集与样式
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        dataset = new XYMultipleSeriesDataset();

        if (ChartType == AppConstant.CHART_CAL) {


            try {
                jsonarray = JSONArray.fromObject(result);
                adapter.setJsonarray(jsonarray, ChartType);
                adapter.notifyDataSetChanged();
                jsonsize = jsonarray.size();
            } catch (Exception e) {
                jsonsize = 0;
                UiUtil.showToast(context,
                        getResources().getString(R.string.show_date));
            }
            date = new String[jsonsize];
            // 获取数据

            calorieData = new float[jsonsize];
            for (int i = 0, j = jsonsize; i < j; i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                date[i] = jsonobject.getString("TestDate").substring(5,
                        10);
                calorieData[i] = Float.parseFloat(jsonobject
                        .getString("Calorie"));

                float[] calorieCopyData = calorieData.clone();
                // 得到Y轴最大值

                Arrays.sort(calorieCopyData);
                calorieYMax = calorieCopyData[calorieCopyData.length - 1];

            }
            // 将数据添加到XY轴
            series = new XYSeries(getResources().getString(R.string.calorie));
            // Y轴数据
            for (int k = 1; k < jsonsize + 1; k++) {
                float y = calorieData[k - 1];
                series.add(k, y);
            }
            dataset.addSeries(series);
            // X轴数据

            renderer = buildRenderer(0, 0, calorieYMax + 100, getResources().getString(R.string.cal_unit), getResources().getString(R.string.date), getResources().getString(R.string.calorie)
                    + getResources().getString(R.string.chart));

            renderer.addXTextLabel(0, " ");

            for (int i = 0; i < jsonsize; i++) {
                renderer.addXTextLabel(i + 1, date[i]);
            }

            // 柱状图
            chartView = ChartFactory.getBarChartView(context, dataset, renderer,
                    Type.DEFAULT);

        } else if (ChartType == AppConstant.CHART_HR) {

            if(!jwotchModel.equals(AppConstant.JWOTCHMODEL_031)){
                JSONObject json = JSONObject.fromObject(result);
                int code = json.getInt("code");
                if (code != 200) {
                    UiUtil.showToast(context, json.getString("message"));
                    return;
                }

                result = json.getString("Data");
            }


            try {
                jsonarray = JSONArray.fromObject(result);
                adapter.setJsonarray(jsonarray, ChartType);
                adapter.notifyDataSetChanged();
                jsonsize = jsonarray.size();
            } catch (Exception e) {
                jsonsize = 0;
                UiUtil.showToast(context,
                        getResources().getString(R.string.show_date));
            }
            date = new String[jsonsize];

            // 获取数据

            prData = new float[jsonsize];

            for (int i = 0, j = jsonsize; i < j; i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if(jsonobject.has("HR")) {
                    date[i] = jsonobject.getString("TD").substring(5,
                            10);
                    prData[i] = Float.parseFloat(jsonobject.getString("HR"));
                }else if(jsonobject.has("Pr")){
                    date[i] = jsonobject.getString("TestDate").substring(5,
                            10);
                    prData[i] = Float.parseFloat(jsonobject.getString("PR"));
                }


                float[] prCopyData = prData.clone();

                // 得到Y轴最大值

                Arrays.sort(prCopyData);
                prYMax = prCopyData[prCopyData.length - 1];
                prYMin = prCopyData[0];
            }
            // 将数据添加到XY轴
            series = new XYSeries(getResources().getString(R.string.pr));
            // Y轴数据
            for (int k = 1; k < jsonsize + 1; k++) {
                float y = prData[k - 1];
                series.add(k, y);
            }


            dataset.addSeries(series);
            // X轴数据


            // 对点的绘制进行描述
            renderer = buildRenderer(0, prYMin > 10 ? prYMin - 10 : 0, prYMax + 10, getResources().getString(R.string.pr_unit), getResources().getString(R.string.date), getResources().getString(R.string.pr)
                    + getResources().getString(R.string.chart));

            renderer.addXTextLabel(0, " ");

            for (int i = 0; i < jsonsize; i++) {
                renderer.addXTextLabel(i + 1, date[i]);
            }

            chartView = ChartFactory.getLineChartView(context, dataset, renderer);

        } else if (ChartType == AppConstant.CHART_BP) {

//            if(jwotchModel.equals(AppConstant.JWOTCHMODEL_041)) {
                JSONObject json = JSONObject.fromObject(result);
                int code = json.getInt("code");
                if (code != 200) {
                    UiUtil.showToast(context, json.getString("message"));
                    return;
                }

                result = json.getString("Data");
//            }

            try {
                jsonarray = JSONArray.fromObject(result);
                adapter.setJsonarray(jsonarray, ChartType);
                adapter.notifyDataSetChanged();
                jsonsize = jsonarray.size();
            } catch (Exception e) {
                jsonsize = 0;
                UiUtil.showToast(context,
                        getResources().getString(R.string.show_date));
            }
            date = new String[jsonsize];

            // 获取数据

            psData = new float[jsonsize];
            pdData = new float[jsonsize];
            for (int i = 0, j = jsonsize; i < j; i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if(jwotchModel.equals(AppConstant.JWOTCHMODEL_041)){
                    date[i] = jsonobject.getString("TD").substring(5,
                            10);
                }else {
                    date[i] = jsonobject.getString("TestDate").substring(5,
                            10);
                }

                psData[i] = Float.parseFloat(jsonobject.getString("PS"));
                pdData[i] = Float.parseFloat(jsonobject.getString("PD"));

                float[] psCopyData = psData.clone();
                float[] pdCopyData = pdData.clone();
                // 得到Y轴最大值

                Arrays.sort(psCopyData);
                Arrays.sort(pdCopyData);
                psYMax = psCopyData[psCopyData.length - 1];
                pdYMin = pdCopyData[0];
            }
            // 将数据添加到XY轴
            series = new XYSeries(getResources().getString(R.string.ps));
            for (int k = 1; k < jsonsize + 1; k++) {
                float y = psData[k - 1];
                series.add(k, y);
            }
            dataset.addSeries(series);
//            series.clear();
            series = new XYSeries(getResources().getString(R.string.pd));
            // Y轴数据

            for (int k = 1; k < jsonsize + 1; k++) {
                float y = pdData[k - 1];
                series.add(k, y);
            }

            dataset.addSeries(series);

            renderer = buildRenderer(0, pdYMin > 10 ? pdYMin - 10 : 0, psYMax + 10, "(mmHg)", getResources().getString(R.string.date), getResources().getString(R.string.bp)
                    + getResources().getString(R.string.chart));
            // X轴数据
            renderer.addXTextLabel(0, " ");

            for (int i = 0; i < jsonsize; i++) {
                renderer.addXTextLabel(i + 1, date[i]);
            }


            chartView = ChartFactory.getLineChartView(context, dataset, renderer);

        }

        chartLL.addView(chartView, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
    }

    protected XYMultipleSeriesRenderer buildRenderer(int XLabel, double Ymin,
                                                     double Ymax, String Ytitle, String Xtitle, String chatTitle) {// 设置图表中曲线本身的样式，包括颜色�?点的大小以及线的粗细�?
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();// 对点的描绘进行设�?
//
        if (ChartType == AppConstant.CHART_HR) {
            r = new XYSeriesRenderer();// 对点的描绘进行设�?
            r.setColor(Color.MAGENTA);
            r.setPointStyle(PointStyle.CIRCLE);
            r.setFillPoints(true);
            r.setLineWidth(3);
//			r.setDisplayChartValuesDistance(10);
            renderer.addSeriesRenderer(r);
        }
        if (ChartType == AppConstant.CHART_BP) {
            r = new XYSeriesRenderer();// 对点的描绘进行设�?
            r.setColor(Color.RED);
            r.setPointStyle(PointStyle.TRIANGLE);
            r.setFillPoints(true);
            r.setLineWidth(3);
//			r.setDisplayChartValuesDistance(400);
            renderer.addSeriesRenderer(r);

            r = new XYSeriesRenderer();// 对点的描绘进行设�?
            r.setColor(Color.BLUE);
            r.setPointStyle(PointStyle.SQUARE);
            r.setFillPoints(true);
            r.setLineWidth(3);
//			r.setDisplayChartValuesDistance(400);
            renderer.addSeriesRenderer(r);
        }
        if (ChartType == AppConstant.CHART_CAL) {
            r.setColor(Color.GREEN);
            r.setPointStyle(PointStyle.POINT);
            r.setFillPoints(true);
            r.setLineWidth(2);
            renderer.addSeriesRenderer(r);
            renderer.setBarSpacing(0.2f);
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
//        renderer.setYLabels(YLabel);// Y轴格�?
        renderer.setMargins(new int[]{20, 40, 30, 20});// 上，左，下，右?

        renderer.setShowLegend(true);// 不显示图�?
        renderer.setZoomEnabled(false);
        renderer.setPanEnabled(true, false);// 允许左右拖动,但不允许上下拖动.
        renderer.setClickEnabled(false);
        renderer.setShowAxes(true);
        renderer.setLabelsTextSize(18);
        renderer.setLegendTextSize(15); // 设置图例文本大小
        renderer.setChartTitle(chatTitle);
        renderer.setXTitle(Xtitle);
        renderer.setYTitle(Ytitle);
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

    // 更新视图
    private void updateChart() {
        if (jsonsize == 0) {

        } else {
            jsonarray.clear();
            adapter.setJsonarray(null, ChartType);
            adapter.notifyDataSetChanged();
            chartLL.removeAllViews();
        }
        if (series == null) {

        } else {
            dataset.removeSeries(series);// 移除数据集中旧的点集
            series.clear();
            chartLL.removeAllViews();
        }

    }

}
