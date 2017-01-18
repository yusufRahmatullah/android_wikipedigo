package go.wikipedi.wikipedigo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PhotosAdapter;
import go.wikipedi.wikipedigo.contoller.PhotosController;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	private ProgressDialog progressDialog;
	private PhotosAdapter adapter;

	@ViewById(R.id.rv_container)
	RecyclerView container;

	@AfterViews
	void initViews() {
		PhotosController.getInstance().setSharedPreferences(getSharedPreferences(PhotosController.PHOTOS_PREF,
				Context.MODE_PRIVATE));
		adapter = new PhotosAdapter(this, PhotosController.getInstance().getPhotos());
		container.setAdapter(adapter);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
				StaggeredGridLayoutManager.VERTICAL);
		container.setLayoutManager(layoutManager);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading");
		progressDialog.show();
		PhotosController.getInstance().fetchPhotos(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				progressDialog.dismiss();
			}
		});
	}

	@Click(R.id.btn_fetch_more)
	void fetchMore() {
		adapter.nextItems();
	}
}
