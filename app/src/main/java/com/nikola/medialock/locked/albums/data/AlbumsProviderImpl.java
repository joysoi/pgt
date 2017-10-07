package com.nikola.medialock.locked.albums.data;

import android.os.Handler;
import android.os.Looper;

import com.nikola.medialock.data.LockedAlbumsOperations;


public class AlbumsProviderImpl implements AlbumsProvider{

  @Override
  public void loadDataSet(final Callback callback) {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        callback.onLoadFinished(LockedAlbumsOperations.getProtectedAlbums());
      }
    });
  }
}
