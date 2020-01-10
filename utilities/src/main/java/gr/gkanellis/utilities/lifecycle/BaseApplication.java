package gr.gkanellis.utilities.lifecycle;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.franmontiel.localechanger.LocaleChanger;

import java.util.List;
import java.util.Locale;

import gr.gkanellis.utilities.model.LocalePair;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public abstract class BaseApplication extends Application {

	private static BaseApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		sInstance = this;

		List<LocalePair> locales = getSupportedLocales();
		if (!locales.isEmpty()) {
			List<Locale> localeList = StreamSupport.stream(locales)
					.map(LocalePair::getLocale)
					.collect(Collectors.toList());
			LocaleChanger.initialize(this, localeList);
		}
	}

	@Nullable
	public Locale findLocaleById(@NonNull String id) {
		return StreamSupport.stream(getSupportedLocales())
				.filter(localePair -> localePair.getId().equals(id))
				.findFirst()
				.map(LocalePair::getLocale)
				.orElse(null);
	}

	@NonNull
	protected abstract List<LocalePair> getSupportedLocales();

	public static BaseApplication getInstance() {
		return sInstance;
	}
}
