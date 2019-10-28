package tip.tdc.com.tip.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by phamngocthanh on 7/19/17.
 */

public class ColorUtils {
    public static int getColor(Context mContext, int colorId) {
        if(Build.VERSION.SDK_INT>=25){
            return mContext.getColor(colorId);
        } else {
            return  mContext.getResources().getColor(colorId);
        }
    }
}
