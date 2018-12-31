package com.divercity.app.core.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Util {

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static Date getDateWithServerTimeStamp(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                Locale.getDefault());
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Long getMilisecFromStringDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                Locale.US);
        try {
            return dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringDateTimeWithServerDate(String date){
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                    Locale.getDefault());
            try {
                Date myDate = dateFormat.parse(date);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy K:mm a", Locale.getDefault());
                String res = df.format(myDate);
                if (res != null && !res.equals(""))
                    return res.replace("AM", "am").replace("PM", "pm");
                else
                    return "";
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getStringAppTimeWithDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("K:mm a",
                Locale.getDefault());
        return dateFormat.format(date).replace("AM", "am").replace("PM", "pm");
    }

    public static String getStringAppTimeWithString(String date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                    Locale.getDefault());
            try {
                Date myDate = dateFormat.parse(date);
                DateFormat df = new SimpleDateFormat("K:mm a", Locale.getDefault());
                String res = df.format(myDate);
                if (res != null && !res.equals(""))
                    return res.replace("AM", "am").replace("PM", "pm");
                else
                    return "";
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Helper method to check if a string not null
     * and if it's null return empty string
     *
     * @param string the string to check
     * @return the String after the check
     */
    public static String notNullOrEmpty(@Nullable String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    public static String getStringFromArray(List<String> list) {
        return list.toString().substring(1, list.toString().length() - 1);
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

//    public static <T extends DataBase<T>> T checkResponse(Response<T> response){
//        if (response.isSuccessful()) {
//            return response.body().getData();
//        } else {
//            throw new HttpException(response);
//        }
//    }
}
