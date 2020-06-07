package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.openingTime.OpeningDateTimeFragment;
import it.uniba.di.sms1920.everit.restaurateur.activities.openingTime.OpeningTimeManager;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;


public class OpeningTimeSelectionFragment extends Fragment {

    private SignUpActivity signUpActivity;
    private Restaurateur.Builder restaurateurBuilder;
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


        btnBack = viewRoot.findViewById(R.id.buttonBackOpeningTime);
        btnBack.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        btnNext = viewRoot.findViewById(R.id.btnNextOpeningTime);
        btnNext.setOnClickListener(view -> {
            SignUp3Fragment signUp3Fragment = new SignUp3Fragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerSignUp, signUp3Fragment).addToBackStack(null).commit();

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
}