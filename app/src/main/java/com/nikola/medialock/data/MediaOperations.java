package com.nikola.medialock.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.nikola.medialock.model.MediaEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MediaOperations {

  private static final String ID = MediaStore.Files.FileColumns._ID;
  private static final String BUCKET_DISPLAY_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
  private static final Uri QUERY_URI_AND_IMAGES = MediaStore.Files.getContentUri("external");

  public static List<String> getAlbums(Context context) {
    Cursor cursor =
        context.getContentResolver()
            .query(QUERY_URI_AND_IMAGES, new String[] {
                BUCKET_DISPLAY_NAME, ID
            }, MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, null, null);
    List<String> arrayList = new ArrayList<>();
    if (cursor != null) {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        String albumName = cursor.getString(cursor
            .getColumnIndex(BUCKET_DISPLAY_NAME));
        if (!arrayList.contains(albumName)) {
          arrayList.add(albumName);
        }
        cursor.moveToNext();
      }
      cursor.close();
    }
    return arrayList;
  }

  public static List<MediaEntry> getAllFilesFromAlbum(Context context, String albumName) {
    Cursor cursor = context.getContentResolver()
        .query(QUERY_URI_AND_IMAGES, new String[] {
            BUCKET_DISPLAY_NAME, MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.TITLE
        }, BUCKET_DISPLAY_NAME + " = ? ", new String[] {albumName}, null);
    List<MediaEntry> mediaEntries = new ArrayList<>();
    MediaEntry mediaEntry;
    if (cursor != null) {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
          mediaEntry =
              new MediaEntry(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)),
                  cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)), false);
          mediaEntries.add(mediaEntry);
        cursor.moveToNext();
      }
      cursor.close();
    }
    return mediaEntries;
  }

  public static void deleteImageFromPreviousLocation(File file, Context context) {
    ContentResolver contentResolver = context.getContentResolver();
    Cursor cursor = contentResolver.query(QUERY_URI_AND_IMAGES, new String[] {ID},
        MediaStore.Files.FileColumns.DATA + " = ?", new String[] {file.getAbsolutePath()}, null);
    if (cursor != null && cursor.moveToFirst()) {
      long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
      Uri deleteUri = ContentUris.withAppendedId(QUERY_URI_AND_IMAGES, id);
      contentResolver.delete(deleteUri, null, null);
    } else {
      Timber.e("File not found in media store DB");
    }
    if (cursor != null) {
      cursor.close();
    }
  }

  public static File getCoverPhoto(Context context, String albumName) {
    List<MediaEntry> allFilesFromAlbum = getAllFilesFromAlbum(context, albumName);
    if (allFilesFromAlbum != null && !allFilesFromAlbum.isEmpty()) {
      return new File(allFilesFromAlbum.get(allFilesFromAlbum.size() - 1).data);
    }
    return new File("");
  }

  public static int getFileCount(Context context, String albumName) {
    return getAllFilesFromAlbum(context, albumName).size();
  }
}

