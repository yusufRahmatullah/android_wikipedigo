package go.wikipedi.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by E460 on 12/01/2017.
 */

public class BaseApplication extends Application {

	private static Context appContext;

	@Override
	public void onCreate() {
		super.onCreate();
		appContext = getApplicationContext();
	}

	public static Context getAppContext() {
		return appContext;
	}
}
