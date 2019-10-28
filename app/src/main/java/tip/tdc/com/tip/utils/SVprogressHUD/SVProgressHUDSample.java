/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.utils.SVprogressHUD;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.bpackingapp.vietnam.travel.R;

public class SVProgressHUDSample extends Activity {
    private SVProgressHUD mSVProgressHUD;
    int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSVProgressHUD = new SVProgressHUD(this);
    }

    public void show(View view){
        mSVProgressHUD.show();
    }
    public void showWithMaskType(View view){
        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.None);
//        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Black)
//        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.BlackCancel);
//        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
//        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
//        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Gradient);
//        mSVProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
    }
    public void showWithStatus(View view){
        mSVProgressHUD.showWithStatus("showWithStatus...");
    }
    public void showInfoWithStatus(View view){
        mSVProgressHUD.showInfoWithStatus("showInfoWithStatus", SVProgressHUD.SVProgressHUDMaskType.None);
    }
    public void showSuccessWithStatus(View view){
        mSVProgressHUD.showSuccessWithStatus("showSuccessWithStatus！");
    }
    public void showErrorWithStatus(View view){
        mSVProgressHUD.showErrorWithStatus("showErrorWithStatus", SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress = progress + 5;
            if (mSVProgressHUD.getProgressBar().getMax() != mSVProgressHUD.getProgressBar().getProgress()) {
                mSVProgressHUD.getProgressBar().setProgress(progress);
                mSVProgressHUD.setText("progress "+progress+"%");

                mHandler.sendEmptyMessageDelayed(0,500);
            }
            else{
                mSVProgressHUD.dismiss();
            }

        }
    };
    public void showWithProgress(View view){
        progress = 0;
        mSVProgressHUD.getProgressBar().setProgress(progress);
        mSVProgressHUD.showWithProgress("progress " + progress + "%", SVProgressHUD.SVProgressHUDMaskType.Black);
        mHandler.sendEmptyMessageDelayed(0,500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if(mSVProgressHUD.isShowing()){
                mSVProgressHUD.dismiss();
                mHandler.removeCallbacksAndMessages(null);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
