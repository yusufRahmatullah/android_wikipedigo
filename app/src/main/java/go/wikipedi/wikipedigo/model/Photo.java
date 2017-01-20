package go.wikipedi.wikipedigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E460 on 13/01/2017.
 */

public class Photo {

	@SerializedName("name")
	@Expose
	String name;

	@SerializedName("image")
	@Expose
	String image;

	public boolean isContains(String text) {
		return name.toLowerCase().contains(text.toLowerCase());
	}

	//region cons, get-set

	public Photo(String name, String imageLink) {
		this.name = name;
		this.image = imageLink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	//endregion
}
