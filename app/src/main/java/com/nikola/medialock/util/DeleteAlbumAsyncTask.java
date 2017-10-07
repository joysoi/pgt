package com.nikola.medialock.util;

import android.content.Context;
import android.content.Intent;

import com.nikola.medialock.MainActivity;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.data.LockedFolderHelper;

import java.util.List;

import timber.log.Timber;

public class DeleteAlbumAsyncTask extends LockerAsyncTask<Void, Void, Boolean> {

  private String albumName;

  public DeleteAlbumAsyncTask(Context context, String albumName,
                              OnTaskCompleted listener) {
    super(context, listener);
    this.albumName = albumName;
  }

  @Override
  protected Boolean doInBackground(Void... params) {
    try {
        List<String> files =
            LockedAlbumsOperations.getAllTitlesFromAlbum(albumName);
        if (files.size() != 0) {
          for (String file : files) {
            LockedAlbumsOperations.isFileDeletedPermanently(file, albumName);
          }
        }
        Timber.v(LockedAlbumsOperations.isAlbumDeleted(albumName) ? "album deleted"
                                                                              : "album not deleted");
      return true;
    } catch (Exception e) {
      Timber.e(e.getMessage());
    }
    return false;
  }

  @Override
  protected void onPostExecute(Boolean aBoolean) {
    super.onPostExecute(aBoolean);
    //todo: optimise condition method
    if (LockedFolderHelper.exists() && LockedAlbumsOperations.getProtectedAlbums().size() == 0) {
      Intent intent = new Intent(context, MainActivity.class);
      context.startActivity(intent);
    }
  }
}