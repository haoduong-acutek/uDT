package acutek.com.udt;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import acutek.com.udt.databinding.MainActivityBinding;
import acutek.com.udt.fragment.HomeFragment;


public class MainActivity extends AppCompatActivity  implements OnTabSelectedListener{
    MainActivityBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        setupTab();

        setDefaultTabWhenLoad();
    }

    private void setupTab() {

        dataBinding.tabLayout.addTab(dataBinding.tabLayout.newTab());
        dataBinding.tabLayout.addTab(dataBinding.tabLayout.newTab());

        for (int i = 0; i < dataBinding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = dataBinding.tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));

        }
    }



    void setDefaultTabWhenLoad() {

        dataBinding.tabLayout.addOnTabSelectedListener(this);
        dataBinding.tabLayout.getTabAt(0).getCustomView().setSelected(true);

        dataBinding.tabLayout.getTabAt(0).select();

    }

    private View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.home_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTextView);
        tv.setText(getTabText(position));

        //set full width if only 1 tab
        if(dataBinding.tabLayout.getTabCount()==1)
            tv.getLayoutParams().width= Utility.getScreenSize(this).x;

        return v;
    }

    private String getTabText(int position) {
        String[] arrTabText = new String[]{"Home","Test","default"};
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
                replaceSubFragment(HomeFragment.instantiate(this,HomeFragment.class.getName()));
                break;
            case 1:
                replaceSubFragment(HomeFragment.instantiate(this,TestFragment.class.getName()));
                break;
            default:
                replaceSubFragment(HomeFragment.instantiate(this,TestFragment.class.getName()));
                break;
        }
    }


    public void replaceSubFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment, fragment.getClass().getName());
        ft.commit();
    }



}
