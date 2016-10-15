package com.example.hujiahong.hujiahong;

import android.content.Context;
import android.os.Bundle;

import nf.framework.act.AbsBaseActivity;
import nf.framework.core.util.android.CloseActivityClass;
import nf.framework.expand.dialog.AbsBaseDialog;
import nf.framework.expand.dialog.BaseDialog;
import utils.TransitionUtility;

/**
 * 基类,activity都继承它方便类的统一处理
 * Created by hujiahong on 16/6/23.
 */
public class MyBaseActivity extends AbsBaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityClass.activityList.add(this);

    }
    public static void showInfoDialog(Context context, String message) {

        BaseDialog dialog = new BaseDialog(context,
                AbsBaseDialog.DIALOG_BUTTON_STYLE_ONE);
        dialog.show();
        dialog.setBtnName("确定", null, null);
        dialog.setTitle(R.drawable.expand_dialog_login_logo, "提示");
        dialog.setContent(message);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        TransitionUtility.LeftPushInTrans(this);
    }
}
