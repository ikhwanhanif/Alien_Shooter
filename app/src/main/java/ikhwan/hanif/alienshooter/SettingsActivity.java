package ikhwan.hanif.alienshooter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout ganti_bahasa;
    TextView textEmail;
    Button hapusAkun, toggleMuteButton;
    private FirebaseAuth mAuth;

    private AudioManager audioManager;
    private boolean isMuted; // New variable to store the original volume

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        toggleMuteButton = findViewById(R.id.toggleMuteButton);
        toggleMuteButton.setOnClickListener(view -> toggleMute());

        // Initialize the mute button text based on the current mute status
        updateMuteButtonText();

        hapusAkun = findViewById(R.id.hapusAkun);
        hapusAkun.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(getString(R.string.hapus_akun));
                TextView textView = new TextView(SettingsActivity.this);
                textView.setText(getString(R.string.anda_yakin_untuk_hapus_akun));
                textView.setTextColor(getColor(R.color.black));
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);
                LinearLayout linearLayout = new LinearLayout(SettingsActivity.this);
                linearLayout.addView(textView);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setPadding(10,10,10,10);
                builder.setIcon(getResources().getDrawable(R.drawable.baseline_warning_24));
                builder.setView(linearLayout);
                builder.setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DeleteAkun();

                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });
                builder.create().show();

            }
        });

        textEmail = findViewById(R.id.textEmailUser);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, update the UI with the user's email
            String userEmail = currentUser.getEmail();
            textEmail.setText(userEmail);
        } else {
            // User is not signed in, you may want to redirect them to the login screen
        }


        loadLocale();

        ganti_bahasa = findViewById(R.id.gantiBahasa);
        ganti_bahasa.setOnClickListener(view -> {

            showBarGantiBahasa();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void toggleMute() {
        isMuted = !isMuted;

        if (isMuted) {
            // Mute by setting the volume to zero
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {

            // Unmute by setting the volume to the original value
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        }

        updateMuteButtonText();
        showToast(isMuted ? "Sound Muted" : "Sound Unmuted");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateMuteButtonText() {
        toggleMuteButton.setText(isMuted ? "Unmute" : "Mute");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void DeleteAkun() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this,getString(R.string.sukses_menghapus_akun),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
                    finish();
                }
            }
        });



    }

    private void showBarGantiBahasa() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);



        final String[] listItems = {"English", "Indonesia"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingsActivity.this);
        mBuilder.setTitle(getString(R.string.choose_language));
        mBuilder.setSingleChoiceItems(listItems, -1, (dialogInterface, i) -> {

            if (i == 0){

                setLocale("en");
                recreate();
                startActivity(new Intent(SettingsActivity.this, SplashActivity.class));

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


                finish();

            }
            if (i == 1){

                setLocale("in");
                recreate();
                startActivity(new Intent(SettingsActivity.this, SplashActivity.class));

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                finish();

            }
            dialogInterface.dismiss();

        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }


    private void loadLocale() {

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);

    }
}