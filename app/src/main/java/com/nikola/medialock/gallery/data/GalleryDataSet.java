package com.nikola.medialock.gallery.data;

import com.nikola.medialock.model.MediaEntry;

import java.util.ArrayList;
import java.util.List;



public class GalleryDataSet {

  private List<MediaEntry> items = new ArrayList<>();

  public List<MediaEntry> getFiles() {
    return items;
  }


  public void setFiles(List<MediaEntry> items) {
    this.items = items;
  }
  private void deleteAllFiles(MediaEntry file) {
    for (int i = 0; i < items.size(); i++) {
      if (items.contains(file)) {
        items.remove(file);
        return;
      }
    }
  }
  public void deleteFiles(List<MediaEntry> filesToDelete) {
    for (MediaEntry file : filesToDelete) {
      deleteAllFiles(file);
    }
  }
  public void clear() {
    items.clear();
  }
}
