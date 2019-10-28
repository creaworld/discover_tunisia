package tip.tdc.com.tip.activity.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;

import tip.tdc.com.tip.activity.login.LoginActivity;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.interfaces.DialogCallback;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.PlaceService;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by thanh on 19/7/2017.
 */
public class DialogTip {

    private Context mContext;


    public DialogTip() {
    }

    /**
     * show dialog
     */
    private Dialog dialogNetwork = null;

    TextView dialogTitle;
    EditText dialogMessage;
    View VlineButton;

    private Button buttonOke;
    private Button buttonCancel;
    PlaceService placeService;
    FCity fCity;

    public void setfCity(FCity fCity) {
        this.fCity = fCity;
    }

    public DialogTip buildDialog(final String objectKey, final Context mContext, final DialogCallback dialogCallback) {

        this.mContext = mContext;

        try {
            dialogNetwork = new Dialog(mContext, R.style.tt_dialog);
            dialogNetwork.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogNetwork.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialogNetwork.setContentView(R.layout.dialog_tip);

            FontUtils.setFont(dialogNetwork.findViewById(R.id.buttonOke));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogTitle));
            FontUtils.setFont(dialogNetwork.findViewById(R.id.dialogMessage));


            dialogTitle = (TextView) dialogNetwork.findViewById(R.id.dialogTitle);
            dialogMessage = (EditText) dialogNetwork.findViewById(R.id.dialogMessage);

            buttonOke = (Button) dialogNetwork.findViewById(R.id.buttonOke);
            buttonOke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogNetwork.dismiss();
                    if (dialogCallback != null) {
                        dialogCallback.OnOkSelected();
                    }

                    if (dialogMessage.getText().toString().length() > 0) {
                        placeService.addPlaceTip(dialogMessage.getText().toString(), mContext, fCity);
                    } else {

                    }
                }
            });

            placeService = new PlaceService(objectKey);
            placeService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {

                }

                @Override
                public void onDataChanged() {

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }

        return this;
    }

    public DialogTip setCancelButtonTitle(String cancelBtnTitle) {
        buttonCancel.setText(cancelBtnTitle);
        return this;
    }

    public DialogTip setOkButtonTitle(String okBtnTitle) {
        buttonOke.setText(okBtnTitle);
        return this;
    }

    public DialogTip setDialogTitle(String dialogTitleStr) {
        if(dialogTitleStr == null ) {
            dialogTitle.setVisibility(View.GONE);
            VlineButton.setVisibility(View.GONE);
            return this;
        }

        dialogTitle.setText(dialogTitleStr);
        return this;
    }

    public DialogTip setDialogMessage(String dialogMessageStr) {
        dialogMessage.setText(dialogMessageStr);
        return this;
    }

    public void show() {
        if(AppState.currentFireUser != null && AppState.currentFireUser.getEmail()!= null) {
            if (dialogNetwork != null) {
                    dialogNetwork.show();
            } else {
                LoggerFactory.e("Dialog Empty");
            }
        } else {
            mContext.startActivity(LoginActivity.createIntent(mContext));
            ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
        }
    }
}
