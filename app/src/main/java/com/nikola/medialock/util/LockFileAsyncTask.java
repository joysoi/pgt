package com.nikola.medialock.util;

import android.content.Context;
import android.widget.Toast;

import com.nikola.medialock.R;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.model.MediaEntry;

import java.io.File;
import java.util.List;

import timber.log.Timber;

public class LockFileAsyncTask extends LockerAsyncTask<Void, Void, Boolean> {

  List<MediaEntry> selectedItems;

  public LockFileAsyncTask(Context context, List<MediaEntry> selectedItems,
                           OnTaskCompleted listener) {
    super(context, listener);
    this.selectedItems = selectedItems;
  }

  @Override
  protected Boolean doInBackground(Void... params) {
    try {
      for (MediaEntry mediaEntry : selectedItems) {
        LockedAlbumsOperations.moveFileToLockedAlbum(context, new File(mediaEntry.data));
      }
      return true;
    } catch (Exception e) {
      Timber.e(e.getMessage());
    }
    return false;
  }

  @Override
  protected void onPostExecute(Boolean aBoolean) {
    super.onPostExecute(aBoolean);
    //todo: file / files ?
    showToast(context.getResources().getString(R.string.files_are_locked), Toast.LENGTH_LONG);
  }
}
