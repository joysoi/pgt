package com.nikola.medialock.locked.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nikola.medialock.R;
import com.nikola.medialock.SelectedItemActivity;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.model.MediaEntry;
import com.nikola.medialock.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleLockedAlbumAdapter
    extends RecyclerView.Adapter<SingleLockedAlbumAdapter.ViewHolder> {

  private List<MediaEntry> filesList;
  private Context context;
  private String album;
  private static final String KEY = "key";

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.image_frame)
    FrameLayout layout;
    @BindView(R.id.img_video)
    ImageView imgVideo;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      final MediaEntry mediaEntry = (MediaEntry) v.getTag();
      final ArrayList<String> arrayList = new ArrayList<>();
      arrayList.add(mediaEntry.title);
      arrayList.add(album);
      final Intent intent = new Intent(context, SelectedItemActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.putStringArrayListExtra(KEY, arrayList);
      context.startActivity(intent);
    }
  }

  public SingleLockedAlbumAdapter(List<MediaEntry> filesList, Context context, String album) {
    this.filesList = filesList;
    this.context = context;
    this.album = album;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.locked_media_list_item, parent, false);

    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    MediaEntry mediaEntry = filesList.get(position);
      holder.imgVideo.setVisibility(View.INVISIBLE);
    GlideUtil.setImage(context, holder.imageView,
        LockedAlbumsOperations.getMediaFile(mediaEntry.title, album, true));
    holder.layout.setTag(mediaEntry);
  }

  @Override
  public int getItemCount() {
    return filesList.size();
  }
}

