package example.org.githubjobs;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    static final String TAG = MainActivity.class.getSimpleName();
    public static final String SEARCH_TERM = "search_term";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final long REQUIRE_NEW_GPS_TIME = 60 * 60 * 1000; // 1 hour
    GetGitHubData mGetGitHubData;
    RecyclerView mJobRecyclerView;
    JobViewAdapter mJobViewAdapter;
    SearchView mSearchView;
    String searchTerm;
    private GestureDetectorCompat mGestureDetector;
    List<Job> mJobList;
    LocationManager mLocationManager;
    boolean isGPSPermission;
    double locationLat;
    double locationLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize location variables
        locationLat = GetGitHubData.NO_GPS_DATA;
        locationLong = GetGitHubData.NO_GPS_DATA;

        // Check for saved GPS coordinates. If none exist, check for requisite GPS permissions and
        // check for a recent last known location. If last location is not recent, request a location
        // update. If GPS permissions have not been granted to the application, location variables
        // will remain set to NO_GPS_DATA.
        if(savedInstanceState != null) {
            Log.d(TAG, "savedInstance recieved");
            locationLat = savedInstanceState.getDouble(LATITUDE);
            locationLong = savedInstanceState.getDouble(LONGITUDE);
        } else {

            isGPSPermission = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    android.os.Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
            Log.d(TAG, "GPS permission: " + isGPSPermission);

            if (isGPSPermission) {
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - REQUIRE_NEW_GPS_TIME) {
                    locationLat = location.getLatitude();
                    locationLong = location.getLongitude();
                } else {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
            }

            Log.d(TAG, "Location: " + locationLat + " " + locationLong);
        }

        // Create RecyclerView to hold job search results.
        mJobViewAdapter = new JobViewAdapter(new ArrayList<Job>(), MainActivity.this);
        mJobRecyclerView = (RecyclerView) findViewById(R.id.jobRecyclerView);
        mJobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mJobRecyclerView.setAdapter(mJobViewAdapter);
        mJobRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        // Add OnItemTouchListener to RecyclerView to allow user to select jobs and see their details.
        mJobRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Job selectedJob = mJobList.get(position);
                Log.d(TAG, "item selected: " + position);
                Intent jobDetailsIntent = new Intent(MainActivity.this, JobDetailsActivity.class);
                jobDetailsIntent.putExtra(Job.JOB_TITLE, selectedJob.getJobTitle());
                jobDetailsIntent.putExtra(Job.COMPANY_NAME, selectedJob.getCompanyName());
                jobDetailsIntent.putExtra(Job.JOB_DESCRIPTION, selectedJob.getJobDescription());
                startActivity(jobDetailsIntent);
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for the last search query from the user, if none exists default to a search for PHP.
        searchTerm = checkForSharedPref(SEARCH_TERM);
        if(searchTerm.length() < 1) {
            searchTerm = "PHP";
        }

        // If the application has GPS permissions and location has not been set, the application will
        // wait for a GPS fix before loading jobs. A toast notifies the user that the application is
        // awaiting a GPS fix.
        if(isGPSPermission && locationLat == GetGitHubData.NO_GPS_DATA) {
            Toast.makeText(this,"Getting GPS Fix",Toast.LENGTH_LONG).show();
            return;
        }

        // Retrieve job data from GitHub Jobs website to display to user
        mGetGitHubData = new LoadJobs(searchTerm, locationLat, locationLong);
        mGetGitHubData.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Preserve location data if this activity is paused
        if(locationLat != GetGitHubData.NO_GPS_DATA && locationLat != GetGitHubData.NO_GPS_DATA) {
            outState.putDouble(LATITUDE, locationLat);
            outState.putDouble(LONGITUDE, locationLong);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Create a search view and search manager so the user can specify the search query being
        // sent to the GitHub Jobs site.
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setQueryHint("Search Jobs");
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        mSearchView.setIconified(false);
        mSearchView.setQuery(searchTerm, false);

        // Listen for submission of the search view text and load jobs from GitHub Jobs website
        // that satisfy the query. Save the query in the shared preferences
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTerm = query;
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPref.edit().putString(SEARCH_TERM, query).commit();
                mSearchView.clearFocus();
//                mGetGitHubData = new LoadJobs(searchTerm, locationLat, locationLong);
//                mGetGitHubData.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLocationChanged(Location location) {
        // Only update location variables and load new job data if the application has GPS permissions
        // and the location has not been set. If updating is necessary, only do it once in the application's
        // lifecycle (remove updates from the location manager). Location will not be updated again
        // because location data is saved in a saved instance state and reloaded upon return to this activity.
        if(locationLat == GetGitHubData.NO_GPS_DATA && location!=null && isGPSPermission) {
            mLocationManager.removeUpdates(this);
            locationLong = location.getLongitude();
            locationLat = location.getLatitude();
            mGetGitHubData = new LoadJobs(searchTerm, locationLat, locationLong);
            mGetGitHubData.execute();
            Log.d(TAG, "Location update: " + locationLat + " " + locationLong);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderDisabled(String provider) {}

    public String checkForSharedPref(String key) {
        // Check for a preference with the specified key, return empty string if not found
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getString(key, "");
    }

    public class LoadJobs extends GetGitHubData {

        public LoadJobs(String searchTerm, double locationLat, double locationLong) {
            super(searchTerm, locationLat, locationLong);
            Log.d(TAG, "new LoadJobs Created");
        }

        @Override
        public void execute() {
            super.execute();

            // Download new job data, let the user know this is occuring with a toast.
            Toast.makeText(MainActivity.this,"Loading Results...",Toast.LENGTH_LONG).show();
            DownloadJobData mDownloadJobData = new DownloadJobData();
            mDownloadJobData.execute();
        }

        public class DownloadJobData extends DownloadGitHubData {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                // Get the Job List loaded from GitHub Jobs and update the JobViewAdapter with the
                // new jobs. Display a toast if no jobs were found using the most recent query.
                mJobList = getJobs();
                mJobViewAdapter.updateJobs(getJobs());
                if (mJobList.size() < 1) {
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
