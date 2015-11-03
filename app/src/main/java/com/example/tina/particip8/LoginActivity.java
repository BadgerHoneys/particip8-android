package com.example.tina.particip8;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Context mContext = this;

    private Particip8API mParticip8API;
    private final String AUTHENTICATION_TOKEN_KEY = "auth_token";
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //create an instance of the Particip8API
        mParticip8API = new Particip8API(LoginActivity.this);

        //map instance properties to UI elements
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login_button);

        //bind click listener and anonymous function to loginButton
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //get email and password elements
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                //get Call request to web service to authenticate
                Call authenticationCall = mParticip8API.getAuthenticationCall(email, password);

                //if call is not null, make a call to authenticate
                if(authenticationCall!=null){

                    //make a call to authenticate user credentials
                    authenticationCall.enqueue(new Callback(){

                        //failure callback
                        public void onFailure(Request request, IOException e){
                            Log.d(TAG, "There was an unsuccessful request made to the api");
                        }

                        //response callback
                        public void onResponse(Response response) throws IOException{

                            if(response.isSuccessful()){

                                String jsonData = response.body().string();
                                Log.d(TAG, "JSON data from response: " + jsonData);

                                try{
                                    //access and persist the returned authentication token
                                    JSONObject authenticationData = new JSONObject(jsonData);
                                    String authenticationToken = authenticationData.getString(AUTHENTICATION_TOKEN_KEY);
                                    persistAuthenticationToken(authenticationToken);
                                }catch(JSONException e){
                                    Log.d(TAG,"JSON Exception Experienced");
                                    e.printStackTrace();
                                }

                                //navigate the user to the evaluation creation page
                                Intent evaluationCreationIntent = new Intent(LoginActivity.this,
                                        EvaluationCreationActivity.class);
                                startActivity(evaluationCreationIntent);

                            }else{
                                Log.d(TAG, "Response was not successful");

                                //alert the user that their response was not successful
                            }
                        }
                    });

                }
            }
        });
    }

    //writes the authentication token to SharedPreferences
    protected void persistAuthenticationToken(String authenticationToken){
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.shared_preferences_identifier),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("auth_token",authenticationToken);
        editor.apply();
        Log.d(TAG, "Successful write to shared preferences");
    }
}

