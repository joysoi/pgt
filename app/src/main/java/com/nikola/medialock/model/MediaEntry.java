package com.nikola.medialock.model;

public class MediaEntry {

  public String title;
  public String data;
  public boolean selectedStatus;

  public MediaEntry(String title, String data, boolean selectedStatus) {
    this.title = title;
    this.data = data;
    this.selectedStatus = selectedStatus;

  }

  public void toggleLockedStatus() {
    selectedStatus = !selectedStatus;
  }
}
