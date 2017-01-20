package go.wikipedi.wikipedigo.activity;

import android.content.Context;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.contoller.PhotosController;
import go.wikipedi.wikipedigo.fragment.BaseFragment;
import go.wikipedi.wikipedigo.fragment.PhotoListFragment_;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity{

	BaseFragment fragment;

	@AfterViews
	void initViews() {
		PhotosController.getInstance().setSharedPreferences(getSharedPreferences(PhotosController.PHOTOS_PREF,
				Context.MODE_PRIVATE));
		fragment = new PhotoListFragment_();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
	}

	@Override
	public void onBackPressed() {
		if (!fragment.onBackPressed()) {
			super.onBackPressed();
		}
	}
}
