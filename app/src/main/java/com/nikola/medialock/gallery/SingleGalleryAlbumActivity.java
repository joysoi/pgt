package com.nikola.medialock.gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.nikola.medialock.R;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.gallery.adapter.GalleryAlbumsAdapter;
import com.nikola.medialock.gallery.adapter.SingleGalleryAlbumAdapter;
import com.nikola.medialock.gallery.adapter.SpaceItemDecorNoEdge;
import com.nikola.medialock.gallery.data.GalleryItemsManager;
import com.nikola.medialock.locked.ui.SingleLockedAlbumTabHolderActivity;
import com.nikola.medialock.model.MediaEntry;
import com.nikola.medialock.ui.BaseActivity;
import com.nikola.medialock.util.LockFileAsyncTask;
import com.nikola.medialock.util.OnTaskCompleted;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SingleGalleryAlbumActivity extends BaseActivity
    implements GalleryItemsManager.EventListener, OnTaskCompleted {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  private SingleGalleryAlbumAdapter adapter;
  private GalleryItemsManager galleryItemsManager = GalleryItemsManager.getInstance();
  private List<MediaEntry> selectedItems;
  private static final String ALBUM_NAME = "albumName";
  private static final int COLUMN_COUNT = 3;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    String albumName = getIntent().getStringExtra(
        GalleryAlbumsAdapter.ALBUM_NAME);
    galleryItemsManager.loadDataSet(this, albumName);
    galleryItemsManager.registerEventListener(this);
    assert getSupportActionBar() != null;
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(getResources().getString(R.string.select_photos_videos));
    initAdapter();
  }

  private void initAdapter() {
    adapter = new SingleGalleryAlbumAdapter(
        galleryItemsManager.getFiles(),
        SingleGalleryAlbumActivity.this);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, COLUMN_COUNT);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.addItemDecoration(
        new SpaceItemDecorNoEdge(getResources().getDimensionPixelSize(R.dimen.space),
            0, getResources().getDimensionPixelSize(R.dimen.space), COLUMN_COUNT));
    recyclerView.setAdapter(adapter);
  }

  @Override
  protected int getContentViewLayoutId() {
    return R.layout.activity_single_gallery_layout;
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
  protected void onDestroy() {
    super.onDestroy();
    galleryItemsManager.unregisterEventListener();
    galleryItemsManager.clearDataSet();
    galleryItemsManager = GalleryItemsManager.destroyInstance();
  }

  @OnClick(R.id.btn_select_all)
  public void onClickDelete() {
    if (!galleryItemsManager.areAllItemsSelected()) {
      galleryItemsManager.selectAll();
    } else {
      showToast(getResources().getString(R.string.items_already_selected), Toast.LENGTH_SHORT);
    }
  }

  @OnClick(R.id.btn_lock)
  public void onClickUnlock() {
    selectedItems = galleryItemsManager.getSelectedItems();
    if (selectedItems.size() != 0) {
      showConformationDialog(this, selectedItems, this);
    } else {
      showToast(getResources().getString(R.string.please_select_items), Toast.LENGTH_SHORT);
    }
  }

  @Override
  public void updateAllItems(List<MediaEntry> files) {
    adapter.updateDataSet(files);
  }

  @Override
  public void onTaskCompleted() {
    galleryItemsManager.deleteFiles(selectedItems);
    Intent intent = new Intent(this, SingleLockedAlbumTabHolderActivity.class);
    intent.putExtra(ALBUM_NAME, LockedAlbumsOperations.getAlbumName(this));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    this.startActivity(intent);
  }

  private void showConformationDialog(final Context context, final List<MediaEntry> selectedItems,
                                      final OnTaskCompleted onTaskCompleted) {
    new AlertDialog.Builder(this, R.style.AlertDialogStyle)
        .setTitle(getResources().getString(R.string.move_in_title))
        .setMessage(getResources().getString(R.string.move_in_msg))
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            new LockFileAsyncTask(context, selectedItems, onTaskCompleted).execute();
          }
        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
          }
        })
        .show();
  }
}

