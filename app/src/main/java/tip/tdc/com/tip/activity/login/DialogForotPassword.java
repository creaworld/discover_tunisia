package tip.tdc.com.tip.activity.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by thanh on 19/7/2017.
 */
public class DialogForotPassword {

    private Context mContext;


    public DialogForotPassword() {
    }

    /**
     * show dialog
     */
    private Dialog dialogNetwork = null;

    TextView dialogTitle;
    TextView dialogMessage;
    View VlineButton;

    private Button buttonOke;

    AutoCompleteTextView signupEmail;

    public DialogForotPassword buildDialog(final Context mContext, final DialogCallback dialogCallback) {

        this.mContext = mContext;

        try {
            dialogNetwork = new Dialog(mContext, R.style.tt_dialog);
            dialogNetwork.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogNetwork.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialogNetwork.setContentView(R.layout.dialog_forgot_password);

            FontUtils.setFont(dialogNetwork.findViewById(R.id.buttonOke));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogTitle));
//            FontUtils.setFont(dialogNetwork.findViewById(R.id.buttonCancel));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogMessage));


            dialogTitle = (TextView) dialogNetwork.findViewById(R.id.dialogTitle);
            dialogMessage = (TextView) dialogNetwork.findViewById(R.id.dialogMessage);
            signupEmail = (AutoCompleteTextView) dialogNetwork.findViewById(R.id.signupEmail);


            buttonOke = (Button) dialogNetwork.findViewById(R.id.buttonOke);

            buttonOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dialogCallback != null) {
                        dialogCallback.OnOkSelected();
                    }

                    if (signupEmail.getText().toString().trim().length() == 0 || !signupEmail.getText().toString().contains("@")) {
                        Toast.makeText(mContext, "Email invalid", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseAuth.getInstance().sendPasswordResetEmail(signupEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        LoggerFactory.d("Email sent.");
                                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    dialogNetwork.dismiss();
                }
            });

        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }

        return this;
    }

    public DialogForotPassword setOkButtonTitle(String okBtnTitle) {
        buttonOke.setText(okBtnTitle);
        return this;
    }

    public DialogForotPassword setDialogTitle(String dialogTitleStr) {
        if (dialogTitleStr == null) {
            dialogTitle.setVisibility(View.GONE);
            VlineButton.setVisibility(View.GONE);
            return this;
        }

        dialogTitle.setText(dialogTitleStr);
        return this;
    }

    public DialogForotPassword setDialogMessage(String dialogMessageStr) {
        dialogMessage.setText(dialogMessageStr);
        return this;
    }

    public void show() {
        if (dialogNetwork != null) {
            if (mContext != null && mContext instanceof Activity && !((Activity) mContext).isFinishing())
                dialogNetwork.show();
        } else {
            LoggerFactory.e("Dialog Empty");
        }
    }
}
