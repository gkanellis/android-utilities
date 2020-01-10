package gr.gkanellis.utilities.helper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class AbstractObserver<T> implements Observer<T> {

	@Override
	public void onSubscribe(Disposable d) {

	}

	@Override
	public void onNext(T t) {

	}

	@Override
	public void onError(Throwable e) {

	}

	@Override
	public void onComplete() {

	}
}
