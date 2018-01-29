package lepetinez.marcinwisniewski;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pc on 22.01.2018.
 */

@SuppressWarnings("ALL")
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Wydatki";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        WydatekDb.onCreate(sqLiteDatabase);
        TypDb.onCreate(sqLiteDatabase);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        WydatekDb.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        TypDb.onUpgrade(sqLiteDatabase,oldVersion,newVersion);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
