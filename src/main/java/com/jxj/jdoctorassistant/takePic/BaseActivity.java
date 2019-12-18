package com.jxj.jdoctorassistant.takePic;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;





import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;


public class BaseActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {

	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 =0 ;
	private View mLayout;
	/**
	 * 选择图片的返回码
	 */
	public final static int SELECT_IMAGE_RESULT_CODE = 200;

	/**
	 * 当前选择的图片的路径
	 */
	public String mImagePath;
	/**
	 * 自定义的PopupWindow
	 */
	private SelectPicPopupWindow menuWindow;
	/**
	 * Fragment回调接口
	 *
	 *
	 */
	public OnFragmentResult mOnFragmentResult;


	private static final int REQUEST_CAMERA = 0;
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static final int REQUEST_CAMERA_CODE = 3;
	private static String[] PERMISSIONS_STORAGE = {	Manifest.permission.READ_EXTERNAL_STORAGE,	Manifest.permission.WRITE_EXTERNAL_STORAGE	};
	private static String[] TAKEPIC = {	Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,	Manifest.permission.WRITE_EXTERNAL_STORAGE		};




	/*6.0  需要拍照   获取的权限 */
	public static void verifyTakePicPermissions(Activity activity) {
		int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.CAMERA);
		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, TAKEPIC,REQUEST_CAMERA_CODE);

		}


	}

	/*6.0 写 内存 的 权限  */
	public static void verifyStoragePermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity,	Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
		}

	}


	public void setOnFragmentResult(OnFragmentResult onFragmentResult){
		mOnFragmentResult = onFragmentResult;
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {



	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {

	}

	/** 回调数据给Fragment的接口*/
	public interface OnFragmentResult{
		void onResult(String mImagePath);
	}
	
	/** 拍照或从图库选择图片(Dialog形式)	 */
	public void showPictureDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[] { "拍摄照片", "选择照片", "取消" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int position) {
						switch (position) {
						case 0://拍照

							jianchaQuanxian();
							//takePhoto();
							break;
						case 1://相册选择图片
							pickPhoto();


							break;
						case 2://取消
							break;
						default:
							break;
						}
					}
				});
		builder.create().show();


	}

	private void jianchaQuanxian() {
		if (ContextCompat.checkSelfPermission(this,	Manifest.permission.CAMERA)	!= PackageManager.PERMISSION_GRANTED)
		{

			//权限还没有授予，需要在这里写申请权限的代码
			ActivityCompat.requestPermissions(this,		new String[]{Manifest.permission.CAMERA},	MY_PERMISSIONS_REQUEST_CALL_PHONE2);
			Log.v("qqq","走到这里去申请权限");

		}else {
			//权限已经被授予，在这里直接写要执行的相应方法即可
			beforeDoPic();


		}


	}

	/** 拍照获取图片 */
	private void takePhoto() {
	verifyTakePicPermissions(this);
			// 执行拍照前，应该先判断SD卡是否存在
		beforeDoPic();



	}

	public void beforeDoPic() {
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            /**
             * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
             */
            //获取与应用相关联的路径
            String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            //根据当前时间生成图片的名称
            String timestamp = "/"+formatter.format(new Date())+".jpg";
            File imageFile = new File(imageFilePath,timestamp);// 通过路径创建保存文件

            mImagePath = imageFile.getAbsolutePath();
            Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri

            startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE);
			Log.v("qqq","拍照完成");

        } else {
            //Toast.makeText(this, "内存卡不存在!", Toast.LENGTH_LONG).show();
        }
	}


	/*** 从相册中取图片 */
	private void pickPhoto() {
		verifyStoragePermissions(this);
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE);

	}


}
