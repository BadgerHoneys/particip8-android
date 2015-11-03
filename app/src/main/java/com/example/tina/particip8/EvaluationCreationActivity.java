package com.example.tina.particip8;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class EvaluationCreationActivity extends ActionBarActivity {

    private final String TAG = EvaluationCreationActivity.class.getSimpleName();
    private Particip8API mParticip8API;

    private EditText mEvaluationNameEditText;
    private Button mEvaluationCreationButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_creation);

        mParticip8API = new Particip8API(EvaluationCreationActivity.this);

        //set Interface Elements here
        mEvaluationNameEditText = (EditText) findViewById(R.id.evaluation_name_text);
        mEvaluationCreationButton = (Button) findViewById(R.id.evaluation_create_button);


        //request all classes (and evaluation template information) belonging to teacher
        Call classInformationCall = mParticip8API.getClassInformationCall();

        if(classInformationCall!=null){
            classInformationCall.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG,"Failure Callback hit");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.d(TAG,"Response Callback hit");
                }
            });
        }


            //on success, populate Class selection with results




        //listen for changes in class selection, re-populate evaluation template
        //selection with options at this time


        //set click listener on EvaluationCreationButton
        mEvaluationCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Evaluation Creation Button Clicked");

                //grab form information

                //make post to service

                //on success, start evaluation

            }
        });


    }

}
