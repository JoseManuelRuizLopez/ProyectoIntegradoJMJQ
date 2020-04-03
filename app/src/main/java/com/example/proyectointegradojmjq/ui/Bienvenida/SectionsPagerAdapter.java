package com.example.proyectointegradojmjq.ui.Bienvenida;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.proyectointegradojmjq.Bienvenida_fragment1;
import com.example.proyectointegradojmjq.Bienvenida_fragment2;
import com.example.proyectointegradojmjq.Bienvenida_fragment3;
import com.example.proyectointegradojmjq.Bienvenida_fragment4;
import com.example.proyectointegradojmjq.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment frag = null;

        switch (position)
        {
            case 0:
                frag = new Bienvenida_fragment1();
                break;
            case 1:
                frag = new Bienvenida_fragment2();
                break;
            case 2:
                frag = new Bienvenida_fragment3();
                break;
            case 3:
                frag = new Bienvenida_fragment4();
        }

        return frag;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount()
    {
        return 4;
    }
}