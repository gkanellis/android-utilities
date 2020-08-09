package gr.gkanellis.utilities.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

public class BaseRecyclerViewAdapter<T extends RecyclerViewModel> extends
        RecyclerView.Adapter<BaseItemViewHolder<T>> {

    protected final Context mContext;
    protected final LayoutInflater mLayoutInflater;
    protected final List<OnItemClickListener<T>> mOnItemClickListeners;
    protected OnCreateItemViewListener mOnCreateViewHolderListener;
    private final List<T> mItemsList;
    private final List<OnBindViewHolderListener<T>> mOnBindViewHolderListeners;
    private final List<AdapterObserver<T>> mObservers;

    public BaseRecyclerViewAdapter(@NonNull Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mOnBindViewHolderListeners = new ArrayList<>();
        this.mObservers = new ArrayList<>();
        this.mItemsList = new ArrayList<>();
        this.mOnItemClickListeners = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseItemViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mOnCreateViewHolderListener == null) {
            throw new NullPointerException("OnCreateViewHolderListener should not be null. " +
                    "Have you called setOnCreateViewHolderListener()?");
        }
        View view = mOnCreateViewHolderListener.onCreateItemView(mLayoutInflater, parent, viewType);
        return new BaseItemViewHolder<>(this, view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseItemViewHolder<T> holder, int position) {
        for (OnBindViewHolderListener<T> listener : mOnBindViewHolderListeners) {
            if (listener != null) {
                T t = mItemsList.get(position);
                listener.onBindViewHolder(holder, t, position, getItemViewType(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewModel viewModel = mItemsList.get(position);
        return viewModel.getViewType();
    }

    @SafeVarargs
    public final void addAll(T... models) {
        addAll(Arrays.asList(models));
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        int postLastPosition = mItemsList.size() - 1;
        mItemsList.addAll(collection);
        notifyItemRangeInserted(postLastPosition, mItemsList.size());
        notifyObservers();
    }

    public <S extends T> void addItem(S viewModel) {
        if (viewModel == null) {
            throw new NullPointerException("Cannot add a null model to list");
        }
        mItemsList.add(viewModel);
        notifyItemInserted(mItemsList.size() - 1);
        notifyObservers();
    }

    public <S extends T> void setItem(int index, S viewModel) {
        if (viewModel == null) {
            throw new NullPointerException("Cannot add a null model to list");
        }
        mItemsList.set(index, viewModel);
        notifyItemChanged(index);
        notifyObservers();
    }

    public void removeItem(int position) {
        if (position < 0 && position >= mItemsList.size() - 1) {
            throw new IndexOutOfBoundsException("Adapter index out of bounds: " + position);
        }
        mItemsList.remove(position);
        notifyItemRemoved(position);
        notifyObservers();
    }

    public void clear() {
        mItemsList.clear();
        notifyDataSetChanged();
        notifyObservers();
    }

	public boolean isEmpty() {
		return mItemsList.isEmpty();
	}

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public T getItem(int position) {
        return mItemsList.get(position);
    }

	public int indexOf(T t) {
		return mItemsList.indexOf(t);
	}

    public Stream<T> stream() {
        return StreamSupport.stream(mItemsList);
    }

    public void addOnItemClickListener(@NonNull OnItemClickListener<T> listener) {
        mOnItemClickListeners.add(listener);
    }

	public int getOnItemClickListenersCount() {
		return mOnItemClickListeners.size();
	}

	public OnItemClickListener<T> getOnItemClickListener(int position) {
		return mOnItemClickListeners.get(position);
	}

    public void addOnBindViewHolderListener(@NonNull OnBindViewHolderListener<T> listener) {
        mOnBindViewHolderListeners.add(listener);
    }

    public void setOnCreateViewHolderListener(@NonNull OnCreateItemViewListener listener) {
        this.mOnCreateViewHolderListener = listener;
    }

    public void registerObserver(AdapterObserver<T> observer) {
        mObservers.add(observer);
    }

    public void notifyItemChangedWithObservers(int position) {
        notifyItemChanged(position);
        notifyObservers();
    }

    private void notifyObservers() {
        for (AdapterObserver<T> observer : mObservers) {
            if (observer != null) {
                observer.onAdapterModified(this, mItemsList);
            }
        }
    }

    public interface AdapterObserver<T extends RecyclerViewModel> {

        void onAdapterModified(BaseRecyclerViewAdapter<T> adapter, List<T> list);

    }

    public interface OnBindViewHolderListener<T extends RecyclerViewModel> {

        void onBindViewHolder(@NonNull BaseItemViewHolder<T> holder, T t, int position,
							  int viewType);

    }

    public interface OnCreateItemViewListener {

        @NonNull
        View onCreateItemView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent,
							  int viewType);

    }

    public interface OnItemClickListener<T extends RecyclerViewModel> {

		void onItemClick(@NonNull BaseItemViewHolder<? extends T> holder, @NonNull T t, int position,
						 int viewType);

    }

}
