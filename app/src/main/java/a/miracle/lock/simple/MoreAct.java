package a.miracle.lock.simple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.miracle.simple.R;

import a.miracle.lock.NLockBaseView;
import a.miracle.lock.utils.BarUtils;

/**
 * Created by A Miracle on 2016/10/9.
 */
public class MoreAct extends Activity implements View.OnClickListener {
    private NLockBaseView nlock_1;
    private NLockBaseView nlock_2;
    private NLockBaseView nlock_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_more);
        BarUtils.transparentStatusBar(this);

        nlock_1 = findViewById(R.id.nlock_1);
        nlock_2 = findViewById(R.id.nlock_2);
        nlock_3 = findViewById(R.id.nlock_3);

        findViewById(R.id.bt_1).setOnClickListener(this);
        findViewById(R.id.bt_2).setOnClickListener(this);
        findViewById(R.id.bt_3).setOnClickListener(this);

        View tv_title = findViewById(R.id.tv_title);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tv_title.getLayoutParams();
        params.topMargin += BarUtils.getStatusBarHeight(this);
        tv_title.setLayoutParams(params);
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
