package com.nikola.medialock.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.model.MediaEntry;

import java.io.UnsupportedEncodingException;
import java.util.List;



public class DeleteUnlockAsyncTask extends LockerAsyncTask<Void, Void, Boolean> {

  private boolean isDeleted;
  private List<MediaEntry> selectedFiles;
  private String albumName;
  private String fileName;

  public DeleteUnlockAsyncTask(Context context, @NonNull List<MediaEntry> selectedFiles,
                               String albumName, boolean isDeleted, OnTaskCompleted onTaskCompleted) {
    super(context, onTaskCompleted);
    this.isDeleted = isDeleted;
    this.selectedFiles = selectedFiles;
    this.albumName = albumName;
  }

  public DeleteUnlockAsyncTask(Context context, String fileName, String albumName,
                               boolean isDeleted, OnTaskCompleted onTaskCompleted) {
    super(context, onTaskCompleted);
    this.isDeleted = isDeleted;
    this.fileName = fileName;
    this.albumName = albumName;
  }

  @Override
  protected Boolean doInBackground(Void... params) {

    return fileName != null ? unlockDeleteFile() : unlockDeleteFiles();
  }

  @NonNull
  private Boolean unlockDeleteFile() {
    return isDeleted ? LockedAlbumsOperations.isFileDeletedPermanently(fileName, albumName)
                     : unlockFile(fileName);
  }

  @NonNull
  private Boolean unlockDeleteFiles() {

    for (MediaEntry fileName : selectedFiles) {
      if (isDeleted) {
        LockedAlbumsOperations.isFileDeletedPermanently(fileName.title,
            albumName);
      } else {unlockFile(fileName.title);}
      // Todo: for does not loop?
      //return isDeleted ? LockedAlbumsOperations.isFileDeletedPermanently(fileName.title,
      //    albumName) : unlockFile(fileName.title);
    }
    return false;
  }

  private boolean unlockFile(String fileName) {
    try {
      LockedAlbumsOperations.unlockFile(fileName, albumName, context);
      return true;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  protected void onPostExecute(Boolean aBoolean) {
    super.onPostExecute(aBoolean);
  }
}