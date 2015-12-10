package example.org.githubjobs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class JobDetailsActivity extends AppCompatActivity {

    static final String TAG = JobDetailsActivity.class.getSimpleName();

    String jobTitle;
    String jobDescription;
    String companyName;
    String companyUrl;
    TextView jobTitleView;
    TextView jobDescriptionView;
    TextView companyNameView;
    TextView companyUrlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the views to hold the job information
        jobTitleView = (TextView) findViewById(R.id.detailJobTitle);
        jobDescriptionView = (TextView) findViewById(R.id.detailJobDescription);
        companyNameView = (TextView) findViewById(R.id.detailCompanyView);
        companyUrlView = (TextView) findViewById(R.id.detailCompanyUrlView);


        // Retrieve the job information from the extras passed with the intent that started this
        // activity.
        Intent intent = getIntent();
        jobTitle = intent.getStringExtra(Job.JOB_TITLE);
        jobDescription = intent.getStringExtra(Job.JOB_DESCRIPTION);
        companyName = intent.getStringExtra(Job.COMPANY_NAME);
        companyUrl = intent.getStringExtra(Job.JOB_COMPANY_URL);

        // Set the job information in the views
        jobTitleView.setText(jobTitle);
        jobDescriptionView.setText(Html.fromHtml(jobDescription));
        companyNameView.setText(companyName);
        companyUrlView.setText(companyUrl);

        companyUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(companyUrl.length() > 0) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(companyUrl));
                    startActivity(i);
                }

            }
        });

    }

}
