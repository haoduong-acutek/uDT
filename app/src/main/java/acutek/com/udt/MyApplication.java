package acutek.com.udt;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;

import java.io.File;
import java.io.IOException;

import acutek.com.udt.config.Config;
import acutek.com.udt.handler.MyUncaughtExceptionHandler;
import acutek.com.udt.helper.DateHelper;


/**
 * Created by kiemhao on 12/27/16.
 */

public class MyApplication extends Application {
    private static Logger logger = LoggerFactory.getLogger();
    private static MyApplication instance;

    public static synchronized MyApplication getInstance() {
        // TODO Auto-generated method stub
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private static void createLogFileIfNeed() {

        File file = new File(Environment.getExternalStorageDirectory() + "/"
                + Config.LOG_NAME + "/" + Config.LOGFILE_NAME);
        try {
            if (!file.exists())
                file.createNewFile();
            else if (file.length() / 1024 > Config.MAX_LOGFILE_KB) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initLogger() {
        new File(Environment.getExternalStorageDirectory() + "/"
                + Config.LOG_NAME).mkdir();
        createLogFileIfNeed();
        PropertyConfigurator.getConfigurator(this).configure();
        FileAppender appender = new FileAppender();
        appender.setFileName(Config.LOG_NAME + "/" + Config.LOGFILE_NAME);
        appender.setAppend(true);
        logger.addAppender(appender);
    }



    public static void log(String msg) {
        // createLogFileIfNeed();

        logger.debug(DateHelper.getcurrentDateString() + "-----" + msg);
    }

    public static void log(Exception exp) {
        logger.debug(DateHelper.getcurrentDateString()
                + Log.getStackTraceString(exp));
    }

    public static void setEnableUncatchException(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(
                context));

    }


}
