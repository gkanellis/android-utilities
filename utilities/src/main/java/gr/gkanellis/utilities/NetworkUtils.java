package gr.gkanellis.utilities;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gr.gkanellis.utilities.network.ThreadIndependentCallFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkUtils {

	private NetworkUtils() {
	}

	@NonNull
	public static Gson getDefaultGson() {
		return new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.create();
	}

	@NonNull
	public static Retrofit.Builder getRetrofitBuilder(@NonNull String baseUrl) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		if (BuildConfig.DEBUG) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
			builder.addInterceptor(loggingInterceptor);
		}
		return new Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(builder.build());
	}

	@NonNull
	public static Retrofit getDefaultRetrofit(@NonNull String baseUrl) {
		Retrofit.Builder builder = getRetrofitBuilder(baseUrl);
		return builder
				.addConverterFactory(GsonConverterFactory.create(getDefaultGson()))
				.addCallAdapterFactory(ThreadIndependentCallFactory.getDefault())
				.build();
	}

}
