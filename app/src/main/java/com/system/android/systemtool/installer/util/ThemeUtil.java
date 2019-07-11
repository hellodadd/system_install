package com.system.android.systemtool.installer.util;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;

import com.system.android.systemtool.installer.R;
import com.system.android.systemtool.installer.SystemToolApp;
import com.system.android.systemtool.installer.SystemToolBaseActivity;

public final class ThemeUtil {
	private static int[] THEMES = new int[] {
			R.style.Theme_SystemToolInstaller_Light,
			R.style.Theme_SystemToolInstaller_Dark,
			R.style.Theme_SystemToolInstaller_Dark_Black, };

	private ThemeUtil() {
	}

	public static int getSelectTheme() {
		int theme = SystemToolApp.getPreferences().getInt("theme", 0);
		return (theme >= 0 && theme < THEMES.length) ? theme : 0;
	}

	public static void setTheme(SystemToolBaseActivity activity) {
		activity.mTheme = getSelectTheme();
		activity.setTheme(THEMES[activity.mTheme]);
	}

	public static void reloadTheme(SystemToolBaseActivity activity) {
		int theme = getSelectTheme();
		if (theme != activity.mTheme)
			activity.recreate();
    }

    public static int getThemeColor(Context context, int id) {
        Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[] { id });
		int result = a.getColor(0, 0);
		a.recycle();
		return result;
	}
}
