package com.nikola.medialock.locked.albums.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nikola.medialock.R;
import com.nikola.medialock.data.LockedAlbumsOperations;
import com.nikola.medialock.locked.albums.adapter.LockedAlbumsAdapter;
import com.nikola.medialock.util.OnTaskCompleted;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LockedAlbumsFragment extends Fragment implements OnTaskCompleted {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  private LockedAlbumsAdapter adapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter =
        new LockedAlbumsAdapter(LockedAlbumsOperations.getProtectedAlbumsWithImageAndVideoCount(),
            getActivity(), this );
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recycle_view_layout, container, false);
    ButterKnife.bind(this, rootView);
    initAdapter();
    return rootView;
  }

  private void initAdapter() {
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onTaskCompleted() {
    Toast.makeText(getActivity(), getResources().getString(R.string.deleted_album),
            Toast.LENGTH_LONG).show();
  }
}
