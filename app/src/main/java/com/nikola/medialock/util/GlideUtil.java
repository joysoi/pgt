package com.nikola.medialock.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nikola.medialock.R;

import java.io.File;



public class GlideUtil {

  public static void setImage(Context context, ImageView imageView, File file) {
    Glide.with(context)
        .load(file)
        .error(R.drawable.placeholder)
        .into(imageView);
  }
}
