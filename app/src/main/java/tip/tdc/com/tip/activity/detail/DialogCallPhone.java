/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import tip.tdc.com.tip.activity.login.LoginActivity;
import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;

import com.bpackingapp.vietnam.travel.R;

/**
 * Created by thanh on 19/7/2017.
 */
public class DialogCallPhone {

    private Context mContext;


    public DialogCallPhone() {
    }

    /**
     * show dialog
     */
    private Dialog dialogNetwork = null;

    TextView dialogTitle;
    View VlineButton;

    private Button buttonOke;
    PlaceService placeService;
    RelativeLayout rlView1, rlView2, rlView3, rlView4;
    Button btn1, btn2, btn3, btn4;


    public DialogCallPhone buildDialog(final String phoneNumber, final Context mContext, final DialogCallback dialogCallback) {

        this.mContext = mContext;

        try {
            dialogNetwork = new Dialog(mContext, R.style.tt_dialog);
            dialogNetwork.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogNetwork.setContentView(R.layout.dialog_call_phone);
            dialogNetwork.setCanceledOnTouchOutside(true);

            FontUtils.setFont(dialogNetwork.findViewById(R.id.buttonOke));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogTitle));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.btn1));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.btn2));


            dialogTitle = (TextView) dialogNetwork.findViewById(R.id.dialogTitle);
            rlView1 = (RelativeLayout) dialogNetwork.findViewById(R.id.rlView1);
            rlView2 = (RelativeLayout) dialogNetwork.findViewById(R.id.rlView2);
            rlView3 = (RelativeLayout) dialogNetwork.findViewById(R.id.rlView3);
            rlView4 = (RelativeLayout) dialogNetwork.findViewById(R.id.rlView4);

            btn1 = (Button) dialogNetwork.findViewById(R.id.btn1);
            btn2 = (Button) dialogNetwork.findViewById(R.id.btn2);
            btn3 = (Button) dialogNetwork.findViewById(R.id.btn3);
            btn4 = (Button) dialogNetwork.findViewById(R.id.btn4);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.callPhoneNumber(mContext, btn1.getText().toString().trim().replace(" ", ""));
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.callPhoneNumber(mContext, btn2.getText().toString().trim().replace(" ", ""));
                }
            });
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.callPhoneNumber(mContext, btn3.getText().toString().trim().replace(" ", ""));
                }
            });
            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.callPhoneNumber(mContext, btn4.getText().toString().trim().replace(" ", ""));
                }
            });

            buttonOke = (Button) dialogNetwork.findViewById(R.id.buttonOke);
            buttonOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogNetwork.dismiss();
                    if (dialogCallback != null) {
                        dialogCallback.OnOkSelected();
                    }

                }
            });

            String[] listPhone = phoneNumber.split("/");
            int phoneIndex = 0;
            for (int i = 0; i < listPhone.length; i++) {
                if (listPhone[i].length() > 0) {
                    if (phoneIndex == 0) {
                        btn1.setText(listPhone[i]);
                        rlView1.setVisibility(View.VISIBLE);
                        phoneIndex++;
                    } else if (phoneIndex == 1) {
                        btn2.setText(listPhone[i]);
                        rlView2.setVisibility(View.VISIBLE);
                        phoneIndex++;
                    } else if (phoneIndex == 2) {
                        btn3.setText(listPhone[i]);
                        rlView3.setVisibility(View.VISIBLE);
                        phoneIndex++;
                    } else if (phoneIndex == 3) {
                        btn4.setText(listPhone[i]);
                        rlView4.setVisibility(View.VISIBLE);
                        phoneIndex++;
                    }
                }
            }


        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }

        return this;
    }

    public DialogCallPhone setCancelButtonTitle(String cancelBtnTitle) {
        return this;
    }

    public DialogCallPhone setOkButtonTitle(String okBtnTitle) {
        buttonOke.setText(okBtnTitle);
        return this;
    }

    public DialogCallPhone setDialogTitle(String dialogTitleStr) {
        if (dialogTitleStr == null) {
            dialogTitle.setVisibility(View.GONE);
            VlineButton.setVisibility(View.GONE);
            return this;
        }

        dialogTitle.setText(dialogTitleStr);
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
