package model;

public class MdBean {

    private String md_title;
    private String md_url;

    public MdBean(String md_title, String md_url) {
        this.md_title = md_title;
        this.md_url = md_url;
    }

    @Override
    public String toString() {
        return "MdBean{" +
                "md_title='" + md_title + '\'' +
                ", md_url='" + md_url + '\'' +
                '}';
    }

    public String getMd_title() {
        return md_title;
    }

    public void setMd_title(String md_title) {
        this.md_title = md_title;
    }

    public String getMd_url() {
        return md_url;
    }

    public void setMd_url(String md_url) {
        this.md_url = md_url;
    }
}