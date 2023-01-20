package com.example.weatherhub;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataService {

    static final String REQUEST_QUERY = "https://api.open-meteo.com/v1/forecast?";

    Context context;
    JSONObject weatherInfo;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(JSONObject response);
    }

    public void getTempCurrent(double latitude, double longitude, VolleyResponseListener volleyResponseListener) {
        String url = REQUEST_QUERY + "latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    weatherInfo = response.getJSONObject("current_weather");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(weatherInfo);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Error occurred");
            }
        });

        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }
}
