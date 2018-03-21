package com.ejar.chapp.baselibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017\12\19 0019.
 */

public class TU {
    private static volatile Toast toast;
    public static Context c;


    private TU() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static void register(Context context) {
        c = context;
    }

    public static void t(String msg) {
        if (toast == null) {
            synchronized (TU.class) {
                if (toast == null) {
                    toast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        } else {
            toast.setText(msg);
            toast.show();
        }
    }

    public static void t(int msgRes) {
        t(c.getString(msgRes));
    }

}
