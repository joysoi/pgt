package com.nikola.medialock.gallery.data;

import android.content.Context;

import com.nikola.medialock.model.MediaEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GalleryItemsManager implements GalleryProvider.Callback {

  public interface EventListener {

    void updateAllItems(List<MediaEntry> files);
  }

  private static GalleryItemsManager galleryItemsManager;
  private GalleryProvider galleryProvider = new GalleryProviderImpl();
  private final GalleryDataSet files;
  private List<MediaEntry> allFiles;
  private EventListener eventListener;

  private GalleryItemsManager() {
    files = new GalleryDataSet();
  }

  public static GalleryItemsManager getInstance() {
    if (galleryItemsManager == null) {
      galleryItemsManager = new GalleryItemsManager();
    }
    return galleryItemsManager;
  }

  public static GalleryItemsManager destroyInstance() {
    if (galleryItemsManager != null) {
      galleryItemsManager = null;
    }
    return null;
  }
  public void loadDataSet(final Context context, final String albumName) {
    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(new Runnable() {
      @Override
      public void run() {
        galleryProvider.loadDataSet(context,albumName, GalleryItemsManager.this);
      }
    });
    executorService.shutdown();
  }

  public void update(MediaEntry file) {
    file.toggleLockedStatus();
  }
  public void selectAll(){
    List<MediaEntry> allFiles = files.getFiles();
    for (MediaEntry mediaModel : allFiles) {
      if (!mediaModel.selectedStatus) {
        mediaModel.selectedStatus = true;
      }
    }
    notifyListener();
  }

  public void deleteFiles(List<MediaEntry> filesToDelete) {
    files.deleteFiles(filesToDelete);
    notifyListener();
    filesToDelete.clear();
  }

  public List<MediaEntry> getSelectedItems() {
    allFiles = files.getFiles();
    List<MediaEntry> selected = new ArrayList<>();
    for (MediaEntry mediaModel : allFiles) {
      if (mediaModel.selectedStatus) {
        selected.add(mediaModel);
      }
    }
    return selected;
  }
  public boolean areAllItemsSelected(){
    allFiles = files.getFiles();
    return getSelectedItems().size() == allFiles.size();
  }
  public void clearDataSet() {
    files.clear();
  }

  public void registerEventListener(EventListener eventListener) {
    this.eventListener = eventListener;
  }

  public void unregisterEventListener() {
    this.eventListener = null;
  }

  private void notifyListener() {
    eventListener.updateAllItems(files.getFiles());
  }
  public List<MediaEntry> getFiles() {
    return files.getFiles();
  }

  @Override
  public void onLoadFinished(List<MediaEntry> items) {
    files.setFiles(items);
    notifyListener();
  }
}
