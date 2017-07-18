package com.yashkakkar.licagentdiary.utils;

import android.app.Activity;
import android.content.Intent;

import com.yashkakkar.licagentdiary.R;

/**
 * Created by Yash Kakkar on 07-07-2017.
 */

public class ThemeUtils {

    private static int setTheme;
    private final static int LIGHT_THEME = 0;
    private final static int DARK_THEME = 1;

    public static void onChangeTheme(Activity activity, int theme){
        setTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity){

        switch (setTheme){
            default:
            case LIGHT_THEME:
                activity.setTheme(R.style.DarkTheme_NoActionBar);
                break;
            case DARK_THEME:
                activity.setTheme(R.style.DarkTheme_NoActionBar);
                break;
        }
    }


}
