package com.example.orcdemo.util;


import com.example.orcdemo.intf.Constant;
import com.example.orcdemo.intf.PostIntf;
import com.example.orcdemo.sign.YoutuSign;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.orcdemo.intf.Constant.EXPIRED_SECONDS;


/**
 * Created by sheng on 2018/2/26.
 */

public class HttpUtil {
	public static void uploadIdCard(String bitmap, String card_type, final SimpleCallBack callback) {
		final StringBuffer mySign = new StringBuffer("");
		YoutuSign.appSign(Constant.AppID, Constant.SecretID, Constant.SecretKey,
				System.currentTimeMillis() / 1000 + EXPIRED_SECONDS,
				"", mySign);
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.addInterceptor(new Interceptor() {
					@Override
					public okhttp3.Response intercept(Chain chain) throws IOException {
						Request original = chain.request();

						Request request = original.newBuilder()
								.header("accept", "")
								.header("Host", "api.youtu.qq.com")
								.header("user-agent", "youtu-java-sdk")
								.header("Authorization", mySign.toString())
								.header("Content-Type", "text/json")
								.method(original.method(), original.body())
								.build();
						return chain.proceed(request);
					}
				})
				.build();

		String url = "http://api.youtu.qq.com/youtu/ocrapi/";// URL地址
		Retrofit build=new Retrofit.Builder().baseUrl(url).client(httpClient).build();
		HashMap<String,Object> paramsMap=new HashMap<>();
		paramsMap.put("card_type", Integer.valueOf(card_type));
		paramsMap.put("image", bitmap);
		paramsMap.put("app_id", Constant.AppID);
		paramsMap.put("session_id","1000000111111");
		JSONObject jsonObj = new JSONObject(paramsMap);
		RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
				jsonObj.toString());
		PostIntf ti=build.create(PostIntf.class);

		Call<ResponseBody> call=ti.getHTMLString(requestBody);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try{
					callback.Succ(response.body().string());
				}catch (IOException e){}

			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {

			}
		});


	}

	public interface SimpleCallBack {
		void Succ(String result);

		void error();
	}


}
