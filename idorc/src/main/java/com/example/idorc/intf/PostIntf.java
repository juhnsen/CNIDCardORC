package com.example.idorc.intf;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sheng on 2018/3/6.
 */

public interface PostIntf {
	@POST("idcardocr")
	Call<ResponseBody> getHTMLString(@Body RequestBody route);
}
