package com.attribution.android;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectNumberActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SELECT_TYPE = "SELECT_TYPE";
    private ListView mListView;
    private SelectAdapter mAdapter;
    private static final int CONTACT_NUMBER_LOADER = 1;
    private static final int CALLLOG_NUMBER_LOADER = 2;
    private int mSelectType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_layout);

        mSelectType = getIntent().getIntExtra(SELECT_TYPE, 0);

        getSupportActionBar().setTitle(mSelectType == 0 ? R.string.select_contact : R.string.select_calllog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new SelectAdapter(SelectNumberActivity.this, R.layout.select_item, null);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String number;
                if (mSelectType == 0) {
                    number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                } else {
                    number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                }
                number = number.replaceAll(" ", "").replace("-", "");
                Intent intent = new Intent();

                intent.putExtra("NUMBER", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getLoaderManager().initLoader(mSelectType == 0 ? CONTACT_NUMBER_LOADER : CALLLOG_NUMBER_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (id) {
            case CONTACT_NUMBER_LOADER:
                uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                break;
            case CALLLOG_NUMBER_LOADER:
                uri = CallLog.Calls.CONTENT_URI;
                break;
        }

        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private class SelectAdapter extends ResourceCursorAdapter {

        public SelectAdapter(Context context, int layout, Cursor c) {
            super(context, layout, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = super.newView(context, cursor, parent);
            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.textview);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (mSelectType == 0) {
                final String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                holder.textView.setText(name + " : " + number);
            } else {
                final String callLogNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                final int callLogType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String type = "";
                switch (callLogType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        type = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        type = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        type = "未接";
                        break;
                }
                holder.textView.setText(type + " : " + callLogNumber);
            }
        }
    }

    private final class ViewHolder {
        TextView textView;
    }
}
