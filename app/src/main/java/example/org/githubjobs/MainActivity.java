package example.org.githubjobs;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    getGitHubData mGetGitHubData;
    RecyclerView mJobRecyclerView;
    JobViewAdapter mJobViewAdapter;
    private GestureDetectorCompat mGestureDetector;
    List<Job> mJobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext().getPackageName());
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mGetGitHubData = new LoadJobs("PHP", (long) -78, (long) 35);
        mGetGitHubData.execute();

        mJobViewAdapter = new JobViewAdapter(new ArrayList<Job>(), MainActivity.this);

        mJobRecyclerView = (RecyclerView) findViewById(R.id.jobRecyclerView);
        mJobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mJobRecyclerView.setAdapter(mJobViewAdapter);

        mGestureDetector = new GestureDetectorCompat(this, new jobSelectGestureDetector());

        mJobRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                mGestureDetector.onTouchEvent(e);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        this.mGestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LoadJobs extends getGitHubData {

        public LoadJobs(String searchTerm, Long locationLat, Long locationLong) {
            super(searchTerm, locationLat, locationLong);
        }

        @Override
        public void execute() {
            super.execute();
            DownloadJobData mDownloadJobData = new DownloadJobData();
            mDownloadJobData.execute();
        }

        public class DownloadJobData extends DownloadGitHubData {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mJobViewAdapter.updateJobs(getJobs());
            }



        }
    }

    public class jobSelectGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

            View view = mJobRecyclerView.findChildViewUnder(e.getX(),e.getY());
            int position = -1;
            if(view != null) {
                position = mJobRecyclerView.getChildAdapterPosition(view);
            }
            Log.d("h", "position: " + position);
        }
    }
}
