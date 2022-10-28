package br.com.devleo.tcc;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import br.com.devleo.tcc.model.Locale;
import br.com.devleo.tcc.model.User;
import br.com.devleo.tcc.model.Warning;

public class Principal extends AppCompatActivity {

    private TextView name;
    public static User userAtual = null;
    public static ArrayList<Warning> warningList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_principal);

        //contents
        name = findViewById(R.id.text_name);
        warningList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userAtual.getId());
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                userAtual.setFirstName(documentSnapshot.getString("firstName"));
                userAtual.setLastName(documentSnapshot.getString("lastName"));
                userAtual.setPhone(documentSnapshot.getString("phone"));
                name.setVisibility(View.VISIBLE);
                name.setText(userAtual.getCompleteName());
            }
        });
        loadWarnings();
    }

    private void loadWarnings() {
        warningList.clear();
        DocumentReference arduinoReference = FirebaseFirestore.getInstance().collection("arduino").document("01");
        arduinoReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                FirebaseFirestore.getInstance().collection("locals")
                        .addSnapshotListener((value, error2) -> {
                            if (error2 != null) {
                                return;
                            }
                            assert value != null;
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    if (Objects.equals(dc.getDocument().get("user_id"), Principal.userAtual.getId())) {
                                        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
                                        Date time = documentSnapshot.getDate("dtUpdate");
                                        long diffMillies = Math.abs(System.currentTimeMillis() - time.getTime());
                                        if (TimeUnit.MILLISECONDS.toHours(diffMillies) <= 2) {
                                            java.util.Locale locale = new java.util.Locale("pt", "BR");
                                            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale);
                                            double idealDistance = 2.0 * 1000;
                                            double okDistance = 4.0 * 1000;
                                            Locale local = dc.getDocument().toObject(Locale.class);
                                            float[] resultDistante = new float[1];
                                            Location.distanceBetween(-23.632617541112257, -46.69640626475535,
                                                    local.getLatitude(), local.getLongitude(), resultDistante);
                                            if (resultDistante[0] < idealDistance) {
                                                Warning warning = new Warning(local.getNome() + " - " + dt.format(time), "Alerta de enchente em " + local.getNome() + ".", local, false);
                                                warningList.add(warning);
                                            } else if (resultDistante[0] < okDistance) {
                                                Warning warning = new Warning(null, "Alerta de enchente próximo a " + local.getNome() + ".", local, false);
                                                warningList.add(warning);
                                            }
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }

    public void profile(View view) {
        startActivity(new Intent(Principal.this, UserProfile.class));
        finish();
    }

    public void locals(View view) {
        startActivity(new Intent(Principal.this, Locals.class));
        finish();
    }

    public void warnings(View view) {
        startActivity(new Intent(Principal.this, Warnings.class));
        finish();
    }
}
