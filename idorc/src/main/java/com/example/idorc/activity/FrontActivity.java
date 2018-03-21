package com.example.idorc.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.idorc.R;


public class FrontActivity extends AppCompatActivity {
	private static final int GETPERMISSION_SUCCESS = 1;//获取权限成功
	private static final int GETPERMISSION_FAILER = 2;//获取权限失败

	EditText reName,reId,reSex,reNation,reAddress;
	LinearLayout activityMain;
	ImageView iv1;


	private int MY_SCAN_REQUEST_CODE = 100;
	private Context mContext;

	private MyHandler myHandler = new MyHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);

		mContext = this;
		init();
		findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				getPermission();
			}
		});
	}

	private void init(){
		reId=(EditText)findViewById(R.id.re_id);
		reAddress=(EditText)findViewById(R.id.re_address);
		reName=(EditText)findViewById(R.id.re_name);
		reNation=(EditText)findViewById(R.id.re_nation);
		reSex=(EditText)findViewById(R.id.re_sex);
		activityMain=(LinearLayout)findViewById(R.id.activity_main);
		iv1=(ImageView)findViewById(R.id.iv1);
	}

	private void getPermission() {
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			//权限还没有授予，需要在这里写申请权限的代码
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA},
					1);
		} else {
			//权限已经被授予，在这里直接写要执行的相应方法即可
			myHandler.sendEmptyMessage(GETPERMISSION_SUCCESS);

		}
	}

	//因为权限管理类无法监听系统，所以需要重写onRequestPermissionResult方法，更新权限管理类，并回调结果。这个是必须要有的。
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 1) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//申请成功，可以拍照
				myHandler.sendEmptyMessage(GETPERMISSION_SUCCESS);
			} else {
				Toast.makeText(this, "CAMERA PERMISSION DENIED", Toast.LENGTH_SHORT).show();
			}
			return;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MY_SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			String name = data.getStringExtra("name");
			String sex = data.getStringExtra("sex");
			String nation = data.getStringExtra("nation");
			String address = data.getStringExtra("address");
			String id = data.getStringExtra("id");
			//String frontImage=data.getStringExtra("frontimage");
			reName.setText(name);
			reSex.setText(sex);
			reNation.setText(nation);
			reAddress.setText(address);
			reId.setText(id);
			//Bitmap _image=BitMapUtils.base64ToBitmap(frontImage);
			//iv1.setImageBitmap(_image);
		}
	}





	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case GETPERMISSION_SUCCESS:
					Intent scanIntent = new Intent(mContext, CameraFrontActivity.class);
					startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
					break;
				case GETPERMISSION_FAILER:
					Toast.makeText(mContext, "此功能须获摄像头权限1111111", Toast.LENGTH_LONG).show();
					//Intent scanIntent2 = new Intent(mContext, CameraFrontActivity.class);
					//startActivityForResult(scanIntent2, MY_SCAN_REQUEST_CODE);
					break;
			}
		}
	}
}
