package acutek.com.udt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import acutek.com.udt.BaseFragment;
import acutek.com.udt.R;
import acutek.com.udt.TestFragment;
import acutek.com.udt.Utility;
import acutek.com.udt.databinding.HomeFragmentBinding;

/**
 * Created by kiemhao on 12/27/16.
 */

public class HomeFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private HomeFragmentBinding homeFragmentBinding;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeFragmentBinding = (HomeFragmentBinding) viewDataBinding;

        setupTab();

        setDefaultTabWhenLoad();

    }


    @Override
    public int getLayoutViewId() {
        return R.layout.home_fragment;
    }


    private void setupTab() {

        homeFragmentBinding.tabLayout.addTab(homeFragmentBinding.tabLayout.newTab());
        homeFragmentBinding.tabLayout.addTab(homeFragmentBinding.tabLayout.newTab());
        homeFragmentBinding.tabLayout.addTab(homeFragmentBinding.tabLayout.newTab());

        for (int i = 0; i < homeFragmentBinding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = homeFragmentBinding.tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));

        }
    }



    void setDefaultTabWhenLoad() {

        homeFragmentBinding.tabLayout.addOnTabSelectedListener(this);
        homeFragmentBinding.tabLayout.getTabAt(0).getCustomView().setSelected(true);

        homeFragmentBinding.tabLayout.getTabAt(0).select();

    }

    private View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTextView);
        tv.setText(getTabText(position));

        //set full width if only 1 tab
        if(homeFragmentBinding.tabLayout.getTabCount()==1)
            tv.getLayoutParams().width= Utility.getScreenSize(context).x;

        return v;
    }

    private String getTabText(int position) {
        String[] arrTabText = new String[]{"Ping","HDMI","QAM"};
        return arrTabText[position];
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getCustomView().setSelected(true);
        setCurrentTabFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        tab.getCustomView().setSelected(true);
        setCurrentTabFragment(tab.getPosition());
    }


    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceSubFragment(PingFragment.instantiate(context,PingFragment.class.getName()));
                break;
            case 1:
                replaceSubFragment(HdmiFragment.instantiate(context,HdmiFragment.class.getName()));
                break;
            default:
                replaceSubFragment(QamFragment.instantiate(context,QamFragment.class.getName()));
                break;
        }
    }


    public void replaceSubFragment(Fragment fragment) {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment, fragment.getClass().getName());
        ft.commit();
    }


}
