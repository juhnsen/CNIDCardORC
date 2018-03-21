package com.example.idorc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.idorc.R;
import com.example.idorc.util.NetWorkUtils;


public class MainActivity extends AppCompatActivity {
	Button front,back,click;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		//LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) clor.getLayoutParams();
		front=(Button)findViewById(R.id.front);
		back=(Button)findViewById(R.id.back);
		front.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (NetWorkUtils.isNetworkConnected(context)) {
					Intent intent = new Intent(MainActivity.this, FrontActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(context, "无网络，请确保已联网", Toast.LENGTH_SHORT).show();
				}
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (NetWorkUtils.isNetworkConnected(context)) {
					Intent intent = new Intent(MainActivity.this, BackActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(context, "无网络，请确保已联网", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}




}
