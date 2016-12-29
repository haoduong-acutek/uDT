package acutek.com.udt.handler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;


import acutek.com.udt.MyApplication;


public class MyUncaughtExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private Context context;
    private Thread.UncaughtExceptionHandler defaultUEH;

    public MyUncaughtExceptionHandler(Context context) {
        this.context = context;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread thread, Throwable exception) {

            MyApplication.log(
                    Log.getStackTraceString(exception));
            this.defaultUEH.uncaughtException(thread, exception);



    }

}