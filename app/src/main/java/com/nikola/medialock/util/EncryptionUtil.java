package com.nikola.medialock.util;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncryptionUtil {

  private static final String ENCODING_SCHEME = "UTF-8";

  @NonNull
  public static String encode(File file) {
    try {
      return URLEncoder.encode(file.getAbsolutePath(), ENCODING_SCHEME);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  @NonNull
  public static String getTitle(String string) {
    try {
      String fileName = URLDecoder.decode(string, ENCODING_SCHEME);
      return (fileName.substring(fileName.lastIndexOf('/') + 1).trim());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static File decodeToFile(String name) throws UnsupportedEncodingException {
    return new File(URLDecoder.decode(name, ENCODING_SCHEME));
  }

  public static String decodeToString(String name) throws UnsupportedEncodingException {
    return URLDecoder.decode(name, ENCODING_SCHEME);
  }
}
