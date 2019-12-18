package com.jxj.jdoctorassistant.main;


import net.sf.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.PhoneListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.main.community.activity.MainPinguActivity;
import com.jxj.jdoctorassistant.main.doctor.DoctorMainActivity;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnItemClickListener;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnPhoneClickListener;
import com.jxj.jdoctorassistant.main.easymob.MyConnectionListener;
import com.jxj.jdoctorassistant.main.register.RegisterForDoctorActivity;
import com.jxj.jdoctorassistant.main.register.ResultActivity;
import com.jxj.jdoctorassistant.thread.DoctorLoginThread;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.GetPartnerAPIThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.thread.UpdateVersionThread;
import com.jxj.jdoctorassistant.util.Acache;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.util.UpdateManger;
import com.jxj.jdoctorassistant.util.ValidateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.baidu.location.b.g.p;
import static com.baidu.location.b.g.x;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnPhoneClickListener {

	private static final int LOGIN_SUCCESS=1;
	private static final int LOGIN_FAILED=2;
	private static final int ADD_USER=5;
	private static final int USER_EXIST=205;

	private Context context;
//	private Handler loginHandler;
	private DoctorLoginThread loginThread;
	private DoctorSHThread loginForDoctorThread;
	private PopularThread addEasemobAccountThread,getEasemobAccountThread;
	private GetPartnerAPIThread getCodeThread;
	private UpdateVersionThread updateVersionThread;
	private UpdateManger updateManger;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private boolean isTest=false;

	private String tempPhone;
	private String tempPwd;
	
	@ViewInject(R.id.login_username_etv)
	EditText usernameEtv;
	@ViewInject(R.id.login_password_etv)
	EditText passwordEtv;
	@ViewInject(R.id.login_password_visiable_cb)
	CheckBox passwordVisiableCb;
	@ViewInject(R.id.login_find_password_btn)
	Button findPasswordBtn;
	@ViewInject(R.id.login_igv)
	ImageView loginIgv;
	@ViewInject(R.id.text_register)
	TextView text_register;
	@ViewInject(R.id.tv_code_login)
	TextView tv_code_login;
	@ViewInject(R.id.tv_reset_password)
	TextView tv_reset_password;


	ImageView img_arrow;
	Set phoneSet = new HashSet();
	RecyclerView phone_list;
    PhoneListAdapter phoneListAdapter;
    RelativeLayout login_usernameEtv_rl,rl_arrow;
	PopupWindow pop;


	private Handler loginHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case LOGIN_SUCCESS:
					UiUtil.showToast(context,"登录成功");

					break;
				case LOGIN_FAILED:
					int code=msg.arg1;
					switch (code){
						case 202:
							UiUtil.showToast(context,"用户名或密码错误");
							break;
					}
					break;
				case USER_EXIST:
					addEasemobAccount(tempPhone,tempPwd);
					break;
				case ADD_USER:
					addEasemobAccount(tempPhone,tempPwd);
					break;
			}
		}
	};

	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static final int REQUEST_AUDIO=2;
	private static String[] PERMISSIONS_STORAGE = {	Manifest.permission.READ_EXTERNAL_STORAGE,	Manifest.permission.WRITE_EXTERNAL_STORAGE	};

	private EditText codeEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		context = this;
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				MODE_APPEND);
		editor=sp.edit();

//		usernameEtv.setText("ce001");
//		passwordEtv.setText("111111");

//		usernameEtv.setText("13738297964");
//		passwordEtv.setText("a12345");

		loginIgv.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				showTestDialog();
				return false;
			}
		});
		isTest= sp.getBoolean(AppConstant.Is_test,false);

		if(UiUtil.isOpenNetwork(context)){
			updateVersion();
		}

		if(sp.getBoolean(AppConstant.USER_isFirstLogin, true)){

			editor.putString(AppConstant.USER_code,"573000");
			editor.commit();
			if(UiUtil.isOpenNetwork(context)){
				getCode(sp.getString(AppConstant.USER_code, null));
			}
		}else{
			if(UiUtil.isOpenNetwork(context)){
//				System.out.println("不是第一次登录，更新版本。。。");
//				System.out.println("sp: "+sp.getAll().toString());


				if(!sp.contains(AppConstant.USER_url)){
					editor.putString(AppConstant.USER_url,"http://122.225.60.118:6280/PartnersAPI/V3/SHDoctorAPI.asmx");
					editor.commit();
				}
//				updateVersion();
			}
		}

		passwordVisiableCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				if(isChecked){
					passwordEtv.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else {
					passwordEtv.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});

		if(sp.contains(AppConstant.LOGIN_state)){
			int state=sp.getInt(AppConstant.LOGIN_state,0);
			if(state==AppConstant.DOCTOR_LOGIN){
				if((sp.contains(AppConstant.USER_doctor_username))&&(sp.contains(AppConstant.USER_doctor_password))){
					String s1=sp.getString(AppConstant.USER_doctor_username,"");
					usernameEtv.setText(s1);
					usernameEtv.setSelection(s1.length());
					String s2=sp.getString(AppConstant.USER_doctor_password,"");
					passwordEtv.setText(s2);
					passwordEtv.setSelection(s2.length());

					if(ValidateUtil.isValidMobileNo(usernameEtv.getEditableText().toString().trim())){
						loginForDoctor();
					}
				}
			}else if(state==AppConstant.USER_LOGIN){
				if((sp.contains(AppConstant.USER_username))&&(sp.contains(AppConstant.USER_password))){
					String s1=sp.getString(AppConstant.USER_username,"");
					usernameEtv.setText(s1);
					usernameEtv.setSelection(s1.length());
					String s2=sp.getString(AppConstant.USER_password,"");
					passwordEtv.setText(s2);
					passwordEtv.setSelection(s2.length());
				}
			}else {

			}
		}
		rl_arrow = findViewById(R.id.rl_arrow);
		login_usernameEtv_rl = findViewById(R.id.login_usernameEtv_rl);
		setpop();
		rl_arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				pop.showAsDropDown(login_usernameEtv_rl);

			}
		});


		verifyStoragePermissions(this);

	}
	@SuppressLint("WrongConstant")
	public void setpop(){
		View contentView = View.inflate(context,R.layout.phone_list,null);
		Set<String> s = sp.getStringSet("phone",phoneSet);
		phone_list = contentView.findViewById(R.id.phone_list);
		phoneListAdapter = new PhoneListAdapter(context,s,LoginActivity.this);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
		phone_list.setLayoutManager(layoutManager);
		phone_list.setAdapter(phoneListAdapter);
//		pop = new PopupWindow(contentView,
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
		pop = new PopupWindow(contentView,-2,-2);

		pop.setFocusable(true);
		pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		pop.setOutsideTouchable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOnDismissListener(new poponDismissListener());

	}
	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			// Log.v("List_noteTypeActivity:", "我是关闭事件");
			backgroundAlpha(1f);
		}
	}
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		if (bgAlpha == 1) {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
		}
		getWindow().setAttributes(lp);
	}

	@OnClick({ R.id.login_login_btn, R.id.text_register,R.id.login_system_code_tv,
			 R.id.login_find_password_btn ,R.id.tv_code_login,R.id.tv_reset_password})
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_login_btn:	//登录
//			if(ValidateUtil.isValidMobileNo(usernameEtv.getEditableText().toString().trim())){
				loginForDoctor();
//			}else {
////				loginForService();
//				loginForCommunity();
//			}
			break;
		case R.id.text_register:
			startActivity(new Intent(context, RegisterForDoctorActivity.class));
			break;
			case R.id.tv_code_login:	//验证码登录
				startActivity(new Intent(context,CodeLoginActivity.class));
				break;
			case R.id.tv_reset_password:	//忘记密码
				startActivity(new Intent(context,ResetPasswordActivity.class));
				break;
		case R.id.login_find_password_btn:
			Toast.makeText(context, "请前往官网找回密码", Toast.LENGTH_SHORT).show();
			break;
			case R.id.login_system_code_tv:
				setSystemCode(true);
				break;
		default:
			break;
		}
	}

	public void loginForCommunity(){//评估人员登录

		if(!UiUtil.isOpenNetwork(context)){
			return;
		}
		final String username = usernameEtv.getText().toString();
		final String password = passwordEtv.getText().toString();
		System.out.println(password);
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 0x133) {
						try {
							String result=loginThread.getResult();
							if(UiUtil.isResultSuccess(context, result)) {
								if(isTest){
									UiUtil.showToast(context,result);
								}


								JSONObject json = JSONObject.fromObject(result);
								int code = json.getInt("code");
								if (code == 200) {
									JSONObject data = json.getJSONObject("Data");
									UiUtil.showToast(context, getResources()
											.getString(R.string.login_succeeded));

									int userId = data.getInt("UserID");
									sp.edit().putInt(AppConstant.ADMIN_userId, userId).commit();
									sp.edit().putString(AppConstant.USER_username, username).commit();
									sp.edit().putString(AppConstant.USER_password, password).commit();
									sp.edit().putInt(AppConstant.LOGIN_state,AppConstant.USER_LOGIN).commit();

									startActivity(new Intent(context, MainPinguActivity.class));
									finish();

								} else {
									UiUtil.showToast(context, json.getString("message"));
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			};

			loginThread = new DoctorLoginThread(ApiConstant.DOCTORLOGIN,	handler, context);
			loginThread.setAccount(username);
			loginThread.setPassword(password);
			loginThread.start();
		} else {
			UiUtil.showToast(context,
					getResources().getString(R.string.null_warning));
		}
	}
//	public void loginForService() {//客服人员登录
//		if(!UiUtil.isOpenNetwork(context)){
//			return;
//		}
//		final String username = usernameEtv.getText().toString();
//		final String password = passwordEtv.getText().toString();
//		System.out.println(password);
//		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//			Handler handler = new Handler() {
//				@Override
//				public void handleMessage(Message msg) {
//					if (msg.what == 0x133) {
//						try {
//							String result=loginThread.getResult();
//							if(UiUtil.isResultSuccess(context, result)) {
//              					if(isTest){
//									UiUtil.showToast(context,result);
//								}
//								JSONObject json = JSONObject.fromObject(result);
//								int code = json.getInt("code");
//								if (code == 200) {
//									JSONObject data = json.getJSONObject("Data");
//									UiUtil.showToast(context, getResources()
//											.getString(R.string.login_succeeded));
//
//									int userId = data.getInt("UserID");
//									sp.edit().putInt(AppConstant.ADMIN_userId, userId).commit();
//									sp.edit().putString(AppConstant.USER_username, username).commit();
//									sp.edit().putString(AppConstant.USER_doctor_username, username).commit();
//									sp.edit().putString(AppConstant.USER_doctor_password,password).commit();
//									sp.edit().putInt(AppConstant.LOGIN_state,AppConstant.DOCTOR_LOGIN).commit();
//
//									startActivity(new Intent(context, UserListActivity.class));
//
//								} else {
//									UiUtil.showToast(context, json.getString("message"));
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//			};
//
//			loginThread = new DoctorLoginThread(ApiConstant.DOCTORLOGIN,
//						handler, context);
//			loginThread.setAccount(username);
//			loginThread.setPassword(password);
//			loginThread.start();
//		} else {
//			UiUtil.showToast(context,
//					getResources().getString(R.string.null_warning));
//		}
//	}

	/*6.0 写 内存 的 权限  */
	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity,	Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
		}

	}

	public void loginForDoctor() {//医生登录

//		startActivity(new Intent(context, DoctorMainActivity.class));

		if(!UiUtil.isOpenNetwork(context)){
			return;
		}
		final String username = usernameEtv.getText().toString();
		final String password = passwordEtv.getText().toString();
		System.out.println(password);
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 0x133) {
						try {
							String result=loginForDoctorThread.getResult();
							if(UiUtil.isResultSuccess(context, result)) {
								if(isTest){
									UiUtil.showToast(context,result);
								}
								JSONObject json = JSONObject.fromObject(result);
								int code = json.getInt("code");
								if (code == 200) {
									JSONObject data = json.getJSONObject("Data");
									Acache.get(context).put("userdata",data);
									int audit=data.getInt("AUDIT");
									int doctorId=data.getInt("Id");
									String account=data.getString("Account");
									sp.edit().putInt(AppConstant.USER_doctor_id,doctorId).commit();
									MyApplication.Doctor_ID = doctorId;
									sp.edit().putString(AppConstant.USER_doctor_info,data.toString()).commit();
                                    sp.edit().putInt(AppConstant.USER_doctor_community_id,data.getInt("CommunityId")).commit();
									sp.edit().putString(AppConstant.USER_doctor_ease_name,account).commit();
									phoneSet = sp.getStringSet("phone",phoneSet);
                                    phoneSet.add(data.getString("Phone"));
                                    sp.edit().putStringSet("phone",phoneSet).commit();

                                    //暂时取消
//									if(audit==1){

										int userId = data.getInt("UserId");

										sp.edit().putInt(AppConstant.ADMIN_userId, userId).commit();
										sp.edit().putString(AppConstant.USER_doctor_username, username).commit();
										sp.edit().putString(AppConstant.USER_doctor_password,password).commit();
										sp.edit().putString(AppConstant.USER_doctor_ease_name,account).commit();
										sp.edit().putInt(AppConstant.LOGIN_state,AppConstant.DOCTOR_LOGIN).commit();
										getEasemobAccount(account,password);


										startActivity(new Intent(context, DoctorMainActivity.class));
//									}else{
//										startActivity(new Intent(context, ResultActivity.class));
//									}



								} else {
									UiUtil.showToast(context, json.getString("message"));
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			};

			loginForDoctorThread = new DoctorSHThread(ApiConstant.DOCTORLOGIN,handler, context);
			loginForDoctorThread.setPhone(username);
			loginForDoctorThread.setPassword(password);
			loginForDoctorThread.start();
		} else {
			UiUtil.showToast(context,
					getResources().getString(R.string.null_warning));
		}
	}
	void getEasemobAccount(final String account,final String password){
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==ApiConstant.MSG_API_HANDLER){
					String result=getEasemobAccountThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
						int code=jsonObject.getInt("code");
						if(code==200){
							String pwd=jsonObject.getString("Data");
							login(account,pwd);
						}else {
							register(account,password,password);
						}

					}
				}
			}
		};
		getEasemobAccountThread=new PopularThread(ApiConstant.GETEASEMOBPASSWORD,handler,context);
		getEasemobAccountThread.setAccount(account);
		getEasemobAccountThread.start();


	}
	void addEasemobAccount(final String account, final String password){
		final String pwdd=account.substring(7)+password.substring(0,3);
		System.out.println("环信密码："+pwdd);
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==ApiConstant.MSG_API_HANDLER){
					String result=addEasemobAccountThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
						if(code==200){
							login(account,pwdd);
						}else {
							UiUtil.showToast(context,jsonObject.getString("message"));
						}

					}
				}
			}
		};

		addEasemobAccountThread=new PopularThread(ApiConstant.ADDEASEMOBACCOUNT,handler,context);
		addEasemobAccountThread.setAccount(account);
		addEasemobAccountThread.setPassword(pwdd);
		addEasemobAccountThread.start();
	}

	private void register(final String userPhone, final String pwd, String rePwd){
		if(TextUtils.isEmpty(userPhone)||
				TextUtils.isEmpty(pwd)||
				TextUtils.isEmpty(rePwd)){
			UiUtil.showToast(context,"不能为空");

		}

		new Thread(){
			@Override
			public void run() {
				try{
					String pwdd=userPhone.substring(7)+pwd.substring(0,3);
					System.out.println("登录 环信密码："+pwdd);
					EMClient.getInstance().createAccount(userPhone,pwdd);
					tempPhone=userPhone;
					tempPwd=pwdd;
					loginHandler.sendEmptyMessage(ADD_USER);
//					addEasemobAccount(userPhone,pwdd);
//					handler.sendEmptyMessage(REG_SUCCESS);
//					startActivity(new Intent(context,EasyLoginActivity.class));
//					loaded();
				}catch (HyphenateException e){
					e.printStackTrace();
					Log.e("注册 错误信息:" ,e.getMessage()+" "+e.getErrorCode());
					if(e.getErrorCode()==203){
						tempPhone=userPhone;
						tempPwd=pwd;
						loginHandler.sendEmptyMessage(USER_EXIST);
					}
//					e.getErrorCode();

				}
			}
		}.start();
	}
	private void login(final String userName,String pwd){
		EMClient.getInstance().getInstance().login(userName, pwd, new EMCallBack() {
			@Override
			public void onSuccess() {

				EMClient.getInstance().groupManager().loadAllGroups();;
				EMClient.getInstance().chatManager().loadAllConversations();

				getSharedPreferences(AppConstant.EASY_USER_INFO,MODE_PRIVATE).edit().putString("username",userName).commit();
				Log.d("环信登录信息","登录成功了");

				loginHandler.sendEmptyMessage(LOGIN_SUCCESS);

				startActivity(new Intent(context,DoctorMainActivity.class));

//				handler.sendEmptyMessage(LOGIN_SUCCESS);
				finish();
			}

			@Override
			public void onError(int code, String message) {
				Log.e("main", "登录聊天服务器失败！" + message + " code = " + code);

				startActivity(new Intent(context,DoctorMainActivity.class));

//				handler.sendEmptyMessage(LOGIN_SUCCESS);
				finish();

//				Message msg = handler.obtainMessage();
//				msg.obj = message;
//				msg.arg1 = code;
//				msg.what = LOGIN_FAILED;
//				handler.sendMessage(msg);


			}

			@Override
			public void onProgress(int i, String s) {

			}
		});
	}

	private void showTestDialog() {

		View viewTest = LayoutInflater.from(context)
				.inflate(R.layout.view_test, null);
		CheckBox testModeCb=(CheckBox)viewTest.findViewById(R.id.test_mode_cb);
		CheckBox testServerCb=(CheckBox)viewTest.findViewById(R.id.test_server_cb);
		boolean isTestMode= sp.getBoolean(AppConstant.Is_test,false);
		boolean isTestServer= sp.getBoolean(AppConstant.Is_test_server,false);
		if(isTestMode){
			testModeCb.setChecked(true);
		}
		if(isTestServer){
			testServerCb.setChecked(true);
		}

		testModeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				sp.edit().putBoolean(AppConstant.Is_test,isChecked).commit();
			}
		});
		testServerCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				sp.edit().putBoolean(AppConstant.Is_test_server,isChecked).commit();
			}
		});


		android.app.AlertDialog mAlertDialog = new android.app.AlertDialog.Builder(context)
				.setTitle("测试设置")
				.setView(viewTest)
				.create();

		mAlertDialog.show();

	}
	private void updateVersion(){
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x136) {
					String mResult = updateVersionThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						try {
//							System.out.println("更新版本返回结果:"+mResult);
							JSONObject j = JSONObject.fromObject(mResult);
							String versionLoc= ValidateUtil.getVersionName(getApplicationContext());
							String versionNow=j.getString("version");
							float versionLocF=Float.parseFloat(versionLoc);
							float versionNowF=Float.parseFloat(versionNow);
//							System.out.println("versionLocF:"+versionLocF+"  versionNowF:"+versionNowF);
							if (versionLocF<versionNowF) {//正式
//								System.out.println("更新版本");
//								if (versionLocF>versionNowF) {//测试
								updateManger = new UpdateManger(context,
										j.getString("ApkDownloadUrl"));
//									mUpdateManger = new UpdateManger(context,
//										"http://211.161.126.174/imtt.dd.qq.com/16891/9C85E1C83CE830F60137BD6609FB201F.apk?mkey=587db5241af15529&f=c352&c=0&fsname=com.jxj.healthambassador_3.2_22.apk&csr=4d5s&p=.apk");
								showNoticeDialog();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		};


		updateVersionThread = new UpdateVersionThread(context,handler);
		updateVersionThread.start();
	}

	private void showNoticeDialog() {

		View viewNull = LayoutInflater.from(context)
				.inflate(R.layout.view_null, null);

		AlertDialog mAlertDialog = new AlertDialog.Builder(context)
				.setTitle(getResources().getString(R.string.duv_title))
				.setView(viewNull)
				.setPositiveButton(getResources().getString(R.string.mycancel),null)
				.setNegativeButton(getResources().getString(R.string.ok),new ok())
				.create();

		mAlertDialog.show();

	}


	@Override
	public void OnPhoneClick(View v, String phone) {
		usernameEtv.setText(phone);
		pop.dismiss();
	}

	private class ok implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			updateManger.showDownloadDialog();
		}

	}

	private void setSystemCode(boolean isShow){
		View viewCode = LayoutInflater.from(context)
				.inflate(R.layout.view_input_system_code, null);

		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle(getResources().getString(R.string.system_code))
				.setView(viewCode)
				.setPositiveButton(getResources().getString(R.string.mycancel),null)
				.setNegativeButton(getResources().getString(R.string.ok),new ok_Code())
				.create();

		codeEt=(EditText) viewCode.findViewById(R.id.system_code_et);

		if(isShow){
			String code=sp.getString(AppConstant.USER_code, "");
			codeEt.setText(code);
		}


		alertDialog.show();
	}

	private class ok_Code implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			if(!UiUtil.isEditTextNull(codeEt)){
				getCode(codeEt.getEditableText().toString().trim());

			}else{
//				if(codeEt.getEditableText().toString().trim().equals("000000")){
//					editor.putString(jHAppConstant.USER_code, "000000");
//					editor.putString(jHAppConstant.USER_key, "c01b4754-fcf0-4047-aff2-19abf6cd0653");
//					editor.putString(jHAppConstant.USER_api, "us.iprecare.com:6280");
//
//					editor.commit();
//
//					return;
//				}
				UiUtil.showToast(context, getResources().getString(R.string.not_null));
				setSystemCode(true);
			}
		}

	}

	private void getCode(String code){
		Handler getCodeHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==0x135){
					String result=getCodeThread.getResult();
					if(UiUtil.isResultSuccess(context, result)){
						try{
							JSONObject json=JSONObject.fromObject(result);
							if(json.getInt("code")==200){

								String data=json.getString("Data");
								JSONObject jsonObj=JSONObject.fromObject(data);
								editor.putString(AppConstant.USER_code, jsonObj.getString("Code"));
								editor.putString(AppConstant.USER_key, jsonObj.getString("KEY"));
								editor.putString(AppConstant.USER_api, jsonObj.getString("IPreCareAPI"));
//								if(jsonObj.getString("Account").equals("usa")){
//									editor.putString(AppConstant.USER_url,"http://www.iprecare.com");
//									editor.putInt(AppConstant.USER_map,AppConstant.MAP_GOOGLE);
//								}else {
									editor.putString(AppConstant.USER_url,"http://122.225.60.118:6280/PartnersAPI/SHDoctorAPI.asmx");
//									editor.putInt(AppConstant.USER_map,AppConstant.MAP_BAIDU);
//								}
								editor.putBoolean(AppConstant.USER_isFirstLogin, false);

								editor.commit();
//								System.out.println("是否修改成功："+b);
//								updateVersion();


							}else{
								setSystemCode(false);
							}
							UiUtil.showToast(context, json.getString("message"));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
//					loginBtn.setClickable(true);
				}
			}
		};
//		String partnerCode=sp.getString(jHAppConstant.USER_code, null);
		if(code!=null){
			getCodeThread=new GetPartnerAPIThread(code, getCodeHandler);
			getCodeThread.start();
//			loginBtn.setClickable(false);

		}

	}

}
