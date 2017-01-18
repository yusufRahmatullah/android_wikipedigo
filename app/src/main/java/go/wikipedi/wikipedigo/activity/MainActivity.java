package go.wikipedi.wikipedigo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.OnItemSelectedListener;
import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PhotosAdapter;
import go.wikipedi.wikipedigo.contoller.PhotosController;
import go.wikipedi.wikipedigo.model.PhotoDB;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	private ProgressDialog progressDialog;
	private PhotosAdapter adapter;
	private SearchView searchView;

	@ViewById(R.id.rv_container)
	RecyclerView container;

	@ViewById(R.id.tv_alert)
	TextView alertText;

	@AfterViews
	void initViews() {
		alertText.setVisibility(View.GONE);
		PhotosController.getInstance().setSharedPreferences(getSharedPreferences(PhotosController.PHOTOS_PREF,
				Context.MODE_PRIVATE));
		adapter = new PhotosAdapter(this, PhotosController.getInstance().getPhotos(), new OnItemSelectedListener() {
			@Override
			public void onItemSelected(int position) {
				goToPersonalPhoto(adapter.getItem(position).copy() );
			}
		});
		container.setAdapter(adapter);
		GridLayoutManager layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
		container.setLayoutManager(layoutManager);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading");
		progressDialog.show();
		PhotosController.getInstance().fetchPhotos(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
				adapter.setItems(PhotosController.getInstance().getPhotos());
			}
		});
	}

	@Click(R.id.btn_fetch_more)
	void fetchMore() {
		adapter.nextItems();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_bar, menu);
		MenuItem searchItem = menu.findItem(R.id.action_find);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint(getString(R.string.text_search_igo));
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				if (query.trim().length() == 0) {
					alertText.setVisibility(View.GONE);
					adapter.setItems(PhotosController.getInstance().getPhotos());
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.trim().length() == 0) {
					alertText.setVisibility(View.GONE);
					adapter.setItems(PhotosController.getInstance().getPhotos());
				} else {
					PhotosController.getInstance().searchPhotos(newText, new BaseRunnable<List<PhotoDB>>() {
						@Override
						public void run(List<PhotoDB> object) {
							if (object.size() == 0) {
								alertText.setVisibility(View.VISIBLE);
							}
							adapter.setItems(object);
						}
					});
				}
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	private void goToPersonalPhoto(PhotoDB photo) {
		PersonalPhotoActivity_.intent(this).photo(photo).start();
	}
}
