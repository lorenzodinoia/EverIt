package it.uniba.di.sms1920.everit.customer.cart;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.RestaurateurRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class Cart {
    private static final String CART_FILE_NAME = "cart.json";
    private static final Gson jsonConverter = new Gson();

    private static Cart instance;

    private Context context;
    private PartialOrder partialOrder;

    private Cart(Context context) {
        this.context = context;
    }

    public static Cart getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (Cart.exists(context)) {
            Cart.load(context);
        }
        else {
            Cart.instance = new Cart(context);
        }
    }

    private static boolean exists(Context context) {
        File cartFile = context.getFileStreamPath(CART_FILE_NAME);

        return ((cartFile != null) && (cartFile.exists()));
    }

    public static void load(Context context) {
        Cart.instance = new Cart(context);

        final PartialOrder.SerializableItem serializedPartialOrder = Cart.instance.loadSerializedItems();

        RestaurateurRequest restaurateurRequest = new RestaurateurRequest();
        restaurateurRequest.read(serializedPartialOrder.getRestaurateurId(), new RequestListener<Restaurateur>() {
            @Override
            public void successResponse(Restaurateur response) {
                PartialOrder partialOrder = new PartialOrder(response, serializedPartialOrder.getDeliveryAddress());
                partialOrder.setOrderNotes(serializedPartialOrder.getOrderNotes());
                partialOrder.setDeliveryNotes(serializedPartialOrder.getDeliveryNotes());

                for (Map.Entry<Long, Integer> entry : serializedPartialOrder.getProducts().entrySet()) {
                    Product product = response.getProductById(entry.getKey());
                    if (product != null) {
                        partialOrder.addProduct(product, entry.getValue());
                    }
                }

                Cart.getInstance().partialOrder = partialOrder;
            }

            @Override
            public void errorResponse(RequestException error) {}
        });
    }

    public PartialOrder getPartialOrder() {
        return this.partialOrder;
    }

    public PartialOrder getPartialOrderOf(Restaurateur restaurateur) {
        PartialOrder partialOrder = null;

        if (this.partialOrder != null) {
            if (this.partialOrder.getRestaurateur().getId() == restaurateur.getId()) {
                partialOrder = this.partialOrder;
            }
        }

        return partialOrder;
    }

    public boolean isEmpty() {
        return (this.partialOrder == null);
    }

    public PartialOrder initPartialOrderFor(Restaurateur restaurateur, Address deliveryAddress) {
        this.partialOrder = new PartialOrder(restaurateur, deliveryAddress);

        return this.partialOrder;
    }

    public boolean saveToFile() {
        String content = jsonConverter.toJson(this.getSerializableItems()).toString();
        boolean success = true;

        try (FileOutputStream outputStream = this.context.openFileOutput(CART_FILE_NAME, Context.MODE_PRIVATE)) {
            outputStream.write(content.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public void clear() {
        this.partialOrder = null;
        this.context.deleteFile(CART_FILE_NAME);
    }

    private PartialOrder.SerializableItem getSerializableItems() {
        if (this.partialOrder != null) {
            return this.partialOrder.getSerializableItem();
        }
        else {
            return null;
        }
    }

    private PartialOrder.SerializableItem loadSerializedItems() {
        PartialOrder.SerializableItem serializedItem = null;

        try {
            FileInputStream inputStream = this.context.openFileInput(CART_FILE_NAME);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();

            try (BufferedReader fileReader = new BufferedReader(streamReader)) {
                String line;

                while ((line = fileReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                streamReader.close();
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            String jsonFromFile = stringBuilder.toString();
            if (!jsonFromFile.equals("")) {
                serializedItem = jsonConverter.fromJson(jsonFromFile, PartialOrder.SerializableItem.class);
            }
        }
        catch (FileNotFoundException ignored) { }

        return serializedItem;
    }
}
