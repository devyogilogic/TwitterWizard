package com.developer.twitterwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText searchterm;
    Button find;
    Button download;
    Button seepositive;

    private TwitterServiceApi mAPIService;
    PieChart pieChart;
    ProgressDialog pd;
    float positive,neutral,negative;
    String searchT;
    public static  String server="http://192.168.2.108:5000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchterm= (EditText)findViewById(R.id.searchTerm);
        seepositive=(Button)findViewById(R.id.seepositive);

        find= (Button) findViewById(R.id.finder);
        download=(Button)findViewById(R.id.download);
        pieChart=(PieChart)findViewById(R.id.piechart);
        pd =new ProgressDialog(MainActivity.this);

        seepositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,Showtweets.class);
                i.putExtra("Typetweet","Positive");
                startActivity(i);
            }
        });
        mAPIService = ApiUtils.getTwitterServiceApi();
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchT=searchterm.getText().toString();

                new PieChartDeveloper().execute();
            }
        });
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                       )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mAPIService.downloadFileWithFixedUrl().enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                Log.d("r", "server contacted and has file");

                                                boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                                                Log.d("p", "file download was a success? " + writtenToDisk);
                                                download.setVisibility(View.GONE);
                                            } else {
                                                Log.d("g", "server contact failed");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();




    }
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Combined.zip");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("status", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    public  class  PieChartDeveloper  extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Processing...");
            pd.setMessage("Please Wait");
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAPIService.fetchTweetsResult("/percentage/"+searchT).enqueue(new Callback<Results>() {
                @Override
                public void onResponse(Call<Results> call, Response<Results> response) {
                    Toast.makeText(MainActivity.this, ""+response.body().getResult(), Toast.LENGTH_SHORT).show();
                    String atp=response.body().getResult().toString();
                    String [] values=atp.split(",");
                    positive=Float.parseFloat(values[0]);
                    neutral=Float.parseFloat(values[1]);
                    negative=Float.parseFloat(values[2]);
                    pd.dismiss();
                    ArrayList Analysis = new ArrayList();

                    Analysis.add(new Entry(positive, 0));
                    Analysis.add(new Entry(neutral, 1));
                    Analysis.add(new Entry(negative, 2));
                    PieDataSet dataSet = new PieDataSet(Analysis, "Analysis of Tweets");
                    ArrayList tweetTitle = new ArrayList();

                    tweetTitle.add("Positive");
                    tweetTitle.add("Neutral");
                    tweetTitle.add("Negative");
                    PieData data = new PieData(tweetTitle, dataSet);
                   data.setValueTextSize(12f);
                    pieChart.setData(data);
                    pieChart.setCenterText("Twitter Analysis");
                    pieChart.setCenterTextColor(Color.CYAN);
                    pieChart.setCenterTextSize(24f);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieChart.animateXY(5000, 5000);
                    download.setVisibility(View.VISIBLE);
                    seepositive.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<Results> call, Throwable t) {

                }
            });
            return null;
        }
    }
}
