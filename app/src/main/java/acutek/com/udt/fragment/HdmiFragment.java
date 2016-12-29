package acutek.com.udt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.File;
import java.util.Scanner;

import acutek.com.udt.BaseFragment;
import acutek.com.udt.R;
import acutek.com.udt.databinding.HdmiFragmentBinding;

/**
 * Created by kiemhao on 12/27/16.
 */

public class HdmiFragment extends BaseFragment {
    private HdmiFragmentBinding hdmiFragmentBinding;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hdmiFragmentBinding= (HdmiFragmentBinding) viewDataBinding;

    }

    @Override
    public int getLayoutViewId() {
        return R.layout.hdmi_fragment;
    }

    private boolean isHdmiSwitchSet() {

        // The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
        // An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
        File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
        if (!switchFile.exists()) {
            switchFile = new File("/sys/class/switch/hdmi/state");
        }
        try {
            Scanner switchFileScanner = new Scanner(switchFile);
            int switchValue = switchFileScanner.nextInt();
            switchFileScanner.close();
            return switchValue > 0;
        } catch (Exception e) {
            return false;
        }
    }

}
