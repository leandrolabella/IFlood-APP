package br.com.devleo.tcc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity {

    private TextInputLayout email;
    private TextView resultText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reset_password);

        //contents
        email = findViewById(R.id.email_changepassword);
        resultText = findViewById(R.id.resultTextViewReset);
        progressBar = findViewById(R.id.progressBarReset);
    }

    public void login(View view) {
        startActivity(new Intent(ChangePassword.this, Login.class));
        finish();
        Principal.userAtual = null;
    }

    public void callChangePassword(View view) {
        if (email.getEditText().getText().toString().isEmpty()) {
            email.setError("Informe um e-mail.");
            return;
        }
        email.setError(null);
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getEditText().getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    resultText.setText("Instruções enviadas para o e-mail!");
                    resultText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }, 2600);
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    email.setError("E-mail inválido.");
                } catch (FirebaseAuthInvalidUserException e) {
                    email.setError("E-mail não encontrado.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
