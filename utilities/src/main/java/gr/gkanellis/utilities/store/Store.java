package gr.gkanellis.utilities.store;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Optional;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface Store {

    @NonNull
    String name();

    boolean isEncrypted();

    @NonNull
    Gson getGsonFactory();

    Single<Boolean> contains(@NonNull CharSequence key);

    @NonNull
    Single<Boolean> remove(@NonNull CharSequence key);

    Single<Boolean> clear();

    @NonNull
    Single<Boolean> getBoolean(@NonNull CharSequence key, boolean defValue);

    @NonNull
    Single<Integer> getInteger(@NonNull CharSequence key, int defValue);

    @NonNull
    Single<Long> getLong(@NonNull CharSequence key, long defValue);

    @NonNull
    Single<Double> getDouble(@NonNull CharSequence key, double defValue);

    @NonNull
    Single<Float> getFloat(@NonNull CharSequence key, float defValue);

    @NonNull
    Single<Byte> getByte(@NonNull CharSequence key, byte defValue);

    @NonNull
    Single<Short> getShort(@NonNull CharSequence key, short defValue);

    @NonNull
    Single<Optional<CharSequence>> getString(@NonNull CharSequence key, CharSequence defValue);

    @NonNull
    <T> Single<Optional<T>> getObject(@NonNull CharSequence key, @NonNull Class<T> cls, T defValue);

    @NonNull
    Completable putBoolean(@NonNull CharSequence key, boolean value);

    @NonNull
    Completable putInteger(@NonNull CharSequence key, int value);

    @NonNull
    Completable putLong(@NonNull CharSequence key, long value);

    @NonNull
    Completable putDouble(@NonNull CharSequence key, double value);

    @NonNull
    Completable putFloat(@NonNull CharSequence key, float value);

    @NonNull
    Completable putByte(@NonNull CharSequence key, byte value);

    @NonNull
    Completable putShort(@NonNull CharSequence key, short value);

    @NonNull
    Completable putString(@NonNull CharSequence key, CharSequence value);

    @NonNull
    <T> Completable putObject(@NonNull CharSequence key, T value);

    default void validateKey(CharSequence key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Could not refer to a null/empty key");
        }
    }

    @NonNull
    default Single<Boolean> getBoolean(@NonNull CharSequence key) {
        return getBoolean(key, false);
    }

    @NonNull
    default Single<Integer> getInteger(@NonNull CharSequence key) {
        return getInteger(key, -1);
    }

    @NonNull
    default Single<Long> getLong(@NonNull CharSequence key) {
        return getLong(key, -1L);
    }

    @NonNull
    default Single<Double> getDouble(@NonNull CharSequence key) {
        return getDouble(key, -1D);
    }

    @NonNull
    default Single<Float> getFloat(@NonNull CharSequence key) {
        return getFloat(key, -1f);
    }

    @NonNull
    default Single<Byte> getByte(@NonNull CharSequence key) {
        return getByte(key, (byte) -1);
    }

    @NonNull
    default Single<Short> getShort(@NonNull CharSequence key) {
        return getShort(key, (short) -1);
    }

    @NonNull
    default Single<Optional<CharSequence>> getString(@NonNull CharSequence key) {
        return getString(key, null);
    }

    @NonNull
    default <T> Single<Optional<T>> getObject(@NonNull CharSequence key, @NonNull Class<T> cls) {
        return getObject(key, cls, null);
    }

}
