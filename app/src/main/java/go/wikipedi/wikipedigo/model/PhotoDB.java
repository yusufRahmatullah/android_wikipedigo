package go.wikipedi.wikipedigo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotoDB implements Parcelable {

	@SerializedName("name")
	@Expose
	String name;

	@SerializedName("images")
	@Expose
	List<String> images = new ArrayList<>();

	public boolean isContains(String text) {
		return name.toLowerCase().contains(text.toLowerCase());
	}

	//region cons, get, set

	public PhotoDB(String name) {
		this.name = name;
		images = new ArrayList<>();
	}

	public PhotoDB copy() {
		PhotoDB photoDB = new PhotoDB(this.name);
		photoDB.images = new ArrayList<>();
		for (String image : this.images) {
			photoDB.images.add(image);
		}
		return photoDB;
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

	//region parcelable
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(name);
		parcel.writeStringList(images);
	}

	public static final Creator<PhotoDB> CREATOR = new Creator<PhotoDB>() {
		@Override
		public PhotoDB createFromParcel(Parcel parcel) {
			return new PhotoDB(parcel);
		}

		@Override
		public PhotoDB[] newArray(int size) {
			return new PhotoDB[size];
		}
	};

	protected PhotoDB(Parcel in) {
		name = in.readString();
		in.readStringList(images);
	}
	//endregion
}
