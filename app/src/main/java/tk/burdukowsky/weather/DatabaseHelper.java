package tk.burdukowsky.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio
 * User: STANISLAV
 * Date: 25 Февр. 2017 23:28
 */

class DatabaseHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "weather.db";
    private final String TABLE_NAME_WEATHER = "weather";
    private final String COLUMN_NAME_ID = "_id";
    private final String COLUMN_NAME_DATE = "date";
    private final String COLUMN_NAME_SUMMARY = "summary";
    private final String COLUMN_NAME_PRECIP_ACCUMULATION = "precip_accumulation";
    private final String COLUMN_NAME_TEMPERATURE_MIN = "temperature_min";
    private final String COLUMN_NAME_TEMPERATURE_MAX = "temperature_max";
    private final String COLUMN_NAME_APPARENT_TEMPERATURE_MIN = "apparent_temperature_min";
    private final String COLUMN_NAME_APPARENT_TEMPERATURE_MAX = "apparent_temperature_max";
    private final String COLUMN_NAME_HUMIDITY = "humidity";
    private final String COLUMN_NAME_WIND_SPEED = "wind_speed";
    private final String COLUMN_NAME_WIND_BEARING = "wind_bearing";
    private final String COLUMN_NAME_PRESSURE = "pressure";
    private final String COLUMN_NAME_ICON = "icon";
    private final String CREATE_TABLE_WEATHER = "CREATE TABLE " + TABLE_NAME_WEATHER + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME_DATE + " INTEGER," +
            COLUMN_NAME_SUMMARY + " TEXT," +
            COLUMN_NAME_PRECIP_ACCUMULATION + " REAL," +
            COLUMN_NAME_TEMPERATURE_MIN + " REAL," +
            COLUMN_NAME_TEMPERATURE_MAX + " REAL," +
            COLUMN_NAME_APPARENT_TEMPERATURE_MIN + " REAL," +
            COLUMN_NAME_APPARENT_TEMPERATURE_MAX + " REAL," +
            COLUMN_NAME_HUMIDITY + " REAL," +
            COLUMN_NAME_WIND_SPEED + " REAL," +
            COLUMN_NAME_WIND_BEARING + " INTEGER," +
            COLUMN_NAME_PRESSURE + " REAL," +
            COLUMN_NAME_ICON + " TEXT" +
            ")";
    //private Context mContext;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean insertIntoWeather(Day day, SQLiteDatabase db) {
        if (db.isReadOnly()) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DATE, day.getDateTimestamp());
        values.put(COLUMN_NAME_SUMMARY, day.getSummary());
        values.put(COLUMN_NAME_PRECIP_ACCUMULATION, day.getPrecipAccumulation());
        values.put(COLUMN_NAME_TEMPERATURE_MIN, day.getTemperatureMin());
        values.put(COLUMN_NAME_TEMPERATURE_MAX, day.getTemperatureMax());
        values.put(COLUMN_NAME_APPARENT_TEMPERATURE_MIN, day.getApparentTemperatureMin());
        values.put(COLUMN_NAME_APPARENT_TEMPERATURE_MAX, day.getApparentTemperatureMax());
        values.put(COLUMN_NAME_HUMIDITY, day.getHumidity());
        values.put(COLUMN_NAME_WIND_SPEED, day.getWindSpeed());
        values.put(COLUMN_NAME_WIND_BEARING, day.getWindBearing());
        values.put(COLUMN_NAME_PRESSURE, day.getPressure());
        values.put(COLUMN_NAME_ICON, day.getIcon());

        return db.insert(TABLE_NAME_WEATHER, null, values) != -1;
    }

    /*public boolean insertIntoWeather(Day day) {
        return insertIntoWeather(day, this.getWritableDatabase());
    }*/

    boolean insertIntoWeather(List<Day> days) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Day day : days) {
            // взять по таймштампу день из базы
            // если нет такого, то делаем инсерт
            // если есть, то делайм апдейт
            if (dayInDatabaseExists(day, db)) {
                if (!updateWeather(day, db)) {
                    return false;
                }
            } else {
                if (!insertIntoWeather(day, db)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean updateWeather(Day day, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SUMMARY, day.getSummary());
        values.put(COLUMN_NAME_PRECIP_ACCUMULATION, day.getPrecipAccumulation());
        values.put(COLUMN_NAME_TEMPERATURE_MIN, day.getTemperatureMin());
        values.put(COLUMN_NAME_TEMPERATURE_MAX, day.getTemperatureMax());
        values.put(COLUMN_NAME_APPARENT_TEMPERATURE_MIN, day.getApparentTemperatureMin());
        values.put(COLUMN_NAME_APPARENT_TEMPERATURE_MAX, day.getApparentTemperatureMax());
        values.put(COLUMN_NAME_HUMIDITY, day.getHumidity());
        values.put(COLUMN_NAME_WIND_SPEED, day.getWindSpeed());
        values.put(COLUMN_NAME_WIND_BEARING, day.getWindBearing());
        values.put(COLUMN_NAME_PRESSURE, day.getPressure());
        values.put(COLUMN_NAME_ICON, day.getIcon());
        String where = COLUMN_NAME_DATE + "=" + day.getDateTimestamp();
        return db.update(TABLE_NAME_WEATHER, values, where, null) > 0;
    }

    private boolean dayInDatabaseExists(Day day, SQLiteDatabase db) {
        String Query = "SELECT * FROM " + TABLE_NAME_WEATHER + " WHERE " + COLUMN_NAME_DATE + " = " + day.getDateTimestamp();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    List<Day> getStoredData() {
        List<Day> days = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sortOrder = COLUMN_NAME_DATE + " ASC";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDayTimestamp = calendar.getTimeInMillis() / 1000L;
        String selection = COLUMN_NAME_DATE + " >= " + startOfDayTimestamp;
        Cursor c = db.query(TABLE_NAME_WEATHER, null, selection, null, null, null, sortOrder, "8");
        while (c.moveToNext()) {
            Date date = new java.util.Date(c.getLong(c.getColumnIndex(COLUMN_NAME_DATE)) * 1000);
            String summary = c.getString(c.getColumnIndex(COLUMN_NAME_SUMMARY));
            Double precipAccumulation = c.getDouble(c.getColumnIndex(COLUMN_NAME_PRECIP_ACCUMULATION));
            Double temperatureMin = c.getDouble(c.getColumnIndex(COLUMN_NAME_TEMPERATURE_MIN));
            Double temperatureMax = c.getDouble(c.getColumnIndex(COLUMN_NAME_TEMPERATURE_MAX));
            Double apparentTemperatureMin = c.getDouble(c.getColumnIndex(COLUMN_NAME_APPARENT_TEMPERATURE_MIN));
            Double apparentTemperatureMax = c.getDouble(c.getColumnIndex(COLUMN_NAME_APPARENT_TEMPERATURE_MAX));
            Double humidity = c.getDouble(c.getColumnIndex(COLUMN_NAME_HUMIDITY));
            Double windSpeed = c.getDouble(c.getColumnIndex(COLUMN_NAME_WIND_SPEED));
            Integer windBearing = c.getInt(c.getColumnIndex(COLUMN_NAME_WIND_BEARING));
            Double pressure = c.getDouble(c.getColumnIndex(COLUMN_NAME_PRESSURE));
            String icon = c.getString(c.getColumnIndex(COLUMN_NAME_ICON));
            days.add(new Day(date, summary, precipAccumulation, temperatureMin, temperatureMax, apparentTemperatureMin, apparentTemperatureMax, humidity, windSpeed, windBearing, pressure, icon));
        }
        c.close();
        return days;
    }
}
