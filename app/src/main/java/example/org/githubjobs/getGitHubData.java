package example.org.githubjobs;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taylor on 12/7/2015.
 */
public class getGitHubData {

    static final String TAG = getGitHubData.class.getSimpleName();
    static final String GITHUB_API_URL = "https://jobs.github.com/positions.json";
    static final String DESCRIPTION_PARAMETER = "description";
    static final String LAT_PARAMETER = "lat";
    static final String LONG_PARAMETER = "lon";
    static final String LOCATION_PARAMETER = "location";
    public static final String DEFAULT_LOCATION = "San Francisco, CA";
    static final double NO_GPS_DATA = 400;
    private String searchTerm;
    private double locationLong;
    private double locationLat;
    private Uri mUri;

    private List<Job> mJobs;

    public getGitHubData(String searchTerm, double locationLat, double locationLong) {
        this.searchTerm = searchTerm;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        createUri();
    }

    public void createUri() {
        Log.d(TAG, "Lat and Long: " + locationLat + " " + locationLong);
        if(locationLat != NO_GPS_DATA && locationLong != NO_GPS_DATA) {
            mUri = Uri.parse(GITHUB_API_URL)
                    .buildUpon()
                    .appendQueryParameter(DESCRIPTION_PARAMETER, null == searchTerm ? "PHP" : searchTerm)
                    .appendQueryParameter(LAT_PARAMETER, String.valueOf(locationLat))
                    .appendQueryParameter(LONG_PARAMETER, String.valueOf(locationLong))
                    .build();
        } else {
            mUri = Uri.parse(GITHUB_API_URL)
                    .buildUpon()
                    .appendQueryParameter(DESCRIPTION_PARAMETER, null == searchTerm ? "PHP" : searchTerm)
                    .appendQueryParameter(LOCATION_PARAMETER, DEFAULT_LOCATION)
                    .build();
        }
        Log.d(TAG, mUri.toString());
    }

    public void parseJSON(String rawJSON) {

        final String JOB_TITLE_OBJECT = "title";
        final String LOGO_URL_OBJECT = "company_logo";
        final String COMPANY_OBJECT = "company";
        final String DESCRIPTION_OBJECT = "description";

        mJobs = new ArrayList<Job>();

        try {
            JSONArray rawJSONArray = new JSONArray(rawJSON);
            for(int i = 0; i<rawJSONArray.length(); i++) {
                JSONObject jobObject = rawJSONArray.getJSONObject(i);
                String jobTitle = jobObject.getString(JOB_TITLE_OBJECT);
                String jobCompany = jobObject.getString(COMPANY_OBJECT);
                String jobDescription = jobObject.getString(DESCRIPTION_OBJECT);
                String jobLogoUrl = jobObject.getString(LOGO_URL_OBJECT);

                mJobs.add(new Job(jobCompany, jobLogoUrl, jobTitle, jobDescription));
            }

//            Log.d(TAG, "Job Array:\n" + mJobs.get(0).getLogoURL());

        } catch (JSONException e) {
            Log.e(TAG, "JSON error: " + e.getMessage());
        }

    }

    public void execute() {
        DownloadGitHubData downloadGitHubData = new DownloadGitHubData();
        downloadGitHubData.execute(mUri);
    }

    public class DownloadGitHubData extends AsyncTask<Uri, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJSON(s);
        }

        @Override
        protected String doInBackground(Uri... params) {

            HttpURLConnection gitHubHttpConnection = null;
            BufferedReader streamReader = null;

            try {
                URL mUrl = new URL(mUri.toString());//params[0].toString());

                Log.d(TAG, mUrl.toString());

                gitHubHttpConnection = (HttpURLConnection) mUrl.openConnection();
                gitHubHttpConnection.setRequestMethod("GET");
                gitHubHttpConnection.connect();
                InputStream inputStream = gitHubHttpConnection.getInputStream();
                if(inputStream == null) {
                    return null;
                }
                streamReader = new BufferedReader(new InputStreamReader(inputStream));

                if(streamReader==null) {
                    return null;
                }

                StringBuffer buffer = new StringBuffer();

                String line;
                while((line = streamReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                Log.d(TAG, "Data read:\n" + buffer.toString());

                return buffer.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "Malformed URL: " + e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, "Url connection not opened: " + e.getMessage());
            } finally {
                if(gitHubHttpConnection != null) {
                    gitHubHttpConnection.disconnect();;
                }

                if(streamReader != null) {
                    try {
                        streamReader.close();
                    } catch( IOException e) {
                        Log.e(TAG, "Reader not closed: " + e.getMessage());
                    }
                }
            }

            return null;
        }
    }

    public List<Job> getJobs() {
        return mJobs;
    }
}
