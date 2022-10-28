package br.com.devleo.tcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Warnings extends AppCompatActivity {

    private WarningAdapter warningAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_warnings);

        //contents
        RecyclerView recyclerView = findViewById(R.id.recyclerViewWarning);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        warningAdapter = new WarningAdapter(this, Principal.warningList);
        recyclerView.setAdapter(warningAdapter);
    }

    public void last(View view) {
        startActivity(new Intent(Warnings.this, Principal.class));
        finish();
    }

    public void nova(View view) {
        startActivity(new Intent(Warnings.this, NewLocation.class));
        finish();
    }
}
