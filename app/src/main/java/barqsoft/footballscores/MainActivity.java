package barqsoft.footballscores;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static int selected_match_id;

    /**
     * Determines which tab will be active by default.
     */
    public static int current_fragment = 1;

    // The main app screen.
    private PagerFragment my_main;

    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for network connectivity.
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();

        if (network != null && network.isConnectedOrConnecting()) {
            connected = true;
            if (savedInstanceState == null) {
                my_main = new PagerFragment();
                //my_main.setRetainInstance(true);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, my_main)
                        .commit();
            }
        }
        else{
            connected = false;
            // Show alert dialog because there is no internet connectivity.
            AlertDialog.Builder alertContent = new AlertDialog.Builder(this);
            alertContent.setTitle(R.string.no_internet_title)
                    .setMessage(R.string.no_internet_body)
                    .setPositiveButton(R.string.no_internet_positive_button,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            })
                    .setNegativeButton(R.string.no_internet_negative_button, null);

            // Show the alert.
            AlertDialog alertDialog = alertContent.create();
            alertDialog.show();
        }
        // Check if this is the first time running the app.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(connected) {
            outState.putInt("Pager_Current", my_main.mPagerHandler.getCurrentItem());
            outState.putInt("Selected_match", selected_match_id);
            getSupportFragmentManager().putFragment(outState, "my_main", my_main);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(connected) {
            current_fragment = savedInstanceState.getInt("Pager_Current");
            selected_match_id = savedInstanceState.getInt("Selected_match");
            my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "my_main");
            super.onRestoreInstanceState(savedInstanceState);
        }
    }
}
