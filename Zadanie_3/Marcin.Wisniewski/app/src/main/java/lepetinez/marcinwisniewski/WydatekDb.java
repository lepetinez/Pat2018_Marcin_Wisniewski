package lepetinez.marcinwisniewski;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pc on 22.01.2018.
 */

@SuppressWarnings("ALL")
public class WydatekDb  {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_VALUE = "value";
    public static final String KEY_TYPE = "typeId";
    public static final String KEY_DATE = "date";

    public static final String SQLITE_TABLE = "Wydatek";

    private static final String TABLE_CREATE  =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID +  " integer PRIMARY KEY autoincrement, " +
                    KEY_VALUE + ", "+
                    KEY_NAME + ", "+
                    KEY_DATE +", "
                    + KEY_TYPE + " INTEGER , FOREIGN KEY ("+ KEY_TYPE +") REFERENCES "
                    + TypDb.SQLITE_TABLE+" (" + TypDb.KEY_ROWID+") ON DELETE CASCADE );";

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public static void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }


}
