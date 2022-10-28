package br.com.devleo.tcc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import br.com.devleo.tcc.model.User;

public class SecondRegister extends AppCompatActivity {

    private TextInputLayout phone;
    private CountryCodePicker code;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_custom2);

        //contents
        progressBar = findViewById(R.id.progressBar2);
        phone = findViewById(R.id.signup_phone_number);
        code = findViewById(R.id.country_code_picker);
    }

    public void last(View view) {
        startActivity(new Intent(SecondRegister.this, Register.class));
        finish();
    }

    public void login(View view) {
        startActivity(new Intent(SecondRegister.this, Login.class));
        finish();
        Principal.userAtual = null;
    }

    public void secondRegister(View view) {
        if (phone.getEditText().getText().toString().isEmpty()) {
            phone.setError("Preencha este campo.");
            return;
        }
        phone.setError(null);
        User user = Principal.userAtual;
        user.setPhone(this.code.getSelectedCountryCodeWithPlus() + "" + this.phone.getEditText().getText().toString());

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.VISIBLE);
                user.setId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> userInformations = new HashMap<>();
                userInformations.put("firstName", user.getFirstName());
                userInformations.put("lastName", user.getLastName());
                userInformations.put("phone", user.getPhone());

                DocumentReference documentReference = db.collection("users").document(user.getId());
                documentReference.set(userInformations).addOnSuccessListener(e -> new Handler().postDelayed(() -> {
                            startActivity(new Intent(SecondRegister.this, Principal.class));
                            finish();
                        }, 3000))
                        .addOnFailureListener(e -> {
                            Snackbar snackbar = Snackbar.make(view, "Ocorreu um erro, tente novamente.", BaseTransientBottomBar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("ERROR: ", e.toString());
                        });
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthUserCollisionException e) {
                    Snackbar snackbar = Snackbar.make(view, "E-mail já cadastrado.", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    Snackbar snackbar = Snackbar.make(view, "E-mail inválido.", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
