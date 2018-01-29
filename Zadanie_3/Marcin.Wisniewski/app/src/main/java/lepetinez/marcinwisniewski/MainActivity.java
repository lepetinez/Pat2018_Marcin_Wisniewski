package lepetinez.marcinwisniewski;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private MenuItem deleteItem;
    private MenuItem editItem;
    private SimpleCursorAdapter adapter;
    private int itemPosition;
    private boolean itemsVisibility = false;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        displayListView();
    }

    private void displayListView() {
        String[] columns = new String[]{
                WydatekDb.KEY_NAME,
                WydatekDb.KEY_VALUE,
                TypDb.KEY_TYPE,
                WydatekDb.KEY_DATE,
        };

        int[] to = new int[]{
                R.id.nazwaa,
                R.id.value,
                R.id.type,
                R.id.date,
        };
        adapter = new SimpleCursorAdapter(this, R.layout.wydatek_info, null, columns, to, 0);

        listView = findViewById(R.id.wydatekList);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!itemsVisibility) {
                    itemsVisibility = true;
                    deleteItem = mMenu.findItem(R.id.action_drop);
                    editItem = mMenu.findItem(R.id.action_edit);
                    if (deleteItem != null && editItem != null) {
                        deleteItem.setVisible(true);
                        editItem.setVisible(true);
                    }
                }
                itemPosition = i;
                for (int j = 0; j < listView.getChildCount(); j++) {
                    if (i == j) {
                        listView.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                    } else {
                        listView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                WydatekDb.SQLITE_TABLE + "." + WydatekDb.KEY_ROWID,
                WydatekDb.KEY_VALUE,
                WydatekDb.KEY_NAME,
                WydatekDb.KEY_TYPE,
                TypDb.KEY_TYPE,
                WydatekDb.KEY_DATE,
        };

        return new CursorLoader(this, DatabaseContentProvider.CONTENT_URI_WYDATKI, projection, null, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
        for (int j = 0; j < listView.getChildCount(); j++) {
            listView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
        }
        if (deleteItem != null && editItem != null) {
            deleteItem.setVisible(false);
            editItem.setVisible(false);
            itemsVisibility = false;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override

    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addAction();
                break;
            case R.id.action_drop:
                if (adapter.getCount() > 0) {
                    dropAction();
                }
                break;
            case R.id.action_edit:
                if (adapter.getCount() > 0) {
                    editAction();
                }
                break;
        }
        return false;
    }

    private void addAction() {
        Intent wydatekEdit = new Intent(getBaseContext(), WydatekEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("mode", "add");
        wydatekEdit.putExtras(bundle);
        startActivity(wydatekEdit);
    }

    private void editAction() {
        Cursor editCursor = (Cursor) listView.getItemAtPosition(itemPosition);
        String rowId = editCursor.getString(editCursor.getColumnIndexOrThrow(WydatekDb.KEY_ROWID));
        int typeId = editCursor.getInt(editCursor.getColumnIndexOrThrow(WydatekDb.KEY_TYPE));
        Intent wydatekEdit = new Intent(getBaseContext(), WydatekEdit.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("mode", "update");
        bundle2.putString("rowId", rowId);
        bundle2.putInt("typeId", typeId);
        wydatekEdit.putExtras(bundle2);
        startActivity(wydatekEdit);
        deleteItem.setVisible(false);
        editItem.setVisible(false);
        itemsVisibility = false;
    }

    private void dropAction() {
        Cursor dropCursor = (Cursor) listView.getItemAtPosition(itemPosition);
        String rowId = dropCursor.getString(dropCursor.getColumnIndexOrThrow(WydatekDb.KEY_ROWID));
        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_WYDATKI + "/" + rowId);
        getContentResolver().delete(uri, null, null);
        getLoaderManager().restartLoader(0, null, this);
        deleteItem.setVisible(false);
        editItem.setVisible(false);
        itemsVisibility = false;
        for (int j = 0; j < listView.getChildCount(); j++) {
            listView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
        }
    }
}