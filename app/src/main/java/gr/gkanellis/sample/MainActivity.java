package gr.gkanellis.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import gr.gkanellis.utilities.store.TableStore;
import io.reactivex.Single;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SampleStore sampleStore = new SampleStore(this);
        sampleStore.putBoolean("test_1", true)
                .andThen(sampleStore.putObject("test_2", new SimpleObject("george", 22)))
                .andThen(sampleStore.putDouble("test_3", 233.632))
                .subscribe(() -> {
                            sampleStore.getBoolean("test_1", false)
                                    .flatMap(v -> {
                                        Log.e(TAG, "test_1: " + v);
                                        return sampleStore.getObject("test_2", SimpleObject.class, null);
                                    })
                                    .flatMap(v -> {
                                        Log.e(TAG, "test_2: " + v.orElse(null));
                                        return sampleStore.getDouble("test_3", 0D);
                                    })
                                    .flatMap(v -> {
                                        Log.e(TAG, "test_3: " + v);
                                        return Single.just(true);
                                    })
                                    .subscribe(b -> {
                                    }, throwable -> Log.e(TAG, "onCreate: ", throwable));
                        },
                        throwable -> Log.e(TAG, "onCreate: Failed to complete actions", throwable));

    }

    private static class SimpleObject {
        private String fieldA;
        private int filedB;

        public SimpleObject(String fieldA, int filedB) {
            this.fieldA = fieldA;
            this.filedB = filedB;
        }

        public String getFieldA() {
            return fieldA;
        }

        public int getFiledB() {
            return filedB;
        }

        @Override
        public String toString() {
            return "SimpleObject{" +
                    "fieldA='" + fieldA + '\'' +
                    ", filedB=" + filedB +
                    '}';
        }
    }

    private static class SampleStore extends TableStore {

        public SampleStore(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public String name() {
            return "test";
        }

        @Override
        public boolean isEncrypted() {
            return false;
        }

        @NonNull
        @Override
        public Gson getGsonFactory() {
            return new Gson();
        }
    }

}
