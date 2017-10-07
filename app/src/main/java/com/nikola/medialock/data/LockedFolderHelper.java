package com.nikola.medialock.data;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

public class LockedFolderHelper {

  private static final String PROTECTED_FOLDER_HIDDEN = ".mediaLock";
  private static final String NO_MEDIA = ".nomedia";

  public static boolean isCreated() {

    final File hiddenDir =
        new File(Environment.getExternalStorageDirectory(), PROTECTED_FOLDER_HIDDEN);
    try {
      if (!hiddenDir.exists()) {
        if (hiddenDir.mkdir()) {
          createNoMediaFile();
          return true;
        } else {
          Timber.e("Folder not Created");
          return false;
        }
      } else {
        createNoMediaFile();
        return true;
      }
    } catch (Exception e) {
      Timber.e(e.getMessage());
    }
    return false;
  }

  private static void createNoMediaFile() {
    try {
      if (!new File(
          Environment.getExternalStorageDirectory() + File.separator + PROTECTED_FOLDER_HIDDEN,
          NO_MEDIA).exists()) {
        if (new File(
            Environment.getExternalStorageDirectory() + File.separator + PROTECTED_FOLDER_HIDDEN,
            NO_MEDIA).createNewFile()) {
          Timber.v(".nomedia File created");
        } else {
          Timber.e(".nomedia File not created");
        }
      } else {
        Timber.v(".nomedia already File created");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean exists() {
    return new File(Environment.getExternalStorageDirectory(), PROTECTED_FOLDER_HIDDEN).exists();
  }
}
