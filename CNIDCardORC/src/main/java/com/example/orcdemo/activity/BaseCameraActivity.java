package com.example.orcdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;

import com.example.orcdemo.manager.CameraManager;


/**
 * Created by sheng on 2018/1/25.
 */

public class BaseCameraActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback,SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private CameraManager cameraManager;
	private boolean hasSurface;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // 获取传感器管理对象
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// 获取传感器的类型(TYPE_ACCELEROMETER:加速度传感器)
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}




	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

	}

	@Override
	protected void onPause() {

		// 取消监听
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {

	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public void onPreviewFrame(byte[] bytes, Camera camera) {

	}



}
