package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.openingTime.OpeningDateTimeFragment;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;


public class OpeningTimeSelectionFragment extends Fragment {

    private SignUpActivity signUpActivity;
    private OpeningDateTimeFragment openingDateTimeFragment;
    private Restaurateur.Builder restaurateurBuilder;
    private TextView textViewErrorOpeningTimes;
    private MaterialButton btnNext;
    private MaterialButton btnBack;

    public OpeningTimeSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_opening_time_selection, container, false);

        restaurateurBuilder = signUpActivity.getRestaurateurBuilder();

        textViewErrorOpeningTimes = viewRoot.findViewById(R.id.textViewErrorOpeningTimes);
        btnBack = viewRoot.findViewById(R.id.buttonBackOpeningTime);
        btnBack.setOnClickListener(view -> {
            signUpActivity.getSupportFragmentManager().popBackStack();
        });
        btnNext = viewRoot.findViewById(R.id.btnNextOpeningTime);
        btnNext.setOnClickListener(view -> {
            textViewErrorOpeningTimes.setText("");
            boolean valid = false;
            for(OpeningDay day : restaurateurBuilder.getOpeningDays()){
                if(day.getOpeningTimes().size() > 1){
                    valid = true;
                }
            }

            if(valid) {
                for(OpeningDay day : restaurateurBuilder.getOpeningDays()){
                    day.getOpeningTimes().remove(day.getOpeningTimes().size()-1);
                }
                SignUp3Fragment signUp3Fragment = new SignUp3Fragment();
                FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.containerSignUp, signUp3Fragment).addToBackStack(null).commit();
            }
            else{
                textViewErrorOpeningTimes.setText(R.string.error_not_selected_opening_time);
                textViewErrorOpeningTimes.setTextColor(Color.parseColor("#ae0022"));
            }

        });
        return viewRoot;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  SignUpActivity){
            signUpActivity = (SignUpActivity) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        openingDateTimeFragment = new OpeningDateTimeFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.opening_time_container, openingDateTimeFragment).addToBackStack(null).commit(); 
    }

}