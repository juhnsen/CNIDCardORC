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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.idorc.R;


public class BackActivity extends AppCompatActivity {

	private static final int GETPERMISSION_SUCCESS = 1;//获取权限成功
	private static final int GETPERMISSION_FAILER = 2;//获取权限失败

	EditText reLocation;
	EditText reDate;
	Button back;

	private int MY_SCAN_REQUEST_CODE = 100;
	private Context mContext;

	private MyHandler myHandler = new MyHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back);
		reLocation=(EditText)findViewById(R.id.re_location);
		reDate=(EditText)findViewById(R.id.re_date);
		mContext = this;
		back=(Button)findViewById(R.id.btn_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getPermission();
			}
		});

	}


	private void getPermission(){
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED)
		{
			//权限还没有授予，需要在这里写申请权限的代码
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.CAMERA},
					1);
		}else {
			//权限已经被授予，在这里直接写要执行的相应方法即可
			myHandler.sendEmptyMessage(GETPERMISSION_SUCCESS);

		}
	}

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

			String location = data.getStringExtra("date");
			String date=data.getStringExtra("location");

			reDate.setText(date);
			reLocation.setText(location);
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case GETPERMISSION_SUCCESS:
					Intent intent1 = new Intent(mContext, CameraBackActivity.class);
					startActivityForResult(intent1, MY_SCAN_REQUEST_CODE);
					break;
				case GETPERMISSION_FAILER:
					Toast.makeText(mContext, "此功能须获摄像头权限1111111", Toast.LENGTH_LONG).show();
					//Intent intent2 = new Intent(mContext, CameraBackActivity.class);
					//startActivityForResult(intent2, MY_SCAN_REQUEST_CODE);
					break;
			}
		}
	}
}
