package users;

public class User {

    private String username;
    private String password;
    private String degree;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.degree = "";
    }

    public User(String username, String password, String degree) {
        this.username = username;
        this.password = password;
        this.degree = degree;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public String getDegree() {
        return this.degree;
    }
}
