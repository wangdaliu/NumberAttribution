package com.attribution.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.snail.attribution.AttributionManager;
import com.wandoujia.ads.sdk.Ads;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private String number = "";
    private EditText mEditText;


    private static final String APP_ID = "100043918";
    private static final String SECRET_KEY = "46bf8ddf9c54a45e4e3f032778e3f612";
    private static final String BANNER = "8b9263ce3a6319ac45df5e46d5339043";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.search);
        mEditText = (EditText) findViewById(R.id.input);

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                number = mEditText.getText().toString();
                String location = AttributionManager.getAttributionInfo(number, MainActivity.this);

                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(MainActivity.this, "号码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                String operator = validate(number);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("operator", operator);
                intent.putExtra("number", number);
                startActivity(intent);
                startActivityAnimation(MainActivity.this);
            }
        });

        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Ads.init(MainActivity.this, APP_ID, SECRET_KEY);
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "error", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                Log.e(TAG, "result " + success);
                if (success) {
                    /**
                     * pre load
                     */
                    Ads.preLoad(BANNER, Ads.AdFormat.banner);

                    /**
                     * add ad views
                     */
                    View bannerView = Ads.createBannerView(MainActivity.this, BANNER);
                    FrameLayout banner = (FrameLayout) findViewById(R.id.banner);
                    banner.addView(bannerView, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                }
            }
        }.execute();
    }

    private String validate(String number) {
        String telcomPatternY = "^134[0-8]\\d{7}$|^(?:13[5-9]|147|15[0-27-9]|178|18[2-478])\\d{8}$";
        String telcomPatternU = "^(?:13[0-2]|145|15[56]|176|18[56])\\d{8}";
        String telcomPatternD = "^(?:133|153|177|18[019])\\d{8}$";
        if (number.matches(telcomPatternY)) {
            return "移动";
        } else if (number.matches(telcomPatternU)) {
            return "联通";
        } else if (number.matches(telcomPatternD)) {
            return "电信";
        } else {
            return "";
        }
    }

}
