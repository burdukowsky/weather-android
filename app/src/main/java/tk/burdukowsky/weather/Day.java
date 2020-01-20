package tk.burdukowsky.weather;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Android Studio
 * User: STANISLAV
 * Date: 25 Февр. 2017 17:06
 */

public class Day {
    private Date date;
    private String summary;
    private Double precipAccumulation;
    private Double temperatureMin;
    private Double temperatureMax;
    private Double apparentTemperatureMin;
    private Double apparentTemperatureMax;
    private Double humidity;
    private Double windSpeed;
    private Integer windBearing;
    private Double pressure;
    private String icon;

    public Date getDate() {
        return date;
    }

    public String getStringDate() {
        String[] russianMonth = {
                "января",
                "февраля",
                "марта",
                "апреля",
                "мая",
                "июня",
                "июля",
                "августа",
                "сентября",
                "октября",
                "ноября",
                "декабря"
        };
        DateFormatSymbols russianSymbols = new DateFormatSymbols();
        russianSymbols.setMonths(russianMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM", russianSymbols);
        return simpleDateFormat.format(date);
    }

    public String getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public String getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }

    public long getDateTimestamp() {
        return date.getTime() / 1000;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public String getStringSummary() {
        return (summary != null) ? summary : "";
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getPrecipAccumulation() {
        return precipAccumulation;
    }

    public String getStringPrecipAccumulation() {
        return (precipAccumulation != 0) ? "Осадки: " + precipAccumulation + " см" : "Осадков нет";
    }

    public void setPrecipAccumulation(Double precipAccumulation) {
        this.precipAccumulation = precipAccumulation;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public void setApparentTemperatureMin(Double apparentTemperatureMin) {
        this.apparentTemperatureMin = apparentTemperatureMin;
    }

    public Double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public void setApparentTemperatureMax(Double apparentTemperatureMax) {
        this.apparentTemperatureMax = apparentTemperatureMax;
    }

    public Double getHumidity() {
        return humidity;
    }

    public String getStringHumidity() {
        return (humidity != null) ? "Влажность: " + (int) (humidity * 100) + "%" : "";
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getStringWindSpeed() {
        return (windSpeed != null) ? "Ветер: " + windSpeed + " м/c" : "";
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindBearing() {
        return windBearing;
    }

    public String getStringWindBearing() {
        if (windSpeed != null) {
            if (windBearing > 338 && windBearing <= 22) {
                return "С";
            } else if (windBearing > 22 && windBearing <= 68) {
                return "СВ";
            } else if (windBearing > 68 && windBearing <= 113) {
                return "В";
            } else if (windBearing > 113 && windBearing <= 158) {
                return "ЮВ";
            } else if (windBearing > 158 && windBearing <= 203) {
                return "Ю";
            } else if (windBearing > 203 && windBearing <= 248) {
                return "ЮЗ";
            } else if (windBearing > 248 && windBearing <= 293) {
                return "З";
            } else {
                return "СЗ";
            }
        }
        return "";
    }

    public String getStringWind() {
        return getStringWindSpeed() + " " + getStringWindBearing();
    }

    public void setWindBearing(Integer windBearing) {
        this.windBearing = windBearing;
    }

    public Double getPressure() {
        return pressure;
    }

    public String getStringPressure() {
        return (pressure != null) ? "Давление: " + pressure + " гПа" : "";
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public String getStringTemperature() {
        if (temperatureMin != null && temperatureMax != null) {
            return String.format(Locale.getDefault(), "%+d℃/%+d℃", (int) Math.round(temperatureMin), (int) Math.round(temperatureMax));
        }
        return "";
    }

    public String getStringApparentTemperature() {
        if (apparentTemperatureMin != null && apparentTemperatureMax != null) {
            return String.format(Locale.getDefault(), "ощущ. %+d℃/%+d℃", (int) Math.round(apparentTemperatureMin), (int) Math.round(apparentTemperatureMax));
        }
        return "";
    }

    public Day(Date date, String summary, Double precipAccumulation, Double temperatureMin, Double temperatureMax, Double apparentTemperatureMin, Double apparentTemperatureMax, Double humidity, Double windSpeed, Integer windBearing, Double pressure, String icon) {
        this.date = date;
        this.summary = summary;
        this.precipAccumulation = precipAccumulation;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.apparentTemperatureMin = apparentTemperatureMin;
        this.apparentTemperatureMax = apparentTemperatureMax;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windBearing = windBearing;
        this.pressure = pressure;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
