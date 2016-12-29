package acutek.com.udt.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import acutek.com.udt.fragment.HomeFragment;
import acutek.com.udt.fragment.PingFragment;

/**
 * Created by kiemhao on 12/28/16.
 */

public class AutoPingService extends IntentService {

    public static String RECEIVER_ARG="receiver";
    public static String LIST_ARG="list arg";
    public static String INDEX_ARG="index arg";
    public static String IS_SINGLE_ARG="is single";
//    public static String INDEX="index";
    public static String RESULT="result";

    public static final int RESULT_OK=1;
    public static final int RESULT_PINGING=2;

    public volatile static boolean isStop;


    public AutoPingService() {
        super("AutoPingService");

    }
    public AutoPingService(String name) {
        super(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_ARG);
        PingFragment.ServerInfo serverInfo= intent.getParcelableExtra(LIST_ARG);
        boolean isSingle=intent.getBooleanExtra(IS_SINGLE_ARG,false);
        int index=intent.getIntExtra(INDEX_ARG,-1);
        if(isSingle){
            Bundle bundle = new Bundle();
            bundle.putInt(INDEX_ARG, index);
            bundle.putBoolean(IS_SINGLE_ARG, true);
            if(!isStop)
                receiver.send(RESULT_PINGING, bundle);
            if (ping(serverInfo.name)) {
                bundle.putBoolean(RESULT, true);
            } else {
                bundle.putBoolean(RESULT, false);
            }
            if(!isStop)
                receiver.send(RESULT_OK, bundle);
        }
        else {

                Bundle bundle = new Bundle();
                bundle.putInt(INDEX_ARG, index);
                bundle.putBoolean(IS_SINGLE_ARG, false);
                if(!isStop)
                    receiver.send(RESULT_PINGING, bundle);
                if (ping(serverInfo.name)) {
                    bundle.putBoolean(RESULT, true);
                } else {
                    bundle.putBoolean(RESULT, false);
                }
                if(!isStop)
                    receiver.send(RESULT_OK, bundle);

        }
        stopSelf();

    }

    private boolean ping(String host){
        try {
            String cmd="ping -c 3 -w 3 "+host.replace("http://","");
            Process p;

            p= Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            //p.destroy();
            return res.length()>0?true:false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
