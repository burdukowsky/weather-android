package tk.burdukowsky.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio
 * User: STANISLAV
 * Date: 26 Февр. 2017 13:39
 */

public class WeekFragment extends Fragment {

    ListView weekList;
    WeekListAdapter weekListAdapter;
    String requestUrl;
    LinearLayout linearLayoutLoadingSpinner;

    final static int STATUS_SUCCESS = 1;
    final static int STATUS_ERROR = 2;
    final static int STATUS_DATABASE_ERROR = 3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        linearLayoutLoadingSpinner = (LinearLayout) rootView.findViewById(R.id.linearLayoutLoadingSpinner);
        weekList = (ListView) rootView.findViewById(R.id.weekList);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestUrl = MainActivity.API_ADDRESS + MainActivity.API_KEY + "/" + MainActivity.NSK_LOCATION + MainActivity.REQUEST_PARAMS;
        new UpdateDatabase(getActivity()).execute();
    }

    private class UpdateDatabase extends AsyncTask<Void, Void, Integer> {

        private Context mContext;

        UpdateDatabase(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            linearLayoutLoadingSpinner.setVisibility(View.VISIBLE);
        }

        protected Integer doInBackground(Void... args) {
            JSONObject data = getJsonByUrl(requestUrl);
            if (data == null) {
                return STATUS_ERROR;
            }
            List<Day> days;
            try {
                days = getListDaysFromJsonObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
                return STATUS_ERROR;
            }
            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
            if (!databaseHelper.insertIntoWeather(days)) {
                return STATUS_DATABASE_ERROR;
            }
            weekListAdapter = new WeekListAdapter(mContext, days);

            return STATUS_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case STATUS_SUCCESS:
                    weekList.setAdapter(weekListAdapter);
                    break;
                case STATUS_ERROR:
                    Toast.makeText(getActivity(),
                            "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
                    break;
                case STATUS_DATABASE_ERROR:
                    weekList.setAdapter(weekListAdapter);
                    Toast.makeText(getActivity(),
                            "Ошибка при записи в базу данных", Toast.LENGTH_SHORT).show();
                    break;
            }
            linearLayoutLoadingSpinner.setVisibility(View.GONE);
        }
    }

    static List<Day> getListDaysFromJsonObject(JSONObject data) throws JSONException {
        List<Day> days = new ArrayList<>();
        JSONObject daily = data.getJSONObject("daily");
        JSONArray daysData = daily.getJSONArray("data");
        for (int i = 0; i < daysData.length(); i++) {
            JSONObject row = daysData.getJSONObject(i);
            Date date = new java.util.Date(row.getLong("time") * 1000);
            String summary = row.has("summary") ? row.getString("summary") : null;
            Double precipAccumulation = row.has("precipAccumulation") ? row.getDouble("precipAccumulation") : 0;
            Double temperatureMin = row.has("temperatureMin") ? row.getDouble("temperatureMin") : null;
            Double temperatureMax = row.has("temperatureMax") ? row.getDouble("temperatureMax") : null;
            Double apparentTemperatureMin = row.has("apparentTemperatureMin") ? row.getDouble("apparentTemperatureMin") : null;
            Double apparentTemperatureMax = row.has("apparentTemperatureMax") ? row.getDouble("apparentTemperatureMax") : null;
            Double humidity = row.has("humidity") ? row.getDouble("humidity") : null;
            Double windSpeed = row.has("windSpeed") ? row.getDouble("windSpeed") : null;
            Integer windBearing = row.has("windBearing") ? row.getInt("windBearing") : null;
            Double pressure = row.has("pressure") ? row.getDouble("pressure") : null;
            String icon = row.has("icon") ? row.getString("icon") : "";
            days.add(new Day(date, summary, precipAccumulation, temperatureMin, temperatureMax, apparentTemperatureMin, apparentTemperatureMax, humidity, windSpeed, windBearing, pressure, icon));
        }
        return days;
    }

    @Nullable
    static JSONObject getJsonByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            String jsonString = json.toString();
            if (isJsonValid(jsonString)) {
                return new JSONObject(jsonString);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public boolean isJsonValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
