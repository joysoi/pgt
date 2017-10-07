package com.nikola.medialock.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class MediaEditText extends EditText {

  public interface OnClickListener {

    void onActionDownClick();
  }

  private OnClickListener onClickListener;

  public MediaEditText(Context context) {
    super(context);
  }

  public MediaEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MediaEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void OnClickListener(OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
  }

  @Override
  public boolean onKeyPreIme(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (onClickListener != null) {
        onClickListener.onActionDownClick();
      }
    }
    return true;
  }
}
