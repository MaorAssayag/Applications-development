// ------------------------------------ DBAdapter.java ---------------------------------------------

package optimisticapps.v_taskmangement.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;

	public static final String KEY_NAME = "name";
	public static final String KEY_TIME = "time";
	
	public static final int COL_NAME = 1;
	public static final int COL_TIME = 2;
	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_TIME};
	
	// DB info: it's name, and the table we are using (just one).
	public static final String DATABASE_NAME = "MyDb.db";
	public static final String DATABASE_TABLE = "mainTable";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE
			+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_NAME + " TEXT NOT NULL, "
			+ KEY_TIME + " TEXT"
			+ ");";
	
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		this.myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(String name, String time) {
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_TIME, time);

		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public void insertRow_void (String name, String time) {
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_TIME, time);

		// Insert it into the database.
		db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}

	// Get a specific row (by KEY_NAME)
	public long getRowByName(String name) {
		Cursor cursor = null;
		long id = 0;
		try {
			cursor = db.rawQuery("SELECT "+KEY_ROWID+" FROM "+ DATABASE_TABLE +" WHERE name=?", new String[]{name});
			if(cursor.getCount() > 0) {
				cursor.moveToFirst();
				id = cursor.getLong(cursor.getColumnIndex(KEY_ROWID));
			}
			return id;
		}finally {
			cursor.close();
		}
	}

	public void deleteRowByName (String name){
		db.delete(DATABASE_TABLE, KEY_NAME + "=?", new String[]{name});
    }

	public void deleteRowByNameAndTime (String name,String Time){
		db.delete(DATABASE_TABLE, KEY_NAME + " = ? AND " + KEY_TIME + " = ?", new String[]{name,Time});
	}

	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		//String where = null;
        Cursor c = db.query(DATABASE_TABLE, null, null,
                null, null, null, KEY_TIME + " ASC", null);
       // Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String time) {
		String where = KEY_ROWID + "=" + rowId;
		// Create row's data:
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TIME, time);

		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}

	/////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Private class which handles database creation and upgrading.
	 * Used to handle low-level database access.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
