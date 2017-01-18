package go.wikipedi.wikipedigo.view;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import go.wikipedi.wikipedigo.R;

/**
 * Created by E460 on 18/01/2017.
 */

@EViewGroup(R.layout.item_personal_photo)
public class ItemPersonalPhotoView extends RelativeLayout{

	private Context context;

	@ViewById(R.id.img_photo)
	ImageView imgPhoto;

	public ItemPersonalPhotoView(Context context) {
		super(context);
		this.context = context;
	}

	public void bind(String url) {
		Glide
				.with(context)
				.load(url)
				.placeholder(R.drawable.ic_face)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imgPhoto);
	}

}
