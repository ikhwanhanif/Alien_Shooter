package ikhwan.hanif.alienshooter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity {


    Button hTSBtn, storyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        storyBtn = findViewById(R.id.btnStory);
        storyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Info.this, StoryActivity.class));
            }
        });

        hTSBtn = findViewById(R.id.btnHowToShot);
        hTSBtn.setOnClickListener(view -> startActivity(new Intent(Info.this, HowToShotActivity.class)));



    }

}