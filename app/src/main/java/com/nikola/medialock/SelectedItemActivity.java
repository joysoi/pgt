package com.nikola.medialock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.ui.BaseActivity;
import com.nikola.medialock.util.DeleteUnlockAsyncTask;
import com.nikola.medialock.util.EncryptionUtil;
import com.nikola.medialock.util.OnTaskCompleted;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectedItemActivity extends BaseActivity implements OnTaskCompleted {

    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.delete_move_btns)
    LinearLayout linearLayout;
    private static final String KEY = "key";
    private static final int FILE_NAME_POSITION = 0;
    private static final int ALBUM_NAME_POSITION = 1;
    private String fileName;
    private String albumName;
    private boolean isDeleteClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        final ArrayList<String> fileDataList = getIntent().getExtras().getStringArrayList(KEY);
        if (fileDataList != null && !fileDataList.isEmpty()) {
            fileName = fileDataList.get(FILE_NAME_POSITION);
            albumName = fileDataList.get(ALBUM_NAME_POSITION);
        }
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(EncryptionUtil.getTitle(fileName));
        imageSetup();
    }


    private void menageStatusBarVisibility() {
        assert getSupportActionBar() != null;
        if (getSupportActionBar().isShowing()) {
            hideStatusBar();
            getSupportActionBar().hide();
            linearLayout.setVisibility(View.INVISIBLE);
        } else {
            showStatusBar();
            getSupportActionBar().show();
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void imageSetup() {
        Glide.with(this)
                .load(new File(LockedAlbumsOperations.getMediaPath(fileName, albumName)))
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menageStatusBarVisibility();
            }
        });
    }

    private void hideStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showStatusBar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_selected_item_layout;
    }

    @OnClick({R.id.unlock, R.id.delete})
    public void onButtonClick(View view) {
        showConformationDialog(isDeleteClicked = view.getId() == R.id.delete);
    }

    private void showConformationDialog(final boolean isDeletedClicked) {
        new AlertDialog.Builder(SelectedItemActivity.this, R.style.AlertDialogStyle)
                .setTitle(getResources().getString(
                        isDeletedClicked ? R.string.delete_title : R.string.move_out_title))
                .setMessage(getResources().getString(
                        isDeletedClicked ? R.string.delete_msg : R.string.move_out_msg))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteUnlockAsyncTask(SelectedItemActivity.this, fileName, albumName,
                                isDeletedClicked, SelectedItemActivity.this).execute();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    @Override
    public void onTaskCompleted() {
        showToast(!isDeleteClicked ? getResources().getString(R.string.image_is_unlocked) : getResources().getString(R.string.image_is_deleted),
                Toast.LENGTH_LONG);

        onBackPressed();
    }
}
