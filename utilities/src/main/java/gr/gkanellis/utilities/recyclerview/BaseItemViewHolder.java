package gr.gkanellis.utilities.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class BaseItemViewHolder<T extends RecyclerViewModel> extends RecyclerView.ViewHolder {

	protected BaseItemViewHolder(@NonNull View itemView) {
		this(null, itemView);
	}

	protected BaseItemViewHolder(@Nullable BaseRecyclerViewAdapter<T> adapter,
                                 @NonNull View itemView) {
        super(itemView);
		if (adapter != null && adapter.mOnItemClickListeners.size() > 0) {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                for (BaseRecyclerViewAdapter.OnItemClickListener<T>
                        listener : adapter.mOnItemClickListeners) {
                    listener.onItemClick(this, adapter.getItem(position), position,
                            getItemViewType());
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
	public final <V extends View> V findViewById(int id) {
        return (V) itemView.findViewById(id);
    }

}
