package go.wikipedi.wikipedigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E460 on 13/01/2017.
 */

public class Photo {

	@SerializedName("id")
	@Expose
	long id;

	@SerializedName("name")
	@Expose
	String name;

	@SerializedName("link")
	@Expose
	String link;

	@SerializedName("favorite_count")
	@Expose
	int favoriteCount;

	@SerializedName("img_link")
	@Expose
	String imageLink;

	//region cons, get-set
	public Photo(long id, String name, int favoriteCount, String link, String imageLink) {
		this.id = id;
		this.name = name;
		this.favoriteCount = favoriteCount;
		this.link = link;
		this.imageLink = imageLink;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	//endregion
}
