package tip.tdc.com.tip.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tip.tdc.com.tip.activity.detail.DialogCallPhone;
import tip.tdc.com.tip.activity.weather.WeatherPresenter;
import tip.tdc.com.tip.interfaces.DialogCallback;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/19/17.
 */

public class Utils {
    public static void getKeyHash(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static void setupWeatherIconHome(ImageView imageView, String icon) {
            switch (icon) {
                case WeatherPresenter.CLEAR_SKY:
                    imageView.setImageResource(R.drawable.w01d);
                    break;
                case WeatherPresenter.MIST:
                    imageView.setImageResource(R.drawable.w03d);

                case WeatherPresenter.SCATTERED_CLOUDS:
                    imageView.setImageResource(R.drawable.w03d);
                    break;
                case WeatherPresenter.PARTIALLY_CLOUDY:
                    imageView.setImageResource(R.drawable.w02d);
                    break;
                case WeatherPresenter.RAINY:
                    imageView.setImageResource(R.drawable.w09d);
                    break;
                case WeatherPresenter.SHOWER_RAIN:
                    imageView.setImageResource(R.drawable.w09d);
                    break;
                case WeatherPresenter.THUNDERSTORM:
                    imageView.setImageResource(R.drawable.w11d);
                    break;
                case WeatherPresenter.SNOW:
                    imageView.setImageResource(R.drawable.w13d);
                    break;
                case WeatherPresenter.FEW_CLOUD:
                    imageView.setImageResource(R.drawable.w03d);
                    break;
                case WeatherPresenter.CLOUDY:
                    imageView.setImageResource(R.drawable.w03d);
                    break;

        }
    }

    public static void setupWeatherIcon(ImageView imageView, String icon, int currentHour) {
        if (currentHour >= 6 || currentHour <= 18) {

            switch (icon) {
                case WeatherPresenter.CLEAR_SKY:
                    imageView.setImageResource(R.drawable.w01d_s);
                    break;
                case WeatherPresenter.MIST:
                    imageView.setImageResource(R.drawable.w03d_s);

                case WeatherPresenter.SCATTERED_CLOUDS:
                    imageView.setImageResource(R.drawable.w03d_s);
                    break;
                case WeatherPresenter.PARTIALLY_CLOUDY:
                    imageView.setImageResource(R.drawable.w02d_s);
                    break;
                case WeatherPresenter.RAINY:
                    imageView.setImageResource(R.drawable.w09d_s);
                    break;
                case WeatherPresenter.SHOWER_RAIN:
                    imageView.setImageResource(R.drawable.w09d_s);
                    break;
                case WeatherPresenter.THUNDERSTORM:
                    imageView.setImageResource(R.drawable.w11d_s);
                    break;
                case WeatherPresenter.SNOW:
                    imageView.setImageResource(R.drawable.w13d_s);
                    break;
                case WeatherPresenter.FEW_CLOUD:
                    imageView.setImageResource(R.drawable.w03d_s);
                    break;
                case WeatherPresenter.CLOUDY:
                    imageView.setImageResource(R.drawable.w03d_s);
                    break;
            }
        } else {
            switch (icon) {
                case WeatherPresenter.CLEAR_SKY:
                    imageView.setImageResource(R.drawable.w01n_s);
                    break;
                case WeatherPresenter.MIST:
                    imageView.setImageResource(R.drawable.w03n_s);

                case WeatherPresenter.SCATTERED_CLOUDS:
                    imageView.setImageResource(R.drawable.w03n_s);
                    break;
                case WeatherPresenter.PARTIALLY_CLOUDY:
                    imageView.setImageResource(R.drawable.w02n_s);
                    break;
                case WeatherPresenter.RAINY:
                    imageView.setImageResource(R.drawable.w09n_s);
                    break;
                case WeatherPresenter.SHOWER_RAIN:
                    imageView.setImageResource(R.drawable.w09n_s);
                    break;
                case WeatherPresenter.THUNDERSTORM:
                    imageView.setImageResource(R.drawable.w11n_s);
                    break;
                case WeatherPresenter.SNOW:
                    imageView.setImageResource(R.drawable.w13n_s);
                    break;
                case WeatherPresenter.FEW_CLOUD:
                    imageView.setImageResource(R.drawable.w03n_s);
                    break;
                case WeatherPresenter.CLOUDY:
                    imageView.setImageResource(R.drawable.w03n_s);
                    break;
            }

        }
    }


    public static void openAppInStore(Context mContext, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            mContext.startActivity(goToMarket);
        } catch (Exception ignore) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(myIntent);
        }
    }

    public static void openWebsite(Context mContext, String website) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(website));
            mContext.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendEmail(Context mContext, String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            mContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callPhoneNumber(Context mContext, String phoneNumber) {
        try {
            if(phoneNumber.length() < 15) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber.trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(callIntent);
            } else {
                DialogCallPhone dialogCallPhone = new DialogCallPhone();
                dialogCallPhone.buildDialog(phoneNumber, mContext, null);
                dialogCallPhone.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getDirection(Context mContext, String longitute, String latitute, String desLat, String desLon) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s", latitute, longitute, desLat, desLon)));
            mContext.startActivity(intent);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("google.navigation:q=an+address+city"));
    }

    public static void setStatusBarColor(Activity mActivity, final int colorId) {
        int statusColor = colorId;
        if (colorId == 0) {
            statusColor = mActivity.getResources().getColor(R.color.colorBlue500);
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = mActivity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(statusColor);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Window w = mActivity.getWindow();
//                w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                //status bar height
//                int actionBarHeight = getActionBarHeight(mActivity);
//                int statusBarHeight = getStatusBarHeight(mActivity);
//                //action bar height
//                View statusBar = mActivity.findViewById(R.id.statusBarBackground);
//                statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
//                statusBar.setBackgroundColor(mActivity.getResources().getColor(R.color.main_app_status_bar_color));
////                statusBar.setBackgroundResource(R.drawable.status_bar);
            }
        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }
    }

    public static int countLove(String love) {
        int counter = 0;
        if (love == null || love.length() == 0)
            return counter;

        String[] loves = love.split("#");
        for (int i = 0; i < loves.length; i++) {
            if (loves[i] != null && loves[i].length() > 0) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static Point getScreenSize(Activity mContext) {
        Display display = mContext.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
        return size;
    }

    public Bitmap takeScreenshot(Activity mContext, int idView) {
        View rootView = mContext.findViewById(idView).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Calendar mCalendar = Calendar.getInstance();

    public static String convertDateHistory(long fullDate, Context mContext) {
        String dateFinal = "";
        try {
            Calendar transferDate = Calendar.getInstance();
            mCalendar = Calendar.getInstance();

            transferDate.setTimeInMillis(fullDate*1000);
            LoggerFactory.e("Convert Date" + simpleDateFormat.format(transferDate.getTime()));

            if (isDateInToday(transferDate)){
                if (mCalendar.get(Calendar.HOUR_OF_DAY) - transferDate.get(Calendar.HOUR_OF_DAY) > 3) {
                    // hom nay
                    return "Today" + "," + toStringWithFormat("HH:mm", transferDate);
                }
            else if (mCalendar.get(Calendar.HOUR_OF_DAY) - transferDate.get(Calendar.HOUR_OF_DAY) >= 1) {
                    // 3-1 h truoc
                    return ""+(mCalendar.get(Calendar.HOUR_OF_DAY) - transferDate.get(Calendar.HOUR_OF_DAY)) + " " + mContext.getString(R.string.hour_ago);
                }
            else if (mCalendar.get(Calendar.MINUTE) - transferDate.get(Calendar.MINUTE) >= 1) {
                    // x ph truoc
                    return ""+(mCalendar.get(Calendar.MINUTE) - transferDate.get(Calendar.MINUTE)) +" " + mContext.getString(R.string.minute_ago);
                }
            else {
                    return mContext.getString(R.string.date_recent_second);
                }
            }
        else if (isDateInYesterday(transferDate)){
                // hom qua
                return toStringWithFormat("HH:mm", transferDate) + " " + mContext.getString(R.string.date_yesterday);
            }
        else if (isDateInWeekend(transferDate)) {
                // trong tuan nay
                return ""+ (mCalendar.get(Calendar.DAY_OF_YEAR) - transferDate.get(Calendar.DAY_OF_YEAR))+ " " + mContext.getString(R.string.day_ago);
            }
        else {
                // tuần trc
                //let d = calendar.component(.day, from: self)
                //let suffix = Utils.getDayOfMonthSuffix(day: d)
                //let format = “EEE, MMM dd’” + suffix + “' yyyy, HH:mm”
//                let format = “HH:mm dd/MM/yyyy” // 1/3/17 a Thọ muốn đổi format ngắn như thế này
                return toStringWithFormat("HH:mm dd/MM/yyyy", transferDate);
            }

//            // older than 1 year
//            if ((mCalendar.get(Calendar.YEAR) - transferDate.get(Calendar.YEAR)) > 0) {
//                dateTransfer = simpleDateFormatDMY.format(transferDate.getTime());
//            }
//            // older than 1 day
//            else if ((mCalendar.get(Calendar.DAY_OF_YEAR) - transferDate.get(Calendar.DAY_OF_YEAR)) > 1) {
//                dateTransfer = simpleDateFormatDMY.format(transferDate.getTime());
//            }
//            // yesterday
//            else if ((mCalendar.get(Calendar.DAY_OF_YEAR) - transferDate.get(Calendar.DAY_OF_YEAR)) == 1) {
//                // yesterday: hom qua HH:MM
//                dateTransfer = "Yesterday" + simpleDateFormatHM.format(transferDate.getTime());
//            }
//            //today
//            else {
//                // today: HH:MM
//                if ((mCalendar.get(Calendar.HOUR_OF_DAY) - transferDate.get(Calendar.HOUR_OF_DAY)) > 0) {
//                    dateTransfer = simpleDateFormatHM.format(transferDate.getTime());
//                } else if (mCalendar.get(Calendar.HOUR_OF_DAY) == transferDate.get(Calendar.HOUR_OF_DAY)) {
//                    // This Hour
//                    if (mCalendar.get(Calendar.MINUTE) - transferDate.get(Calendar.MINUTE) < 2) {
//                        // < 2 minutes
//                        dateTransfer = mContext.getString(R.string.date_recent_second);
//                    } else if (mCalendar.get(Calendar.MINUTE) - transferDate.get(Calendar.MINUTE) <= 60) {
//                        // < 60 minutes
//                        dateTransfer = "" + (mCalendar.get(Calendar.MINUTE) - transferDate.get(Calendar.MINUTE)) + " " + mContext.getString(R.string.minute_ago);
//                    } else {
//                        dateTransfer = "" + (mCalendar.get(Calendar.HOUR_OF_DAY) - transferDate.get(Calendar.HOUR_OF_DAY)) + " " + mContext.getString(R.string.hour_ago);
//                    }
//                }
//            }
        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
        }

        return dateFinal;
    }

    static boolean isDateInToday(Calendar tDate) {
        Calendar cDate = Calendar.getInstance();

        if (cDate.get(Calendar.YEAR) == tDate.get(Calendar.YEAR)) {
            if (cDate.get(Calendar.MONTH) == tDate.get(Calendar.MONTH)) {
                if (cDate.get(Calendar.DAY_OF_MONTH) == tDate.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean isDateInYesterday(Calendar tDate) {
        Calendar cDate = Calendar.getInstance();
        cDate.add(Calendar.DAY_OF_YEAR, -1);

        if (cDate.get(Calendar.YEAR) == tDate.get(Calendar.YEAR)) {
            if (cDate.get(Calendar.DAY_OF_YEAR) == tDate.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }

        return false;
    }

    static boolean isDateInWeekend(Calendar tDate) {
        Calendar cDate = Calendar.getInstance();
        cDate.add(Calendar.WEEK_OF_YEAR, -1);

        if (cDate.get(Calendar.YEAR) == tDate.get(Calendar.YEAR)) {
            if (cDate.get(Calendar.WEEK_OF_YEAR) == tDate.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }

        return false;
    }

    static String toStringWithFormat(String formatDate, Calendar tDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
        return simpleDateFormat.format(tDate.getTime());
    }

    public static int getColor(Context mContext, int color){
        if(Build.VERSION.SDK_INT>=25){
            return mContext.getColor(color);
        } else {
            return  mContext.getResources().getColor(color);
        }
    }

    public static boolean isInternetConnected(Context mContext) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
            return false;
        }
    }

    public static void showDialog(String title, String content, Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
