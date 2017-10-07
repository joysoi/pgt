package com.nikola.medialock.gallery.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.nikola.medialock.data.MediaOperations;


class GalleryProviderImpl implements GalleryProvider {

  @Override
  public void loadDataSet(final Context context, final String albumName, final Callback callback) {
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        callback.onLoadFinished(MediaOperations.getAllFilesFromAlbum(context, albumName));
      }
    });
  }
}
