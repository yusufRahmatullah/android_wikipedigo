package go.wikipedi.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by E460 on 12/01/2017.
 */

public class BaseResponse {

	@SerializedName("message")
	@Expose
	protected String message;

	public BaseResponse() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String _message) {
		message = _message;
	}

}