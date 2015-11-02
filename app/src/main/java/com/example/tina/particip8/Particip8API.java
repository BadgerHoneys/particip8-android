package com.example.tina.particip8;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by tina on 11/1/15.
 */
public class Particip8API {

    private final String TAG=Particip8API.class.getSimpleName();

    //the base url of the hosted application
    private final String mParticip8URL="http://ec2-52-23-157-29.compute-1.amazonaws.com";

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


    public Call authenticate(){

        if(isNetworkAvailable()) {

            //create url for the authenticate request
            //Structure: xxxxxx
            String authenticationURL = mParticip8URL + "replace";

            Log.d(TAG,"AuthenticationURL: " + authenticationURL);

            //create http client
            OkHttpClient client = new OkHttpClient();
            Request authenticationRequest= new Request.Builder().url(authenticationURL).build();

            Call call=client.newCall(authenticationRequest);

            return call;
        }else{
            return null;
        }
    }
}
