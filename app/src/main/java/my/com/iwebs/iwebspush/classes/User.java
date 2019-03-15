package my.com.iwebs.iwebspush.classes;

public class User {

    private String name, email, domain, secretKey, logoURL;
    private int id;

    public User(int id, String name, String email, String domain, String secretKey, String logoURL) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.domain = domain;
        this.secretKey = secretKey;
        this.logoURL = logoURL;
    }

    public User() {}

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDomain() { return domain; }

    public String getSecretKey() { return secretKey; }

    public String getLogoURL() { return logoURL; }

}
