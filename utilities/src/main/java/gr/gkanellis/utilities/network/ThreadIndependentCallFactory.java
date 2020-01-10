package gr.gkanellis.utilities.network;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public final class ThreadIndependentCallFactory extends CallAdapter.Factory {

    private static final RxJava2CallAdapterFactory RX_JAVA_CALL_ADAPTER_FACTORY =
            RxJava2CallAdapterFactory.create();
    private Scheduler subscribeScheduler;
    private Scheduler observerScheduler;

    public ThreadIndependentCallFactory(Scheduler subscribeScheduler, Scheduler observerScheduler) {
        this.subscribeScheduler = subscribeScheduler;
        this.observerScheduler = observerScheduler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations,
                                 @NonNull Retrofit retrofit) {
        CallAdapter<?, Observable<?>> callAdapter =
                (CallAdapter<?, Observable<?>>) RX_JAVA_CALL_ADAPTER_FACTORY.get(returnType,
                        annotations, retrofit);
        return callAdapter != null ? new ThreadCallAdapter(callAdapter) : null;
    }

    public static ThreadIndependentCallFactory getDefault() {
        return new ThreadIndependentCallFactory(Schedulers.io(), AndroidSchedulers.mainThread());
    }

    final class ThreadCallAdapter<T> implements CallAdapter<T, Observable<?>> {

        CallAdapter<T, Observable<?>> delegateAdapter;

        ThreadCallAdapter(CallAdapter<T, Observable<?>> delegateAdapter) {
            this.delegateAdapter = delegateAdapter;
        }

        @Override
        public Type responseType() {
            return delegateAdapter.responseType();
        }

        @Override
        @NonNull
        public Observable<?> adapt(@NonNull Call<T> call) {
            return delegateAdapter.adapt(call)
                    .subscribeOn(subscribeScheduler)
                    .observeOn(observerScheduler);
        }
    }
}
