package example.org.githubjobs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

        // Draw the company logo in the JobViewHolder ImageView such that it fits in the view while
        // preserving aspect ratio. Set the company name and job title in the JobViewHolder
        Picasso.with(mContext).load(job.getLogoURL()).placeholder(R.drawable.placeholder).error(R.drawable.nologo).fit().centerInside().into(holder.jobLogo);
        holder.companyName.setText(job.getCompanyName());
        holder.jobTitle.setText(job.getJobTitle());
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

        public JobViewHolder(View v) {
            super(v);

            // Retrieve the views from the view that this JobViewHolder is holding
            jobLogo = (ImageView) v.findViewById(R.id.jobLogo);
            companyName = (TextView) v.findViewById(R.id.companyName);
            jobTitle = (TextView) v.findViewById(R.id.jobTitle);
        }

    }


}
