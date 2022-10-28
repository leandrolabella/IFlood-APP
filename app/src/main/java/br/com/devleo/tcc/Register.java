package br.com.devleo.tcc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import br.com.devleo.tcc.model.User;

public class Register extends AppCompatActivity {

    private TextInputLayout firstName, lastName, email, password;
    private ImageView backBtn;
    private Button next, login;
    private TextView titleText, slideText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_custom);

        //contents
        firstName = findViewById(R.id.signup_firstname);
        lastName = findViewById(R.id.signup_lastname);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    private void load() {
        if (Principal.userAtual != null) {
            User user = Principal.userAtual;
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                firstName.getEditText().setText(user.getFirstName());
            }
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                lastName.getEditText().setText(user.getLastName());
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                email.getEditText().setText(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                password.getEditText().setText(user.getPassword());
            }
        }
    }

    public void login(View view) {
        startActivity(new Intent(Register.this, Login.class));
        finish();
        Principal.userAtual = null;
    }

    public void firstRegister(View view) {
        if (firstName.getEditText().getText().toString().isEmpty()) {
            firstName.setError("Preencha este campo.");
            return;
        }
        firstName.setError(null);
        if (lastName.getEditText().getText().toString().isEmpty()) {
            lastName.setError("Preencha este campo.");
            return;
        }
        lastName.setError(null);
        if (email.getEditText().getText().toString().isEmpty()) {
            email.setError("Preencha este campo.");
            return;
        }
        email.setError(null);
        if (password.getEditText().getText().toString().isEmpty()) {
            password.setError("Preencha este campo.");
            return;
        }
        if (password.getEditText().getText().length() < 6) {
            password.setError("A senha deve ter no mínimo 6 caracteres.");
            return;
        }
        password.setError(null);
        User user = new User();
        user.setFirstName(this.firstName.getEditText().getText().toString());
        user.setLastName(this.lastName.getEditText().getText().toString());
        user.setEmail(this.email.getEditText().getText().toString());
        user.setPassword(this.password.getEditText().getText().toString());
        Principal.userAtual = user;

        Intent intent = new Intent(getApplicationContext(), SecondRegister.class);
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");
        pairs[4] = new Pair<View, String>(slideText, "transition_slide_text");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }
}
