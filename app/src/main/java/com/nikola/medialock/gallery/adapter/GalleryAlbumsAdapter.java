package com.nikola.medialock.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nikola.medialock.R;
import com.nikola.medialock.data.MediaOperations;
import com.nikola.medialock.gallery.SingleGalleryAlbumActivity;
import com.nikola.medialock.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GalleryAlbumsAdapter extends RecyclerView.Adapter<GalleryAlbumsAdapter.ViewHolder> {

  public static final String ALBUM_NAME = "albumName";
  private List<String> albumList;
  private Context context;

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.album_title)
    TextView title;
    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.txt_file_count)
    TextView txtFileCount;
    @BindView(R.id.img_layout)
    RelativeLayout layout;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      String album = (String) v.getTag();
      Intent intent = new Intent(context, SingleGalleryAlbumActivity.class);
      intent.putExtra(ALBUM_NAME, album);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }


  }

  public GalleryAlbumsAdapter(List<String> albumList, Context context) {
    this.albumList = albumList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_gallery_album_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    String title = albumList.get(position);
    holder.title.setText(title);
    holder.txtFileCount.setText(
        String.format(context.getResources().getString(R.string.album_count),
            String.valueOf(MediaOperations.getFileCount(context, title))));
    GlideUtil.setImage(context, holder.imageView, MediaOperations.getCoverPhoto(context, title));
    holder.layout.setTag(albumList.get(position));
  }

  @Override
  public int getItemCount() {
    return albumList.size();
  }
}
