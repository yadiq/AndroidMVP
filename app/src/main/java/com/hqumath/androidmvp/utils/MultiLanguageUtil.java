package com.hqumath.androidmvp.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hqumath.androidmvp.app.Constant;

import java.util.Locale;

/**
 * 多语言切换的帮助类
 */
public class MultiLanguageUtil {

    public class LanguageType {
        public static final int LANGUAGE_EN = 0;    //英文
        public static final int LANGUAGE_CHINESE_SIMPLIFIED = 1; //简体中文
        public static final int LANGUAGE_CHINESE_TRADITIONAL = 2;  //繁体中文
    }

    private static final String TAG = "MultiLanguageUtil";
    private static MultiLanguageUtil instance;

    public static MultiLanguageUtil getInstance() {
        if (instance == null) {
            synchronized (MultiLanguageUtil.class) {
                if (instance == null) {
                    instance = new MultiLanguageUtil();
                }
            }
        }
        return instance;
    }

    private MultiLanguageUtil() {
    }

    /**
     * 如果不是英文、简体中文、繁体中文，默认返回简体中文
     *
     * @return
     */
    public Locale getLanguageLocale(Context context) {
        int languageType = SPUtil.getInstance().getInt(Constant.LANGUAGE, LanguageType.LANGUAGE_EN);
        Locale locale = Locale.ENGLISH;
        if (languageType == LanguageType.LANGUAGE_EN) {
            locale = Locale.ENGLISH;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            locale = Locale.TRADITIONAL_CHINESE;
        }
        return locale;
    }

    /**
     * 更新语言
     *
     * @param context
     * @param languageType
     */
    public void updateLanguage(Context context, int languageType) {
        SPUtil.getInstance().put(Constant.LANGUAGE, languageType);
        setConfiguration(context);
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context);
        } else {
            MultiLanguageUtil.getInstance().setConfiguration(context);
            return context;
        }
    }

    /**
     * 设置语言
     */
    public void setConfiguration(Context context) {
        if (context == null) {
            Log.e(TAG, "No context, MultiLanguageUtil will not work!");
            return;
        }
        Context appContext = context.getApplicationContext();
        Locale targetLocale = getLanguageLocale(appContext);
        Locale.setDefault(targetLocale);
        Configuration configuration = appContext.getResources().getConfiguration();
        configuration.setLocale(targetLocale);
        context.createConfigurationContext(configuration);
        Resources resources = appContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale(context);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
