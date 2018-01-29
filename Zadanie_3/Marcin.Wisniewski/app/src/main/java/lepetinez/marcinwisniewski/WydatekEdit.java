package lepetinez.marcinwisniewski;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@SuppressWarnings("ALL")
public class WydatekEdit extends AppCompatActivity implements View.OnClickListener {
    private List<String> labels;
    private Cursor cursor;
    private int day, month, year;
    private Calendar currentDate;
    private Spinner typesList;
    private Button save;
    private String mode;
    private EditText nazwa, wartosc;
    private TextView date;
    private String id;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wydatek_page);
        currentDate = Calendar.getInstance();
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH) + 1;
        year = currentDate.get(Calendar.YEAR);
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        mode = bundle.getString("mode");
        save = findViewById(R.id.save);
        date = findViewById(R.id.dateText);
        save.setOnClickListener(this);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(WydatekEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        date.setText(dayOfMonth + "/" + month + "/" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        nazwa = findViewById(R.id.nazwa);
        wartosc = findViewById(R.id.wartosc);

        typesList = findViewById(R.id.typeList);
        cursor = getAllTypes();
        String[] adapterCols = new String[]{"type"};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, adapterCols, new int[]{android.R.id.text1}, 0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typesList.setAdapter(sca);

        if (mode.trim().equalsIgnoreCase("add")) {
            date.setText(day + "/" + month + "/" + year);

        } else {
            Bundle bundle3 = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadWydatekInfo();

        }
    }

    @Override
    public void onClick(View view) {
        Cursor typeCursor = (Cursor) typesList.getSelectedItem();
        if (typeCursor == null) {
            Toast.makeText(WydatekEdit.this, R.string.typBlad, Toast.LENGTH_SHORT).show();
            return;
        }
        Integer myId = typeCursor.getInt(typeCursor.getColumnIndexOrThrow(TypDb.KEY_ROWID));
        String myNazwa = nazwa.getText().toString();
        String myWartosc = wartosc.getText().toString();
        String myDate = date.getText().toString();
        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        if (myNazwa.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, R.string.nazwaBlad, Toast.LENGTH_SHORT).show();
            return;
        }
        if (myWartosc.trim().equalsIgnoreCase("")) {
            Toast.makeText(this, R.string.wartoscBlad, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.save:
                ContentValues values = new ContentValues();
                values.put(WydatekDb.KEY_NAME, myNazwa);
                values.put(WydatekDb.KEY_VALUE, myWartosc);
                values.put(WydatekDb.KEY_TYPE, myId);
                values.put(WydatekDb.KEY_DATE, myDate);

                if (mode.trim().equalsIgnoreCase("add")) {
                    getContentResolver().insert(DatabaseContentProvider.CONTENT_URI_WYDATKI, values);
                } else {
                    Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_WYDATKI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

        }
    }

    private void loadWydatekInfo() {
        String[] projection = {
                WydatekDb.KEY_ROWID,
                WydatekDb.KEY_VALUE,
                WydatekDb.KEY_NAME,
                WydatekDb.KEY_TYPE,
                WydatekDb.KEY_DATE,

        };
        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_WYDATKI + "/" + id);
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(WydatekDb.KEY_NAME));
            String myValue = cursor.getString(cursor.getColumnIndexOrThrow(WydatekDb.KEY_VALUE));
            String myDate = cursor.getString(cursor.getColumnIndexOrThrow(WydatekDb.KEY_DATE));
            int myType = cursor.getInt(cursor.getColumnIndexOrThrow(WydatekDb.KEY_TYPE));
            this.cursor.moveToFirst();
            do {
                if (myType == this.cursor.getInt(cursor.getColumnIndexOrThrow(WydatekDb.KEY_ROWID))) {
                    typesList.setSelection(this.cursor.getPosition());
                    break;
                }
            }
            while (this.cursor.moveToNext());
            nazwa.setText(myName);
            wartosc.setText(myValue);
            date.setText(myDate);

        }
    }

    private Cursor getAllTypes() {
        labels = new ArrayList<>();
        String[] projection = {
                TypDb.KEY_ROWID,
                TypDb.KEY_TYPE,

        };
        Uri uri = Uri.parse(DatabaseContentProvider.CONTENT_URI_TYP + "");
        cursor = getContentResolver().query(uri, projection, null, null, null);

        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return cursor;
    }
}
