package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.HdDataAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.HdDataTypeAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.HorizontalListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Arrays;

public class HdDataActivity extends Activity implements GestureDetector.OnGestureListener{
    @ViewInject(value = R.id.title_tv,parentId = R.id.ha_data_title)
    private TextView titleTv;

    @ViewInject(R.id.hd_data_hlv)
    private HorizontalListView dataHlv;
    @ViewInject(R.id.hd_data_chart_ll)
    private LinearLayout chartLL;
    @ViewInject(R.id.hd_data_lv)
    private ListView dataLv;
    private WebView webView;


    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            default:
                break;
        }
    }


    private Context context;
    private HdDataTypeAdapter adapter;

    private HdDataAdapter dataAdapter;
    private JSONArray dataArray;

    private SharedPreferences sp;
    private int doctorId;
    private String customerId;
    private DoctorSHThread getHdDataThread;
    private String[] methodName={ ApiConstant.GETVHDBLOODPRESSUREBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDHEARTRATEBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDBLOODOXYGENBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDBLOODGLUCOSEBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDSTEPBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDCALORIEBYCUSTOMERID,
                                    ApiConstant.GETVHDTEMPERATUREBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDWEIGHTLISTBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDBFRBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDMOISTUREBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDMUSCLEBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDBONEBYCUSTOMERID_TOP10,
                                    ApiConstant.GETVHDBMBYCUSTOMERID_TOP10};
    public static final String[] TYPE={"BP","HR","SPO","Glucose","StepNumber","CalorieValue","Temper","Weight","BFR","Moisture","Muscle","Bone","BM"};
    public static final String[] UNIT={"mmHg","次/分","mmol/L","mmol/L","步数","kcal","℃","kg","%","%","%","%","%"};

    private float[] psData,pdData,chartData;
    private float psYMax,pdYMin,chartYMax,chartYMin;
    private XYSeries series;
    private XYMultipleSeriesDataset dataset;
    private GraphicalView chartView;

    private String[] dataArr;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hd_data);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.hd_data_title));
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int type=bundle.getInt("type");

        dataArr=getResources().getStringArray(R.array.data);
        adapter=new HdDataTypeAdapter(context);
        adapter.setArray(dataArr);
        dataHlv.setAdapter(adapter);
        dataHlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adapter.setSelectPos(position);
                adapter.notifyDataSetChanged();
                getHdData(methodName[position],position);

            }
        });



        dataAdapter=new HdDataAdapter(context);
        dataLv.setAdapter(dataAdapter);

        System.out.println("HddataActivity "+"type:"+type);
        adapter.setSelectPos(type);
        adapter.notifyDataSetChanged();

        getHdData(methodName[type],type);

        webView = findViewById(R.id.webview);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://122.225.60.118:6280/partnersapi/h5/hddata6.html?key=58f1d615-ed3c-457a-98fe-320dcdf08b74&customerId=100001396");

    }

    void getHdData(String method, final int type){
        dataArray=null;
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getHdDataThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject object=JSONObject.fromObject(result);
                        int code=object.getInt("code");
                        if(code==200){
                            updateChart();
                            dataArray=object.getJSONArray("Data");
                            dataAdapter.setType(type);
                            drawChart(type);
                        }else {
                            UiUtil.showToast(context,object.getString("message"));
                        }
                    }
                    dataAdapter.setArray(dataArray);
                    dataAdapter.notifyDataSetChanged();
                }
            }
        };
        getHdDataThread=new DoctorSHThread(method,handler,context);
        getHdDataThread.setDoctorId(doctorId);
        getHdDataThread.setCustomerId(customerId);
        getHdDataThread.setDatetime(GetDate.currentDate());
        getHdDataThread.start();
    }

    // 开始绘图
    private void drawChart(int type) {

//        JSONArray jsonarray = null;
        int jsonsize = 0;
        String[] date = null;

        // 数据集与样式
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        dataset = new XYMultipleSeriesDataset();


        if(dataArray!=null) {
            jsonsize = dataArray.size();
        }else {
            jsonsize = 0;
        }


        if(jsonsize!=0) {
            date = new String[jsonsize];
        }else {
            return;
        }

        if (type!= 0) {
            if(jsonsize!=0) {
                chartData = new float[jsonsize];

                for (int i = 0, j = jsonsize; i < j; i++) {
                    JSONObject jsonobject = dataArray.getJSONObject(i);
                    if (jsonobject.has(TYPE[type])) {
                        date[i] = jsonobject.getString("TestDate").substring(5, 10);
                        chartData[i] = Float.parseFloat(jsonobject.getString(TYPE[type]));
                    }
                }


                float[] copyData = chartData.clone();
//                // 得到Y轴最大值
                Arrays.sort(copyData);
                chartYMax = copyData[copyData.length - 1];
                chartYMin = copyData[0];

                System.out.println("最大值：" + chartYMax + "最小值：" + chartYMin);
                // 将数据添加到XY轴
                series = new XYSeries(dataArr[type]);
                // Y轴数据
                for (int k = 1; k < jsonsize + 1; k++) {
                    float y = chartData[k - 1];
                    series.add(k, y);
                }


                dataset.addSeries(series);
                // X轴数据


                // 对点的绘制进行描述
                renderer = buildRenderer(type, 0, chartYMin > 10 ? chartYMin - 10 : 0, chartYMax + 10, UNIT[type], getResources().getString(R.string.date), dataArr[type]
                        + getResources().getString(R.string.chart));

                renderer.addXTextLabel(0, " ");

                for (int i = 0; i < jsonsize; i++) {
                    renderer.addXTextLabel(i + 1, date[i]);
                }

                chartView = ChartFactory.getLineChartView(context, dataset, renderer);
            }else {
                //无数据
            }

        } else {//血压
            if(jsonsize!=0) {

                psData = new float[jsonsize];
                pdData = new float[jsonsize];
                for (int i = 0, j = jsonsize; i < j; i++) {
                    JSONObject jsonobject = dataArray.getJSONObject(i);
                    date[i] = jsonobject.getString("TestDate").substring(5,
                            10);

                    if (jsonobject.has("PS") && jsonobject.has("PD")) {
                        psData[i] = Float.parseFloat(jsonobject.getString("PS"));
                        pdData[i] = Float.parseFloat(jsonobject.getString("PD"));

                        float[] psCopyData = psData.clone();
                        float[] pdCopyData = pdData.clone();
                        // 得到Y轴最大值

                        Arrays.sort(psCopyData);
                        Arrays.sort(pdCopyData);
                        psYMax = psCopyData[psCopyData.length - 1];
                        pdYMin = pdCopyData[0];

                    } else {
                        return;
                    }


                }
                System.out.println("数组长度：" + jsonsize + " 最大值：" + psYMax + "最小值：" + pdYMin);
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

                renderer = buildRenderer(type, 0, pdYMin > 10 ? pdYMin - 10 : 0, psYMax + 10, "(mmHg)", getResources().getString(R.string.date), getResources().getString(R.string.bp)
                        + getResources().getString(R.string.chart));
                // X轴数据
                renderer.addXTextLabel(0, " ");

                for (int i = 0; i < jsonsize; i++) {
                    renderer.addXTextLabel(i + 1, date[i]);
                }


                chartView = ChartFactory.getLineChartView(context, dataset, renderer);
            }else {
                //无数据
            }

        }
        if(jsonsize!=0) {
            chartLL.addView(chartView, LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
        }
    }

    protected XYMultipleSeriesRenderer buildRenderer(int type,int XLabel, double Ymin,
                                                     double Ymax, String Ytitle, String Xtitle, String chatTitle) {// 设置图表中曲线本身的样式，包括颜色�?点的大小以及线的粗细�?
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();// 对点的描绘进行设�?
//
        if (type != 0) {
            r = new XYSeriesRenderer();// 对点的描绘进行设�?
            r.setColor(Color.MAGENTA);
            r.setPointStyle(PointStyle.CIRCLE);
            r.setFillPoints(true);
            r.setLineWidth(3);
//			r.setDisplayChartValuesDistance(10);
            renderer.addSeriesRenderer(r);
        }
        if (type == 0) {
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
        renderer.setMargins(new int[]{60, 100, 60, 60});// 上，左，下，右?

        renderer.setShowLegend(true);// 不显示图�?
        renderer.setZoomEnabled(false);
        renderer.setPanEnabled(true, false);// 允许左右拖动,但不允许上下拖动.
        renderer.setClickEnabled(false);
        renderer.setShowAxes(true);
        renderer.setLabelsTextSize(getResources().getDimension(R.dimen.text_12));
        renderer.setLegendTextSize(getResources().getDimension(R.dimen.text_16)); // 设置图例文本大小
        renderer.setChartTitle(chatTitle);
        renderer.setChartTitleTextSize(getResources().getDimension(R.dimen.text_16));
        renderer.setXTitle(Xtitle);
        renderer.setYTitle(Ytitle);
        renderer.setAxisTitleTextSize(getResources().getDimension(R.dimen.text_12));
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(7);
        renderer.setYAxisMin(Ymin);
        renderer.setYAxisMax(Ymax);
        renderer.setAxesColor(Color.BLACK);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setFitLegend(true);
        renderer.setPointSize(10);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);

        return renderer;
    }

    // 更新视图
    private void updateChart() {
        if (dataArray==null||dataArray.size() == 0) {

        } else {
            dataArray.clear();
            chartLL.removeAllViews();
        }
        if (series == null) {

        } else {
            dataset.removeSeries(series);// 移除数据集中旧的点集
            series.clear();
            chartLL.removeAllViews();
        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

}
