package a.miracle.lock.simple;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.miracle.simple.R;

import a.miracle.lock.utils.BarUtils;

/**
 * Created by A Miracle on 2016/3/29.
 */
public class MainAct extends Activity implements View.OnClickListener {

    private View bt_set, bt_edit, bt_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        BarUtils.transparentStatusBar(this);

        bt_set = findViewById(R.id.bt_set);
        bt_edit = findViewById(R.id.bt_edit);
        bt_check = findViewById(R.id.bt_check);

        bt_set.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_check.setOnClickListener(this);

        View bt_history = findViewById(R.id.bt_history);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bt_history.getLayoutParams();
        params.topMargin += BarUtils.getStatusBarHeight(this);
        bt_history.setLayoutParams(params);

        bt_history.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = App.get().getSp();
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
            startActivity(new Intent(getApplicationContext(), MoreAct.class));
            return;
        }
        Intent intent = new Intent(this, NLockAct.class);
        switch (v.getId()){
            case R.id.bt_set: //设置
                intent.putExtra(NLockAct.PARAMETER_TYPE, NLockAct.TYPE_SETUP);
                break;
            case R.id.bt_edit: //修改
                intent.putExtra(NLockAct.PARAMETER_TYPE, NLockAct.TYPE_MODIFY);
                break;
            case R.id.bt_check: //校验
                intent.putExtra(NLockAct.PARAMETER_TYPE, NLockAct.TYPE_CHECK);
                break;
        }
        startActivity(intent);
    }
}
