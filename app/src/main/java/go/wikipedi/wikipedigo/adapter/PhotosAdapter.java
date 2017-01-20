package go.wikipedi.wikipedigo.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.List;

import go.wikipedi.base.OnItemSelectedListener;
import go.wikipedi.base.OnLastItemVisibleListener;
import go.wikipedi.base.RecyclerViewAdapterBase;
import go.wikipedi.base.ViewWrapper;
import go.wikipedi.wikipedigo.model.Photo;
import go.wikipedi.wikipedigo.view.ItemPhotoView;
import go.wikipedi.wikipedigo.view.ItemPhotoView_;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotosAdapter extends RecyclerViewAdapterBase<Photo, ItemPhotoView> {

	private static final int MAX_ITEM = 21;

	private int itemCount = 0;
	private OnItemSelectedListener onItemSelectedListener;
	private OnLastItemVisibleListener onLastItemVisibleListener;

	public PhotosAdapter(Context context, List<Photo> items) {
		super(context, items);
		itemCount = MAX_ITEM;
	}

	public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
		this.onItemSelectedListener = onItemSelectedListener;
	}

	public void setOnLastItemVisibleListener(OnLastItemVisibleListener onLastItemVisibleListener) {
		this.onLastItemVisibleListener = onLastItemVisibleListener;
	}

	@Override
	protected ItemPhotoView onCreateItemView(ViewGroup parent, int viewType) {
		return ItemPhotoView_.build(context);
	}

	@Override
	public void onBindViewHolder(final ViewWrapper<ItemPhotoView> holder, final int position) {
		Photo item = getItem(position);
		if (item != null) {
			holder.getView().bind(getItem(position));
			holder.getView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onItemSelectedListener.onItemSelected(position);
				}
			});
			if (position == itemCount - 1) {
				holder.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							holder.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
						} else {
							holder.getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
						}
						onLastItemVisibleListener.onLastItemVisible();
					}
				});
			}
		}
	}

	public void setItems(List<Photo> items) {
		this.items = items;
		resetItemCount();
		notifyDataSetChanged();
	}

	@Override
	@Nullable
	public Photo getItem(int position) {
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

	public void showNextItems() {
		itemCount += MAX_ITEM;
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
