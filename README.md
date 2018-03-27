简介
===
这是一个基于腾讯优图提供的[身份证识别接口](http://open.youtu.qq.com/#/develop/api-ocr-card)（免费的）编写的module，可一键导入你的项目中使用，无需自己编写相机功能和接口交互

导入
===
```Java
dependencies {
    compile 'com.github.juhnsen:CNIDCardORC:v1.0'
}
```

怎么使用
===
在你需要获取身份证数据的Activity里设置跳转，如<br><br>

* 身份证相片面

```Java
Intent scanIntent = new Intent(context, CameraFrontActivity.class);
startActivityForResult(scanIntent, 1);
```

* 身份证国徽面

```Java
Intent scanIntent = new Intent(context, CameraBackActivity.class);
startActivityForResult(scanIntent, 1);
```

然后再重写`onActivityResult()`获取返回的数据，如<br>

* 身份证相片面
```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
		String name = data.getStringExtra("name");
		String sex = data.getStringExtra("sex");
		String nation = data.getStringExtra("nation");
		String address = data.getStringExtra("address");
		String id = data.getStringExtra("id");	
	}
}
```

* 身份证国徽面

```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
		String location = data.getStringExtra("date");
		String date=data.getStringExtra("location");
	}
}
```
<br>
拿到数据之后你就可以对数据进行操作了

<br><br>
更多功能与注意事项
===

因为需要联网，所以记得再`AndroidManifest`里加入联网权限
```PHP
<uses-permission android:name="android.permission.INTERNET"/>
```

而相机权限则已经封装在了`BaseActivity`里，如果想偷懒不想写相机权限的话可以让你的Activity继承`BaseActivity`,然后再将你的跳转放进下面的方法里
```Java
BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.CAMERA}, new PermissionListener() {
	@Override
	public void onGranted() {
	  //放入你的操作
	}
	@Override
	public void onDenied(List<String> deniedPermission) {}
});
```

这个`BaseActivity`可以用于各种权限申请，只需要将String[]{}里的权限改掉即可

