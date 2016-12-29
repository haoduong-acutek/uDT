package acutek.com.udt.config;

import acutek.com.udt.MyApplication;

/**
 * Created by kiemhao on 12/28/16.
 */

public class Config {
    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int SOCKET_TIMEOUT = 60000;
    public static String LOGFILE_NAME = "log.txt";
    public static int MAX_LOGFILE_KB = 100;

    public static final String LOG_NAME = "uDT";
    public static final String APP_DATA_DIR = MyApplication
            .getInstance().getExternalCacheDir()
            + "/" + LOG_NAME;
}
