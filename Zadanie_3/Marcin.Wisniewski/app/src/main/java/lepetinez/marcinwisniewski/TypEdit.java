package lepetinez.marcinwisniewski;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("ALL")
public class TypEdit extends AppCompatActivity implements View.OnClickListener {
    private Button save;
    private String mode;
    private EditText nazwaTypu;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typ_page);
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        mode = bundle.getString("mode");
        save = findViewById(R.id.saveTyp);
        save.setOnClickListener(this);
        nazwaTypu = findViewById(R.id.nazwaTypu);

        if (mode.trim().equalsIgnoreCase("update")) {
            Bundle bundle3 = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadTypInfo();
        }
    }

    @Override
    public void onClick(View view) {
        String myNazwa = nazwaTypu.getText().toString();
        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        if (myNazwa.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Prosze wpisac nazwe typu !", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()) {
            case R.id.saveTyp:
                ContentValues values = new ContentValues();
                values.put(TypDb.KEY_TYPE, myNazwa);


                if (mode.trim().equalsIgnoreCase("add")) {
                    getContentResolver().insert(DatabaseContentProvider.CONTENT_URI_TYP, values);
                } else {
                    Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_TYP + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

        }
    }

    private void loadTypInfo() {
        String[] projection = {
                TypDb.KEY_ROWID,
                TypDb.KEY_TYPE,
        };
        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_TYP + "/" + id);
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myTyp = cursor.getString(cursor.getColumnIndexOrThrow(TypDb.KEY_TYPE));
            nazwaTypu.setText(myTyp);

        }
    }

}
