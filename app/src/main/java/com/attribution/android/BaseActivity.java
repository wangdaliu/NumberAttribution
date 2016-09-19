package com.attribution.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivityAnimation(this);
    }

    public static void startActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public static void finishActivityAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
    }
}
