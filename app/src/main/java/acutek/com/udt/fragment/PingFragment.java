package acutek.com.udt.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import acutek.com.udt.BR;
import acutek.com.udt.BaseFragment;
import acutek.com.udt.IRecyclerServer;
import acutek.com.udt.R;
import acutek.com.udt.Utility;
import acutek.com.udt.databinding.HomeFragmentBinding;
import acutek.com.udt.databinding.HomeRcvServerItemBinding;
import acutek.com.udt.databinding.PingFragmentBinding;
import acutek.com.udt.service.AutoPingService;
import acutek.com.udt.task.BgProcessingResultReceiver;

/**
 * Created by kiemhao on 12/29/16.
 */

public class PingFragment extends BaseFragment implements BgProcessingResultReceiver.Receiver  {
    private PingFragmentBinding pingFragmentBinding;
    private BgProcessingResultReceiver mReceiver;

    @Override
    public int getLayoutViewId() {
        return R.layout.ping_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pingFragmentBinding = (PingFragmentBinding) viewDataBinding;

        mReceiver = new BgProcessingResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        List<ServerInfo> serverInfoList = new ArrayList<>();
        serverInfoList.add(new ServerInfo("http://lv-api.acuteksolutions.com"));
        serverInfoList.add(new ServerInfo("http://bsdev.acuteksolutions.com"));
        serverInfoList.add(new ServerInfo("http://microsoft.com"));
        serverInfoList.add(new ServerInfo("http://haoduong.acuteksolution.com"));
        final Adapter adapter = new Adapter(serverInfoList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pingFragmentBinding.rcvServer.setLayoutManager(linearLayoutManager);
        pingFragmentBinding.rcvServer.setAdapter(adapter);

        //Checkbox: auto test
        pingFragmentBinding.chkAutoTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isAutoPingServiceRunning()) {
                    Utility.showMessage(context, "Service is running, can not stop");
                    pingFragmentBinding.chkAutoTest.setChecked(!isChecked);
                    return;
                }
                stopService();
                for (ServerInfo serverInfo : adapter.list) {
                    serverInfo.isAutoTestEnable = !isChecked;
                    serverInfo.status =isChecked? "waiting":"";
                    serverInfo.pingResult = null;

                }
                adapter.notifyDataSetChanged();
                if (isChecked) {
                    startAutoTest(adapter.list.get(0), 0);
                }
            }
        });


    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int index = resultData.getInt(AutoPingService.INDEX_ARG);
        boolean isSingle = resultData.getBoolean(AutoPingService.IS_SINGLE_ARG);
        ServerInfo serverInfo = ((Adapter) pingFragmentBinding.rcvServer.getAdapter()).getItem(index);


        switch (resultCode) {
            case AutoPingService.RESULT_PINGING:
                serverInfo.status = "pinging...";
                serverInfo.pingResult = null;
                pingFragmentBinding.rcvServer.getAdapter().notifyItemChanged(index);
                break;
            case AutoPingService.RESULT_OK:
                AutoPingService.isStop = true;
                final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AutoPingService.class);
                context.stopService(intent);

                if (index == ((Adapter) pingFragmentBinding.rcvServer.getAdapter()).getItemCount() - 1) {
                    //last item
                    boolean result = resultData.getBoolean(AutoPingService.RESULT);
                    serverInfo.status = "";
                    serverInfo.pingResult = result;
                    pingFragmentBinding.rcvServer.getAdapter().notifyItemChanged(index);
                    break;
                } else {
                    boolean result = resultData.getBoolean(AutoPingService.RESULT);
                    serverInfo.status = "";
                    serverInfo.pingResult = result;
                    pingFragmentBinding.rcvServer.getAdapter().notifyItemChanged(index);

                    if (!isSingle) {
                        index = index + 1;
                        serverInfo = ((Adapter) pingFragmentBinding.rcvServer.getAdapter()).getItem(index);
                        startAutoTest(serverInfo, index);
                    }


                }


        }


    }


    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        public List<ServerInfo> list;


        public Adapter(List<ServerInfo> list) {

            this.list = list;


        }


        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.home_rcv_server_item, viewGroup, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;

        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final ServerInfo serverInfo = list.get(pos);
            myViewHolder.binding.setVariable(BR.serverInfo, serverInfo);
            myViewHolder.binding.setClick(new IRecyclerServer() {
                @Override
                public void onPingClick(View view) {
                    startSingleTest(list.get(pos), pos);
                }
            });

            myViewHolder.binding.btnPing.setVisibility(serverInfo.isAutoTestEnable ? View.VISIBLE : View.INVISIBLE);
            if (serverInfo.isAutoTestEnable) {
                myViewHolder.binding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            if (serverInfo.pingResult != null) {
                if (serverInfo.pingResult) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ping_indicator_success);
                    myViewHolder.binding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.ping_indicator_failed);
                    myViewHolder.binding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                }
            } else
                myViewHolder.binding.txtStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            myViewHolder.binding.executePendingBindings();


        }

        public ServerInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private HomeRcvServerItemBinding binding;

            public MyViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }


        }
    }

    public static class ServerInfo implements Parcelable {
        public String name;
        public boolean isAutoTestEnable;
        public String status;
        public Boolean pingResult;

        public ServerInfo(String name) {
            this.name = name;
            isAutoTestEnable = true;


        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeByte(this.isAutoTestEnable ? (byte) 1 : (byte) 0);
            dest.writeString(this.status);
        }

        protected ServerInfo(Parcel in) {
            this.name = in.readString();
            this.isAutoTestEnable = in.readByte() != 0;
            this.status = in.readString();
        }

        public static final Parcelable.Creator<ServerInfo> CREATOR = new Parcelable.Creator<ServerInfo>() {
            @Override
            public ServerInfo createFromParcel(Parcel source) {
                return new ServerInfo(source);
            }

            @Override
            public ServerInfo[] newArray(int size) {
                return new ServerInfo[size];
            }
        };
    }

    private void stopService() {
        AutoPingService.isStop = true;
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AutoPingService.class);
        context.stopService(intent);
        resetList();
    }

    private void resetList() {
        Adapter adapter = ((Adapter) pingFragmentBinding.rcvServer.getAdapter());
        for (ServerInfo serverInfo : adapter.list) {
            serverInfo.isAutoTestEnable = true;
            serverInfo.status = "";
            serverInfo.pingResult = null;
        }
        adapter.notifyDataSetChanged();
    }

    private void startAutoTest(ServerInfo serverInfo, int index) {
        AutoPingService.isStop = false;
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AutoPingService.class);
        intent.putExtra(AutoPingService.RECEIVER_ARG, mReceiver);
        intent.putExtra(AutoPingService.LIST_ARG, serverInfo);
        intent.putExtra(AutoPingService.IS_SINGLE_ARG, false);
        intent.putExtra(AutoPingService.INDEX_ARG, index);
        context.startService(intent);
    }

    private void startSingleTest(ServerInfo serverInfo, int index) {
        AutoPingService.isStop = false;
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, AutoPingService.class);
        intent.putExtra(AutoPingService.RECEIVER_ARG, mReceiver);
        intent.putExtra(AutoPingService.LIST_ARG, serverInfo);
        intent.putExtra(AutoPingService.IS_SINGLE_ARG, true);
        intent.putExtra(AutoPingService.INDEX_ARG, index);

        context.startService(intent);
    }

    private boolean isAutoPingServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("acutek.com.udt.service.AutoPingService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
