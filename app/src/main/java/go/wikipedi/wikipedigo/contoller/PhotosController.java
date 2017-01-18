package go.wikipedi.wikipedigo.contoller;

import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import go.wikipedi.base.BaseRunnable;
import go.wikipedi.base.api.APIRequest;
import go.wikipedi.wikipedigo.model.PhotoDB;
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
	private static final Type PHOTOS_TYPE = new TypeToken<List<PhotoDB>>(){}.getType();

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
	private List<PhotoDB> photos = new ArrayList<>();
	private URLInfo urlInfo;

	public void fetchPhotos(final Runnable onSuccess) {
		APIRequest.getInstance().getService().getPhotos().enqueue(new Callback<List<PhotoDB>>() {
			@Override
			public void onResponse(Call<List<PhotoDB>> call, Response<List<PhotoDB>> response) {
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
			public void onFailure(Call<List<PhotoDB>> call, Throwable t) {
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

	public void fetchMorePhotos(final int index, final BaseRunnable<List<PhotoDB>> onSuccess) {
		firstFetch(new Runnable() {
			@Override
			public void run() {
				if (index < urlInfo.getParts().size()) {
					APIRequest.getInstance().getService().getParts(urlInfo.getParts().get(index))
							.enqueue(new Callback<List<PhotoDB>>() {
								@Override
								public void onResponse(Call<List<PhotoDB>> call, Response<List<PhotoDB>> response) {
									onSuccess.run(response.body());
								}

								@Override
								public void onFailure(Call<List<PhotoDB>> call, Throwable t) {
									t.printStackTrace();
								}
							});
				}
			}
		});
	}

	private void firstFetch(final Runnable onSuccess) {
		if (urlInfo == null) {
			APIRequest.getInstance().getService().getURLInfo().enqueue(new Callback<URLInfo>() {
				@Override
				public void onResponse(Call<URLInfo> call, Response<URLInfo> response) {
					urlInfo = response.body();
					onSuccess.run();
				}

				@Override
				public void onFailure(Call<URLInfo> call, Throwable t) {
					t.printStackTrace();
				}
			});
		}
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		if (sharedPreferences == null) {
			this.sharedPreferences = sharedPreferences;
		}
	}

	public void searchPhotos(String query, BaseRunnable<List<PhotoDB>> onFound) {
		List<PhotoDB> result = new ArrayList<>();
		for (int i = 0; i < photos.size(); i++) {
			if (photos.get(i).isContains(query)) {
				result.add(photos.get(i));
			}
		}
		onFound.run(result);
	}

	public List<PhotoDB> getPhotos() {
		return photos;
	}
}
