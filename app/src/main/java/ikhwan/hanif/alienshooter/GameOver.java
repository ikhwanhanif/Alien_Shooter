package ikhwan.hanif.alienshooter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    TextView tvPoints, highScoreTv;
    int highScores, lastScore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        int points = getIntent().getExtras().getInt("points");
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText("" + points);



        highScoreTv = findViewById(R.id.highScoreTv);

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        lastScore = preferences.getInt("lastScore", 0);
        highScores = preferences.getInt("highScores", 0);

        if (lastScore > highScores){
            highScores = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highScores", highScores);
            editor.apply();
        }

        highScoreTv.setText("" + highScores);


    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, Start.class);
        startActivity(intent);
        finish();
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.main_sound);
        mPlayer.start();
    }

    public void exit(View view) {
        finish();
    }

}
