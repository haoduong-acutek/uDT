package acutek.com.udt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import acutek.com.udt.BaseFragment;
import acutek.com.udt.R;
import acutek.com.udt.databinding.QamFragmentBinding;
import acutek.com.udt.databinding.TestFragmentBinding;

/**
 * Created by kiemhao on 12/27/16.
 */

public class QamFragment extends BaseFragment {
    private QamFragmentBinding qamFragmentBinding;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qamFragmentBinding= (QamFragmentBinding) viewDataBinding;


    }

    @Override
    public int getLayoutViewId() {
        return R.layout.qam_fragment;
    }
}
