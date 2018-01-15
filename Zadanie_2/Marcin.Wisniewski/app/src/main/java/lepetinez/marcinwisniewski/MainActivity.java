package lepetinez.marcinwisniewski;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    private EditText nameEditText;
    private TextView dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainViews();
    }

    private void initMainViews() {
        nameEditText = findViewById(R.id.loginField);
        dialogMessage = findViewById(R.id.dialogMessage);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validateNameField(nameEditText.getText().toString());
            }
        });
    }

    private void validateNameField(String nameString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_layout, null);
        dialogMessage = view.findViewById(R.id.dialogMessage);
        builder.setView(view);

        if (nameString.length() == 0) {
            dialogMessage.setText(R.string.failed_login_text);

        } else {
            dialogMessage.setText(buildMessage(nameString));

        }
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private String buildMessage(String nameString){
        return MainActivity.this.getString(R.string.hello) + " " + nameString;
    }

}
