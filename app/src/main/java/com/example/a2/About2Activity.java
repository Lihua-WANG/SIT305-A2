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

import java.util.ArrayList;
import java.util.List;

public class About2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView AboutAI, AboutFight, AboutBasic, AboutVideo;
    private ViewPager vp;
    private AboutAIFragment aboutAIFragment;
    private AboutFightFragment aboutFightFragment;
    private AboutBasicFragment aboutBasicFragment;
    private AboutVideoFragment aboutVideoFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about2);
        initViews();

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(2);//ViewPager的缓存为2帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        AboutAI.setTextColor(Color.parseColor("#485607"));

        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
        AboutAI = (TextView) findViewById(R.id.AboutAI);
        AboutFight = (TextView) findViewById(R.id.AboutFight);
        AboutBasic = (TextView) findViewById(R.id.AboutBasic);
        AboutVideo = (TextView) findViewById(R.id.AboutVideo);

        AboutAI.setOnClickListener(this);
        AboutFight.setOnClickListener(this);
        AboutBasic.setOnClickListener(this);
        AboutVideo.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        aboutAIFragment = new AboutAIFragment();
        aboutFightFragment = new AboutFightFragment();
        aboutBasicFragment = new AboutBasicFragment();
        aboutVideoFragment = new AboutVideoFragment();
        //给FragmentList添加数据
        mFragmentList.add(aboutAIFragment);
        mFragmentList.add(aboutFightFragment);
        mFragmentList.add(aboutBasicFragment);
        mFragmentList.add(aboutVideoFragment);
    }

    /**
     * 点击头部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AboutAI:
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
     * 由ViewPager的滑动修改头部导航Text的颜色
     *
     * @param position
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            AboutAI.setTextColor(Color.parseColor("#485607"));
            AboutFight.setTextColor(Color.parseColor("#000000"));
            AboutBasic.setTextColor(Color.parseColor("#000000"));
            AboutVideo.setTextColor(Color.parseColor("#000000"));
        } else if (position == 1) {
            AboutAI.setTextColor(Color.parseColor("#000000"));
            AboutFight.setTextColor(Color.parseColor("#485607"));
            AboutBasic.setTextColor(Color.parseColor("#000000"));
            AboutVideo.setTextColor(Color.parseColor("#000000"));
        } else if (position == 2) {
            AboutAI.setTextColor(Color.parseColor("#000000"));
            AboutFight.setTextColor(Color.parseColor("#000000"));
            AboutBasic.setTextColor(Color.parseColor("#485607"));
            AboutVideo.setTextColor(Color.parseColor("#000000"));
        } else if (position == 3) {
            AboutAI.setTextColor(Color.parseColor("#000000"));
            AboutFight.setTextColor(Color.parseColor("#000000"));
            AboutBasic.setTextColor(Color.parseColor("#000000"));
            AboutVideo.setTextColor(Color.parseColor("#485607"));
        }
    }
}
