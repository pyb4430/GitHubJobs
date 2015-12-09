package example.org.githubjobs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class JobDetailsActivity extends AppCompatActivity {

    static final String TAG = JobDetailsActivity.class.getSimpleName();

    String jobTitle;
    String jobDescription;
    String companyName;
    TextView jobTitleView;
    TextView jobDescriptionView;
    TextView companyNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jobTitleView = (TextView) findViewById(R.id.detailJobTitle);
        jobDescriptionView = (TextView) findViewById(R.id.detailJobDescription);
        companyNameView = (TextView) findViewById(R.id.detailCompanyView);

        Intent intent = getIntent();

        jobTitle = intent.getStringExtra(Job.JOB_TITLE);
        jobDescription = intent.getStringExtra(Job.JOB_DESCRIPTION);
        companyName = intent.getStringExtra(Job.COMPANY_NAME);

        jobTitleView.setText(jobTitle);
        jobDescriptionView.setText(Html.fromHtml(jobDescription));
        companyNameView.setText(companyName);
    }

}
