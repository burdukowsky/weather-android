package tk.burdukowsky.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Android Studio
 * User: STANISLAV
 * Date: 25 Февр. 2017 17:09
 */

class WeekListAdapter extends BaseAdapter {
    //private Context context;
    private LayoutInflater layoutInflater;
    private List<Day> objects;

    WeekListAdapter(Context context, List<Day> days) {
        //this.context = context;
        this.objects = days;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.day, parent, false);
        }

        Day day = getDay(position);

        ((TextView) view.findViewById(R.id.textViewDayDayOfWeek)).setText(day.getDayOfWeek());
        ((TextView) view.findViewById(R.id.textViewDayTime)).setText(day.getStringDate());
        ((TextView) view.findViewById(R.id.textViewDayYear)).setText(day.getYear());
        ((ImageView) view.findViewById(R.id.imageViewDayIcon)).setImageResource(getResourceIdByIconName(day.getIcon()));
        ((TextView) view.findViewById(R.id.textViewDaySummary)).setText(day.getStringSummary());
        ((TextView) view.findViewById(R.id.textViewDayTemperature)).setText(day.getStringTemperature());
        ((TextView) view.findViewById(R.id.textViewDayApparentTemperature)).setText(day.getStringApparentTemperature());
        ((TextView) view.findViewById(R.id.textViewDayHumidity)).setText(day.getStringHumidity());
        ((TextView) view.findViewById(R.id.textViewDayWind)).setText(day.getStringWind());
        ((TextView) view.findViewById(R.id.textViewDayPrecipAccumulation)).setText(day.getStringPrecipAccumulation());
        ((TextView) view.findViewById(R.id.textViewDayPressure)).setText(day.getStringPressure());

        return view;
    }

    private Day getDay(int position) {
        return ((Day) getItem(position));
    }

    private int getResourceIdByIconName(String iconName) {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        switch (iconName) {
            /*case "clear-day":
                return R.drawable.ic_clear_day;*/
            case "clear-night":
                return R.drawable.ic_clear_night;
            case "rain":
                return R.drawable.ic_rain;
            case "snow":
                return R.drawable.ic_snow;
            case "sleet":
                return R.drawable.ic_sleet;
            case "wind":
                return R.drawable.ic_wind;
            case "fog":
                return R.drawable.ic_fog;
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "partly-cloudy-day":
                return R.drawable.ic_partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.ic_partly_cloudy_night;
            default:
                return R.drawable.ic_clear_day;
        }
    }

}