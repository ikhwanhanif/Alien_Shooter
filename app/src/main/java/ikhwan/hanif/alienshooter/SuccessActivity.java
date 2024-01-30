package ikhwan.hanif.alienshooter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {
    TextView tvPoints;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        points = getIntent().getExtras().getInt("points");
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText("" + points);

    }
    public void restart(View view) {
        startActivity(new Intent(SuccessActivity.this, Start.class));
        finish();
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.main_sound);
        mPlayer.start();

    }

    public void exit(View view) {

        finish();
    }
}