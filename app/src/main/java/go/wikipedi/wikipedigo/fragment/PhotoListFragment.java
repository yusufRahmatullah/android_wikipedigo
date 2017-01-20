package go.wikipedi.wikipedigo.fragment;

import android.graphics.Rect;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.OnItemSelectedListener;
import go.wikipedi.base.OnLastItemVisibleListener;
import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PhotosAdapter;
import go.wikipedi.wikipedigo.contoller.PhotosController;
import go.wikipedi.wikipedigo.model.Photo;

@EFragment(R.layout.fragment_photo_list)
public class PhotoListFragment extends BaseFragment {

	public PhotoListFragment() {
		// Required empty public constructor
	}

	private boolean isImageFullscreen;
	private GridLayoutManager layoutManager;
	private PhotosAdapter adapter;
	private SearchView searchView;

	//region annotations
	@ViewById(R.id.img_full)
	ImageView fullImage;

	@ViewById(R.id.rv_container)
	RecyclerView container;

	@ViewById(R.id.content)
	SwipeRefreshLayout swipeRefreshLayout;

	@ViewById(R.id.tv_alert)
	TextView alertText;

	@AfterViews
	void initViews() {
		setHasOptionsMenu(true);

		fullImage.setVisibility(View.GONE);
		isImageFullscreen = false;
		alertText.setVisibility(View.GONE);

		adapter = new PhotosAdapter(getContext(), PhotosController.getInstance().getPhotos());
		adapter.setOnItemSelectedListener(onItemSelected);
		adapter.setOnLastItemVisibleListener(onLastItemVisible);
		container.setAdapter(adapter);
		layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
		container.setLayoutManager(layoutManager);
		PhotosController.getInstance().fetchPhotos(new Runnable() {
			@Override
			public void run() {
				adapter.setItems(PhotosController.getInstance().getPhotos());
			}
		});
		swipeRefreshLayout.setOnRefreshListener(onUpdatePhotos);
	}
	//endregion

	void toggleImageFull(Photo photo) {
		isImageFullscreen = !isImageFullscreen;
		if (isImageFullscreen) {
			container.setVisibility(View.GONE);
			fullImage.setVisibility(View.VISIBLE);
			swipeRefreshLayout.setEnabled(false);
			Glide.with(this)
					.load(photo.getImage())
					.animate(R.anim.grow_from_middle)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(fullImage);
		} else {
			container.setVisibility(View.VISIBLE);
			fullImage.setVisibility(View.GONE);
		}
	}

	void searchPhoto(String query) {
		if (query.trim().length() == 0) {
			alertText.setVisibility(View.GONE);
			adapter.setItems(PhotosController.getInstance().getPhotos());
		} else {
			PhotosController.getInstance().searchPhotos(query, new BaseRunnable<List<Photo>>() {
				@Override
				public void run(List<Photo> object) {
					if (object.size() == 0) {
						alertText.setVisibility(View.VISIBLE);
					} else {
						alertText.setVisibility(View.GONE);
					}
					adapter.setItems(object);
				}
			});
		}
	}


	//region override methods
	@Override
	public boolean onBackPressed() {
		swipeRefreshLayout.setEnabled(true);
		if (isImageFullscreen) {
			toggleImageFull(null);
			return true;
		}
		if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
			return true;
		}
		return super.onBackPressed();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search_bar, menu);
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem searchItem = menu.findItem(R.id.action_find);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getString(R.string.text_search_igo));
		searchView.setOnQueryTextListener(onSearchPhoto);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_find:
				if (isImageFullscreen) {
					toggleImageFull(null);
				}
				super.onOptionsItemSelected(item);
				break;
			default:
				break;
		}
		return false;
	}
	//endregion

	//region listeners
	OnItemSelectedListener onItemSelected = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(int position) {
			toggleImageFull(adapter.getItem(position));
		}
	};

	OnLastItemVisibleListener onLastItemVisible = new OnLastItemVisibleListener() {
		@Override
		public void onLastItemVisible() {
			adapter.showNextItems();
		}
	};

	SearchView.OnQueryTextListener onSearchPhoto = new SearchView.OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String query) {
			searchPhoto(query);
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			searchPhoto(newText);
			return false;
		}
	};

	SwipeRefreshLayout.OnRefreshListener onUpdatePhotos = new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			if (isImageFullscreen) {
				swipeRefreshLayout.setRefreshing(false);
			} else {
				PhotosController.getInstance().updatePhotos(new Runnable() {
					@Override
					public void run() {
						swipeRefreshLayout.setRefreshing(false);
						adapter.notifyDataSetChanged();
					}
				});
			}
		}
	};
	//endregion
}
