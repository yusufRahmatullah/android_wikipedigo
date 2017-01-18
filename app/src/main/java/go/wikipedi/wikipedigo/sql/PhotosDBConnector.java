package go.wikipedi.wikipedigo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by E460 on 17/01/2017.
 */

public class PhotosDBConnector extends SQLiteOpenHelper {

	private static final String TABLE_NAME = "photos";
	private static final String DATABASE_NAME = "photo.db";
	private static final int DATABASE_VERSION = 1;

	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + PhotoEntry.TABLE_NAME + " (" +
					PhotoEntry._ID + " INTEGER PRIMARY KEY," +
					PhotoEntry.COLUMN_NAME_NAME + " TEXT," +
					PhotoEntry.COLUMN_NAME_IMAGES + " TEXT)";

	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME;

	public PhotosDBConnector(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}

	public static class PhotoEntry implements BaseColumns{
		public static final String TABLE_NAME = "photos";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_IMAGES = "images";

	}
}
