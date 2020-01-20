package tk.burdukowsky.weather;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Android Studio
 * User: STANISLAV
 * Date: 26 Февр. 2017 13:52
 */

public class CertainDateFragment extends Fragment {

    String requestUrl;
    WeekListAdapter weekListAdapter;
    ListView certainDateList;
    LinearLayout certainDateLoadingSpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_certain_date, container, false);
        certainDateList = (ListView) rootView.findViewById(R.id.certainDateList);
        Button button = (Button) rootView.findViewById(R.id.datePickerButton);
        certainDateLoadingSpinner = (LinearLayout) rootView.findViewById(R.id.certainDateLoadingSpinner);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), onDateSetListener, year, month, day).show();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth, 17, 0);
            long date = calendar.getTimeInMillis() / 1000;
            requestUrl = MainActivity.API_ADDRESS + MainActivity.API_KEY + "/" + MainActivity.NSK_LOCATION + "," + date + MainActivity.REQUEST_PARAMS_CERTAIN_DATE;
            new CertainDate(getActivity()).execute();
        }
    };

    private class CertainDate extends AsyncTask<Void, Void, Integer> {

        private Context mContext;

        CertainDate(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            certainDateLoadingSpinner.setVisibility(View.VISIBLE);
        }

        protected Integer doInBackground(Void... args) {
            JSONObject data = WeekFragment.getJsonByUrl(requestUrl);
            if (data == null) {
                return WeekFragment.STATUS_ERROR;
            }
            List<Day> days;
            try {
                days = WeekFragment.getListDaysFromJsonObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
                return WeekFragment.STATUS_ERROR;
            }

            weekListAdapter = new WeekListAdapter(mContext, days);

            return WeekFragment.STATUS_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case WeekFragment.STATUS_SUCCESS:
                    certainDateList.setAdapter(weekListAdapter);
                    break;
                case WeekFragment.STATUS_ERROR:
                    Toast.makeText(getActivity(),
                            "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
                    break;
            }
            certainDateLoadingSpinner.setVisibility(View.GONE);
        }
    }
}
