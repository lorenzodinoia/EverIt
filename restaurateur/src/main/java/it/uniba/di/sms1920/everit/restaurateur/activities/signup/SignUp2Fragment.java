package it.uniba.di.sms1920.everit.restaurateur.activities.signup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.restaurateur.R;
import it.uniba.di.sms1920.everit.restaurateur.activities.openingTime.OpeningDateTimeFragment;
import it.uniba.di.sms1920.everit.utils.Utility;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class SignUp2Fragment extends Fragment {

    private  SignUpActivity signUpActivity;
    private  Restaurateur.Builder restaurateurBuilder;
    private TextInputEditText editTextDeliveryCost;
    private TextInputLayout editTextDeliveryCostContainer;
    private TextInputEditText editTextMinPrice;
    private TextInputLayout editTextMinPriceContainer;
    private TextInputEditText editTextDescription;
    private TextInputLayout editTextDescriptionContainer;

    private ImageButton imgButtonProfileImg;
    private Uri imagePath;

    private MaterialButton btnContinue;
    private MaterialButton btnBack;

    private static int PICK_IMAGE = 1;

    public SignUp2Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_sign_up2, parent, false);
        this.initComponent(viewRoot);
        return viewRoot;
    }

    private void initComponent(View viewRoot){
        restaurateurBuilder = signUpActivity.getRestaurateurBuilder();
        editTextDeliveryCostContainer = viewRoot.findViewById(R.id.editTextDeliveryCostContainer);
        editTextDeliveryCost = viewRoot.findViewById(R.id.editTextDeliveryCost);
        editTextMinPriceContainer = viewRoot.findViewById(R.id.editTextMinPriceContainer);
        editTextMinPrice = viewRoot.findViewById(R.id.editTextMinPrice);
        editTextDescriptionContainer = viewRoot.findViewById(R.id.editTextDescriptionContainer);
        editTextDescription = viewRoot.findViewById(R.id.editTextDescription);

        imgButtonProfileImg = viewRoot.findViewById(R.id.imageButton);
        imgButtonProfileImg.setOnClickListener(v -> {
            fetchImageFromGallery(viewRoot);
        });


        editTextDeliveryCost.setText(Float.toString(restaurateurBuilder.getDeliveryCost()));
        editTextMinPrice.setText(Float.toString(restaurateurBuilder.getMinPrice()));

        if(restaurateurBuilder.getDescription() != null){
            editTextDescription.setText(restaurateurBuilder.getDescription());
        }

        btnBack = viewRoot.findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        btnContinue = viewRoot.findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(view -> {

            boolean flag = true;
            float deliveryCost = Float.parseFloat(editTextDeliveryCost.getText().toString());
            float minPrice = Float.parseFloat(editTextMinPrice.getText().toString());
            String description = editTextDescription.getText().toString();

            if(!Utility.isDeliveryCostValid(deliveryCost)){
                flag = false;
                editTextDeliveryCostContainer.setError(getString(R.string.error_delivery_cost));
            } else{
                editTextDeliveryCostContainer.setError(null);
            }

            if(!Utility.isMinPriceValid(minPrice)){
                flag = false;
                editTextMinPriceContainer.setError(getString(R.string.error_min_price));
            }else{
                editTextMinPriceContainer.setError(null);
            }

            if(!description.equals("")){
                if(!Utility.isDescriptionValid(description)){
                    flag = false;
                    editTextDescriptionContainer.setError(getString(R.string.error_description));
                } else{
                    editTextDescriptionContainer.setError(null);
                }
            } else{
                editTextDescriptionContainer.setError(null);
            }

            if(flag){
                if(imagePath != null){
                    restaurateurBuilder.setImagePath(imagePath.getPath());
                }
                restaurateurBuilder.setDeliveryCost(Float.parseFloat(editTextDeliveryCost.getText().toString()));
                restaurateurBuilder.setMinPrice(Float.parseFloat(editTextMinPrice.getText().toString()));
                if(!editTextDescription.getText().toString().equals("")){
                    restaurateurBuilder.setDescription(editTextDescription.getText().toString());
                }
                OpeningTimeSelectionFragment openingTimeSelectionFragment;
                if(restaurateurBuilder.getOpeningDays() != null){
                    openingTimeSelectionFragment = new OpeningTimeSelectionFragment();
                }
                else{
                    openingTimeSelectionFragment = new OpeningTimeSelectionFragment();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerSignUp, openingTimeSelectionFragment).addToBackStack(null).commit();
            }
        });
    }

    void fetchImageFromGallery(View view){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == PICK_IMAGE){
                Uri selectedImageURI = data.getData();
                imagePath = selectedImageURI;

                Picasso.get().
                        load(selectedImageURI)
                        .placeholder(R.mipmap.icon)
                        .transform(new CropCircleTransformation())
                        .fit()
                        .into(imgButtonProfileImg);
            }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof  SignUpActivity){
            signUpActivity = (SignUpActivity) context;
        }
    }
}
