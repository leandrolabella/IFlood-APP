package br.com.devleo.tcc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewLocation extends AppCompatActivity implements LocationListener {

    private TextInputLayout name, address, city;
    private double latitude, longitude;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_newlocal);

        //contents
        name = findViewById(R.id.newlocal_name);
        address = findViewById(R.id.newlocal_address);
        city = findViewById(R.id.newlocal_cidade);
        progressBar = findViewById(R.id.newLocal_progress);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showPermission();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void showPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, 1);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);
            }
        } else {
            Toast.makeText(this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            city.getEditText().setText(addresses.get(0).getAdminArea());
            address.getEditText().setText(addresses.get(0).getAddressLine(0));
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvar(View view) {
        if (city.getEditText().getText().toString().isEmpty() || address.getEditText().getText().toString().isEmpty()) {
            city.setError("Não encontramos sua cidade.");
            address.setError("Não encontramos seu endereço.");
            return;
        }
        if (name.getEditText().getText().toString().isEmpty()) {
            name.setError("Informe um nome para identificação.");
            return;
        }
        name.setError(null);
        city.setError(null);
        address.setError(null);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> locInformations = new HashMap<>();
        locInformations.put("address", address.getEditText().getText().toString());
        locInformations.put("city", city.getEditText().getText().toString());
        locInformations.put("nome", name.getEditText().getText().toString());
        locInformations.put("latitude", latitude);
        locInformations.put("longitude", longitude);
        locInformations.put("user_id", Principal.userAtual.getId());

        DocumentReference documentReference = db.collection("locals").document();
        documentReference.set(locInformations).addOnSuccessListener(e -> new Handler().postDelayed(() -> {
                    startActivity(new Intent(NewLocation.this, Locals.class));
                    finish();
                }, 3000))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar.make(view, "Ocorreu um erro, tente novamente.", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    Log.e("ERROR: ", e.toString());
                });
    }

    public void returns(View view) {
        startActivity(new Intent(NewLocation.this, Locals.class));
        finish();
    }
}
