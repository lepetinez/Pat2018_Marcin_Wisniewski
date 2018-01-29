package lepetinez.marcinwisniewski;

import android.database.sqlite.SQLiteDatabase;


@SuppressWarnings("ALL")
public class TypDb {
    public static final String KEY_ROWID= "_id";
    public static final String KEY_TYPE = "type";

    public static final String SQLITE_TABLE = "Typ";

    private static final String TABLE_CREATE  =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID +  " integer PRIMARY KEY autoincrement, " +
                    KEY_TYPE + ");";


    public static void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public static void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}
