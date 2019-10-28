/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpackingapp.vietnam.travel.R;
import com.google.firebase.database.DatabaseError;

import tip.tdc.com.tip.activity.login.LoginActivity;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;

/**
 * Created by thanh on 19/7/2017.
 */
public class DialogRate {

    private Context mContext;
    public static int TYPE_LIKE = 0;
    public static int TYPE_LOVE = 1;


    public DialogRate() {
    }

    /**
     * show dialog
     */
    private Dialog dialogNetwork = null;

    TextView dialogMessage;
    private Button buttonNo;
    private Button buttonRate;
    ImageView imvIcon;

    public DialogRate buildDialog(final int type, final Context mContext, final DialogCallback dialogCallback) {

        this.mContext = mContext;

        try {
            dialogNetwork = new Dialog(mContext, R.style.tt_dialog);
            dialogNetwork.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogNetwork.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialogNetwork.setContentView(R.layout.dialog_rate_app_like);

            FontUtils.setFont(dialogNetwork.findViewById(R.id.buttonNo));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogTitle));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogMessage));

            imvIcon = (ImageView) dialogNetwork.findViewById(R.id.imvIcon);


            dialogMessage = (TextView) dialogNetwork.findViewById(R.id.dialogMessage);

            buttonRate = (Button) dialogNetwork.findViewById(R.id.buttonRate);
            buttonRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogNetwork.dismiss();
                    if (type == TYPE_LIKE) {
                        DialogRate dialogRate = new DialogRate();
                        dialogRate.buildDialog(TYPE_LOVE, mContext, null);
                        dialogRate.show();
                    } else {
                        Utils.openAppInStore(mContext, mContext.getPackageName());
                        AppSetting.getInstant(mContext).setAppRated(true);
                    }
                }
            });

            buttonNo = (Button) dialogNetwork.findViewById(R.id.buttonNo);
            buttonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogNetwork.dismiss();
                }
            });

            if (type == TYPE_LIKE) {
                dialogMessage.setText("Are you enjoying Bpacking app?");
                imvIcon.setImageResource(R.drawable.ico_smile_3x);
            } else {
                dialogMessage.setText("Nice! Do you mind leaving a review?\nIt will help other travellers find the app :)");
                imvIcon.setImageResource(R.drawable.ico_love_3x);
                buttonRate.setText("Sure:)");
            }


        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }

        return this;
    }

    public DialogRate setCancelButtonTitle(String cancelBtnTitle) {
        buttonNo.setText(cancelBtnTitle);
        return this;
    }

    public DialogRate setOkButtonTitle(String okBtnTitle) {
        buttonRate.setText(okBtnTitle);
        return this;
    }


    public DialogRate setDialogMessage(String dialogMessageStr) {
        dialogMessage.setText(dialogMessageStr);
        return this;
    }

    public void show() {
        if (dialogNetwork != null) {
            dialogNetwork.show();
        } else {
            LoggerFactory.e("Dialog Empty");
        }
    }
}
