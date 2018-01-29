package lepetinez.marcinwisniewski;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class ChooseModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageButton typesButton =  findViewById(R.id.typButton);
        typesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent typIntent = new Intent(ChooseModeActivity.this, TypActivity.class);
                startActivity(typIntent);

            }
        });
        final ImageButton placesButton =  findViewById(R.id.wydatekButton);
        placesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wydatekIntent = new Intent(ChooseModeActivity.this, MainActivity.class);
                startActivity(wydatekIntent);

            }
        });
    }

}