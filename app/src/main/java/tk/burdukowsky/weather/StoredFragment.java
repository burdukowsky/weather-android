package tk.burdukowsky.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
/**
 * Created by Android Studio
 * User: STANISLAV
 * Date: 26 Февр. 2017 13:51
 */

public class StoredFragment extends Fragment {

    ListView storedList;
    WeekListAdapter weekListAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_stored, container, false);
        storedList = (ListView) rootView.findViewById(R.id.storedList);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Load(getActivity()).execute();
    }

    private class Load extends AsyncTask<Void, Void, Void> {

        private Context mContext;

        Load(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {

        }

        protected Void doInBackground(Void... args) {
            DatabaseHelper db = new DatabaseHelper(mContext);
            weekListAdapter = new WeekListAdapter(mContext, db.getStoredData());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            storedList.setAdapter(weekListAdapter);
        }
    }
}
