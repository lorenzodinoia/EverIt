package it.uniba.di.sms1920.everit.utils.request.core;

/*import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Constants;

public class ImageRequest extends MultipartRequest{

    private static final class Keys  {
        private static final String IMAGE_KEY = "image";
    }

    private String token;
    private File file;

    public ImageRequest(String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener, String token, File file) {
        super(Method.POST, String.format("%s/api/%s", Constants.SERVER_HOST, url), listener, errorListener);
        this.token = token;
        this.file = file;
    }

    @Override
    public Map<String, String> getHeaders(){
        Map<String, String> map = new HashMap<>();
        map.put("Expect", "application/json");
        map.put("Accept", "application/json");
        if(token != null){
            map.put("Authorization", token);
        }
        return map;
    }

    @Override
    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        Map<String, DataPart> params = new HashMap<>();
        // file name could found file base or direct access from real path
        // for now just get bitmap data from ImageView
        params.put(Keys.IMAGE_KEY, new DataPart(file.getName(), fileToByte(file), "image/jpeg"));

        return params;
    }

    public byte[] fileToByte(File file) {
        String filePath = file.getAbsolutePath();
        //Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream byteArrayOutputStream = null;
        try (InputStream is = new URL(file.getAbsolutePath()).openStream()){
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        }catch (IOException e){
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }
}*/
