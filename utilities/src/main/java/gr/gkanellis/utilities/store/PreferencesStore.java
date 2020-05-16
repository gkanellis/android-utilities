package gr.gkanellis.utilities.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

import io.reactivex.Completable;
import io.reactivex.Single;

public abstract class PreferencesStore implements Store {

    private final Context mContext;
    private SharedPreferences mSharedPreferences;

    protected PreferencesStore(@NonNull Context context) {
        mContext = context;
        createSharedPreferencesInstance();
    }

    @NonNull
    @Override
    public abstract String name();

    @Override
    public abstract boolean isEncrypted();

    @NonNull
    @Override
    public abstract Gson getGsonFactory();

    private void createSharedPreferencesInstance() {
        if (mSharedPreferences == null) {
            String storeName = name();
            if (TextUtils.isEmpty(storeName)) {
                throw new IllegalStateException("Could not create a shared preferences " +
                        "store with an empty name.");
            }
            if (isEncrypted()) {
                try {
                    String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
                    mSharedPreferences = EncryptedSharedPreferences.create(
                            storeName,
                            masterKeyAlias,
                            mContext,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    );
                } catch (Exception e) {
                    throw new IllegalStateException("Could not create encrypted " +
                            "shared preferences store", e);
                }
            } else {
                mSharedPreferences = mContext.getSharedPreferences(storeName,
                        Context.MODE_PRIVATE);
            }
        }
    }

    @Override
    @NonNull
    public Single<Boolean> remove(@NonNull CharSequence key) {
        validateKey(key);
        return Single.create(emitter -> {
            mSharedPreferences.edit()
                    .remove(key.toString())
                    .apply();
            emitter.onSuccess(true);
        });
    }

    @Override
    public Single<Boolean> contains(@NotNull CharSequence key) {
        validateKey(key);
        return Single.create(emitter ->
                emitter.onSuccess(mSharedPreferences.contains(key.toString())));
    }

    @NotNull
    @Override
    public Single<Boolean> getBoolean(@NonNull CharSequence key, boolean defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            boolean val = mSharedPreferences.getBoolean(key.toString(), defValue);
            emitter.onSuccess(val);
        });
    }

    @NotNull
    @Override
    public Single<Integer> getInteger(@NonNull CharSequence key, int defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            int val = mSharedPreferences.getInt(key.toString(), defValue);
            emitter.onSuccess(val);
        });
    }

    @NotNull
    @Override
    public Single<Long> getLong(@NonNull CharSequence key, long defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            long val = mSharedPreferences.getLong(key.toString(), defValue);
            emitter.onSuccess(val);
        });
    }

    @NotNull
    @Override
    public Single<Double> getDouble(@NonNull CharSequence key, double defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            long longVal = mSharedPreferences.getLong(key.toString(),
                    Double.doubleToLongBits(defValue));
            emitter.onSuccess(Double.longBitsToDouble(longVal));
        });
    }

    @NotNull
    @Override
    public Single<Float> getFloat(@NonNull CharSequence key, float defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            float val = mSharedPreferences.getFloat(key.toString(), defValue);
            emitter.onSuccess(val);
        });
    }

    @NotNull
    @Override
    public Single<Optional<CharSequence>> getString(@NonNull CharSequence key, CharSequence defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            String val = mSharedPreferences.getString(key.toString(), null);
            emitter.onSuccess(val != null ? Optional.of(val) : Optional.ofNullable(defValue));
        });
    }

    @NotNull
    @Override
    public Single<Byte> getByte(@NonNull CharSequence key, byte defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            byte val = (byte) mSharedPreferences.getInt(key.toString(), defValue);
            emitter.onSuccess(val);
        });
    }

    @NotNull
    @Override
    public Single<Short> getShort(@NonNull CharSequence key, short defValue) {
        validateKey(key);
        return Single.create(emitter -> {
            short val = (short) mSharedPreferences.getInt(key.toString(), defValue);
            emitter.onSuccess(val);
        });
    }

    @NotNull
    @Override
    public <T> Single<Optional<T>> getObject(@NonNull CharSequence key, @NonNull Class<T> cls, T defValue) {
        validateKey(key);
        Objects.requireNonNull(cls, "Object class should not be null");
        return getString(key, null)
                .map(val -> val.map(c -> Optional.ofNullable(getGsonFactory().fromJson(c.toString(), cls)))
                        .orElseGet(() -> Optional.ofNullable(defValue))
                );
    }

    @NotNull
    @Override
    public Completable putLong(@NonNull CharSequence key, long value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit().putLong(key.toString(), value).apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putBoolean(@NonNull CharSequence key, boolean value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit().putBoolean(key.toString(), value).apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putInteger(@NonNull CharSequence key, int value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit().putInt(key.toString(), value).apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putDouble(@NonNull CharSequence key, double value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit()
                    .putLong(key.toString(), Double.doubleToRawLongBits(value))
                    .apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putFloat(@NonNull CharSequence key, float value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit().putFloat(key.toString(), value).apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putByte(@NonNull CharSequence key, byte value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit().putInt(key.toString(), value).apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putShort(@NonNull CharSequence key, short value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit().putInt(key.toString(), value).apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public Completable putString(@NonNull CharSequence key, CharSequence value) {
        validateKey(key);
        return Completable.create(emitter -> {
            mSharedPreferences.edit()
                    .putString(key.toString(), value != null ? value.toString() : null)
                    .apply();
            emitter.onComplete();
        });
    }

    @NotNull
    @Override
    public <T> Completable putObject(@NonNull CharSequence key, T value) {
        validateKey(key);
        return Completable.create(emitter -> {
            String jsonValue = getGsonFactory().toJson(value);
            mSharedPreferences.edit()
                    .putString(key.toString(), jsonValue)
                    .apply();
            emitter.onComplete();
        });
    }

    @Override
    public Single<Boolean> clear() {
        return Single.create(emitter -> emitter.onSuccess(mSharedPreferences.edit().clear().commit()));
    }
}
