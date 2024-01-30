package ikhwan.hanif.alienshooter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity {


    Button hTSBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        hTSBtn = findViewById(R.id.btnHowToShot);
        hTSBtn.setOnClickListener(view -> startActivity(new Intent(Info.this, HowToShotActivity.class)));



    }

}