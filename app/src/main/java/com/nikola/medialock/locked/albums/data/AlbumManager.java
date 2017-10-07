package com.nikola.medialock.locked.albums.data;

import com.nikola.medialock.model.AlbumEntryMulti;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class AlbumManager implements AlbumsProvider.Callback {

  public interface EventListener {

    void updateAllItems(List<AlbumEntryMulti> files);
  }

  private static AlbumManager albumManager;
  private AlbumsProvider albumsProvider = new AlbumsProviderImpl();
  private final AlbumDataSet files;

  private EventListener eventListener;

  private AlbumManager() {
    files = new AlbumDataSet();
  }

  public static AlbumManager getInstance() {
    if (albumManager == null) {
      albumManager = new AlbumManager();
    }
    return albumManager;
  }

  public static AlbumManager destroyInstance() {
    if (albumManager != null) {
      albumManager = null;
    }
    return null;
  }

  public void loadDataSet() {
    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(new Runnable() {
      @Override
      public void run() {
        albumsProvider.loadDataSet(AlbumManager.this);
      }
    });
    executorService.shutdown();
  }

  public void update(AlbumEntryMulti file) {
    file.toggleLockedStatus();
  }

  public void deleteFiles(List<AlbumEntryMulti> filesToDelete) {
    files.deleteFiles(filesToDelete);
    notifyListener();
    filesToDelete.clear();
  }

  public List<AlbumEntryMulti> getSelectedItems() {
    List<AlbumEntryMulti> allFiles = files.getFiles();
    List<AlbumEntryMulti> selected = new ArrayList<>();
    for (AlbumEntryMulti mediaModel : allFiles) {
      if (mediaModel.selectedStatus) {
        selected.add(mediaModel);
      }
    }
    return selected;
  }

  public void clearDataSet() {
    files.clear();
  }

  // TODO: this can cause NULL pointer exception. The Album manager is singleton
  // TODO: and the objects you register here might happen to become null,
  // TODO: so when you call the eventListener methods it will crash the app
  public void registerEventListener(EventListener eventListener) {
    this.eventListener = eventListener;
  }

  public void unregisterEventListener() {
    this.eventListener = null;
  }

  private void notifyListener() {
    eventListener.updateAllItems(files.getFiles());
  }

  public List<AlbumEntryMulti> getFiles() {
    return files.getFiles();
  }

  @Override
  public void onLoadFinished(List<AlbumEntryMulti> items) {
    files.setFiles(items);
    notifyListener();
  }
}