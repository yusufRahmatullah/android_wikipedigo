package go.wikipedi.wikipedigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotoDB {

	@SerializedName("name")
	@Expose
	String name;

	@SerializedName("images")
	@Expose
	List<String> images;

	public boolean isContains(String text) {
		return name.contains(text);
	}

	//region cons, get, set

	public PhotoDB(String name) {
		this.name = name;
		images = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getFirstImage() {
		return images.get(0);
	}

	//endregion
}
