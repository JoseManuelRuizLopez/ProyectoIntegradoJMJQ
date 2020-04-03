package com.example.proyectointegradojmjq;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.proyectointegradojmjq.ui.Bienvenida.SectionsPagerAdapter;
import com.rd.PageIndicatorView;

public class BienvenidaUsuario extends AppCompatActivity
{

    ViewPager viewPager;
    PageIndicatorView pageIndicatorView;

    SectionsPagerAdapter sectionsPagerAdapter;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida_usuario);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        pageIndicatorView = findViewById(R.id.pageIndicatorView);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position)
            {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
    }

    public void selectTab(int position)
    {
        viewPager.setCurrentItem(position);
    }
}