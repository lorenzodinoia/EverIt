package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.openingTime.OpeningDateTimeFragment;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;


public class SignUpActivity extends AppCompatActivity implements  OpeningDateTimeFragment.OnFragmentInteractionListener, OpeningTimeSelectionFragment.OnFragmentInteractionListener{

    private Restaurateur.Builder restaurateur = new Restaurateur.Builder();
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SignUp1Fragment fragmentSignUp1 = new SignUp1Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.containerSignUp, fragmentSignUp1).addToBackStack(null).commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        //TODO agigungere back arrow (bug)
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
                super.onBackPressed();
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
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from parent fragment");
    }

    @Override
    public void messageFromParentFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }
}
