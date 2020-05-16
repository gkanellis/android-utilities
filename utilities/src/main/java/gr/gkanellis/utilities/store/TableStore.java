package gr.gkanellis.utilities.store;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.Optional;

import gr.gkanellis.utilities.database.StoreDatabase;
import io.reactivex.Completable;
import io.reactivex.Single;
import java8.util.function.Function;


public abstract class TableStore implements Store {

    private final Context mContext;
    private StoreDatabase mStoreDatabase;

    protected TableStore(@NonNull Context context) {
        this.mContext = context;
        dispatchDatabaseOperations();
    }

    private void dispatchDatabaseOperations() {
        if (mStoreDatabase == null) {
            String storeName = name();
            if (TextUtils.isEmpty(storeName)) {
                throw new IllegalStateException("Could not create a table " +
                        "store with an empty name.");
            }
            mStoreDatabase = StoreDatabase.getInstance(mContext);
            if (!mStoreDatabase.create(storeName)) {
                throw new IllegalStateException("Failed to create table store");
            }
        }
    }

    @NonNull
    @Override
    public abstract String name();

    @Override
    public abstract boolean isEncrypted();

    @NonNull
    @Override
    public abstract Gson getGsonFactory();

    @NonNull
    private <T> Single<T> getValueFromTable(CharSequence key, T defValue, Function<String, T> mapper) {
        validateKey(key);
        return Single.create(emitter -> {
            String value = mStoreDatabase.getValue(name(), key.toString());
            emitter.onSuccess(value != null ? mapper.apply(value) : defValue);
        });
    }

    @NonNull
    private Completable putValueToTable(CharSequence key, Object value) {
        validateKey(key);
        return Completable.create(emitter -> {
            boolean success = mStoreDatabase.putValue(name(), key.toString(), value);
            if (success) {
                emitter.onComplete();
            } else {
                emitter.onError(new SQLException("Failed to store value with key: " + key));
            }
        });
    }

    @NonNull
    @Override
    public Single<Boolean> getBoolean(@NonNull CharSequence key, boolean defValue) {
        return getValueFromTable(key, defValue, Boolean::parseBoolean);
    }

    @NonNull
    @Override
    public Single<Integer> getInteger(@NonNull CharSequence key, int defValue) {
        return getValueFromTable(key, defValue, Integer::parseInt);
    }

    @NonNull
    @Override
    public Single<Long> getLong(@NonNull CharSequence key, long defValue) {
        return getValueFromTable(key, defValue, Long::parseLong);
    }

    @NonNull
    @Override
    public Single<Double> getDouble(@NonNull CharSequence key, double defValue) {
        return getValueFromTable(key, defValue, Double::parseDouble);
    }

    @NonNull
    @Override
    public Single<Float> getFloat(@NonNull CharSequence key, float defValue) {
        return getValueFromTable(key, defValue, Float::parseFloat);
    }

    @NonNull
    @Override
    public Single<Byte> getByte(@NonNull CharSequence key, byte defValue) {
        return getValueFromTable(key, defValue, Byte::parseByte);
    }

    @NonNull
    @Override
    public Single<Short> getShort(@NonNull CharSequence key, short defValue) {
        return getValueFromTable(key, defValue, Short::parseShort);
    }

    @NonNull
    @Override
    public Single<Optional<CharSequence>> getString(@NonNull CharSequence key, CharSequence defValue) {
        return getValueFromTable(key, Optional.ofNullable(defValue), Optional::ofNullable);
    }

    @NonNull
    @Override
    public <T> Single<Optional<T>> getObject(@NonNull CharSequence key, @NonNull Class<T> cls, T defValue) {
        return getValueFromTable(key,
                Optional.ofNullable(defValue),
                v -> Optional.ofNullable(getGsonFactory().fromJson(v, cls)));
    }

    @NonNull
    @Override
    public Completable putBoolean(@NonNull CharSequence key, boolean value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putInteger(@NonNull CharSequence key, int value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putLong(@NonNull CharSequence key, long value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putDouble(@NonNull CharSequence key, double value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putFloat(@NonNull CharSequence key, float value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putByte(@NonNull CharSequence key, byte value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putShort(@NonNull CharSequence key, short value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public Completable putString(@NonNull CharSequence key, CharSequence value) {
        return putValueToTable(key, value);
    }

    @NonNull
    @Override
    public <T> Completable putObject(@NonNull CharSequence key, T value) {
        String jsonValue = getGsonFactory().toJson(value);
        return putValueToTable(key, jsonValue);
    }

    @Override
    public Single<Boolean> contains(@NonNull CharSequence key) {
        validateKey(key);
        return Single.create(emitter -> {
            boolean contains = mStoreDatabase.contains(name(), key.toString());
            emitter.onSuccess(contains);
        });
    }

    @Override
    public Single<Boolean> clear() {
        return Single.create(emitter -> emitter.onSuccess(mStoreDatabase.clear(name())));
    }

    @NonNull
    @Override
    public Single<Boolean> remove(@NonNull CharSequence key) {
        validateKey(key);
        return Single.create(emitter -> {
            boolean removed = mStoreDatabase.remove(name(), key.toString());
            emitter.onSuccess(removed);
        });
    }
}
