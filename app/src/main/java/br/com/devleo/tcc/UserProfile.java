package br.com.devleo.tcc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

    private TextInputLayout name, email, phone, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        //contents
        name = findViewById(R.id.user_nome_profile);
        email = findViewById(R.id.user_email_profile);
        phone = findViewById(R.id.user_phone_profile);
        password = findViewById(R.id.user_senha_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        name.getEditText().setText(Principal.userAtual.getCompleteName());
        email.getEditText().setText(Principal.userAtual.getEmail());
        phone.getEditText().setText(Principal.userAtual.getPhone());
        password.getEditText().setText(Principal.userAtual.getPassword());
    }

    public void last(View view) {
        startActivity(new Intent(UserProfile.this, Principal.class));
        finish();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Principal.userAtual = null;
        startActivity(new Intent(UserProfile.this, Login.class));
        finish();
    }

    public void update(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(Principal.userAtual.getId());
        if (!phone.getEditText().getText().toString().equals(Principal.userAtual.getPhone())) {
            documentReference.update("phone", phone.getEditText().getText().toString()).addOnSuccessListener(unused -> {
                        Snackbar snackbar = Snackbar.make(view, "Número atualizado.", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    })
                    .addOnFailureListener(e -> {
                        phone.setError("Não foi possível atualizar.");
                        Log.e("ERROR: ", e.toString());
                    });
        }
    }
}
