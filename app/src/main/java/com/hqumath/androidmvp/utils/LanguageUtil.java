package com.hqumath.androidmvp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.hqumath.androidmvp.app.Constant;

import java.util.Locale;

/**
 * 多语言切换
 * 来自：https://blog.csdn.net/m0_38074457/article/details/84993366
 */
public class LanguageUtil {
    public static final String DEFAULT_LANGUAGE = "en";//应用缺省的语言-英语
    public static final String DEFAULT_COUNTRY = "US";

    /**
     * 更新应用语言，当应用语言与SP中存储的语言不相同时
     *
     * @param context
     */
    public static void changeAppLanguageOnDifferent(Context context) {
        String language = SPUtil.getInstance().getString(Constant.SP_LANGUAGE, DEFAULT_LANGUAGE);
        String country = SPUtil.getInstance().getString(Constant.SP_COUNTRY, DEFAULT_COUNTRY);
        if (!LanguageUtil.isSameWithSetting(context, language, country)) {
            LanguageUtil.changeAppLanguage(context, new Locale(language, country), false);
        }
    }

    /**
     * 更新应用语言（核心）
     *
     * @param locale      语言地区
     * @param persistence 是否持久化
     */
    public static void changeAppLanguage(Context context, Locale locale, boolean persistence) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        setLanguage(context, locale, configuration);
        resources.updateConfiguration(configuration, metrics);
        if (persistence) {
            saveLanguageSetting(locale);
        }
    }

    private static void setLanguage(Context context, Locale locale, Configuration configuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
    }

    /**
     * 判断sp中和app中的多语言信息是否相同
     */
    public static boolean isSameWithSetting(Context context, String sp_language, String sp_country) {
        Locale locale = getAppLocale(context);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        return language.equals(sp_language) && country.equals(sp_country);
    }

    /**
     * 判断应用与系统语言是否相同
     */
    public static boolean isSameLocal(Locale appLocale, String sp_language, String sp_country) {
        String appLanguage = appLocale.getLanguage();
        String appCountry = appLocale.getCountry();
        return appLanguage.equals(sp_language) && appCountry.equals(sp_country);
    }

    /**
     * 保存多语言信息到sp中
     */
    public static void saveLanguageSetting(Locale locale) {
        SPUtil.getInstance().put(Constant.SP_LANGUAGE, locale.getLanguage());
        SPUtil.getInstance().put(Constant.SP_COUNTRY, locale.getCountry());
    }

    /**
     * 获取应用语言
     */
    public static Locale getAppLocale(Context context) {
        Locale local;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            local = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            local = context.getResources().getConfiguration().locale;
        }
        return local;
    }

    /**
     * 获取系统语言
     */
    public static LocaleListCompat getSystemLanguage() {
        Configuration configuration = Resources.getSystem().getConfiguration();
        LocaleListCompat locales = ConfigurationCompat.getLocales(configuration);
        return locales;
    }
}
