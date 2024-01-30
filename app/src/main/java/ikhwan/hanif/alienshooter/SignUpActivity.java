package ikhwan.hanif.alienshooter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailET, passwordET, confirmPasswordET;
    private Button registerBtn;

    TextView signInTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        signInTV = findViewById(R.id.signInTV);
        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);
        registerBtn = findViewById(R.id.registBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String confirmPassword = confirmPasswordET.getText().toString().trim();

                if (validateInputs(email, password, confirmPassword)) {
                    registerUser(email, password);
                }
            }
        });
    }

    private boolean validateInputs(String email, String password, String confirmPassword) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError(getString(R.string.masukkan_email_yang_benar));
            return false;
        } else {
            emailET.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordET.setError(getString(R.string.password_harus_setidaknya_6_karakter));
            return false;
        } else {
            passwordET.setError(null);
        }

        if (!confirmPassword.equals(password)) {
            confirmPasswordET.setError(getString(R.string.password_dan_confirm_password_harus_sama));
            return false;
        } else {
            confirmPasswordET.setError(null);
        }

        return true;
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, getString(R.string.sukses_daftar), Toast.LENGTH_SHORT).show();
                        // Registrasi berhasil
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Lakukan tindakan setelah registrasi berhasil (contoh: pindah ke layar login)
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    } else {
                        // Registrasi gagal
                        Toast.makeText(SignUpActivity.this, getString(R.string.gagal_daftar), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
