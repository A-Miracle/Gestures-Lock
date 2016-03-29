package com.ctao.gestureslock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ctao.gestureslock.widget.NLockPatterBaseView;

/**
 * Created by A Miracle on 2016/3/29.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    NLockPatterBaseView nlock_1;
    NLockPatterBaseView nlock_2;
    NLockPatterBaseView nlock_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nlock_1 = (NLockPatterBaseView) findViewById(R.id.nlock_1);
        nlock_2 = (NLockPatterBaseView) findViewById(R.id.nlock_2);
        nlock_3 = (NLockPatterBaseView) findViewById(R.id.nlock_3);

        findViewById(R.id.bt_1).setOnClickListener(this);
        findViewById(R.id.bt_2).setOnClickListener(this);
        findViewById(R.id.bt_3).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_1:
                nlock_1.setVisibility(View.VISIBLE);
                nlock_1.clearPassword(0);
                nlock_2.setVisibility(View.GONE);
                nlock_3.setVisibility(View.GONE);
                break;
            case R.id.bt_2:
                nlock_1.setVisibility(View.GONE);
                nlock_2.setVisibility(View.VISIBLE);
                nlock_2.clearPassword(0);
                nlock_3.setVisibility(View.GONE);
                break;
            case R.id.bt_3:
                nlock_1.setVisibility(View.GONE);
                nlock_2.setVisibility(View.GONE);
                nlock_3.setVisibility(View.VISIBLE);
                nlock_3.clearPassword(0);
                break;
        }
    }
}
