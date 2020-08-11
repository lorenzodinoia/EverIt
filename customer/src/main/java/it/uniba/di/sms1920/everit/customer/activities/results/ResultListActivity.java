package it.uniba.di.sms1920.everit.customer.activities.results;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ResultListActivity extends AppCompatActivity {
    public static final String PARAMETER_ADDRESS = "address";

    private boolean twoPaneMode;
    public static final List<Restaurateur> resultList = new ArrayList<>();
    private RestaurateurRecyclerViewAdapter recyclerViewAdapter;
    private Address searchAddress;
    private TextView textViewEmptyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        Toolbar toolbar = findViewById(R.id.toolbar_default);
        toolbar.setTitle("Ristoranti"); //TODO Impostare stringa multi lingua
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO Gestire ritorno indietro

        resultList.clear();

        if (findViewById(R.id.result_detail_container) != null) {
            /*
             * Se il layout è presente vuol dire che l'app è installata su un dispositivo di grandi dimensioni
             * Pertanto si utilizza la modalità con due pannelli
             */
            twoPaneMode = true;
        }

        textViewEmptyResult = findViewById(R.id.textViewEmptyResultCustomer);
        View recyclerView = findViewById(R.id.result_list);
        if (recyclerView != null) {
            setupRecyclerView((RecyclerView) recyclerView);
        }

        Intent intent = getIntent();
        Address searchAddress;
        if ((intent != null) && ((searchAddress = intent.getParcelableExtra(PARAMETER_ADDRESS)) != null)) {
            this.searchAddress = searchAddress;
            this.search();
        }
        else {
            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_LONG).show(); //TODO Impostare stringa multi lingua
            finish();
        }
    }

    private void search() {
        RestaurateurRequest searchRequest = new RestaurateurRequest();
        searchRequest.search(this.searchAddress.getLatitude(), this.searchAddress.getLongitude(), new RequestListener<Collection<Restaurateur>>() {
            @Override
            public void successResponse(Collection<Restaurateur> response) {
                resultList.clear();
                if(!response.isEmpty()){
                    textViewEmptyResult.setVisibility(View.INVISIBLE);
                    resultList.addAll(response);
                    if (recyclerViewAdapter != null) {
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    textViewEmptyResult.setVisibility(View.VISIBLE);
                    textViewEmptyResult.setText(R.string.no_results);
                    textViewEmptyResult.bringToFront();
                }
            }

            @Override
            public void errorResponse(RequestException error) {
                promptErrorMessage(error.getMessage());
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerViewAdapter = new RestaurateurRecyclerViewAdapter(this, ResultListActivity.resultList, twoPaneMode);
        recyclerView.setAdapter(this.recyclerViewAdapter);
    }

    public static Restaurateur getResultById(long id) {
        Restaurateur restaurateur = null;
        for (Restaurateur r : ResultListActivity.resultList) {
            if (r.getId() == id) {
                restaurateur = r;
                break;
            }
        }

        return restaurateur;
    }

    private void promptErrorMessage(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(it.uniba.di.sms1920.everit.utils.R.layout.dialog_message_ok);

        TextView title = dialog.findViewById(R.id.textViewTitle);
        title.setText(it.uniba.di.sms1920.everit.utils.R.string.error);

        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v ->{
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    public static class RestaurateurRecyclerViewAdapter extends RecyclerView.Adapter<RestaurateurRecyclerViewAdapter.ViewHolder> {
        private final ResultListActivity parentActivity;
        private final List<Restaurateur> results;
        private final boolean towPaneMode;

        private final View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Restaurateur item = (Restaurateur) view.getTag();

                //TODO controllare se bisogna fare qualcosa per la 2pane mode. ResultDeatil non esiste più
                /**
                if (towPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(ResultDetailActivity.ARG_ITEM_ID, item.getId());
                    ResultDetailFragment fragment = new ResultDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.result_detail_container, fragment).commit();
                }
                else {
                 */
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ResultDetailActivity.class);
                    intent.putExtra(ResultDetailActivity.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
        };

        RestaurateurRecyclerViewAdapter(ResultListActivity parentActivity, List<Restaurateur> results, boolean twoPaneMode) {
            this.results = results;
            this.parentActivity = parentActivity;
            towPaneMode = twoPaneMode;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Restaurateur item = this.results.get(position);
            if (item != null) {
                String restaurantLogoPath = item.getImagePath();
                if (restaurantLogoPath != null) {
                    //Carica immagine dal server
                    String imageUrl = String.format("%s/%s", Constants.SERVER_HOST, restaurantLogoPath);
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.mipmap.icon)
                            .placeholder(R.mipmap.icon)
                            .fit()
                            .into(holder.imageViewRestaurantLogo);
                }
                holder.textViewRestaurantName.setText(item.getShopName());
                String minPrice = String.format(Locale.getDefault(), "Ordine minimo: € %.2f", item.getMinPrice());
                holder.textViewMinPrice.setText(minPrice);
                //TODO Gestire distanza del ristorante

                holder.itemView.setTag(results.get(position));
                holder.itemView.setOnClickListener(onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageViewRestaurantLogo;
            final TextView textViewRestaurantName;
            final TextView textViewMinPrice;
            final TextView textViewRestaurantDistance;

            ViewHolder(View view) {
                super(view);
                imageViewRestaurantLogo = view.findViewById(R.id.imageViewRestaurantLogo);
                textViewRestaurantName = view.findViewById(R.id.textViewRestaurantName);
                textViewMinPrice = view.findViewById(R.id.textViewMinPrice);
                textViewRestaurantDistance =  view.findViewById(R.id.textViewRestaurantDistance);
            }
        }
    }
}
