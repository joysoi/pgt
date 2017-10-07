package com.nikola.medialock.gallery.data;

import android.content.Context;

import com.nikola.medialock.model.MediaEntry;

import java.util.List;


interface GalleryProvider {

  interface Callback {

    void onLoadFinished(List<MediaEntry> items);
  }

  void loadDataSet(Context context, String albumName, GalleryProvider.Callback callback);
}
