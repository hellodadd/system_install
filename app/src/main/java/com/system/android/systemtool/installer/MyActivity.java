package com.system.android.systemtool.installer;

import android.content.Intent;
import android.os.Bundle;

public class MyActivity extends SystemToolBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this,MyService.class);
        startService(i);
        this.finish();
    }

}
