package com.nikola.medialock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.data.LockedFolderHelper;
import com.nikola.medialock.gallery.GalleryAlbumsActivity;
import com.nikola.medialock.ui.BaseActivity;
import com.nikola.medialock.ui.MediaEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateLockedAlbumActivity extends BaseActivity {

  @BindView(R.id.edt_enter_name) MediaEditText editText;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    edtListener();
  }

  @Override
  protected void onResume() {
    super.onResume();
    showKeyboard(editText);
  }

  private void edtListener() {

    editText.OnClickListener(new MediaEditText.OnClickListener() {
      @Override
      public void onActionDownClick() {
        onBackPressed();
      }
    });
    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId
            == EditorInfo.IME_ACTION_DONE)) {
          textValidation();
        }
        return true;
      }
    });
  }

  @Override
  protected int getContentViewLayoutId() {
    return R.layout.activity_create_album_layout;
  }

  @OnClick(R.id.txt_done)
  public void onClick() {
    textValidation();
  }

  private void textValidation() {
    String albumName = editText.getText().toString();
    if (albumName.compareTo("") != 0) {
      isNameAlreadyTaken(albumName);
    } else {
      showToast(getResources().getString(R.string.write_album_name), Toast.LENGTH_SHORT);
    }
  }

  private boolean isNameAlreadyTaken(String albumName) {
    return LockedFolderHelper.exists() ?
           !LockedAlbumsOperations.getProtectedAlbumsNames().contains(albumName)
           ? saveNameAndStartActivity(albumName)
           : isToastShown(getResources().getString(R.string.name_already_taken))
                                       : saveNameAndStartActivity(albumName);
  }

  private boolean saveNameAndStartActivity(String albumName) {
    LockedAlbumsOperations.saveAlbumName(getApplicationContext(), albumName);
    startActivity(new Intent(this, GalleryAlbumsActivity.class));
    return true;
  }

  private boolean isToastShown(String message) {
    showToast(message, Toast.LENGTH_SHORT);
    return true;
  }
}
