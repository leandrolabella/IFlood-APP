package br.com.devleo.tcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import br.com.devleo.tcc.model.Locale;

public class Locals extends AppCompatActivity {

    private ArrayList<Locale> userLocals;
    private LocaleAdapter localeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_locals);

        //contents
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userLocals = new ArrayList<>();
        localeAdapter = new LocaleAdapter(this, userLocals);
        recyclerView.setAdapter(localeAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeEventsListeners();
    }

    public void last(View view) {
        startActivity(new Intent(Locals.this, Principal.class));
        finish();
    }

    public void nova(View view) {
        startActivity(new Intent(Locals.this, NewLocation.class));
        finish();
    }

    private void changeEventsListeners() {
        FirebaseFirestore.getInstance().collection("locals")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    assert value != null;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            if (Objects.equals(dc.getDocument().get("user_id"), Principal.userAtual.getId())) {
                                userLocals.add(dc.getDocument().toObject(Locale.class));
                            }
                            localeAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
