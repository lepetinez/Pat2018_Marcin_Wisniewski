package lepetinez.marcinwisniewski;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashscreenActivity extends Activity {

    private static final int THREAD_LENGTH = 5000;
    private boolean PAUSE_FLAG = false;
    private Runnable RUNNABLE;
    private Handler RUNNABLE_HANDLER;
    private long TIME_OF_PAUSE;
    private long TIME_OF_CREATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TIME_OF_CREATE = System.currentTimeMillis();
        setContentView(R.layout.activity_splashscreen);
        createGifImageView();
        initialize();

    }

    public void onBackPressed() {
        super.onBackPressed();
        RUNNABLE_HANDLER.removeCallbacks(RUNNABLE);
    }

    private void initialize() {
        RUNNABLE_HANDLER = new Handler();
        RUNNABLE = new Runnable() {
            @Override
            public void run() {
                if (!PAUSE_FLAG) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        };
        RUNNABLE_HANDLER.postDelayed(RUNNABLE, THREAD_LENGTH);
    }

    protected void onPause() {
        TIME_OF_PAUSE = System.currentTimeMillis();
        super.onPause();
        PAUSE_FLAG = true;
        RUNNABLE_HANDLER.removeCallbacks(RUNNABLE);
    }

    protected void onRestart() {
        super.onRestart();
        PAUSE_FLAG = false;
        RUNNABLE_HANDLER.postDelayed(RUNNABLE, TIME_OF_CREATE - TIME_OF_PAUSE);
    }


    private void createGifImageView() {
        GifImageView gifImageView = findViewById(R.id.gifImageView);
        try {
            InputStream inputStream = getAssets().open("samolocik.png");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
            gifImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
