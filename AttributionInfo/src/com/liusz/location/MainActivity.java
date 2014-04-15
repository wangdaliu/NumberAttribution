package com.liusz.location;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.snail.attribution.AttributionManager;

public class MainActivity extends Activity {

    String number = "";
    TextView txtView;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btn = (Button) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.input);
        txtView = (TextView) findViewById(R.id.result);


        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                number = editText.getText().toString();
                String location = AttributionManager.getAttributionInfo(number, getApplicationContext());
                txtView.setText(TextUtils.isEmpty(location) ? "null" : location);
            }
        });
    }
}
