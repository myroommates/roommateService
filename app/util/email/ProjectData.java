package util.email;

/**
 * Created by florian on 13/01/15.
 */
public class ProjectData {

    private String url;

    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProjectData{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
