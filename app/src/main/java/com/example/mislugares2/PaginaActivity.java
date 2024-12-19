package com.example.mislugares2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PaginaActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiPagerAdapter(this));

        TabLayout tabs = findViewById(R.id.tabs);

        // 配置 TabLayout 并使用图标
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setIcon(R.drawable.home); // Home 图标
                                break;
                            case 1:
                                tab.setIcon(R.drawable.monitorizacion); // Monitorización 图标
                                break;
                            case 2:
                                tab.setIcon(R.drawable.configuracion); //
                                break;
                        }
                    }
                }
        ).attach();
    }

    class MiPagerAdapter extends FragmentStateAdapter {
        public MiPagerAdapter(FragmentActivity activity){
            super(activity);
        }
        @Override
        public int getItemCount() {
            return 3;
        }
        @Override @NonNull
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new HomeFragment();
                case 1: return new MonitorizacionFragment();
                case 2: return new PerfilFragment();
            }
            return null;
        }
    }
}
