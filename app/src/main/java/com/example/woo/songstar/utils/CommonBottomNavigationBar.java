package com.example.woo.songstar.utils;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.woo.songstar.R;
import com.example.woo.songstar.activities.ArtistsActivity;
import com.example.woo.songstar.activities.FavouriteSongsActivity;
import com.example.woo.songstar.activities.SongsActivity;
import com.example.woo.songstar.models.FavouriteSong;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CommonBottomNavigationBar {
    private static CommonBottomNavigationBar commonBottomNavigationBar;

    public static CommonBottomNavigationBar getInstance() {
        if(commonBottomNavigationBar == null) {
            commonBottomNavigationBar = new CommonBottomNavigationBar();
        }

        return commonBottomNavigationBar;
    }

    public void setCommonBottomNavigationBar(final AppCompatActivity activity, BottomNavigationView bottomNavigationView, final int currentSection) {
        if(currentSection == 0) {
            final Menu menu =  bottomNavigationView.getMenu();
            for(int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setChecked(false);
            }
        } else {
            bottomNavigationView.setSelectedItemId(currentSection);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.artist:
                            if(currentSection != R.id.artist) {
                            Intent intent = new Intent(activity.getApplicationContext(), ArtistsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            activity.finish();
                            return true;
                        }
                        break;
                    case R.id.song:
                        if(currentSection != R.id.song) {
                            Intent intent = new Intent(activity.getApplicationContext(), SongsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            activity.finish();
                            return true;
                        }
                        break;

                    case R.id.favourite:
                        if(currentSection != R.id.favourite) {
                            Intent intent = new Intent(activity.getApplicationContext(), FavouriteSongsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(0, 0);
                            activity.finish();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }
}
