package com.dashuai.oclick;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

//import com.dashuai.oclick.Needle;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 pager;
    private TabLayout tab_needle;
    private static final int NUM_PAGES = 2;
    private FragmentStateAdapter pagerAdapter;
    private String tab_name[]={"Needle","Digit"};
    //public Needle needle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //needle=findViewById(R.id.clock);
        pager=findViewById(R.id.pager);
        tab_needle=findViewById(R.id.tab_needle);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        pager.setAdapter(pagerAdapter);
        //tab_needle.setupWithViewPager(pager_needle);
        new TabLayoutMediator(tab_needle, pager,
                (tab, position) ->{
                    tab.setText(tab_name[(position )]);
                }
        ).attach();
    }
    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(MainActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            if(position==0){
                return new needleFragment();
            }else if(position==1){
                return new DigitFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}