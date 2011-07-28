package com.openfeint.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class YambaActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        Log.i("ddd", "-abc-------");
    }
}