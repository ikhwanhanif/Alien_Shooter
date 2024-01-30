package ikhwan.hanif.alienshooter;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class HowToShotActivity extends AppCompatActivity {

    VideoView vV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_shot);

        vV = findViewById(R.id.howToShotVv);
        vV.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.howtoshot);
        MediaController mediaController = new MediaController(this);
        vV.setMediaController(mediaController);
        mediaController.setAnchorView(vV);
        vV.start();

    }
}