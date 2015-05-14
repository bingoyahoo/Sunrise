package com.example.android.sunrise.app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        //FetchWeatherTask task = new FetchWeatherTask();
        weatherTask.execute(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       /* String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs- Asteroids - 75/65",
                "Fri- Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHERSTATION - 60/51",
                "Sun - Sunny - 80/68"
        };
        ArrayList<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));*/
        mForecastAdapter = new ArrayAdapter<String>(
                //This fragment's parent Activity
                getActivity(),
                //ID of list item layout
                R.layout.list_item_forecast,
                //ID of textView to populate
                R.id.list_item_forecast_textview,
                //Fake com.example.android.sunrise.app.data
                new ArrayList<String>());
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT,
                        forecast);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
