package com.nikola.medialock.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nikola.medialock.R;
import com.nikola.medialock.gallery.data.GalleryItemsManager;
import com.nikola.medialock.model.MediaEntry;
import com.nikola.medialock.util.GlideUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleGalleryAlbumAdapter
    extends RecyclerView.Adapter<SingleGalleryAlbumAdapter.ViewHolder> {

  private List<MediaEntry> mediaList;
  private Context context;
  private GalleryItemsManager galleryItemsManager = GalleryItemsManager.getInstance();

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.selected)
    ImageView selectedIcon;
    @BindView(R.id.selected_background_image)
    ImageView selected;
    @BindView(R.id.image_frame)
    FrameLayout layout;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      final MediaEntry mediaEntry = (MediaEntry) v.getTag();
      galleryItemsManager.update(mediaEntry);
      notifyDataSetChanged();
    }
  }

  public SingleGalleryAlbumAdapter(List<MediaEntry> mediaList, Context context) {
    this.mediaList = mediaList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_media_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    MediaEntry mediaEntry = mediaList.get(position);
    GlideUtil.setImage(context, holder.imageView, new File(mediaEntry.data));
    holder.selectedIcon.setImageResource(mediaEntry.selectedStatus ? R.drawable.btn_media_select_on
                                                                   : R.drawable.btn_media_select_off);
    holder.selected.setVisibility(mediaEntry.selectedStatus ? View.VISIBLE : View.INVISIBLE);
    holder.layout.setTag(mediaEntry);
  }

  @Override
  public int getItemCount() {
    return mediaList.size();
  }

  public void updateDataSet(List<MediaEntry> files) {
    this.mediaList.clear();
    this.mediaList.addAll(files);
    notifyDataSetChanged();
  }
}
