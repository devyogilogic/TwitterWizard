package com.developer.twitterwizard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class Showtweets extends AppCompatActivity {
    private TwitterServiceApi mAPIService;
    private RecyclerView recyclerView;
    ArrayList<String>shtweet;
    String value;
    ArrayList<String>shtweetcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtweets);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview) ;
        shtweet=new ArrayList<String>();
        shtweetcount=new ArrayList<String>();
        value = getIntent().getExtras().getString("Typetweet");
        mAPIService = ApiUtils.getTwitterServiceApi();
        new DataSetter().execute();
    }

    public class  DataSetter extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAPIService.fetchPositiveResult("/details/"+value).enqueue(new Callback<TopPositive>() {
                @Override
                public void onResponse(Call<TopPositive> call, Response<TopPositive> response) {
                    String nk=response.body().getTopPositive();
                    String adt[]=nk.split(";");
                  shtweet.add(adt[2]);
                  shtweet.add(adt[4]);
                  shtweet.add(adt[6]);
                  shtweetcount.add(adt[1]);
                  shtweetcount.add(adt[3]);
                  shtweetcount.add(adt[5]);
                    DataAdapter da= new DataAdapter(Showtweets.this,shtweet,shtweetcount);
                    LinearLayoutManager lm = new LinearLayoutManager(Showtweets.this);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(da);

                }

                @Override
                public void onFailure(Call<TopPositive> call, Throwable t) {
                    Toast.makeText(Showtweets.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
            return  null;
        }
    }
}
