package br.com.devleo.tcc;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //components
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        TextView slogan = findViewById(R.id.text_name);

        Animation topAnm = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnm = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image.setAnimation(topAnm);
        logo.setAnimation(bottomAnm);
        slogan.setAnimation(bottomAnm);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Login.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(image, "logo_image");
            pairs[1] = new Pair<View, String>(logo, "logo_text");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(intent, options.toBundle());
        }, 3500);
    }
}