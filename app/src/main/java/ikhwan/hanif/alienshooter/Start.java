package ikhwan.hanif.alienshooter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class Start extends AppCompatActivity {


    ImageButton imageButton;
    TextView textView;

    ImageView settings, logOut, leaderBoard, privacyPolicyIcon, info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start.this, SettingsActivity.class));


            }
        });

        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.signOut();

                // Beralih ke SignInActivity setelah logout
                startActivity(new Intent(Start.this, SignInActivity.class));
                finish();

            }
        });



        leaderBoard = findViewById(R.id.leaderBoard);
        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start.this, LeaderboardActivity.class));
            }
        });

        imageButton = findViewById(R.id.startBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start.this, MainActivity.class));
                finish();
            }
        });
        textView = findViewById(R.id.startText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Start.this, MainActivity.class));
                finish();
            }
        });



        info = findViewById(R.id.infoBtn);
        info.setOnClickListener(view -> {
            startActivity(new Intent(Start.this, Info.class));
        });



        privacyPolicyIcon = findViewById(R.id.privacyPolicy);
        privacyPolicyIcon.setOnClickListener(view -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.privacy_policy_link)));
            startActivity(browserIntent);

        });
    }




}
