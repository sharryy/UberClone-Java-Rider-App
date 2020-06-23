package com.anonymous.uberedmtrider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anonymous.uberedmtrider.Common.Common;
import com.anonymous.uberedmtrider.Remote.IGoogleAPI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetRiderFragment extends BottomSheetDialogFragment {

    String mLocation, mDestination;
    IGoogleAPI mService;
    TextView txtCalculate;

    public static BottomSheetRiderFragment newInstance(String location, String destination) {
        BottomSheetRiderFragment f = new BottomSheetRiderFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("destination", destination);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = getArguments().getString("location");
        mDestination = getArguments().getString("destination");

        Log.d("TAG", mLocation + "," + mDestination);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_rider, container, false);
        TextView txtLocation = (TextView) view.findViewById(R.id.txtLocation);
        TextView txtDestination = (TextView) view.findViewById(R.id.txtDestination);
        txtCalculate = (TextView) view.findViewById(R.id.txtCalculate);

        mService = Common.getGoogleService();
        getPrice(mLocation, mDestination);

        txtLocation.setText(mLocation);
        txtDestination.setText(mDestination);

        return view;
    }

    private void getPrice(String mLocation, String mDestination) {
        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"
                    + "transit_routing_preference=less_driving&"
                    + "origin=" + mLocation + "&"
                    + "destination=" + mDestination + "&"
                    + "key=" + getResources().getString(R.string.google_browser_key);

            Log.e("LINK", requestUrl);

            mService.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");

                        JSONObject legsObject = legs.getJSONObject(0);

                        //Get Distance

                        JSONObject distance = legsObject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                        //Get Time
                        JSONObject time = legsObject.getJSONObject("duration");
                        String time_text = time.getString("text");
                        Integer time_value = Integer.parseInt(time_text.replaceAll("\\D+", ""));

                        String final_calculate = String.format("%s + %s = $%.2f", distance_text, time_text,
                                Common.getPrice(distance_value, time_value));

                        txtCalculate.setText(final_calculate);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("TAG", t.getMessage());

                }
            });
        } catch (Exception e) {
            Log.d("TAG", "getPrice: " + e.getMessage());
        }
    }
}
