package go.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

import go.wikipedi.base.RecyclerViewAdapterBase;
import go.wikipedi.base.ViewWrapper;
import go.wikipedi.wikipedigo.view.ItemPersonalPhotoView;
import go.wikipedi.wikipedigo.view.ItemPersonalPhotoView_;

/**
 * Created by E460 on 18/01/2017.
 */

public class PersonalPhotoAdapter extends RecyclerViewAdapterBase<String, ItemPersonalPhotoView> {

	public PersonalPhotoAdapter(Context context, List<String> items) {
		super(context, items);
	}

	@Override
	protected ItemPersonalPhotoView onCreateItemView(ViewGroup parent, int viewType) {
		return ItemPersonalPhotoView_.build(context);
	}

	@Override
	public void onBindViewHolder(ViewWrapper<ItemPersonalPhotoView> holder, int position) {
		holder.getView().bind(getItem(position));
	}
}
