package acutek.com.udt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


import acutek.com.udt.databinding.TestFragmentBinding;

/**
 * Created by kiemhao on 12/27/16.
 */

public class TestFragment extends BaseFragment {
    private TestFragmentBinding testFragmentBinding;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testFragmentBinding= (TestFragmentBinding) viewDataBinding;
        testFragmentBinding.textView.setText("test");

    }

    @Override
    public int getLayoutViewId() {
        return R.layout.test_fragment;
    }
}
