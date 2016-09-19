package com.attribution.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

    private TextView mLocationText;
    private TextView mNumberText;

    private String mNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        String location = getIntent().getStringExtra("location");
        String operator = getIntent().getStringExtra("operator");
        mNumber = getIntent().getStringExtra("number");

        mLocationText = (TextView) findViewById(R.id.location);
        mNumberText = (TextView) findViewById(R.id.number);

        mLocationText.setText(location + operator);
        mNumberText.setText(mNumber);

        getSupportActionBar().setTitle(mNumber);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.call:
                intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + mNumber));
                startActivity(intent);

                break;
            case R.id.sms:
                Uri smsToUri = Uri.parse("smsto:" + mNumber);
                intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                intent.putExtra("sms_body", "");
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
