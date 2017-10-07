package com.nikola.medialock.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.nikola.medialock.R;

public abstract class BaseActivity extends AppCompatActivity {

  protected Toolbar toolbar;
  protected ProgressDialog progressDialog;
  protected InputMethodManager inputMethodManager;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int contentViewId = getContentViewLayoutId();
    if (contentViewId != 0) {
      setContentView(contentViewId);
    }
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }
  }


  abstract protected int getContentViewLayoutId();

  protected void showProgress(String msg, Context context, int theme) {
    if (progressDialog != null && progressDialog.isShowing()) { dismissProgress(); }
    progressDialog = new ProgressDialog(context, theme);
    progressDialog.setCancelable(false);
    progressDialog.setMessage(msg);
    progressDialog.show();
  }

  protected void dismissProgress() {
    if (progressDialog != null) {
      progressDialog.dismiss();
      progressDialog = null;
    }
  }

  protected void showToast(String msg, int duration) {
    Toast.makeText(this, msg, duration).show();
  }

  protected void showKeyboard(EditText editText) {
    inputMethodManager =
        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
  }

  protected void dismissKeyboard(EditText editText) {
    inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
  }

}
