package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MachineDataAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;
	private ViewHolder holder=null;

	private boolean[] isVisables;

	public interface Delegete{
		void contrast(int pos);
		void record(int pos);
		void changeState(int pos);
	}

	private Delegete delegete;
	public MachineDataAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		if(array!=null) {
			return array.size();
		}else {
			return 0;
		}
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public void setDelegete(Delegete delegete) {
		this.delegete = delegete;
	}

	public void setIsVisables(boolean[] isVisables) {
		this.isVisables = isVisables;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_machine_data, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject=array.getJSONObject(position);

		holder.timeTv.setText(jsonObject.getString("MeasureTime"));


//		if(position==0){
		holder.dataCb.setChecked(isVisables[position]);
//		}else {
//			holder.dataCb.setChecked(false);
//		}

		holder.contrastBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delegete.contrast(position);
			}
		});

		holder.recordBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delegete.record(position);
			}
		});

		holder.dataCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					delegete.changeState(position);
				}else {
					delegete.changeState(position);
				}
			}
		});
		if(isVisables[position]){
			holder.dataLL.setVisibility(View.VISIBLE);
		}else {
			holder.dataLL.setVisibility(View.GONE);
		}
		JSONObject minfat=jsonObject.getJSONObject("MinFat");
		if(minfat!=null&&!minfat.equals("null")){
			if((isEmpty(minfat.getString("Height"))&&isEmpty(minfat.getString("Weight")))&&isEmpty(minfat.getString("Bmi"))){
				holder.heightLL.setVisibility(View.GONE);
			}else {
				holder.heightLL.setVisibility(View.VISIBLE);
				holder.heightTv.setText(isNull(minfat.getString("Height")));
				holder.weightTv.setText(isNull(minfat.getString("Weight")));
				holder.bmiTv.setText(isNull(minfat.getString("Bmi")));
			}

			if((isEmpty(minfat.getString("Physique"))&&isEmpty(minfat.getString("Shape")))&&isEmpty(minfat.getString("FatRate"))){
				holder.physiqueLL.setVisibility(View.GONE);
			}else {
				holder.physiqueLL.setVisibility(View.VISIBLE);
				holder.physiqueTv.setText(isNull(minfat.getString("Physique")));
				holder.shapeTv.setText(isNull(minfat.getString("Shape")));
				holder.fatTv.setText(isNull(minfat.getString("FatRate")));
			}
//			if(isEmpty(minfat.getString("BasicMetabolism"))){
//				holder.bmLL.setVisibility(View.GONE);
//			}else {
				holder.bmLL.setVisibility(View.VISIBLE);
				holder.bmTv.setText(isNull(minfat.getString("BasicMetabolism")));
//			}


		}else {
			holder.heightLL.setVisibility(View.GONE);
			holder.physiqueLL.setVisibility(View.GONE);
//			holder.bmLL.setVisibility(View.GONE);
		}

		JSONObject temperature=jsonObject.getJSONObject("Temperature");
		if(temperature!=null&&!temperature.equals("null")){
//			if(isEmpty(temperature.getString("Temperature"))){
//				holder.temperatureLL.setVisibility(View.GONE);
//			}else {
				holder.temperatureLL.setVisibility(View.VISIBLE);
				holder.temperatureTv.setText(isNull(temperature.getString("Temperature")));
//			}
		}else {
			holder.temperatureLL.setVisibility(View.GONE);
		}

		JSONObject peEcg=jsonObject.getJSONObject("PEEcg");
		if(peEcg!=null&&!peEcg.equals("null")){
			String hr=peEcg.getString("Hr");
//			if(isEmpty(hr)){
//				holder.hrLL.setVisibility(View.GONE);
//			}else {
				holder.hrLL.setVisibility(View.VISIBLE);
				holder.hrTv.setText(isNull(hr));
//			}

		}else {
//			holder.hrLL.setVisibility(View.GONE);
		}

		JSONObject bp=jsonObject.getJSONObject("BloodPressure");
		if(bp!=null&&!bp.equals("null")){
//			holder.bpLL.setVisibility(View.VISIBLE);
			String ps=bp.getString("HighPressure");
			String pd=bp.getString("LowPressure");
			String pr=bp.getString("Pulse");

			if((isEmpty(ps)&&isEmpty(pd))&&isEmpty(pr)){
				holder.bpLL.setVisibility(View.GONE);
			}else {
				holder.bmLL.setVisibility(View.VISIBLE);
				holder.psTv.setText(isNull(ps));
				holder.pdTv.setText(isNull(pd));
				holder.prTv.setText(isNull(pr));
			}

		}else {
			holder.bpLL.setVisibility(View.GONE);
		}

		JSONObject whr=jsonObject.getJSONObject("Whr");
		if(whr!=null&&!whr.equals("null")){
			String waist=whr.getString("Waistline");
			String hipline=whr.getString("Hipline");
			if((isEmpty(waist)&&isEmpty(hipline)&&isEmpty(whr.getString("Whr")))){
				holder.waistLL.setVisibility(View.GONE);
			}else {
				holder.waistLL.setVisibility(View.VISIBLE);
				holder.waistTv.setText(isNull(waist));
				holder.hiplineTv.setText(isNull(hipline));
				holder.whrTv.setText(isNull(whr.getString("Whr")));
			}
		}else {
			holder.waistLL.setVisibility(View.GONE);
		}

		JSONObject bloodsugar=jsonObject.getJSONObject("BloodSugar");
		JSONObject uaObject=jsonObject.getJSONObject("Ua");
		JSONObject cholObject=jsonObject.getJSONObject("Chol");
		String bs,ua,chol;
		if((isEmpty(bloodsugar.toString())&&isEmpty(uaObject.toString()))&&isEmpty(cholObject.toString())){
			holder.bloodsugarLL.setVisibility(View.GONE);
			holder.uaLL.setVisibility(View.GONE);
			holder.cholLL.setVisibility(View.GONE);
		}else {
			bs=bloodsugar.getString("BloodSugar");
			ua=uaObject.getString("Ua");
			chol=cholObject.getString("Chol");
			if((isEmpty(bs)&&isEmpty(ua))&&isEmpty(chol)){
				holder.bloodsugarLL.setVisibility(View.GONE);
				holder.uaLL.setVisibility(View.GONE);
				holder.cholLL.setVisibility(View.GONE);
			}else{
				holder.bloodsugarLL.setVisibility(View.VISIBLE);
				holder.bloodsugarTv.setText(isNull(bloodsugar.getString("BloodsugarType"))+isNull(bs));
				holder.uaLL.setVisibility(View.VISIBLE);
				holder.uaTv.setText(isNull(ua));
				holder.cholLL.setVisibility(View.VISIBLE);
				holder.cholTv.setText(isNull(chol));
			}

		}

		return convertView;
	}

	private String isNull(String str){
		if(str==null||str.equals("null")){
			return "--";
		}else {
			return str;
		}
	}

	private boolean isEmpty(String str){
		if(str==null||str.equals("null")){
			return true;
		}else {
			return false;
		}
	}
	
	class ViewHolder{
		@ViewInject(R.id.machine_data_cb)
		private CheckBox dataCb;
		@ViewInject(R.id.machine_data_time_tv)
		private TextView timeTv;
		@ViewInject(R.id.machine_data_contrast_btn)
		private Button contrastBtn;
		@ViewInject(R.id.machine_data_record_btn)
		private Button recordBtn;

		@ViewInject(R.id.machine_data_ll)
		private LinearLayout dataLL;

		@ViewInject(R.id.machine_data_height_ll)
		private LinearLayout heightLL;
		@ViewInject(R.id.machine_data_height_tv)
		private TextView heightTv;
		@ViewInject(R.id.machine_data_weight_tv)
		private TextView weightTv;
		@ViewInject(R.id.machine_data_bmi_tv)
		private TextView bmiTv;

		@ViewInject(R.id.machine_data_physique_ll)
		private LinearLayout physiqueLL;
		@ViewInject(R.id.machine_data_physique_tv)
		private TextView physiqueTv;
		@ViewInject(R.id.machine_data_shape_tv)
		private TextView shapeTv;
		@ViewInject(R.id.machine_data_fat_tv)
		private TextView fatTv;

		@ViewInject(R.id.machine_data_bm_ll)
		private LinearLayout bmLL;
		@ViewInject(R.id.machine_data_bm_tv)
		private TextView bmTv;

		@ViewInject(R.id.machine_data_temperature_ll)
		private LinearLayout temperatureLL;
		@ViewInject(R.id.machine_data_temperature_tv)
		private TextView temperatureTv;

		@ViewInject(R.id.machine_data_hr_ll)
		private LinearLayout hrLL;
		@ViewInject(R.id.machine_data_hr_tv)
		private TextView hrTv;

		@ViewInject(R.id.machine_data_bp_ll)
		private LinearLayout bpLL;
		@ViewInject(R.id.machine_data_ps_tv)
		private TextView psTv;
		@ViewInject(R.id.machine_data_pd_tv)
		private TextView pdTv;
		@ViewInject(R.id.machine_data_pr_tv)
		private TextView prTv;

		@ViewInject(R.id.machine_data_waist_ll)
		private LinearLayout waistLL;
		@ViewInject(R.id.machine_data_waist_tv)
		private TextView waistTv;
		@ViewInject(R.id.machine_data_hipline_tv)
		private TextView hiplineTv;
		@ViewInject(R.id.machine_data_whr_tv)
		private TextView whrTv;

		@ViewInject(R.id.machine_data_bloodsugar_ll)
		private LinearLayout bloodsugarLL;
		@ViewInject(R.id.machine_data_bloodsugar_tv)
		private TextView bloodsugarTv;

		@ViewInject(R.id.machine_data_ua_ll)
		private LinearLayout uaLL;
		@ViewInject(R.id.machine_data_ua_tv)
		private TextView uaTv;

		@ViewInject(R.id.machine_data_chol_ll)
		private LinearLayout cholLL;
		@ViewInject(R.id.machine_data_chol_tv)
		private TextView cholTv;

	}
	
}
