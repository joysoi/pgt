package com.nikola.medialock;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.data.LockedFolderHelper;
import com.nikola.medialock.locked.albums.ui.LockedAlbumsFragment;
import com.nikola.medialock.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        initFragments();
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateLockedAlbumActivity.class));
            }
        });
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    private void initFragments() {
        if (LockedFolderHelper.exists()) {
            if (LockedAlbumsOperations.getProtectedAlbums().size() == 0) {
                startFragment(new NoLockedAlbumsFragment());
            } else {
                startFragment(new LockedAlbumsFragment());
            }
        } else {startFragment(new NoLockedAlbumsFragment());}

    }
    private void startFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
