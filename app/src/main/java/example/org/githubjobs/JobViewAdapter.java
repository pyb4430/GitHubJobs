package example.org.githubjobs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Taylor on 12/7/2015.
 * A RecyclerView of view representations of Job objects
 */
public class JobViewAdapter extends RecyclerView.Adapter<JobViewAdapter.JobViewHolder> {

    static final String TAG = JobViewAdapter.class.getSimpleName();

    private List<Job> mJobList;
    private Context mContext;

    public JobViewAdapter(List<Job> jobList, Context context) {
        mContext = context;
        mJobList = jobList;
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        // Get the appropriate Job object from the List of Jobs
        Job job = mJobList.get(position);

        // Set the position variable of the JobViewHolder so that when the corresponding child view
        // is clicked the correct corresponding job is associated with it
        holder.setPosition(position);

        // Draw the company logo in the JobViewHolder ImageView such that it fits in the view while
        // preserving aspect ratio. Set the company name and job title in the JobViewHolder
        String logoUrl = job.getLogoURL();
        if(logoUrl != null && Patterns.WEB_URL.matcher(logoUrl).matches()) {
            Picasso.with(mContext).load(job.getLogoURL()).placeholder(R.drawable.placeholder).error(R.drawable.nologo).fit().centerInside().into(holder.jobLogo);
        } else {
            Picasso.with(mContext).load(R.drawable.nologo).fit().centerInside().into(holder.jobLogo);
        }

        String companyName = job.getCompanyName();
        if(companyName != null && companyName.length() > 0) {
            holder.companyName.setText(job.getCompanyName());
        } else {
            Log.d(TAG, "company name: " + companyName);
            holder.companyName.setText("No Company Name");
        }

        String jobTitle = job.getJobTitle();
        if(jobTitle != null && jobTitle.length() > 0) {
            holder.jobTitle.setText(jobTitle);
        } else {
            holder.jobTitle.setText("No Job Title");
        }
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "items: " + mJobList);
        return null!=mJobList ? mJobList.size() : 0;
    }

    @Override
    public JobViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the job_browse_view layout file and create a new JobViewHolder
        View jobView = LayoutInflater.from(mContext).inflate(R.layout.job_browse_view, parent, false);
        return new JobViewHolder(jobView);
    }

    public void updateJobs(List<Job> newJobs) {
        // Update the List of jobs that this RecyclerView is displaying and refresh the child views
        // to reflect the change
        mJobList = newJobs;
        notifyDataSetChanged();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        public ImageView jobLogo;
        public TextView companyName;
        public TextView jobTitle;
        public int position;

        public JobViewHolder(View v) {
            super(v);

            // Retrieve the views from the view that this JobViewHolder is holding. Set an onClickListener
            // for the view so that when the user clicks on the view in the RecyclerView a job details
            // screen opens. Pass the information on the job through extras in the intent.
            jobLogo = (ImageView) v.findViewById(R.id.jobLogo);
            companyName = (TextView) v.findViewById(R.id.companyName);
            jobTitle = (TextView) v.findViewById(R.id.jobTitle);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, v.toString());
                    Job selectedJob = mJobList.get(position);
                    Log.d(TAG, "item selected: " + position);
                    Intent jobDetailsIntent = new Intent(mContext, JobDetailsActivity.class);
                    jobDetailsIntent.putExtra(Job.JOB_TITLE, selectedJob.getJobTitle());
                    jobDetailsIntent.putExtra(Job.COMPANY_NAME, selectedJob.getCompanyName());
                    jobDetailsIntent.putExtra(Job.JOB_DESCRIPTION, selectedJob.getJobDescription());
                    jobDetailsIntent.putExtra(Job.JOB_COMPANY_URL, selectedJob.getJobCompanyUrl());
                    mContext.startActivity(jobDetailsIntent);
                }
            });
        }

        // Set the position variable to a position of this JobViewHolder's child view within the RecyclerView
        // (also the position within the List of Jobs).
        public void setPosition(int position) {
            this.position = position;
            Log.d(TAG, "position set");
        }


    }
}
