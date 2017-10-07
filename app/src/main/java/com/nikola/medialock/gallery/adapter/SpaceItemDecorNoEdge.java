package com.nikola.medialock.gallery.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecorNoEdge extends RecyclerView.ItemDecoration {

  private int space;
  private int topSpace;
  private int columnCount;
  private int itemTop;

  public SpaceItemDecorNoEdge(int space, int topSpace, int itemTop, int spanCount) {
    this.space = space;
    this.topSpace = topSpace;
    this.columnCount = spanCount;
    this.itemTop = itemTop;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
                             RecyclerView parent, RecyclerView.State state) {
    int position = parent.getChildAdapterPosition(view);
    int column = position % columnCount;
    outRect.left = column * space / columnCount;
    outRect.right = space - (column + 1) * space / columnCount;
    if (position < columnCount) {
      outRect.top = topSpace;
    }
    if (position >= columnCount) {
      outRect.top = itemTop;
    }
  }
}