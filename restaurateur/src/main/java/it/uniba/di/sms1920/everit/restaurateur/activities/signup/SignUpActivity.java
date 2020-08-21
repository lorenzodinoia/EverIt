package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class SignUpActivity extends AppCompatActivity {

    private Restaurateur.Builder restaurateur;
    private static String RESTAURATEUR_ITEM_KEY = "restaurateur_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(savedInstanceState == null) {
            restaurateur = new Restaurateur.Builder();
            SignUp1Fragment fragmentSignUp1 = new SignUp1Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.containerSignUp, fragmentSignUp1).addToBackStack(null).commit();
        }
        else{
            restaurateur = savedInstanceState.getParcelable(RESTAURATEUR_ITEM_KEY);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FragmentManager fm = getSupportFragmentManager();
            if(fm.getBackStackEntryCount() > 1){
                fm.popBackStack();
            }
            else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public Restaurateur.Builder getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(Restaurateur.Builder restaurateur) {
        this.restaurateur = restaurateur;
    }

    public Restaurateur.Builder getRestaurateurBuilder(){
        return restaurateur;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RESTAURATEUR_ITEM_KEY, restaurateur);
    }
}
