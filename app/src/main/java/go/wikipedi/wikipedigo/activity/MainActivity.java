package go.wikipedi.wikipedigo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import go.wikipedi.wikipedigo.R;
import go.wikipedi.wikipedigo.fragment.PhotoListFragment_;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	private FragmentManager fm;

	@AfterViews
	void initViews() {
		fm = getSupportFragmentManager();
		Fragment fragment = new PhotoListFragment_();
		navigateTo(fragment);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	private void navigateTo(Fragment fragment) {
		if (!isFinishing()) {
			FragmentTransaction ft = fm.beginTransaction();
			fragment.setArguments(new Bundle());
			ft.replace(R.id.fragmentContainer, fragment);
			ft.commit();
		}
	}
}
