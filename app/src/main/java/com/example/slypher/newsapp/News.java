package com.example.slypher.newsapp;

/**
 * {@link News} object
 */

public class News {

    private static final String DATE_SEPARATOR = "T";
    private String title;
    private String sectionName;
    private String publicationDate;
    private String authorName;
    private String webUrl;

    public News(String title, String sectionName, String publicationDate, String firstName, String lastName, String webUrl) {
        this.title = title;
        this.sectionName = sectionName;

        if (!publicationDate.equals("") || publicationDate.isEmpty()){
            String[] splitDate = publicationDate.split(DATE_SEPARATOR);
            this.publicationDate = splitDate[0];
        } else {
            this.publicationDate = "";
        }

        // authorName if present contain "name lastname"
        // if not present contain " " because the json extract return "" into firstName and into LastName
        this.authorName = firstName + " " + lastName;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
