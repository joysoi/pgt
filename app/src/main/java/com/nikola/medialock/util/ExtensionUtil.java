package com.nikola.medialock.util;

import java.util.Arrays;

public class ExtensionUtil {

  static final String[]
      IMAGE_FORMATS = new String[] {"jpg", "gif", "png", "bmp", "webp"};


  public static boolean isImage(String s) {
    return Arrays.asList(IMAGE_FORMATS).contains(s.substring(s.lastIndexOf('.') + 1).trim());
  }

}
