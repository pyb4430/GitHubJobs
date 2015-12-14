# GitHubJobs
GitHub Jobs Browser for Android

Fixes made based on feedback include:
- Switched from doing manual JSON parsing to using the GSON library to parse the raw JSON from GitHub Jobs straight into a List of Job objects
- Eliminated the RecyclerItemClickListener class and instead placed an OnClickListener in the JobViewHolder so that each job on screen has an OnClickListener. Added a variable "position" to the JobViewHolder class to store the position of the corresponding job in the List of Jobs so that the JobDetailsActivity can be passed the correct job details upon the click.
- Added checks for nulls in Job parameters in the JobViewAdapter and in the JobDetailsActivity
- Added URL verification for the company URL in the JobDetailsActivity and the logo URL in the JobViewAdapter (to prevent the Picasso library from throwing an exception)
- Fixed the GetGitHubData class filename (from getGitHubData)

Other Fixes:
- Added a layout margin between CardViews in the RecyclerView to create some space.
