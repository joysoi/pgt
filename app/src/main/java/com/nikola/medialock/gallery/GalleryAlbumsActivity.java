package com.nikola.medialock.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nikola.medialock.R;
import com.nikola.medialock.data.MediaOperations;
import com.nikola.medialock.gallery.adapter.GalleryAlbumsAdapter;
import com.nikola.medialock.gallery.adapter.SpaceItemDecorNoEdge;
import com.nikola.medialock.ui.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GalleryAlbumsActivity extends BaseActivity {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.txt_no_files)
  TextView textViewNoFiles;
  private static final int COLUMN_COUNT = 3;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    assert getSupportActionBar() != null;
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(getResources().getString(R.string.select_photos_videos));

    setupAdapter(MediaOperations.getAlbums(this));
  }

  private void setupAdapter(List<String> albumEntries) {
    if (albumEntries.size() != 0) {
      updateAdapter(albumEntries);
    } else {
      setEmptyLayout(View.VISIBLE, View.GONE);
    }
  }

  private void updateAdapter(List<String> albumEntries) {
    GalleryAlbumsAdapter galleryAlbumsAdapter =
        new GalleryAlbumsAdapter(albumEntries, this);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, COLUMN_COUNT);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new SpaceItemDecorNoEdge(getResources().getDimensionPixelSize(R.dimen.space),
            getResources().getDimensionPixelSize(R.dimen.top_space), 0, COLUMN_COUNT));
    recyclerView.setAdapter(galleryAlbumsAdapter);
    setEmptyLayout(View.GONE, View.VISIBLE);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected int getContentViewLayoutId() {
    return R.layout.activity_gallery_albums_layout;
  }

  private void setEmptyLayout(int visibilityX, int visibilityY) {
    textViewNoFiles.setVisibility(visibilityX);
    recyclerView.setVisibility(visibilityY);
  }
}
