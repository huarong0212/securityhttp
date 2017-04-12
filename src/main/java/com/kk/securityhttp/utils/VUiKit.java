package com.kk.securityhttp.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Lody
 *         <p>
 *         A set of tools for UI.
 */
public class VUiKit {
    private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable r) {
        gUiHandler.post(r);
    }

    public static void postDelayed(long delay, Runnable r) {
        gUiHandler.postDelayed(r, delay);
    }
}
