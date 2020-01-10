package gr.gkanellis.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java8.util.function.Consumer;
import timber.log.Timber;

public final class LocationUtils {

	public static final int LOCATION_PERMISSIONS_CODE = 921;
	public static final int REQUEST_CHECK_SETTINGS = 922;

	private static final String[] LOCATION_PERMISSIONS = new String[]{
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION
	};
	private static LocationUtils sInstance;

	private FusedLocationProviderClient mFusedLocationClient;
	private LocationRequest mLocationRequest;

	private LocationUtils(@NonNull Context context) {
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
	}

	public static LocationUtils getInstance(@NonNull Context context) {
		if (sInstance == null) {
			synchronized (LocationUtils.class) {
				sInstance = new LocationUtils(context);
			}
		}
		return sInstance;
	}

	public boolean canRequestLocation(@NonNull Context context) {
		Activity activity = (Activity) context;
		if (GenericUtils.hasPermissions(activity, LOCATION_PERMISSIONS)) {
			return true;
		} else {
			ActivityCompat.requestPermissions(activity, LOCATION_PERMISSIONS,
					LOCATION_PERMISSIONS_CODE);
			return false;
		}
	}

	private void getLocationWithRequest(@NonNull Context context,
										@Nullable Consumer<Location> consumer) {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(2000);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(mLocationRequest);
		LocationSettingsRequest locationSettingsRequest = builder.build();

		SettingsClient settingsClient = LocationServices.getSettingsClient(context);
		Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequest);
		task.addOnSuccessListener(locationSettingsResponse ->
				mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
					@Override
					public void onLocationResult(LocationResult locationResult) {
						if (consumer != null) {
							consumer.accept(locationResult.getLastLocation());
						}
						mFusedLocationClient.removeLocationUpdates(this);
					}
				}, Looper.myLooper()));
		task.addOnFailureListener(e -> {
			if (e instanceof ResolvableApiException) {
				try {
					ResolvableApiException resolvable = (ResolvableApiException) e;
					resolvable.startResolutionForResult(((Activity) context),
							REQUEST_CHECK_SETTINGS);
				} catch (IntentSender.SendIntentException ignore) {
				}
			}
			if (consumer != null) {
				consumer.accept(null);
			}
		});
	}

	private void getLastKnownLocation(@NonNull Consumer<Location> consumer) {
		mFusedLocationClient.getLastLocation()
				.addOnSuccessListener(consumer::accept)
				.addOnFailureListener(e -> {
					Timber.d(e);
					consumer.accept(null);
				});
	}

	public void getLocation(@NonNull Context context, @NonNull Consumer<Location> locationConsumer) {
		if (canRequestLocation(context)) {
			getLastKnownLocation(location -> {
				if (location != null) {
					locationConsumer.accept(location);
				} else {
					getLocationWithRequest(context, locationConsumer);
				}
			});
		}
	}

}
