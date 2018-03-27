package com.example.orcdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.orcdemo.R;
import com.example.orcdemo.manager.CameraManager;
import com.example.orcdemo.util.BitMapUtils;
import com.example.orcdemo.util.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class CameraBackActivity extends BaseCameraActivity{

	private CameraManager cameraManager;
	private boolean hasSurface;
	private Button btn_close, light;
	private boolean toggleLight = false;
	int isQuiet = 1;
	private TextView tv_lightstate;
	private int count=0;
	private Long opentime;
	Bitmap bmp;
	Context ThisContext;
	ArrayList<Float> x_s,y_s,z_s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		opentime = System.currentTimeMillis();
		int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		//设置当前窗体为全屏显示
		getWindow().setFlags(flag, flag);
		setContentView(R.layout.activity_camera);
		ThisContext=this;
		x_s=new ArrayList();
		y_s=new ArrayList();
		z_s=new ArrayList();
		tv_lightstate = (TextView) findViewById(R.id.tv_openlight);
		initLayoutParams();
	}

	/**
	 * 重置surface宽高比例为3:4，不重置的话图形会拉伸变形
	 */
	private void initLayoutParams() {

		btn_close = (Button) findViewById(R.id.btn_close);
		light = (Button) findViewById(R.id.light);
		btn_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				onBackPressed();

			}
		});
		light.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();// 摄像头 初始化 需要时间
				if (time - opentime > 2000) {
					opentime = time;
					if (!toggleLight) {
						toggleLight = true;
						tv_lightstate.setText("关闭闪关灯");
						cameraManager.openLight();
					} else {
						toggleLight = false;
						tv_lightstate.setText("打开闪关灯");
						cameraManager.offLight();
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 初始化camera
		 */
		cameraManager = new CameraManager();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();

		if (hasSurface) {
			// activity在paused时但不会stopped,因此surface仍旧存在；
			// surfaceCreated()不会调用，因此在这里初始化camera
			initCamera(surfaceHolder);
		} else {
			// 重置callback，等待surfaceCreated()来初始化camera
			surfaceHolder.addCallback(this);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	/**
	 * 初始camera
	 *
	 * @param surfaceHolder SurfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			return;
		}

		try {
			// 打开Camera硬件设备
			cameraManager.openDriver(surfaceHolder, this);
			// 创建一个handler来打开预览，并抛出一个运行时异常
			cameraManager.startPreview(this);

		} catch (Exception ioe) {
			Log.d("zk", ioe.toString());

		}
	}

	@Override
	protected void onPause() {
		/**
		 * 停止camera，释放资源操作
		 */
		cameraManager.stopPreview();
		cameraManager.closeDriver();

		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}


	@Override
	public void onBackPressed() {
		//if (bitmap!=null) bitmap.recycle();
		super.onBackPressed();
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		float[] values = sensorEvent.values;
		float x=(float)(Math.round(values[0]*100))/100;
		float y=(float)(Math.round(values[1]*100))/100;
		float z=(float)(Math.round(values[2]*100))/100;
		if (x_s.size()<5){
			x_s.add(x);
			y_s.add(y);
			z_s.add(z);
		}else {
			x_s.remove(0);
			x_s.add(x);
			y_s.remove(0);
			y_s.add(y);
			z_s.remove(0);
			z_s.add(z);

		}


	}

	public float isPhoneMove(){
		float sum=0;
		float avg;
		for (int i=0;i<x_s.size();i++){
			sum=sum+Math.abs(x_s.get(i));
		}
		for (int i=0;i<y_s.size();i++){
			sum=sum+Math.abs(y_s.get(i));
		}
		for (int i=0;i<z_s.size();i++){
			sum=sum+Math.abs(z_s.get(i));
		}
		avg=(float)(Math.round((sum/15)*100))/100;

		return avg;
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		ByteArrayOutputStream baos;
		byte[] rawImage;

		Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		YuvImage yuvimage = new YuvImage(
				data,
				ImageFormat.NV21,
				previewSize.width,
				previewSize.height,
				null);
		baos = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, baos);// 80--JPG图片的质量[0-100],100最高
		rawImage = baos.toByteArray();
		//将rawImage转换成bitmap
		bmp= BitMapUtils.transform_Cut(rawImage);

		count++;

		if (bmp == null) {
			Log.d("zka", "bitmap is nlll");
			return;
		} else {
			if (count>1&&isPhoneMove()<0.15) {
				Log.d("isQuiet=", ""+isQuiet);
				if (isQuiet==1) {
					isQuiet=0;
					String msg = BitMapUtils.bitmapToBase64(bmp);

					HttpUtil.uploadIdCard(msg, "1", new HttpUtil.SimpleCallBack() {
						@Override
						public void Succ(String result) {
							Log.d("背面", result);
							JsonParser parser = new JsonParser();  //创建JSON解析器
							JsonObject object = (JsonObject) parser.parse(result);  //创建JsonObject对象
							int errorCode = object.get("errorcode").getAsInt();

							if (errorCode != 0) {
								Log.d("errorcode为", "" + errorCode);
							} else {
								String date = object.get("valid_date").getAsString();
								String location = object.get("authority").getAsString();
								Intent i = new Intent();
								i.putExtra("date",
										date);
								i.putExtra("location",
										"" + location);
								setResult(RESULT_OK, i);

								bmp.recycle();
								finish();
							}
						}

						@Override
						public void error() {

						}
					});

				}else { }
			}else {
				isQuiet=1;
			}
		}
	}
}
