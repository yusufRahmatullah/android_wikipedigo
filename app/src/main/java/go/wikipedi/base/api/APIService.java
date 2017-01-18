package go.wikipedi.base.api;

import java.util.List;

import go.wikipedi.wikipedigo.model.PhotoDB;
import go.wikipedi.wikipedigo.model.URLInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by E460 on 12/01/2017.
 * This class can be edited to adjust with your apps
 */

public interface APIService {

	@GET("97t2j")
	Call<List<PhotoDB>> getPhotos();

	@GET("1dekej")
	Call<URLInfo> getURLInfo();

	@GET("{part}")
	Call<List<PhotoDB>> getParts(@Path("part") String part);
}
