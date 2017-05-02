package com.example.jendrik.moerder.GUI.LittleHelpers.TabContent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jendrik.moerder.R;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String[] tabtitlearray = new String[3];

    public ViewPagerAdapter (FragmentManager manager, Context context){

        super(manager);
        tabtitlearray[0] = context.getResources().getString(R.string.tab_person);
        tabtitlearray[1] = context.getResources().getString(R.string.tab_room);
        tabtitlearray[2] = context.getResources().getString(R.string.tab_weapon);
    }



    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0: return new PersonList();
            case 1: return new RoomList();
            case 2: return new WeaponList();
        }


        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabtitlearray[position];
    }


} 

