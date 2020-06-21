package com.example.bkfoodcourt;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Cook extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private Fragment homeFragment, orderListFragment, notiFragment, profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        mMainNav = findViewById(R.id.mainNavigation);
        mMainFrame = findViewById(R.id.mainFrame);
        homeFragment = new HomeFragment();
        orderListFragment = new OrderListFragment();
        notiFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();
        
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navHome:
                        setFragment(homeFragment);
                        return true;
                    case R.id.navOrderList:
                        setFragment(orderListFragment);
                        return true;
                    case R.id.navNoti:
                        setFragment(notiFragment);
                        return true;
                    case R.id.navProfile:
                        setFragment(profileFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }
}