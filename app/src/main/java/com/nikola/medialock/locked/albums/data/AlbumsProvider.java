package com.nikola.medialock.locked.albums.data;

import com.nikola.medialock.model.AlbumEntryMulti;

import java.util.List;



interface AlbumsProvider {

  interface Callback {

    void onLoadFinished(List<AlbumEntryMulti> items);
  }

  void loadDataSet(AlbumsProvider.Callback callback);
}
