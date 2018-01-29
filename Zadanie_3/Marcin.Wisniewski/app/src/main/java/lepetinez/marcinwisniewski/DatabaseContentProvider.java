package lepetinez.marcinwisniewski;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


@SuppressWarnings("ALL")
public class DatabaseContentProvider extends ContentProvider {
    public static final int ALL_WYDATKI = 1;
    public static final int SINGLE_WYDATEK = 2;
    public static final int ALL_TYPY = 3;
    public static final int SINGLE_TYP = 4;
    public static final String AUTHORITY = "lepetinez.marcinwisniewski.contentprovider";
    public static final Uri CONTENT_URI_WYDATKI =
            Uri.parse("content://" + AUTHORITY + "/wydatki");
    public static final Uri CONTENT_URI_TYP =
            Uri.parse("content://" + AUTHORITY + "/typy");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "wydatki", ALL_WYDATKI);
        uriMatcher.addURI(AUTHORITY, "typy", ALL_TYPY);
        uriMatcher.addURI(AUTHORITY, "wydatki/#", SINGLE_WYDATEK);
        uriMatcher.addURI(AUTHORITY, "typy/#", SINGLE_TYP);
    }

    private MyDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper((getContext()));
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Log.i("taaak",String.valueOf(uriMatcher.match(uri)));
        switch (uriMatcher.match(uri)) {
            case ALL_WYDATKI:
                queryBuilder.setTables(WydatekDb.SQLITE_TABLE + " INNER JOIN " + TypDb.SQLITE_TABLE + " ON "
                        + WydatekDb.SQLITE_TABLE + "." + WydatekDb.KEY_TYPE + " = " + TypDb.SQLITE_TABLE + "." + TypDb.KEY_ROWID);

                break;
            case SINGLE_WYDATEK:
                queryBuilder.setTables(WydatekDb.SQLITE_TABLE);
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(WydatekDb.KEY_ROWID + "=" + id);
                break;
            case ALL_TYPY:
                queryBuilder.setTables(TypDb.SQLITE_TABLE);
                break;
            case SINGLE_TYP:
                queryBuilder.setTables(TypDb.SQLITE_TABLE);
                String idType = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(TypDb.KEY_ROWID + "=" + idType);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_WYDATKI:
                return "vnd.android.cursor.dir/vnd.lepetinez.marcinwisniewski.contentprovider.wydatki";
            case ALL_TYPY:
                return "vnd.android.cursor.dir/vnd.lepetinez.marcinwisniewski.contentprovider.typy";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case ALL_WYDATKI:
                id = db.insert(WydatekDb.SQLITE_TABLE, null, contentValues);
                break;
            case ALL_TYPY:
                id = db.insert(TypDb.SQLITE_TABLE, null, contentValues);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return null;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteCount;
        switch (uriMatcher.match(uri)) {
            case ALL_WYDATKI:
                deleteCount = db.delete(WydatekDb.SQLITE_TABLE, selection, selectionArgs);
                break;
            case SINGLE_WYDATEK:
                String id = uri.getPathSegments().get(1);
                selection = WydatekDb.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        "AND(" + selection + ')' : "");
                deleteCount = db.delete(WydatekDb.SQLITE_TABLE, selection, selectionArgs);
                break;
            case ALL_TYPY:
                deleteCount = db.delete(TypDb.SQLITE_TABLE, selection, selectionArgs);
                break;

            case SINGLE_TYP:
                String idTyp = uri.getPathSegments().get(1);
                selection = TypDb.KEY_ROWID + "=" + idTyp
                        + (!TextUtils.isEmpty(selection) ?
                        "AND(" + selection + ')' : "");
                db.setForeignKeyConstraintsEnabled(true);
                deleteCount = db.delete(TypDb.SQLITE_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateCount;
        switch (uriMatcher.match(uri)) {
            case ALL_WYDATKI:
                updateCount = db.update(WydatekDb.SQLITE_TABLE, contentValues, selection, selectionArgs);
                break;
            case SINGLE_WYDATEK:
                String id = uri.getPathSegments().get(1);
                selection = WydatekDb.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        "AND(" + selection + ')' : "");
                updateCount = db.update(WydatekDb.SQLITE_TABLE, contentValues, selection, selectionArgs);
                break;
            case ALL_TYPY:
                updateCount = db.update(TypDb.SQLITE_TABLE, contentValues, selection, selectionArgs);
                break;
            case SINGLE_TYP:
                String idTyp = uri.getPathSegments().get(1);
                selection = TypDb.KEY_ROWID + "=" + idTyp
                        + (!TextUtils.isEmpty(selection) ?
                        "AND(" + selection + ')' : "");
                updateCount = db.update(TypDb.SQLITE_TABLE,contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}