package go.wikipedi.wikipedigo.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PersonalPhotoAdapter;
import go.wikipedi.wikipedigo.model.PhotoDB;

@EActivity(R.layout.activity_photo_show)
public class PersonalPhotoActivity extends BaseActivity {

	private PersonalPhotoAdapter adapter;

	@Extra
	PhotoDB photo;

	@ViewById(R.id.rv_container)
	RecyclerView container;

	@AfterViews
	void initViews() {
		getSupportActionBar().setTitle(photo.getName());
		adapter = new PersonalPhotoAdapter(this, photo.getImages());
		container.setAdapter(adapter);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		container.setLayoutManager(layoutManager);
	}
}
