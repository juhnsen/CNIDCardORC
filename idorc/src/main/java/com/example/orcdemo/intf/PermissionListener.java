package com.example.orcdemo.intf;

import java.util.List;

/**
 * Created by sheng on 2018/2/24.
 */

public interface PermissionListener {

	void onGranted();
	void onDenied(List<String> deniedPermission);
}
