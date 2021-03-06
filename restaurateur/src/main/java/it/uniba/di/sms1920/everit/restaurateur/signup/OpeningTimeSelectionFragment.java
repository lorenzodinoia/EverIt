package it.uniba.di.sms1920.everit.restaurateur.signup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.openingTime.OpeningDateTimeFragment;
import it.uniba.di.sms1920.everit.utils.models.OpeningDay;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;


public class OpeningTimeSelectionFragment extends Fragment {

    private final String ARG_RESTAURATEUR = "restaurateur_builder_opening_time_sel";
    private SignUpActivity signUpActivity;
    private OpeningDateTimeFragment openingDateTimeFragment;
    private Restaurateur.Builder restaurateurBuilder;
    private MaterialButton btnNext;
    private MaterialButton btnBack;

    public OpeningTimeSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            restaurateurBuilder = signUpActivity.getRestaurateur();
        }
        else {
            restaurateurBuilder = savedInstanceState.getParcelable(ARG_RESTAURATEUR);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_opening_time_selection, container, false);

        btnBack = viewRoot.findViewById(R.id.buttonBackOpeningTime);
        btnNext = viewRoot.findViewById(R.id.btnNextOpeningTime);

        openingDateTimeFragment = new OpeningDateTimeFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.opening_time_container, openingDateTimeFragment).addToBackStack(null).commit();

        return viewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();

        btnBack.setOnClickListener(view -> {
            signUpActivity.getSupportFragmentManager().popBackStack();
        });
        btnNext.setOnClickListener(view -> {
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
                promptErrorMessage(getString(R.string.error_not_selected_opening_time));
            }

        });
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ARG_RESTAURATEUR, restaurateurBuilder);
    }


    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(signUpActivity);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}