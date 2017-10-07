package com.nikola.medialock.locked.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nikola.medialock.MainActivity;
import com.nikola.medialock.R;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.gallery.GalleryAlbumsActivity;
import com.nikola.medialock.gallery.adapter.ItemOffsetDecoration;
import com.nikola.medialock.locked.ui.adapter.SingleLockedAlbumAdapter;
import com.nikola.medialock.model.MediaEntry;
import com.nikola.medialock.ui.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleLockedAlbumTabHolderActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_no_files)
    TextView textViewNoFiles;
    private static final String ALBUM_NAME = "albumName";
    private static final int COLUMN_NUMBER = 3;
    private String albumName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        albumName = getIntent().getStringExtra(ALBUM_NAME);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(albumName);
        setupLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupAdapter(LockedAlbumsOperations.getFilesFromAlbum(albumName));
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.locked_files_layout;
    }

    private void setupLayout() {
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this, COLUMN_NUMBER);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.space);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupAdapter(List<MediaEntry> mediaEntries) {
        if (mediaEntries.size() != 0) {
            updateAdapter(mediaEntries);
        } else {
            setEmptyLayout(View.VISIBLE, View.GONE);
        }
    }

    private void setEmptyLayout(int visibilityX, int visibilityY) {
        textViewNoFiles.setVisibility(visibilityX);
        recyclerView.setVisibility(visibilityY);
    }

    private void updateAdapter(List<MediaEntry> mediaEntries) {
        final SingleLockedAlbumAdapter adapter =
                new SingleLockedAlbumAdapter(mediaEntries,
                        this,
                        albumName);
        recyclerView.setAdapter(adapter);
        setEmptyLayout(View.GONE, View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_lock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add: {
                LockedAlbumsOperations.saveAlbumName(getApplicationContext(), albumName);
                startActivity(new Intent(this, GalleryAlbumsActivity.class));
                return true;
            }
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
