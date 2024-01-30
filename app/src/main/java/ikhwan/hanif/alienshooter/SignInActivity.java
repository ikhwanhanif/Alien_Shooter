package ikhwan.hanif.alienshooter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText emailET, passwordET;
    private Button loginBtn;

    TextView signUpTV, textForgotPass;

    // Initialize alertDialog with null
    private AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mAuth = FirebaseAuth.getInstance();

        textForgotPass = findViewById(R.id.textForgotPassword);
        textForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });

        signUpTV = findViewById(R.id.signUpTV);
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (validateInputs(email, password)) {
                    loginUser(email, password);
                }
            }
        });
    }

    private void showForgotPasswordDialog() {
        // Buat dialog atau layar reset password
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.RoundedCornersAlertDialog);

        // Inflate layout reset password di sini, misalnya, menggunakan layout custom atau XML
        View view = getLayoutInflater().inflate(R.layout.reset_password_dialog, null);
        builder.setView(view);

        // Tentukan elemen UI dalam dialog
        EditText emailResetET = view.findViewById(R.id.emailResetET);
        Button resetPasswordBtn = view.findViewById(R.id.resetPasswordBtn);
        Button backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAlertDialog();
            }
        });

        // Tambahkan listener untuk tombol reset password
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailReset = emailResetET.getText().toString().trim();
                if (!TextUtils.isEmpty(emailReset)) {
                    sendPasswordResetEmail(emailReset);
                } else {
                    // Tampilkan pesan kesalahan jika email kosong
                    Toast.makeText(SignInActivity.this, getString(R.string.enter_your_email), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set the title and center it
        TextView title = new TextView(this);
        title.setText(getString(R.string.reset_password));
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(0, 30, 0, 0);
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);

        // Tampilkan dialog
        alertDialog = builder.show();
    }


    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Berhasil mengirim email reset password
                            Toast.makeText(SignInActivity.this, getString(R.string.email_pass_has_been_sent), Toast.LENGTH_SHORT).show();
                            dismissAlertDialog();
                        } else {
                            // Gagal mengirim email reset password
                            Toast.makeText(SignInActivity.this, getString(R.string.failed_to_sent_email_reset_pass), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError(getString(R.string.masukkan_email_yang_benar));
            return false;
        } else {
            emailET.setError(null);
        }

        if (password.isEmpty()) {
            passwordET.setError(getString(R.string.enter_your_pass));
            return false;
        } else {
            passwordET.setError(null);
        }

        return true;
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login berhasil
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Simpan username di leaderboard (tanpa @gmail.com)
                        saveUsernameToLeaderboard(user.getEmail());

                        Toast.makeText(SignInActivity.this, "Login", Toast.LENGTH_SHORT).show();

                        // Lakukan tindakan setelah login berhasil (contoh: pindah ke layar utama)
                        startActivity(new Intent(SignInActivity.this, Start.class));
                        finish();
                        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.main_sound);
                        mPlayer.start();
                        mPlayer.setLooping(true);
                    } else {
                        // Login gagal
                        Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUsernameToLeaderboard(String email) {
        String username = email.split("@")[0]; // Menghapus @gmail.com
        saveUsernameToFirebase(username);
    }

    private void saveUsernameToFirebase(String username) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userId = preferences.getString("user_id", "");

        if (!userId.isEmpty()) {
            FirebaseDatabase.getInstance().getReference("leaderboard").child(userId).child("username").setValue(username);
        }
    }

    // Ensure alertDialog is not null before using it
    private void dismissAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }


}
