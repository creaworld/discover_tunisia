package tip.tdc.com.tip.utils;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

public class LoggerFactory {
    public static final int BUILD_TINTIN = 1;
    public static final int BUILD_TEST = 2;
    public static final int BUILD_BKAV = 4;
    public static final int BUILD_RELEASE = 5;

    public static int BUILD_VERSION = BUILD_TEST;

    public static String TAG = "TinTin";
    public static boolean isDebuggable = true;
//	public static boolean isDebuggable = false;

    private final static int TYPE_D = 0;
    private final static int TYPE_I = 1;
    private final static int TYPE_W = 2;
    private final static int TYPE_E = 3;
    private final static int TYPE_A = 4;

    public static void init(final Context context) {
//        if (AppConstant.BUILD_VERSION == AppConstant.BUILD_BKAV) {
//            isDebuggable = false;
//        } else if (AppConstant.BUILD_VERSION == AppConstant.BUILD_RELEASE) {
//            isDebuggable = false;
//        } else if (AppConstant.BUILD_VERSION == AppConstant.BUILD_TINTIN) {
//            isDebuggable = (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
//        }

        TAG = context.getApplicationInfo().packageName;
    }

    private static void finalLog(String TAG, String message, int type) {
        if(message == null)
            return;

        switch (type) {
            case TYPE_D:
                Log.d(TAG, message);
                break;
            case TYPE_I:
                Log.i(TAG, message);
                break;
            case TYPE_W:
                Log.w(TAG, message);
                break;
            case TYPE_E:
                Log.e(TAG, message);
                break;
//			case TYPE_A:
//				Log.a(TAG, message);
        }

    }

    public static void d(final String tag, final String message) {
        if (!isDebuggable)
            return;

        finalLog(tag, message, TYPE_D);
    }

    public static void d(final String message) {
        if (!isDebuggable)
            return;
        if (message != null && message.length() > 0)
            finalLog(TAG, message, TYPE_D);
    }

    public static void d(final String tag, String message, Object... objects) {
        if (!isDebuggable)
            return;
        for (Object object : objects) {
            message = message.replaceFirst("\\{\\}", String.valueOf(object));
        }

        if (message != null && message.length() > 0)
            finalLog(tag, message, TYPE_D);
    }

    public static void e(final String tag, final String message) {
        if (!isDebuggable)
            return;
        finalLog(tag, message, TYPE_E);
    }

    public static void e(final String message) {
        if (!isDebuggable)
            return;
        if (message != null && message.length() > 0)
            finalLog(TAG, message, TYPE_E);
    }

    public static void error(final String message) {
        if (!isDebuggable)
            return;
        if (message != null && message.length() > 0)
            finalLog(TAG, message, TYPE_E);
    }

    public static void i(final String tag, final String message) {
        if (!isDebuggable)
            return;
        finalLog(tag, message, TYPE_I);
    }

    public static void i(final String message) {
        if (!isDebuggable)
            return;
        if (message != null && message.length() > 0)
            finalLog(TAG, message, TYPE_I);
    }

    public static void info(final String message) {
        if (!isDebuggable)
            return;
        if (message != null && message.length() > 0)
            finalLog(TAG, message, TYPE_I);
    }

    public static void warn(final String message) {
        if (!isDebuggable)
            return;
        if (message != null && message.length() > 0)
            Log.w(TAG, message);
    }

    public static void logStackTrace(Throwable e) {
        try {
            if (isDebuggable)
                e.printStackTrace();

            Crashlytics.logException(e);
        } catch (Exception ex) {

        }
    }

    public static void logStackTrace(String TAG, Throwable e) {
        try {
            if (isDebuggable) {
                Log.e(TAG, "logStackTrace:");
                e.printStackTrace();
            }

            Crashlytics.logException(e);
        } catch (Exception ex) {

        }
    }

    public static void logStackTrace(UnsatisfiedLinkError e) {
        try {
            if (isDebuggable)
                e.printStackTrace();

            Crashlytics.logException(e);
        } catch (Exception ex) {

        }
    }

    public static void printStackTrace(Throwable e) {
        try {
            if (isDebuggable)
                e.printStackTrace();
        } catch (Exception ex) {

        }
    }

    public static void printStackTrace(String TAG, Throwable e) {
        try {
            if (isDebuggable)
                Log.e(TAG, "printStackTrace:");

            printStackTrace(e);
        } catch (Exception ex) {

        }
    }

    public static void printStackTrace(UnsatisfiedLinkError e) {
        try {
            if (isDebuggable)
                e.printStackTrace();
        } catch (Exception ex) {

        }
    }
}
