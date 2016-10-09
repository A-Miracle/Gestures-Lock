package com.ctao.gestureslock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by A Miracle on 2016/3/29.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private View bt_set, bt_edit, bt_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_set = findViewById(R.id.bt_set);
        bt_edit = findViewById(R.id.bt_edit);
        bt_check = findViewById(R.id.bt_check);

        bt_set.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_check.setOnClickListener(this);

        findViewById(R.id.bt_history).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = WApplication.getApplication().getSp();
        String password = sp.getString(Constants.NLOCK_PASSWORD, "");

        if(TextUtils.isEmpty(password)){
            bt_set.setVisibility(View.VISIBLE);

            bt_edit.setVisibility(View.GONE);
            bt_check.setVisibility(View.GONE);
        }else{
            bt_set.setVisibility(View.GONE);

            bt_edit.setVisibility(View.VISIBLE);
            bt_check.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_history){
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            return;
        }
        Intent intent = new Intent(this, NLockActivity.class);
        switch (v.getId()){
            case R.id.bt_set: //设置
                intent.putExtra(NLockActivity.PARAMETER_TYPE, NLockActivity.TYPE_SETUP);
                break;
            case R.id.bt_edit: //修改
                intent.putExtra(NLockActivity.PARAMETER_TYPE, NLockActivity.TYPE_MODIFY);
                break;
            case R.id.bt_check: //校验
                intent.putExtra(NLockActivity.PARAMETER_TYPE, NLockActivity.TYPE_CHECK);
                break;
        }
        startActivity(intent);
    }
}
