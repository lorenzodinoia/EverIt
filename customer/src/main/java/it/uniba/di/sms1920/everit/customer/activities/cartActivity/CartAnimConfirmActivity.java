package it.uniba.di.sms1920.everit.customer.activities.cartActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.activities.BaseActivity;

public class CartAnimConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_anim_confirm);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

        View view = findViewById(R.id.content);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                view.removeOnLayoutChangeListener(this);

                view.post(() -> initUI());
            }
        });
    }



    private void initUI(){
        View iconConfirm = findViewById(R.id.ic_confirm);

        int iconX = iconConfirm.getWidth() / 2;
        int iconY = iconConfirm.getHeight() / 2;

        float iconFull = (float) Math.hypot(iconX, iconY);

        Animator anim = ViewAnimationUtils.createCircularReveal(iconConfirm, iconX, iconY, 0, iconFull);

        anim.setDuration(800);
        anim.start();


        Button buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(v -> {
            finishAffinity();
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        });
    }
}