package org.dlug.disastercenter.db;

import java.util.ArrayList;
import java.util.List;

import org.dlug.disastercenter.data.DisasterMessageData;
import org.dlug.disastercenter.utils.Trace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	// DB 이름
	private static final String DATABASE_NAME = "DisasterMessage";

	// DB 버전
	private static final int DATABASE_VERSION = 1;

	// 테이블 명
	private static final String TABLE_MESSAGE = "video";
	private static final String COL_MESSAGE_ID = "_id";
	private static final String COL_MESSAGE_SERVER_ID = "server_id";
	private static final String COL_MESSAGE_DISASTER_TYPE = "disaster_type";
	private static final String COL_MESSAGE_CONTENT = "content";
	private static final String COL_MESSAGE_DATE = "date";

	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;
	private final Context mContext;
	
	
	private static final String SQL_CREATE_TABLE_MESSAGE = ""
			+ "CREATE TABLE " + TABLE_MESSAGE + " ("
			+ COL_MESSAGE_ID	+ " integer primary key autoincrement,"
			+ COL_MESSAGE_SERVER_ID + " integer,"			// TODO long 타입으로 변경 할 수 있으면 변경하길 바람. (대신 insert 및 select 시에 얻는 방법을 getInt가 아닌 getLong으로 변경)
			+ COL_MESSAGE_DISASTER_TYPE + " integer,"
			+ COL_MESSAGE_CONTENT + " text,"
			+ COL_MESSAGE_DATE + " long"
			+ ")";

	// DB Helper 클래스
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		public DatabaseHelper(Context context) {	
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE_MESSAGE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TABLE_MESSAGE);
			this.onCreate(db);
		}
	}
	
	public DBAdapter(Context context) {
		mContext = context;
	}

	public DBAdapter open() throws SQLException {
		
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if ( mDbHelper != null ) {
			mDbHelper.close();
			mDbHelper = null;
		}
	}
	
	
	/* TalkThreadKey SQL */
	public boolean insertDisasterMessageData(DisasterMessageData data) {
		if ( !mDb.isOpen() ) 
			return false;
		

		ContentValues initialValues = new ContentValues();
//		initialValues.put(COL_MESSAGE_ID, DisasterMessageData.getId());
		initialValues.put(COL_MESSAGE_SERVER_ID, data.getServerId());

		initialValues.put(COL_MESSAGE_DISASTER_TYPE, data.getDisasterType());
		initialValues.put(COL_MESSAGE_CONTENT, data.getContent());

		initialValues.put(COL_MESSAGE_DATE, data.getDate());
		
		int id = (int)mDb.insert(TABLE_MESSAGE, null, initialValues);
		data.setId(id);

		return id > 0;
	}
	
	public boolean updateDisasterMessageData(DisasterMessageData data) {
		if ( !mDb.isOpen() ) 
			return false;
		
		int id = data.getId();
		
		
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COL_MESSAGE_ID, id);
		initialValues.put(COL_MESSAGE_SERVER_ID, data.getServerId());

		initialValues.put(COL_MESSAGE_DISASTER_TYPE, data.getDisasterType());
		initialValues.put(COL_MESSAGE_CONTENT, data.getContent());

		initialValues.put(COL_MESSAGE_DATE, data.getDate());
		
		
		return mDb.update(TABLE_MESSAGE, initialValues, COL_MESSAGE_ID + "=" + id, null) != 0;
	}
	
	public List<DisasterMessageData> getDisasterMessageDataListAll() {
		
		String query = String.format("SELECT * FROM %s", TABLE_MESSAGE);
		Cursor cursor = mDb.rawQuery(query, null);

		int cursorCnt = cursor.getCount();
		ArrayList<DisasterMessageData> dataList = new ArrayList<DisasterMessageData>(cursorCnt);

		while ( cursor.moveToNext() ) {
			int id = cursor.getInt(cursor.getColumnIndex(COL_MESSAGE_ID));
			int serverId= cursor.getInt(cursor.getColumnIndex(COL_MESSAGE_SERVER_ID));

			int disasterType = cursor.getInt(cursor.getColumnIndex(COL_MESSAGE_DISASTER_TYPE));
			String content = cursor.getString(cursor.getColumnIndex(COL_MESSAGE_CONTENT));

			long date = cursor.getLong(cursor.getColumnIndex(COL_MESSAGE_DATE));

			dataList.add(new DisasterMessageData(id, serverId, disasterType, content, date));			
		}
		
		return dataList;
	}
	
	
	public boolean deleteDisasterMessageData(DisasterMessageData data) {
		if ( !mDb.isOpen() ) 
			return false;
		
		int result = mDb.delete(TABLE_MESSAGE, String.format("%s=%d", COL_MESSAGE_ID, data.getId()), null);
		Trace.Error("DeleteResult: " + result);
		return result != 0;
	}
}