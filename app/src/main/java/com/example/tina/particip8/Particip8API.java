package com.example.tina.particip8;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by tina on 11/1/15.
 */
public class Particip8API {

    private final String TAG=Particip8API.class.getSimpleName();

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    //the base url of the hosted application
    private final String mParticip8BaseURL="http://ec2-52-23-157-29.compute-1.amazonaws.com";

    //Sessions#create
    private final String mSessionsCreateURL = mParticip8BaseURL + "/sessions";

    private Context mContext;

    /* Constructor for the Particip8API */
    public Particip8API(Context context){
        mContext=context;
    };


    //determines if the network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        Boolean isAvailable=false;
        if(networkInfo != null && networkInfo.isConnected()){
            //we have a network and it is connected
            isAvailable=true;
        }
        return isAvailable;
    }

    //builds and returns an executable call to Authenticate to the service layer
    public Call getAuthenticationCall(String email, String password){

        if(isNetworkAvailable()) {

            //instantiate postParamsJSON object
            JSONObject postParamsJSON = new JSONObject();

            try{
                //create the postParams JSON object and include fields to POST
                postParamsJSON.put("email", email);
                postParamsJSON.put("password", password);

            }catch(JSONException e){
                e.printStackTrace();
            }

            //Convert the postParams JSON into parseable string
            String postParams = postParamsJSON.toString();

            RequestBody body = RequestBody.create(JSON, postParams);
            Request authenticationRequest = new Request.Builder()
                    .url(mSessionsCreateURL)
                    .post(body)
                    .build();

            //instantiantiate new OkHttpClient
            OkHttpClient client = new OkHttpClient();

            //build a new call based on the configured request
            Call authenticationCall = client.newCall(authenticationRequest);

            return authenticationCall;
        }else{
            return null;
        }
    }
}
