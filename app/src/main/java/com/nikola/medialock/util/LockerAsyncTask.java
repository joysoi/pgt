package com.nikola.medialock.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nikola.medialock.R;


public abstract class LockerAsyncTask<Parameter, Progress, Result>
    extends AsyncTask<Parameter, Progress, Result> {

  protected ProgressDialog progressDialog;
  protected Context context;
  protected OnTaskCompleted listener;

  public LockerAsyncTask(Context context, OnTaskCompleted listener) {
    this.context = context;
    this.listener = listener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    showProgress(context.getResources().getString(R.string.loading),
        context, R.style.AppCompatAlertDialogStyle);
  }

  @Override
  protected void onPostExecute(Result aBoolean) {
    super.onPostExecute(aBoolean);
    dismissProgress();
    if (listener != null) {
      listener.onTaskCompleted();
    }
  }

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
    Toast.makeText(context, msg, duration).show();
  }
}
