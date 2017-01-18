package go.wikipedi.wikipedigo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by E460 on 17/01/2017.
 */

public class URLInfo {

	@SerializedName("full")
	@Expose
	String name;

	@SerializedName("parts")
	@Expose
	List<String> parts;

	//region cons, get, set
	public URLInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParts() {
		return parts;
	}

	public void setParts(List<String> parts) {
		this.parts = parts;
	}
	//endregion
}
