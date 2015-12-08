package example.org.githubjobs;

/**
 * Created by Taylor on 12/7/2015.
 */
public class Job {

    public String companyName;
    public String logoURL;
    public String jobTitle;
    public String jobDescription;

    public Job(String companyName, String logoURL, String jobTitle, String jobDescription) {
        this.companyName = companyName;
        this.logoURL = logoURL;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}