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

    public String company;
    public String company_logo;
    public String title;
    public String description;
    public String company_url;

    public Job(String companyName, String logoURL, String jobTitle, String jobDescription, String jobCompanyUrl) {

        this.company = companyName;
        this.company_logo = logoURL;
        this.title = jobTitle;
        this.description = jobDescription;
        this.company_url = jobCompanyUrl;
    }

    public String getJobCompanyUrl() {
        return company_url;
    }

    public String getCompanyName() {
        return company;
    }

    public void setCompanyName(String companyName) {
        this.company = companyName;
    }

    public String getLogoURL() {
        return company_logo;
    }

    public void setLogoURL(String logoURL) {
        this.company_logo = logoURL;
    }

    public String getJobTitle() {
        return title;
    }

    public void setJobTitle(String jobTitle) {
        this.title = jobTitle;
    }

    public String getJobDescription() {
        return description;
    }

    public void setJobDescription(String jobDescription) {
        this.description = jobDescription;
    }
}
