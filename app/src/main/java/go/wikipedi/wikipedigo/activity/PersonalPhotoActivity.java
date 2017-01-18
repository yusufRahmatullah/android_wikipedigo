package go.wikipedi.wikipedigo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.adapter.PersonalPhotoAdapter;
import go.wikipedi.wikipedigo.model.PhotoDB;

@EActivity(R.layout.activity_personal_photo)
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
		if (photo.getImages().size() > 1) {
		 	StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
				    getResources().getInteger(R.integer.item_span), StaggeredGridLayoutManager.VERTICAL);
			container.setLayoutManager(layoutManager);
		} else {
			LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
			container.setLayoutManager(layoutManager);
		}

	}
}
