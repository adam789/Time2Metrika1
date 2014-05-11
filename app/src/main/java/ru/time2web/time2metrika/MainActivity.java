package ru.time2web.time2metrika;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.metrika4j.MetrikaApi;
import ru.metrika4j.entity.Counter;

public class MainActivity extends ListActivity {

    private List<Counter> counters = new ArrayList<Counter>();
    private CounterListAdapter listAdapter;
    public static final String COUNTER_ID_KEY = "yaMetrikaCounterId";
    public static final String SITE_NAME_KEY = "yaMetrikaSiteName";
    public static MetrikaApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=getIntent();


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //int appWidgetId = getWidgetId();

        // Записываем в prefernces идентификатор счетчика и имя сайта для данного виджета
        Counter counter = counters.get(position);
        SharedPreferences.Editor prefs = getSharedPreferences(Globals.PREF_FILE, 0).edit();
        prefs.putInt(MainActivity.COUNTER_ID_KEY, counter.getId());
        prefs.putString(MainActivity.SITE_NAME_KEY, counter.getSite());
        prefs.commit();

        // Возвращаем результат widget manager-у
        Intent resultValue = new Intent(this,MetrikaReport.class);
        resultValue.putExtra(MainActivity.COUNTER_ID_KEY, counter.getId());
        startActivity(resultValue);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_addnew) {

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /** Адаптер для отображения Counter в элементе списка счетчиков */
    private class CounterListAdapter extends ArrayAdapter<Counter> {
        public CounterListAdapter() {
            super(MainActivity.this, R.layout.site_list_row, R.id.site_list_name, counters);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView name = (TextView) view.findViewById(R.id.site_list_name);
            name.setText(counters.get(position).getName());
            TextView site = (TextView) view.findViewById(R.id.site_list_site);
            site.setText(counters.get(position).getSite());
            return view;
        }
    }

    public void onAddNewClick(MenuItem item)
    {

        // TextView tvInfo = (TextView) findViewById(R.id.tvInfo);
        // tvInfo.setText("Вы выбрали пункт Settings, лучше бы выбрали кота");
    }

}
