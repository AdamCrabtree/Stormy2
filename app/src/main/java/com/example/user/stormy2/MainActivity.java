package com.example.user.stormy2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humidityLabel) TextView mHumidityValue;
    @BindView(R.id.percipitationLabel) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconLabel) ImageView mIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String apiKey = "e88990ef588ac9f8395dcf2c2ce0759d";
        double latitude = 41.8267, longitude = -83.4233;
        String forecastURL = "https://api.darksky.net/forecast/" + apiKey
                + "/" + latitude + "," + longitude;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if(response.isSuccessful()){
                        mCurrentWeather = getCurrentDetails(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });
                       System.out.println(response.body().string());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "exception caught", e);
                }
                  catch(JSONException e){

                  }
            }
        });
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        mPrecipValue.setText(Math.round(mCurrentWeather.getPrecipChance()*100) +"%");
        mHumidityValue.setText(mCurrentWeather.getHumidity() +"");
        mTimeLabel.setText("It is currently " + mCurrentWeather.getFormattedTime() + " and the weather is");
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconID());
        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTimeZone(timezone);
        return currentWeather;
    }
}
