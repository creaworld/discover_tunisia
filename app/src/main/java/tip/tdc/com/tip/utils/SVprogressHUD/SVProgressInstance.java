/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.utils.SVprogressHUD;

import android.content.Context;

public class SVProgressInstance {
    public static SVProgressHUD show(Context mContext){
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.show();
            return mSVProgressHUD;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SVProgressHUD showWithMaskType(Context mContext, SVProgressHUD.SVProgressHUDMaskType maskType) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showWithMaskType(maskType);
            return mSVProgressHUD;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static SVProgressHUD showWithStatus(Context mContext, String string) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showWithStatus(string);
            return mSVProgressHUD;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SVProgressHUD showWithStatus(Context mContext, String string, SVProgressHUD.SVProgressHUDMaskType maskType) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showWithMaskType(maskType);
            return mSVProgressHUD;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SVProgressHUD showInfoWithStatus(Context mContext, String string) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showInfoWithStatus(string);
            return mSVProgressHUD;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showInfoWithStatus(Context mContext, String string, SVProgressHUD.SVProgressHUDMaskType maskType) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showInfoWithStatus(string, maskType);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessWithStatus(Context mContext, String string) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showSuccessWithStatus(string);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessWithStatus(Context mContext, int stringId) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showSuccessWithStatus(mContext.getString(stringId));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessWithStatus(Context mContext, String string, SVProgressHUD.SVProgressHUDMaskType maskType) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showSuccessWithStatus(string, maskType);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorWithStatus(Context mContext, String string) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showErrorWithStatus(string);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showErrorWithStatus(Context mContext, int stringId) {
        showErrorWithStatus(mContext, mContext.getString(stringId));
    }

    public static void showErrorWithStatus(Context mContext, String string, SVProgressHUD.SVProgressHUDMaskType maskType) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showErrorWithStatus(string, maskType);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SVProgressHUD showWithProgress(Context mContext, String string, SVProgressHUD.SVProgressHUDMaskType maskType) {
        try {
            SVProgressHUD mSVProgressHUD = new SVProgressHUD(mContext);
            mSVProgressHUD.showWithProgress(string, maskType);
            return mSVProgressHUD;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
