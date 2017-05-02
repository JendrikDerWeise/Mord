package com.example.jendrik.moerder.GUI.OnGamingClasses;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jendrik.moerder.GUI.LittleHelpers.TabContent.ViewPagerAdapter;
import com.example.jendrik.moerder.R;

import java.lang.reflect.Field;


public class NoticeList extends Fragment {

    private View fragLayoutV;
    private FragmentTabHost tabHost;

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragLayoutV = inflater.inflate(R.layout.fragment_list, container, false);
        createTabs();
        if(!com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.myTurn){

            fragLayoutV.findViewById(R.id.tablayout).setBackgroundResource(R.color.colorOtherTurn);
            fragLayoutV.findViewById(R.id.viewpager).setBackgroundResource(R.color.colorOtherTurnLight);


        }else {

            fragLayoutV.findViewById(R.id.tablayout).setBackgroundResource(R.color.colorPrimary);
            fragLayoutV.findViewById(R.id.viewpager).setBackgroundResource(R.color.colorPrimaryDark);


        }
        return fragLayoutV;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Methode baut ein Tab-Dingsi
     * Die "onTabSelected" Methoden sprechen fuer sich.
     */
    private void createTabs(){

        //toolbar = (Toolbar) fragLayoutV.findViewById(R.id.toolbar1);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        viewPager = (ViewPager) fragLayoutV.findViewById(R.id.viewpager);
        Context context = getActivity().getApplicationContext();
        final ViewPagerAdapter viewPagerAdpter = new ViewPagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager(), context);
        viewPager.setAdapter(viewPagerAdpter);


        tabLayout = (TabLayout) fragLayoutV.findViewById(R.id.tablayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
