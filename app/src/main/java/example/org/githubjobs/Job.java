package example.org.githubjobs;

/**
 * Created by Taylor on 12/7/2015.
 * A class to hold job information. Holds company name, job title, job description, company url, and the company
 * logo url.
 */
public class Job {

    public static final String COMPANY_NAME = "company_name";
    public static final String JOB_TITLE = "job_title";
    public static final String JOB_DESCRIPTION = "job_description";
    public static final String LOGO_URL = "logo_url";
    public static final String JOB_COMPANY_URL = "company_url";

    public String companyName;
    public String logoURL;
    public String jobTitle;
    public String jobDescription;
    public String jobCompanyUrl;

    public Job(String companyName, String logoURL, String jobTitle, String jobDescription, String jobCompanyUrl) {
        this.companyName = companyName;
        this.logoURL = logoURL;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobCompanyUrl = jobCompanyUrl;
    }

    public String getJobCompanyUrl() {
        return jobCompanyUrl;
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
