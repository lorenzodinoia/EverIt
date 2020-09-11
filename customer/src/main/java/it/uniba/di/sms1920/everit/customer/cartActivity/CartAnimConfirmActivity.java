package it.uniba.di.sms1920.everit.customer.cartActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.customer.BaseActivity;
import it.uniba.di.sms1920.everit.customer.orders.OrderDetailActivity;

public class CartAnimConfirmActivity extends AppCompatActivity {

    private View iconConfirm;
    private View view;
    private TextView textViewOrderSuccess;
    private Button buttonHome, buttonDetail;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_anim_confirm);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey(Cart3Fragment.ARG_ITEM_ID)){
                orderId = getIntent().getExtras().getLong(Cart3Fragment.ARG_ITEM_ID);
            }
        }

        iconConfirm = findViewById(R.id.ic_confirm);
        view = findViewById(R.id.content);
        textViewOrderSuccess = findViewById(R.id.textViewOrderSuccess);
        buttonHome = findViewById(R.id.buttonHome);
        buttonDetail = findViewById(R.id.buttonDetail);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                view.removeOnLayoutChangeListener(this);

                view.post(() -> {
                    iconAnim();
                    textFadeIn();
                });
            }
        });

        buttonHome.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        });

        buttonDetail.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(OrderDetailActivity.ARG_ITEM_ID, orderId);
            startActivity(intent);
        });

    }


    private void iconAnim(){
        int iconX = iconConfirm.getWidth() / 2;
        int iconY = iconConfirm.getHeight() / 2;
        float iconFull = (float) Math.hypot(iconX, iconY);

        Animator anim = ViewAnimationUtils.createCircularReveal(iconConfirm, iconX, iconY, 0, iconFull);

        anim.setDuration(500);
        anim.start();

        Button buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(v -> {
            finishAffinity();
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        });
    }


    private void textFadeIn(){
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(800);
        fadeIn.setStartOffset((long) textViewOrderSuccess.getWidth()/2);

        textViewOrderSuccess.startAnimation(fadeIn);

    }

}