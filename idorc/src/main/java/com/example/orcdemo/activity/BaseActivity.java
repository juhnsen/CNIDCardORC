package com.example.orcdemo.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.orcdemo.intf.PermissionListener;
import com.example.orcdemo.util.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheng on 2018/2/24.
 */

public class BaseActivity extends AppCompatActivity {

	public static PermissionListener mListener;
	public static final int REQUEST_CODE = 1;

	// onCreate/onDestory 中保存 Activity
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	public static void requestRuntimePermission(String[] permissions,
												PermissionListener listener) {
		// 获取栈顶 Activity
		Activity topActivity = ActivityCollector.getTopActivity();
		if (topActivity == null)
			return;
		mListener = listener;
		// 需要请求的权限列表
		List<String> requestPermisssionList = new ArrayList<>();
		// 检查权限  是否已被授权
		for (String permission : permissions) {
			if (ActivityCompat.checkSelfPermission(topActivity, permission)
					!= PackageManager.PERMISSION_GRANTED)
				// 未授权时添加该权限
				requestPermisssionList.add(permission);
		}

		if (requestPermisssionList.isEmpty())
			// 所有权限已经被授权过 回调 Listener onGranted 方法 已授权
			listener.onGranted();
		else
			// 进行请求权限操作
			ActivityCompat.requestPermissions(topActivity,
					requestPermisssionList.toArray(new String[requestPermisssionList.size()]),
					REQUEST_CODE);

	}

	// 请求权限的回调
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case REQUEST_CODE: {

				List<String> deniedPermissionList = new ArrayList<>();
				// 检查返回授权结果不为空
				if (grantResults.length > 0) {
					// 判断授权结果
					for (int i = 0; i < grantResults.length; i++) {
						int result = grantResults[i];
						if (result != PackageManager.PERMISSION_GRANTED)
							// 保存被用户拒绝的权限
							deniedPermissionList.add(permissions[i]);
					}
					if (deniedPermissionList.isEmpty())
						// 都被授权  回调 Listener onGranted 方法 已授权
						mListener.onGranted();
					else
						// 有权限被拒绝 回调 Listner onDeynied 方法
						mListener.onDenied(deniedPermissionList);
				}
				break;
			}
			default:
				break;
		}
	}
}

