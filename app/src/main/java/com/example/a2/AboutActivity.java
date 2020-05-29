package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.TextView;
import android.os.Bundle;

import com.example.a2.About.AboutSingleFragment;
import com.example.a2.About.AboutBasicFragment;
import com.example.a2.About.AboutFightFragment;
import com.example.a2.About.AboutVideoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Use ViewPager + Fragment to implement the sliding menu Tab effect
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView AboutAI, AboutFight, AboutBasic, AboutVideo;
    private ViewPager vp;
    private AboutSingleFragment aboutSingleFragment;
    private AboutFightFragment aboutFightFragment;
    private AboutBasicFragment aboutBasicFragment;
    private AboutVideoFragment aboutVideoFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(2); // ViewPager's cache is 2 frames
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0); // Initially set ViewPager to select the first frame
        AboutAI.setTextColor(Color.parseColor("#55830C"));

        // ViewPager monitoring events
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /*This method is called when the page is selected*/
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*This method is called when the state changes,
                where the arg0 parameter has three states (0, 1, 2).
                arg0==1 implies is sliding,
                arg0==2 implies that the sliding is finished
                arg0==0 implies that nothing is done.*/
            }
        });
    }

    /**
     * Initialize the layout View
     */
    private void initViews() {
        AboutAI = findViewById(R.id.AboutSingle);
        AboutFight = findViewById(R.id.AboutFight);
        AboutBasic = findViewById(R.id.AboutBasic);
        AboutVideo = findViewById(R.id.AboutVideo);

        AboutAI.setOnClickListener(this);
        AboutFight.setOnClickListener(this);
        AboutBasic.setOnClickListener(this);
        AboutVideo.setOnClickListener(this);

        vp = findViewById(R.id.mainViewPager);
        aboutSingleFragment = new AboutSingleFragment();
        aboutFightFragment = new AboutFightFragment();
        aboutBasicFragment = new AboutBasicFragment();
        aboutVideoFragment = new AboutVideoFragment();

        // Add data to FragmentList
        mFragmentList.add(aboutSingleFragment);
        mFragmentList.add(aboutFightFragment);
        mFragmentList.add(aboutBasicFragment);
        mFragmentList.add(aboutVideoFragment);
    }

    /**
     * Click the head Text to dynamically modify the content of ViewPager
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AboutSingle:
                vp.setCurrentItem(0, true);
                break;
            case R.id.AboutFight:
                vp.setCurrentItem(1, true);
                break;
            case R.id.AboutBasic:
                vp.setCurrentItem(2, true);
                break;
            case R.id.AboutVideo:
                vp.setCurrentItem(3, true);
                break;
        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /**
     * Modify the color of the navigation Text by sliding the ViewPager
     *
     * @param position
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            AboutAI.setTextColor(Color.parseColor("#55830C"));
            AboutFight.setTextColor(Color.parseColor("#FF000000"));
            AboutBasic.setTextColor(Color.parseColor("#FF000000"));
            AboutVideo.setTextColor(Color.parseColor("#FF000000"));
        } else if (position == 1) {
            AboutAI.setTextColor(Color.parseColor("#FF000000"));
            AboutFight.setTextColor(Color.parseColor("#55830C"));
            AboutBasic.setTextColor(Color.parseColor("#FF000000"));
            AboutVideo.setTextColor(Color.parseColor("#FF000000"));
        } else if (position == 2) {
            AboutAI.setTextColor(Color.parseColor("#FF000000"));
            AboutFight.setTextColor(Color.parseColor("#FF000000"));
            AboutBasic.setTextColor(Color.parseColor("#55830C"));
            AboutVideo.setTextColor(Color.parseColor("#FF000000"));
        } else if (position == 3) {
            AboutAI.setTextColor(Color.parseColor("#FF000000"));
            AboutFight.setTextColor(Color.parseColor("#FF000000"));
            AboutBasic.setTextColor(Color.parseColor("#FF000000"));
            AboutVideo.setTextColor(Color.parseColor("#55830C"));
        }
    }
}
