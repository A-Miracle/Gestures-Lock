package com.ctao.gestureslock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ctao.gestureslock.widget.NLockPatterBaseView;
import com.ctao.gestureslock.widget.NLockSmallView;
import com.ctao.gestureslock.widget.NLockView;

/**
 * Created by A Miracle on 2016/8/18.
 */
public class NLockActivity extends Activity{

    public static final String PARAMETER_TYPE = "TYPE";
    private final static int TOTAL_COUNT = 5;

    public static final int TYPE_SETUP = 0x11; //设置
    public static final int TYPE_MODIFY = 0x12; //修改
    public static final int TYPE_CHECK = 0x13; //校验

    private NLockSmallView nlock_small_view;
    private NLockView nlock_view;
    private TextView tv_hint;
    private Button bt_forget_password;

    private SharedPreferences sp;
    private int type;

    //设置密码用到成员变量
    private int repeatCount;

    private String tmpPwd;
    //修改, 校验用到成员变量
    private String mPwd;
    private int errorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nlock);

        initData();

        initView();

        initTypeView();

        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getIntExtra(PARAMETER_TYPE, -1);
        }
        sp = WApplication.getApplication().getSp();
        mPwd = sp.getString(Constants.NLOCK_PASSWORD, "");
        errorCount = sp.getInt(Constants.NLOCK_ERRORCOUNT, 0);
    }

    private void initView() {
        nlock_small_view = (NLockSmallView) findViewById(R.id.nlock_small_view);
        nlock_view = (NLockView) findViewById(R.id.nlock_view);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        bt_forget_password = (Button) findViewById(R.id.bt_forget_password);
    }

    private void initTypeView() {
        switch (type){
            case TYPE_SETUP:

                break;
            case TYPE_MODIFY:
                tv_hint.setText("绘制原解锁图案");
                break;
            case TYPE_CHECK:
                nlock_small_view.setVisibility(View.GONE);
                break;
        }
    }

    private void initListener() {
        nlock_view.setOnCompleteListener(new NLockPatterBaseView.OnCompleteListener() {
            @Override
            public void onComplete(String password) {
                switch (type){
                    case TYPE_SETUP:
                        setUp(password);
                        break;
                    case TYPE_MODIFY:
                        modify(password);
                        break;
                    case TYPE_CHECK:
                        check(password);
                        break;
                }
            }

            @Override
            public void onShortHint() {
                tv_hint.setTextColor(Color.parseColor("#ff3200"));
                tv_hint.setText("至少连接4个点");
            }
        });
    }

    private void check(String password) {
        if(password.equals(mPwd)){
            errorCount = 0;
            // 校验成功
            Toast.makeText(this, "解锁成功", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            errorCount++;
            if(errorCount < TOTAL_COUNT){
                tv_hint.setTextColor(Color.parseColor("#ff3200"));
                tv_hint.setText("密码错误, 还有" + (TOTAL_COUNT - errorCount) + "次机会");
                nlock_view.markError();
            }else{
                // 错误5次, 清掉关闭
                errorCount = 0;
                sp.edit().putString(Constants.NLOCK_PASSWORD, "").commit();
                Toast.makeText(this, "手势密码错误5次", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void modify(String password) {
        if(password.equals(mPwd)){
            errorCount = 0;

            // 原密码正确, 重置密码
            type = TYPE_SETUP;
            nlock_view.clearPassword();
            tv_hint.setTextColor(Color.parseColor("#f8f8f8"));
            tv_hint.setText("绘制新解锁图案");
            initTypeView();
        }else{
            errorCount++;
            if(errorCount < TOTAL_COUNT){
                tv_hint.setTextColor(Color.parseColor("#ff3200"));
                tv_hint.setText("密码错误, 还有" + (TOTAL_COUNT - errorCount) + "次机会");
                nlock_view.markError();
            }else{
                // 原密码错误5次, 清掉关闭
                errorCount = 0;
                sp.edit().putString(Constants.NLOCK_PASSWORD, "").commit();
                Toast.makeText(this, "手势密码错误5次", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setUp(String password) {
        if(repeatCount == 0){
            repeatCount++;

            nlock_small_view.setPassword(password); //小图预览
            tv_hint.setText("确认解锁图案");
            tmpPwd = password;
            nlock_view.clearPassword();
        }else{
            if(password.equals(tmpPwd)){
                // 保存密码, 关闭(加密已忽略, 实际项目中请自行加密)
                sp.edit().putString(Constants.NLOCK_PASSWORD, password).commit();
                finish();
            }else{
                //提示两次密码不一样
                tv_hint.setTextColor(Color.parseColor("#ff3200"));
                tv_hint.setText("与上次绘制图案不一致");
                nlock_view.markError();
            }
        }
    }

    @Override
    public void finish() {
        sp.edit().putInt(Constants.NLOCK_ERRORCOUNT, errorCount).commit();
        super.finish();
    }
}
