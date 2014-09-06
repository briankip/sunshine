package com.example.reaper.org.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

     /**
     * Created by reaper on 25/08/14.
     *
     * A placeholder fragment containing a simple view.
     */
    public class ForecastFragment extends Fragment{

        public ForecastFragment(){
        }
         @Override
         public void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
             //For fragment to handle menu options
            setHasOptionsMenu(true);
         }

         @Override
         public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflator){
            menuinflator.inflate(R.menu.forecastfragment, menu);
         }

         @Override
         public boolean onOptionsItemSelected(MenuItem menuitem){
            int id= menuitem.getItemId();
             if(id == R.id.action_refresh) {
                 FetchWeatherTask ftw = new FetchWeatherTask();
                 ftw.execute();
                 return true;
             }
             return super.onOptionsItemSelected(menuitem);
         }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String[] list_items = {
                    "Today Sunny 68°",
                    "Tomorrow Sunny 34°",
                    "The day after Dry 12°",
                    "The APP may rise °",
                    "This is awesome yo!°",
            };

            List<String> weeksforecast  = new ArrayList<String>(Arrays.asList(list_items));

            ArrayAdapter<String> weatheradapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_forecast,
                    R.id.list_item_forecast_textview,
                    weeksforecast);
            ListView listviewforecast = (ListView) rootView.findViewById(R.id.listview_forecast);
            listviewforecast.setAdapter(weatheradapter);

            return rootView;
         }

         public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

             @Override
             protected Void doInBackground(String... q) {
                 // These two need to be declared outside the try/catch
                 // so that they can be closed in the finally block.
                 HttpURLConnection urlConnection = null;
                 BufferedReader reader = null;

                 // Will contain the raw JSON response as a string.
                 String forecastJsonStr = null;

                 try {
                     // Construct the URL for the OpenWeatherMap query
                     // Possible parameters are available at OWM's forecast API page, at
                     // http://openweathermap.org/API#forecast

                     Uri.Builder uribuilder = new Uri.Builder();
                     uribuilder.scheme("http");
                     uribuilder.authority("api.openweathermap.org");
                     uribuilder.appendPath("data").appendPath("2.5").appendPath("forecast").appendPath("daily");
                     uribuilder.appendQueryParameter("q", "ss");

                     String urlyangu = uribuilder.build().toString();

                     Log.v("eeeeeeee", urlyangu);

                     URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                     // Create the request to OpenWeatherMap, and open the connection
                     urlConnection = (HttpURLConnection) url.openConnection();
                     urlConnection.setRequestMethod("GET");
                     urlConnection.connect();

                     // Read the input stream into a String
                     InputStream inputStream = urlConnection.getInputStream();
                     StringBuffer buffer = new StringBuffer();
                     if (inputStream == null) {
                         // Nothing to do.
                         forecastJsonStr = null;
                     }
                     reader = new BufferedReader(new InputStreamReader(inputStream));

                     String line;
                     while ((line = reader.readLine()) != null) {
                         // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                         // But it does make debugging a *lot* easier if you print out the completed
                         // buffer for debugging.
                         buffer.append(line + "\n");
                     }

                     if (buffer.length() == 0) {
                         // Stream was empty.  No point in parsing.
                         forecastJsonStr = null;
                     }
                     forecastJsonStr = buffer.toString();
                     Log.v("sunshine.app", forecastJsonStr);
                 } catch (IOException e) {
                     Log.e("PlaceholderFragment", "Error ", e);
                     // If the code didn't successfully get the weather data, there's no point in attempting
                     // to parse it.
                     forecastJsonStr = null;
                 } finally{
                     if (urlConnection != null) {
                         urlConnection.disconnect();
                     }
                     if (reader != null) {
                         try {
                             reader.close();
                         } catch (final IOException e) {
                             Log.e("PlaceholderFragment", "Error closing stream", e);
                         }
                     }
                 }
                 return null;
             }
         }
    }

