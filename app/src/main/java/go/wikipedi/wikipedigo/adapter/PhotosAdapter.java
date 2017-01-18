package go.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import go.wikipedi.base.OnItemSelectedListener;
import go.wikipedi.base.RecyclerViewAdapterBase;
import go.wikipedi.base.ViewWrapper;
import go.wikipedi.wikipedigo.model.PhotoDB;
import go.wikipedi.wikipedigo.view.ItemPhotoView;
import go.wikipedi.wikipedigo.view.ItemPhotoView_;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotosAdapter extends RecyclerViewAdapterBase<PhotoDB, ItemPhotoView> {

	private static final int MAX_ITEM = 21;

	private int itemCount = 0;
	private OnItemSelectedListener onItemSelectedListener;

	public PhotosAdapter(Context context, List<PhotoDB> items, OnItemSelectedListener listener) {
		super(context, items);
		itemCount = MAX_ITEM;
		onItemSelectedListener = listener;
	}

	@Override
	protected ItemPhotoView onCreateItemView(ViewGroup parent, int viewType) {
		return ItemPhotoView_.build(context);
	}

	@Override
	public void onBindViewHolder(ViewWrapper<ItemPhotoView> holder, final int position) {
		PhotoDB item = getItem(position);
		if (item != null) {
			holder.getView().bind(getItem(position));
			holder.getView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onItemSelectedListener.onItemSelected(position);
				}
			});
		}
	}

	public void setItems(List<PhotoDB> items) {
		this.items = items;
		resetItemCount();
		notifyDataSetChanged();
	}

	@Override
	@Nullable
	public PhotoDB getItem(int position) {
		if (items.size() > 0) {
			return items.get(position);
		} else {
			return null;
		}
	}

	@Override
	public int getItemCount() {
		return itemCount;
	}

	public void nextItems() {
		itemCount += 20;
		if (itemCount > items.size()) {
			itemCount = items.size();
		}
		notifyDataSetChanged();
	}

	private void resetItemCount() {
		itemCount = MAX_ITEM;
		if (itemCount > items.size()) {
			itemCount = items.size();
		}
	}
}
