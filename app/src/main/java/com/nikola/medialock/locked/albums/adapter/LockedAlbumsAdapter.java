package com.nikola.medialock.locked.albums.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikola.medialock.R;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.locked.ui.SingleLockedAlbumTabHolderActivity;
import com.nikola.medialock.model.AlbumEntry;
import com.nikola.medialock.util.DeleteAlbumAsyncTask;
import com.nikola.medialock.util.GlideUtil;
import com.nikola.medialock.util.OnTaskCompleted;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LockedAlbumsAdapter extends RecyclerView.Adapter<LockedAlbumsAdapter.ViewHolder> {

  public static final String ALBUM_NAME = "albumName";
  private List<AlbumEntry> albumList;
  private Context context;
  OnTaskCompleted listener;

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.album_title)
    TextView title;
    @BindView(R.id.img)
    ImageView imageView;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      imageView.setOnClickListener(this);
      imageView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
      AlbumEntry album = (AlbumEntry) v.getTag();
      Intent intent = new Intent(context, SingleLockedAlbumTabHolderActivity.class);
      intent.putExtra(ALBUM_NAME, album.title);
      context.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
      AlbumEntry album = (AlbumEntry) v.getTag();
      showConformationDialog(context,album.title);
      return true;
    }
    private void showConformationDialog(final Context context,
                                        final String albumName) {
      new AlertDialog.Builder(context)
              .setTitle(context.getString(R.string.delete_album_title))
              .setMessage(context.getString(R.string.delete_album_msg))
              .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  new DeleteAlbumAsyncTask(context, albumName, listener).execute();
                }
              })
              .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
              })
              .show();
    }
  }

  public LockedAlbumsAdapter(List<AlbumEntry> albumList, Context context, OnTaskCompleted listener) {
    this.albumList = albumList;
    this.context = context;
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_locked_album_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    AlbumEntry albumEntry = albumList.get(position);
    holder.title.setText(albumEntry.title);
    GlideUtil.setImage(context, holder.imageView,
        LockedAlbumsOperations.getCoverPhoto(albumEntry.title));
    holder.imageView.setTag(albumList.get(position));
  }

  @Override
  public int getItemCount() {
    return albumList.size();
  }
}
