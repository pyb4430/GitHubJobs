package example.org.githubjobs;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taylor on 12/7/2015.
 * Constructs a URI using location data and a search query according to the GitHub Jobs api.
 * Uses this URI to downloads a raw JSON String asynchronously from GitHub Jobs, parses the JSON,
 * and obtains a List of Job objects.
 */
public class GetGitHubData {

    static final String TAG = GetGitHubData.class.getSimpleName();
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
    private Gson mGson;
    private List<Job> mJobs;

    public GetGitHubData(String searchTerm, double locationLat, double locationLong) {
        this.searchTerm = searchTerm;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        createUri();
        mGson = new Gson();
    }

    public void createUri() {
        Log.d(TAG, "Lat and Long: " + locationLat + " " + locationLong);

        // Build the URI that will be requested from GitHub Jobs. If there is GPS data, use it,
        // if there is no GPS data, default to a search location of San Francisco, CA
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

    public void execute() {
        // Create a DownloadGitHubData variable and execute it to download a string of data from
        // GitHub Jobs asynchronously
        DownloadGitHubData downloadGitHubData = new DownloadGitHubData();
        downloadGitHubData.execute(mUri);
    }

    public class DownloadGitHubData extends AsyncTask<Uri, Void, String> {

        @Override
        protected String doInBackground(Uri... params) {

            // Asynchronously retrieve and parse JSON from GitHub Jobs
            HttpURLConnection gitHubHttpConnection = null;
            JsonReader jsonReader = null;
            JsonParser streamParser = null;
            try {
                URL mUrl = new URL(mUri.toString());

                Log.d(TAG, mUrl.toString());

                gitHubHttpConnection = (HttpURLConnection) mUrl.openConnection();
                gitHubHttpConnection.setRequestMethod("GET");
                gitHubHttpConnection.connect();
                InputStream inputStream = gitHubHttpConnection.getInputStream();
                if(inputStream == null) {
                    return null;
                }

                jsonReader = new JsonReader(new InputStreamReader(inputStream));
                streamParser = new JsonParser();
                JsonElement jsonElement = streamParser.parse(jsonReader);
                Type listType = new TypeToken<ArrayList<Job>>() {}.getType();
                mJobs = mGson.fromJson(jsonElement, listType);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Malformed URL: " + e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, "Url connection not opened: " + e.getMessage());
            } finally {
                if(gitHubHttpConnection != null) {
                    gitHubHttpConnection.disconnect();;
                }

                if(jsonReader != null) {
                    try {
                        jsonReader.close();
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
