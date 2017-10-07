package com.nikola.medialock.model;

public class AlbumEntryMulti {

  public String title;
  public boolean selectedStatus;

  public AlbumEntryMulti(String title, boolean selectedStatus) {
    this.title = title;
    this.selectedStatus = selectedStatus;
  }

  public void toggleLockedStatus() {
    selectedStatus = !selectedStatus;
  }
}
