package com.divercity.android.core.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.divercity.android.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

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

    public static boolean areDatesSameDay(String strDate1, String strDate2) {
        Date date1 = getDateWithServerTimeStamp(strDate1);
        Date date2 = getDateWithServerTimeStamp(strDate2);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static boolean areDatesSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static Long getMilisecFromStringDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                Locale.US);
        try {
            return dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringDateTimeWithServerDate(String date) {
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

    public static String getStringDateTimeWithServerDate(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy K:mm a", Locale.getDefault());
            String res = df.format(date);
            return res.replace("AM", "am").replace("PM", "pm");
        } else {
            return "";
        }
    }

    public static String getStringDateWithServerDate(String date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                    Locale.getDefault());
            try {
                Date myDate = dateFormat.parse(date);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                return df.format(myDate);
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getStringDateWithServerDate(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            return df.format(date);
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
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static HashMap<String, String> getUserTypeMap(Context context) {
        String[] userTypeId = context.getResources().getStringArray(R.array.user_type_id);
        String[] userType = context.getResources().getStringArray(R.array.user_type);
        HashMap<String, String> userTypeMap = new HashMap<>();
        for (int i = 0; i < userTypeId.length; i++) {
            userTypeMap.put(userTypeId[i], userType[i]);
        }
        return userTypeMap;
    }

    public static String getTimeAgoWithStringServerDate(String date){
        Date svDate = getDateWithServerTimeStamp(date);
        return TimeAgo.toRelative(svDate, new Date(), 1);
    }

    public static String getNameFormatted(final String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(Character.toUpperCase(word.charAt(0)));
                ret.append(word.substring(1));
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }
}
