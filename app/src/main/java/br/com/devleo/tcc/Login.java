package br.com.devleo.tcc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import br.com.devleo.tcc.model.User;

public class Login extends AppCompatActivity {

    private Button callSignUp, login_btn;
    private ImageView image;
    private TextView logoText, sloganText;
    private TextInputLayout username, password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //contents
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        login_btn = findViewById(R.id.login_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            User user = new User();
            user.setId(usuario.getUid());
            user.setEmail(usuario.getEmail());
            Principal.userAtual = user;
            startActivity(new Intent(Login.this, Principal.class));
            finish();
        }
    }

    public void callSignUpScreen(View view) {
        Intent intent = new Intent(Login.this, Register.class);

        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(logoText, "logo_text");
        pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
        pairs[3] = new Pair<View, String>(username, "username_tran");
        pairs[4] = new Pair<View, String>(password, "password_tran");
        pairs[5] = new Pair<View, String>(login_btn, "button_tran");
        pairs[6] = new Pair<View, String>(callSignUp, "login_signup_tran");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void changePassword(View view) {
        Intent intent = new Intent(Login.this, ChangePassword.class);

        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(logoText, "logo_text");
        pairs[2] = new Pair<View, String>(sloganText, "logo_desc");
        pairs[3] = new Pair<View, String>(username, "username_tran");
        pairs[4] = new Pair<View, String>(password, "password_tran");
        pairs[5] = new Pair<View, String>(login_btn, "button_tran");
        pairs[6] = new Pair<View, String>(callSignUp, "login_signup_tran");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void loginUser(View view) {
        if (username.getEditText().getText().toString().isEmpty()) {
            username.setError("Preencha este campo.");
            return;
        }
        username.setError(null);
        if (password.getEditText().getText().toString().isEmpty()) {
            password.setError("Preencha este campo.");
            return;
        }
        password.setError(null);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username.getEditText().getText().toString(),
                password.getEditText().getText().toString()).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.VISIBLE);
            if (task.isSuccessful()) {
                User user = new User();
                user.setId(Objects.requireNonNull(task.getResult().getUser()).getUid());
                user.setEmail(username.getEditText().getText().toString());
                user.setPassword(password.getEditText().getText().toString());
                Principal.userAtual = user;
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(Login.this, Principal.class));
                    finish();
                }, 3000);
            } else {
                new Handler().postDelayed(() -> {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        username.setError(" ");
                        password.setError("Credenciais inválidas.");
                    } catch (FirebaseAuthInvalidUserException e) {
                        username.setError("E-mail não cadastrado.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        });
    }
}
