package it.uniba.di.sms1920.everit.customer.activities.results;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.uniba.di.sms1920.everit.customer.R;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultListActivity extends AppCompatActivity {
    private boolean twoPaneMode;
    public static final List<Restaurateur> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        toolbar.setTitle("Ristoranti"); //TODO Impostare stringa multi lingua
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO Gestire ritorno indietro

        resultList.clear();

        //Inserito solo a scopo di test
        RestaurateurRequest request = new RestaurateurRequest();
        request.read(1, new RequestListener<Restaurateur>() {
            @Override
            public void successResponse(Restaurateur response) {
                resultList.add(response);

                //Portare fuori
                View recyclerView = findViewById(R.id.result_list);
                if (recyclerView != null) {
                    setupRecyclerView((RecyclerView) recyclerView);
                }
            }

            @Override
            public void errorResponse(RequestException error) {

            }
        });


        if (findViewById(R.id.result_detail_container) != null) {
            /*
             * Se il layout è presente vuol dire che l'app è installata su un dispositivo di grandi dimensioni
             * Pertanto si utilizza la modalità con due pannelli
             */
            twoPaneMode = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ResultListActivity.resultList, twoPaneMode));
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

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final ResultListActivity parentActivity;
        private final List<Restaurateur> results;
        private final boolean towPaneMode;
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurateur item = (Restaurateur) view.getTag();
                if (towPaneMode) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(ResultDetailFragment.ARG_ITEM_ID, item.getId());
                    ResultDetailFragment fragment = new ResultDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.result_detail_container, fragment).commit();
                }
                else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ResultDetailActivity.class);
                    intent.putExtra(ResultDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ResultListActivity parentActivity, List<Restaurateur> results, boolean twoPaneMode) {
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
                    String imageUrl = String.format("https://everitsms.000webhostapp.com/%s", restaurantLogoPath);
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.mipmap.icon)
                            .placeholder(R.mipmap.icon)
                            .fit()
                            .into(holder.imageViewRestaurantLogo);
                }
                holder.textViewRestaurantName.setText(item.getShopName());
                String minPrice = String.format(Locale.getDefault(), "Importo minimo: € %d", item.getMinPrice());
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
                imageViewRestaurantLogo = (ImageView) view.findViewById(R.id.imageViewRestaurantLogo);
                textViewRestaurantName = (TextView) view.findViewById(R.id.textViewRestaurantName);
                textViewMinPrice = (TextView) view.findViewById(R.id.textViewMinPrice);
                textViewRestaurantDistance = (TextView) view.findViewById(R.id.textViewRestaurantDistance);
            }
        }
    }
}
