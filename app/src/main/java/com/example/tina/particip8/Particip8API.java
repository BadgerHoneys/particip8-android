package com.example.tina.particip8;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
    private Context mContext;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mHTTPClient = new OkHttpClient();

    //the base url of the hosted application
    private final String mParticip8BaseURL="http://ec2-52-23-157-29.compute-1.amazonaws.com";
    //sessions#create
    private final String mSessionsCreateURL = mParticip8BaseURL + "/sessions";
    //school_classes#index
    private final String mClassInformationURL = mParticip8BaseURL + "/school_classes";

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

    //reads the auth_token from shared preferences
    private String getAuthenticationToken(){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                mContext.getString(R.string.shared_preferences_identifier),
                Context.MODE_PRIVATE);

        String authentication_token = sharedPreferences.getString("auth_token","");
        Log.d(TAG, "Authentication Token Read from Shared Preferences: " + authentication_token);
        return "";
    }


    //builds and returns executable call to request class and evaluation template
    //information for current teacher
    public Call getClassInformationCall(){
        if(isNetworkAvailable()){

            //get the authentication token from shared preferences
            String token = getAuthenticationToken();
            //set the authentication token in the http header of request being built

            //instantiate request
            Request classInformationRequest = new Request.Builder()
                    .url(mClassInformationURL)
                    .build();

            //build a new call based on the configured request
            Call classInformationCall = mHTTPClient.newCall(classInformationRequest);
            return classInformationCall;
        }else {
            return null;
        }
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

            //build a new call based on the configured request
            Call authenticationCall = mHTTPClient.newCall(authenticationRequest);
            return authenticationCall;
        }else{
            return null;
        }
    }
}
