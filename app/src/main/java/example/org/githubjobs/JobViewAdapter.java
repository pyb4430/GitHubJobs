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
        Job job = mJobList.get(position);

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
        View jobView = LayoutInflater.from(mContext).inflate(R.layout.job_browse_view, parent, false);
        return new JobViewHolder(jobView);
    }

    public void updateJobs(List<Job> newJobs) {
        mJobList = newJobs;
        notifyDataSetChanged();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        public ImageView jobLogo;
        public TextView companyName;
        public TextView jobTitle;

        public JobViewHolder(View v) {
            super(v);
            jobLogo = (ImageView) v.findViewById(R.id.jobLogo);
            companyName = (TextView) v.findViewById(R.id.companyName);
            jobTitle = (TextView) v.findViewById(R.id.jobTitle);
        }

    }


}
