package com.example.idorc.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheng on 2018/2/24.
 */
public class ActivityCollector {

	private static List<Activity> activityList = new ArrayList<>();

	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activityList.remove(activity);
	}
	// 获取栈顶 Activity
	public static Activity getTopActivity() {
		if (activityList.isEmpty())
			return null;
		return activityList.get(activityList.size() - 1);
	}
}

