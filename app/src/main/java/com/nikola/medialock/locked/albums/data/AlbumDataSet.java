package com.nikola.medialock.locked.albums.data;

import com.nikola.medialock.model.AlbumEntryMulti;

import java.util.ArrayList;
import java.util.List;



public class AlbumDataSet {

  private List<AlbumEntryMulti> items = new ArrayList<>();

  public List<AlbumEntryMulti> getFiles() {
    return items;
  }

  public void setFiles(List<AlbumEntryMulti> items) {
    this.items = items;
  }

  private void deleteAllFiles(AlbumEntryMulti file) {
    for (int i = 0; i < items.size(); i++) {
      if (items.contains(file)) {
        items.remove(file);
        return;
      }
    }
  }

  public void deleteFiles(List<AlbumEntryMulti> filesToDelete) {
    for (AlbumEntryMulti file : filesToDelete) {
      deleteAllFiles(file);
    }
  }

  public void clear() {
    items.clear();
  }
}
