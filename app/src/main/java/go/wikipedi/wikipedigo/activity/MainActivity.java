package go.wikipedi.wikipedigo.activity;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.OnItemSelectedListener;
import go.wikipedi.base.OnLastItemVisibleListener;
import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PhotosAdapter;
import go.wikipedi.wikipedigo.contoller.PhotosController;
import go.wikipedi.wikipedigo.model.Photo;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity{

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

	@ViewById(R.id.tv_full)
	TextView fullText;

	@AfterViews
	void initViews() {
		fullImage.setVisibility(View.GONE);
		fullText.setVisibility(View.GONE);
		isImageFullscreen = false;
		alertText.setVisibility(View.GONE);
		PhotosController.getInstance().setSharedPreferences(getSharedPreferences(PhotosController.PHOTOS_PREF,
				Context.MODE_PRIVATE));
		adapter = new PhotosAdapter(this, PhotosController.getInstance().getPhotos());
		adapter.setOnItemSelectedListener(onItemSelected);
		adapter.setOnLastItemVisibleListener(onLastItemVisible);
		container.setAdapter(adapter);
		layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
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
			fullText.setVisibility(View.VISIBLE);
			swipeRefreshLayout.setEnabled(false);
			Glide.with(this)
					.load(photo.getImage())
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(fullImage);
			fullText.setText(photo.getName());
		} else {
			container.setVisibility(View.VISIBLE);
			fullImage.setVisibility(View.GONE);
			fullText.setVisibility(View.GONE);
			swipeRefreshLayout.setEnabled(true);
		}
	}

	//region override methods
	@Override
	public void onBackPressed() {
		if (isImageFullscreen) {
			toggleImageFull(null);
		} else if (swipeRefreshLayout.isRefreshing()) {
			swipeRefreshLayout.setRefreshing(false);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_bar, menu);
		MenuItem searchItem = menu.findItem(R.id.action_find);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getString(R.string.text_search_igo));
		searchView.setOnQueryTextListener(onSearchPhoto);
		return super.onCreateOptionsMenu(menu);
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
	void onQueryEmpty() {
		alertText.setVisibility(View.GONE);
		adapter.setItems(PhotosController.getInstance().getPhotos());
	}

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
			if (query.trim().length() == 0) {
				onQueryEmpty();
			}
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			if (newText.trim().length() == 0) {
				onQueryEmpty();
			} else {
				PhotosController.getInstance().searchPhotos(newText, new BaseRunnable<List<Photo>>() {
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
