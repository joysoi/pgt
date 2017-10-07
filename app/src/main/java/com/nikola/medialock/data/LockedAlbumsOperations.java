package com.nikola.medialock.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.nikola.medialock.model.AlbumEntry;
import com.nikola.medialock.model.AlbumEntryMulti;
import com.nikola.medialock.model.MediaEntry;
import com.nikola.medialock.util.EncryptionUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

public class LockedAlbumsOperations {

  private static final String ALBUM_PREFS = "ALBUM_NAME";
  private static final String PROTECTED_FOLDER = ".mediaLock";
  private static final String THUMBS_FOLDER = "thumbs";
  private static final int QUALITY_COMPRESSION = 50;

  public static boolean isAlbumCreated(String albumName) {
    if (LockedFolderHelper.isCreated()) {
      File dir =
          new File(Environment.getExternalStorageDirectory() + File.separator + PROTECTED_FOLDER,
              albumName);
      try {
        if (!dir.exists()) {
          if (dir.mkdir()) {
            return true;
          } else {
            Timber.e("Folder not Created");
            return false;
          }
        } else {
          return true;
        }
      } catch (Exception e) {
        Timber.e(e.getMessage());
      }
    }
    return false;
  }

  public static List<AlbumEntryMulti> getProtectedAlbums() {
    File protectedDir =
        new File(Environment.getExternalStorageDirectory() + File.separator + PROTECTED_FOLDER);
    FileFilter fileFilter = new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory();
      }
    };
    File[] subDirs = protectedDir.listFiles(fileFilter);
    List<AlbumEntryMulti> albumEntries = new ArrayList<>();
    if (subDirs != null) {
      for (File subDir : subDirs) {
        albumEntries.add(new AlbumEntryMulti(subDir.getName(), false));
      }
    }
    return albumEntries;
  }

  public static List<AlbumEntry> getProtectedAlbumsWithImageAndVideoCount() {
    File protectedDir =
        new File(Environment.getExternalStorageDirectory() + File.separator + PROTECTED_FOLDER);
    FileFilter fileFilter = new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory();
      }
    };
    File[] subDirs = protectedDir.listFiles(fileFilter);
    List<AlbumEntry> albumEntries = new ArrayList<>();
    if (subDirs != null) {
      for (File subDir : subDirs) {
        albumEntries.add(
            new AlbumEntry(subDir.getName(),
                getFilesFromAlbum(subDir.getName()).size()));
      }
    }
    return albumEntries;
  }

  public static List<String> getProtectedAlbumsNames() {
    File protectedDir =
        new File(Environment.getExternalStorageDirectory() + File.separator + PROTECTED_FOLDER);
    FileFilter fileFilter = new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory();
      }
    };
    File[] subDirs = protectedDir.listFiles(fileFilter);
    List<String> names = new ArrayList<>();
    if (subDirs != null) {
      for (File subDir : subDirs) {
        names.add(subDir.getName());
      }
    }
    return names;
  }

  public static File getCoverPhoto(String album) {
    List<String> allFilesFromAlbum = getAllTitlesFromAlbum(album);
    if (allFilesFromAlbum != null && !allFilesFromAlbum.isEmpty()) {
      return getMediaFile(allFilesFromAlbum.get(allFilesFromAlbum.size() - 1), album, true);
    }
    return new File("");
  }

  public static List<String> getAllTitlesFromAlbum(String album) {
    File albumDir = new File(Environment.getExternalStorageDirectory()
        + File.separator
        + PROTECTED_FOLDER
        + File.separator
        + album);
    FileFilter fileFilter = new FileFilter() {
      public boolean accept(File file) {
        return file.isFile();
      }
    };
    File[] subDirs = albumDir.listFiles(fileFilter);
    sortFiles(subDirs);
    List<String> files = new ArrayList<>();
    if (subDirs != null) {
      for (File subDir : subDirs) {
        files.add(subDir.getName());
      }
    }
    return files;
  }

  public static List<MediaEntry> getFilesFromAlbum(String album) {
    File albumDir = new File(Environment.getExternalStorageDirectory()
        + File.separator
        + PROTECTED_FOLDER
        + File.separator
        + album
        + File.separator
        + THUMBS_FOLDER);
    FileFilter fileFilter = new FileFilter() {
      public boolean accept(File file) {
        return file.isFile();
      }
    };
    File[] subDirs = albumDir.listFiles(fileFilter);
    sortFiles(subDirs);
    List<MediaEntry> files = new ArrayList<>();
    if (subDirs != null) {
          for (File subDir : subDirs) {
            files.add(new MediaEntry(subDir.getName(), "", false));
          }
    }
    return files;
  }

  private static void sortFiles(File[] subDirs) {
    Arrays.sort(subDirs, new Comparator<File>() {
      public int compare(File f1, File f2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          return Long.compare(f1.lastModified(), f2.lastModified());
        }
        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
      }
    });
  }

  public static String getAlbumName(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context).getString(ALBUM_PREFS, "");
  }

  public static void saveAlbumName(Context context, String albumName) {
    PreferenceManager.getDefaultSharedPreferences(context)
        .edit()
        .putString(ALBUM_PREFS, albumName)
        .commit();
  }

  public static File getMediaFile(String fileName, String album, boolean isThumb) {
    if (isThumb) {
      return new File(Environment.getExternalStorageDirectory()
          + File.separator
          + PROTECTED_FOLDER
          + File.separator
          + album
          + File.separator
          + THUMBS_FOLDER
          + File.separator
          + fileName);
    } else {
      return new File(Environment.getExternalStorageDirectory()
          + File.separator
          + PROTECTED_FOLDER
          + File.separator
          + album
          + File.separator
          + fileName);
    }
  }

  public static String getMediaPath(String fileName, String album) {
    return Environment.getExternalStorageDirectory() + File.separator +
        PROTECTED_FOLDER + File.separator + album + File.separator + fileName;
  }

  public static boolean isFileDeletedPermanently(String fileName, String album) {
    deleteThumb(fileName, album);
    return new File(Environment.getExternalStorageDirectory() + File.separator +
        PROTECTED_FOLDER + File.separator + album + File.separator + fileName).delete();
  }

  public static void deleteThumb(String fileName, String album) {
    new File(Environment.getExternalStorageDirectory()
        + File.separator
        +
        PROTECTED_FOLDER
        + File.separator
        + album
        + File.separator
        + THUMBS_FOLDER
        + File.separator
        + fileName).delete();
  }

  public static void moveFileToLockedAlbum(Context context, File file) {

    String albumName = LockedAlbumsOperations.getAlbumName(context);
    if (LockedAlbumsOperations.isAlbumCreated(albumName)) {

      File lockedMedia = new File(Environment.getExternalStorageDirectory(),
          PROTECTED_FOLDER + File.separator + albumName + File.separator + EncryptionUtil.encode(
              file));
      FileChannel outputChannel = null;
      FileChannel inputChannel = null;
      try {
        outputChannel = new FileOutputStream(lockedMedia).getChannel();
        inputChannel = new FileInputStream(file).getChannel();
        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        inputChannel.close();

        if (isThumbnailFolderCreated(albumName)) {
          saveThumb(file, albumName);
        }

        MediaOperations.deleteImageFromPreviousLocation(file, context);
      } catch (Exception e) {
        Timber.e(e.getMessage());
      } finally {
        if (inputChannel != null) {
          try {
            inputChannel.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (outputChannel != null) {
          try {
            outputChannel.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } else {
      Timber.e("Image not Locked");
    }
  }

  private static void saveThumb(File file, String albumName) throws FileNotFoundException {
    Bitmap thumbnail;
    Bitmap bitmap;
    FileOutputStream out;
      bitmap =
          BitmapFactory.decodeFile(file.getPath());
      thumbnail =
          Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2,
              false);
    try {
      out = new FileOutputStream(new File(new File(Environment.getExternalStorageDirectory(),
          PROTECTED_FOLDER + File.separator + albumName + File.separator + THUMBS_FOLDER),
          EncryptionUtil.encode(
              file)));
      thumbnail.compress(Bitmap.CompressFormat.JPEG, QUALITY_COMPRESSION, out);
      out.flush();
      out.close();
      bitmap.recycle();
      thumbnail.recycle();
    } catch (Exception e) {
      Timber.e(e.getMessage());
    }
  }

  private static boolean isThumbnailFolderCreated(String albumName) {
    File thumbFolder = new File(Environment.getExternalStorageDirectory(),
        PROTECTED_FOLDER + File.separator + albumName + File.separator + THUMBS_FOLDER);
    return thumbFolder.exists() || thumbFolder.mkdir();
  }

  public static void unlockFile(String fileName, String albumName, Context context)
      throws UnsupportedEncodingException {
    FileChannel outputChannel = null;
    FileChannel inputChannel = null;
    try {
      outputChannel = new FileOutputStream
          (unlockedFile(fileName)).getChannel();
      inputChannel = new FileInputStream(getMediaFile(fileName, albumName, false)).getChannel();
      inputChannel.transferTo(0, inputChannel.size(), outputChannel);
      inputChannel.close();
      if (isFileDeletedPermanently(fileName, albumName)) {
        deleteThumb(fileName, albumName);
        // todo: if multiple files are unlocked at the same time trigger the MediaScanner to scan the sd folder
        scanFile(context, new String[] {EncryptionUtil.decodeToString(fileName)});
      } else {
        Timber.e("NOT DELETED");
      }
    } catch (Exception e) {
      Timber.e(e.getMessage());
    } finally {
      if (inputChannel != null) {
        try {
          inputChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (outputChannel != null) {
        try {
          outputChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  //todo: check the readability on below L versions

  /**
   * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +
   * Environment.getExternalStorageDirectory())));
   * if necessary
   */
  private static File unlockedFile(String fileName) throws UnsupportedEncodingException {
    File unlockedFile = EncryptionUtil.decodeToFile(fileName);
    if (unlockedFile.exists()) {
      return new File(changeName(EncryptionUtil.decodeToString(fileName)));
    }
    return unlockedFile;
  }

  private static String changeName(String s) {
    return s.replace(".", "_1.");
  }

  private static void scanFile(Context context, String[] fileName) {
    MediaScannerConnection.scanFile(context,
        fileName, null, new MediaScannerConnection.OnScanCompletedListener() {
          @Override
          public void onScanCompleted(String path, Uri uri) {
            Timber.e("Scan Completed: " + path);
          }
        });
  }

  public static boolean isAlbumDeleted(String albumName) {
    return deleteThumbnailFolder(
        new File(Environment.getExternalStorageDirectory() + File.separator +
            PROTECTED_FOLDER + File.separator + albumName + File.separator + THUMBS_FOLDER))
        && new File(Environment.getExternalStorageDirectory() + File.separator +
        PROTECTED_FOLDER + File.separator + albumName).delete();
  }

  private static boolean deleteThumbnailFolder(File thumbnailFolder) {
    for (File file : thumbnailFolder.listFiles()) {
      file.delete();
    }
    return thumbnailFolder.delete();
  }
}
