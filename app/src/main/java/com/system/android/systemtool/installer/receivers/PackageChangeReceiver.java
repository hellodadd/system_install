package com.system.android.systemtool.installer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.system.android.systemtool.installer.util.ModuleUtil;
import com.system.android.systemtool.installer.util.ModuleUtil.InstalledModule;
import com.system.android.systemtool.installer.util.NotificationUtil;

public class PackageChangeReceiver extends BroadcastReceiver {
    private final static ModuleUtil mModuleUtil = ModuleUtil.getInstance();

    private static String getPackageName(Intent intent) {
        Uri uri = intent.getData();
        return (uri != null) ? uri.getSchemeSpecificPart() : null;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)
                && intent.getBooleanExtra(Intent.EXTRA_REPLACING, false))
            // Ignore existing packages being removed in order to be updated
            return;

        String packageName = getPackageName(intent);
        Log.e("zwb", "onReceive packageName = " + packageName);
        if (packageName == null)
            return;

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
            // make sure that the change is for the complete package, not only a
            // component
            String[] components = intent.getStringArrayExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
            Log.e("zwb", "onReceive components = " + components.toString());
            if (components != null) {
                boolean isForPackage = false;
                for (String component : components) {
                    if (packageName.equals(component)) {
                        isForPackage = true;
                        break;
                    }
                }
                if (!isForPackage)
                    return;
            }
        }

        InstalledModule module = ModuleUtil.getInstance().reloadSingleModule(packageName);
        Log.e("zwb", "onReceive module = " + module);
        if (module == null
                || intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            // Package being removed, disable it if it was a previously active
            // SystemTool mod
            if (mModuleUtil.isModuleEnabled(packageName)) {
                Log.e("zwb", " isModuleEnabled 1111");
                mModuleUtil.setModuleEnabled(packageName, false);
                mModuleUtil.updateModulesList(false);
            }
            return;
        }

        if (mModuleUtil.isModuleEnabled(packageName)) {
            Log.e("zwb", " isModuleEnabled 22222");
            mModuleUtil.updateModulesList(false);
            NotificationUtil.showModulesUpdatedNotification();
        } else {
            NotificationUtil.showNotActivatedNotification(packageName, module.getAppName());
        }
    }
}
