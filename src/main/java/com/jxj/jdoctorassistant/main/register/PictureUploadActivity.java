package com.jxj.jdoctorassistant.main.register;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;

import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class PictureUploadActivity extends Activity {
	@ViewInject(R.id.headpicture_camera_rl)
	RelativeLayout headPictureCameraRl;
	@ViewInject(R.id.headpicture_photo_rl)
	RelativeLayout headPicturePhotoRl;
//	@ViewInject(R.id.doctor_head_img)
//	ImageView headPictureImg;

	@OnClick({ R.id.headpicture_cancle_rl })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.headpicture_cancle_rl:
			finish();
			break;

		default:
			break;
		}
	}

	private Context context;
	private Uri imageUri;
	private DoctorSHThread getLicenseThread,setLicenseThread;

	private SharedPreferences sp;
	private int doctorId;

	private File photofile;
	private Intent resultIntent;

	// 保存图片本地路径
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath() + "/jDoctorAssistant";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "/cardImages/";

	private  String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;

	private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
	// private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpg";

	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
	private String mAlbumPicturePath = null;

	// 版本比较：是否是4.4及以上版本
	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	private static final int REQUEST_CAMERA_PERMISSION = 0x011;
	private static final int REQUEST_READ_STORGE_PERMISSION=0x012;
	private static final int REQUEST_WRITE_STORGE_PERMISSION=0x013;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_head_picture);
		ViewUtils.inject(this);
		context = this;

		sp = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);

		doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

		initviews();
	}

	private void initviews() {
//		if (ContextCompat.checkSelfPermission(context,
//				Manifest.permission.READ_EXTERNAL_STORAGE)
//				!= PackageManager.PERMISSION_GRANTED) {
//		}else{
//			//
//		}

		headPictureCameraRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestCameraPermission();
				requestWriteStorgePermission();

				// 指定照相机拍照后图片的存储路径，这里存储在自己定义的文件夹下
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					// 创建一个文件夹对象，赋值为外部存储器的目录
					// File sdcardDir =
					// Environment.getExternalStorageDirectory();
					// 得到一个路径，内容是sdcard的文件夹路径和名字
					String path = IMGPATH;
					photofile = new File(path);

					if (!photofile.exists()) {
						// 若不存在，创建目录，可以在应用启动的时候创建
						photofile.mkdirs();

					} else {
						imageUri = Uri.fromFile(new File(photofile,
								IMAGE_FILE_NAME));

						// 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
						// 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
						// 保存照片在自定义的文件夹下面
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, 1);
					}
				} else {
					UiUtil.showToast(context,
							getResources().getString(R.string.SD_warn));
					return;

				}

			}

		});
		headPicturePhotoRl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				requestReadStorgePermission();
				try {

					if (mIsKitKat) {
						selectImageUriAfterKikat();
					} else {
						// cropImageUri();

						// 选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
						// 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intent, 2);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});

	}

	private void requestCameraPermission() {
		int checkSelfPermission=ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA);

		if(checkSelfPermission!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
			if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
				new AlertDialog.Builder(context).setMessage("你需要启动相机的权限").setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ActivityCompat.requestPermissions(PictureUploadActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
					}
				}).setNegativeButton("Cancel",null)
						.create()
						.show();
			}
		}else {

		}

	}
	private void requestReadStorgePermission() {
		int checkSelfPermission=ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE);

		if(checkSelfPermission!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_STORGE_PERMISSION);
			if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
				new AlertDialog.Builder(context).setMessage("你需要读取存储的权限").setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ActivityCompat.requestPermissions(PictureUploadActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_STORGE_PERMISSION);
					}
				}).setNegativeButton("Cancel",null)
						.create()
						.show();
			}
		}else {

		}

	}
	private void requestWriteStorgePermission() {
		int checkSelfPermission=ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if(checkSelfPermission!=PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORGE_PERMISSION);
			if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
				new AlertDialog.Builder(context).setMessage("你需要写入存储的权限").setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ActivityCompat.requestPermissions(PictureUploadActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORGE_PERMISSION);
					}
				}).setNegativeButton("Cancel",null)
						.create()
						.show();
			}
		}else {

		}

	}
	/**
	 * <br>
	 * 功能简述:4.4以上裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		Uri uri = null;
		System.out.println("data:" + data + "requestCode:" + requestCode
				+ "resultCode:" + resultCode);
		// 如果返回的是拍照上传
		if (data == null) {
			uri = imageUri;
		} // 返回的是图库上传
		else {
			uri = data.getData();
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				intent.setDataAndType(uri, "image/*");
				intent.putExtra("crop", true);
				// 设置裁剪尺寸
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra("return-data", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 3);
				break;
			case 2:
				System.out.println("uri:" + uri);

				intent.setDataAndType(uri, "image/*");
				intent.putExtra("crop", true);
				// 设置裁剪尺寸
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra("return-data", true);

				intent.putExtra("scale", true);
				intent.putExtra("outputFormat",
						CompressFormat.JPEG.toString());
				intent.putExtra("noFaceDetection", true); // no face detection

				// 创建一个文件夹对象，赋值为外部存储器的目录
				// File sdcardDir = Environment.getExternalStorageDirectory();
				// // 得到一个路径，内容是sdcard的文件夹路径和名字
				// String path = sdcardDir.getPath() +
				// "/jWotchHelper/cardImages";
				// File myfile = new File(path);
				// uri = Uri.fromFile(new File(myfile, customerId
				// + getHeadPictureName()));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 3);

				break;
			case 3:
				if (data != null) {

					resultIntent = getIntent();
					Bundle bundle = data.getExtras();
					Bitmap myBitmap = bundle.getParcelable("data");

					// bitmap_=myBitmap;
					// 上传医生头像
//					setPhoto(myBitmap);
					setLicense(myBitmap);

					// 设置返回结果
					resultIntent.putExtra("myBitmap", myBitmap);

				}
				break;

			case SELECET_A_PICTURE_AFTER_KIKAT:
				if (resultCode == RESULT_OK && null != data) {
					 Log.v("zou", "SELECET_A_PICTURE_AFTER_KIKAT");
					mAlbumPicturePath = getPath(getApplicationContext(),
							data.getData());
					//20170724 出现问题
//					cropImageUriAfterKikat(Uri.fromFile(new File(
//							mAlbumPicturePath)));
					cropImageUriAfterKikat(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(mAlbumPicturePath)));
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(context,
							getResources().getString(R.string.cancel_config),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case SET_ALBUM_PICTURE_KITKAT:
				Log.v("zou", "SET_ALBUM_PICTURE_KITKAT");
				Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(
						IMGPATH, IMAGE_FILE_NAME)));//********
//				headPictureImg.setImageBitmap(bitmap);

//				setPhoto(bitmap);
				setLicense(bitmap);
				// // 设置返回结果
				// resultIntent.putExtra("myBitmap", bitmap);
				break;
			default:
				break;
			}
		}

	}

	void setLicense(Bitmap bitmap){
		Handler handler= new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0x133) {
					String result = setLicenseThread.getResult();
					// System.out.println(result);
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject json = JSONObject.fromObject(result);
						int code = json.getInt("code");
						UiUtil.showToast(context, json.getString("message"));
							// 得到上传的图片，并且保存显示
						if(code==200){
							finish();
						}
					}

				}
			};
		};
		setLicenseThread = new DoctorSHThread(
				ApiConstant.SETDOCTORLICENSE,
				handler, context);
		setLicenseThread.setDoctorId(doctorId);
		setLicenseThread.setLicense(Stream(bitmap));
		setLicenseThread.start();

	}

	/**
	 * <br>
	 * 功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	private void cropImageUriAfterKikat(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		// intent.putExtra("return-data", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
	}

	/**
	 * <br>
	 * 功能简述:4.4及以上获取图片的方法 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	// 得到图像数据流base64加密字符串
	public String Stream(Bitmap bitmap) {
		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	private void getLicense(){
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==ApiConstant.MSG_API_HANDLER){
					String result=getLicenseThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
						UiUtil.showToast(context,jsonObject.getString("messsage"));
					}
				}
			}
		};
		getLicenseThread=new DoctorSHThread(ApiConstant.GETDOCTORLICENSE,handler,context);
		getLicenseThread.setDoctorId(0);
		getLicenseThread.start();
	}

	/**
	 * <br>
	 * 功能简述: <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}


	// 将解密字符串转化为bitmap
	public Bitmap getBitmap(String bitmapString) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(bitmapString, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

}
