package ru.time2web.time2metrika;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import ru.metrika4j.entity.Counter;
import ru.metrika4j.error.AuthException;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Globals.checkOAuthTokenExists(
                this)) { // Загружаем счетчики только если уже авторизовались, иначе отправляем на авторизацию
            new CountersLoadTask().execute();
        }
        else {
            Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://oauth.yandex.ru/authorize?response_type=token&client_id=54c7b6eb472b413f8428bce48eb819d5"));
            startActivity(browser);
        }
        //new PrefetchData().execute();
       // new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

    /*        @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
*/
    }


    /**
     * Async Task to make http call
     */
    private class CountersLoadTask extends AsyncTask<Void, Void, Counter[]> {

        private Exception error;
        private boolean needAuth = false;
        //private ProgressDialog progressDialog;
        private List<Counter> sites;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected void onPostExecute(Counter[] counters) {
            //super.onPostExecute();
            // After completing http call
            // will close this activity and lauch main activity
            if (needAuth) {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://oauth.yandex.ru/authorize?response_type=token&client_id=54c7b6eb472b413f8428bce48eb819d5"));
                startActivity(browser);
            } else {
                //sites = Arrays.asList(counters);
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                i.putExtra("counters", counters);
                //i.putExtra("earned", earned);
                startActivity(i);
            }

            // close this activity
            finish();
        }
        @Override
        protected Counter[] doInBackground(Void... arg0) {
            try {
                return Globals.getApi(SplashScreen.this).getCounters();
                //return sites;
            } catch (AuthException e) {
                needAuth = true;
            } catch (Exception e) {
                error = e;
            }
            // Если словили Exception, возвращаем пустой список
            return new Counter[0];
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
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
        return super.onOptionsItemSelected(item);
    }




}
