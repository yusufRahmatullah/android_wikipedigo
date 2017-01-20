package go.wikipedi.wikipedigo.contoller;

import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.api.APIRequest;
import go.wikipedi.wikipedigo.model.Photo;
import go.wikipedi.wikipedigo.model.URLInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotosController {

	private static final String FETCH_ERROR = "Error while fetching photos";
	private static final String PHOTOS_COLUMN = "photosColumn";
	private static final Type PHOTOS_TYPE = new TypeToken<List<Photo>>(){}.getType();

	public static final String PHOTOS_PREF = "photosPreference";

	//region singleton
	private static PhotosController instance = new PhotosController();

	private PhotosController() {
	}

	public static PhotosController getInstance() {
		return instance;
	}
	//endregion

	private SharedPreferences sharedPreferences;
	private List<Photo> photos = new ArrayList<>();
	private URLInfo urlInfo;

	public void fetchPhotos(final Runnable onSuccess) {
		APIRequest.getInstance().getService().getPhotos().enqueue(new Callback<List<Photo>>() {
			@Override
			public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
				if (sharedPreferences != null) {
					String json = APIRequest.getInstance().getGson().toJson(response.body(), PHOTOS_TYPE);
					if (!json.equals("")) {
						sharedPreferences.edit().putString(PHOTOS_COLUMN, json).commit();
					}
				}
				photos = response.body();
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<List<Photo>> call, Throwable t) {
				t.printStackTrace();
				if (sharedPreferences != null) {
					String json = sharedPreferences.getString(PHOTOS_COLUMN, "");
					if (!json.equals("")) {
						photos = APIRequest.getInstance().getGson().fromJson(json, PHOTOS_TYPE);
					}
				}
			}
		});
	}

	public void updatePhotos(final Runnable onSuccess) {
		APIRequest.getInstance().getService().getPhotos().enqueue(new Callback<List<Photo>>() {
			@Override
			public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
				if (sharedPreferences != null) {
					String json = APIRequest.getInstance().getGson().toJson(response.body(), PHOTOS_TYPE);
					if (!json.equals("")) {
						sharedPreferences.edit().putString(PHOTOS_COLUMN, json).commit();
					}
				}
				List<Photo> newPhotos = response.body();
				int i = 0;
				while (!newPhotos.get(i).getImage().contentEquals(photos.get(i).getImage())) {
					photos.add(i, newPhotos.get(i));
					i += 1;
				}
				onSuccess.run();
			}

			@Override
			public void onFailure(Call<List<Photo>> call, Throwable t) {
				t.printStackTrace();
				if (sharedPreferences != null) {
					String json = sharedPreferences.getString(PHOTOS_COLUMN, "");
					if (!json.equals("")) {
						photos = APIRequest.getInstance().getGson().fromJson(json, PHOTOS_TYPE);
					}
				}
			}
		});
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		if (sharedPreferences == null) {
			this.sharedPreferences = sharedPreferences;
		}
	}

	public void searchPhotos(String query, BaseRunnable<List<Photo>> onFound) {
		List<Photo> result = new ArrayList<>();
		for (int i = 0; i < photos.size(); i++) {
			if (photos.get(i).isContains(query)) {
				result.add(photos.get(i));
			}
		}
		onFound.run(result);
	}

	public List<Photo> getPhotos() {
		return photos;
	}
}
