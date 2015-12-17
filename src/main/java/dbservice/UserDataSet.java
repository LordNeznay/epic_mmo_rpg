package dbservice;

import java.io.Serializable;

import javax.persistence.*;
/**
 * Created by uschsh on 29.11.15.
 */
@NamedQueries({
        @NamedQuery(name = "userByName", query = "from user u where u.username = :username"),
        @NamedQuery(name = "userBySession", query = "from user u where u.session = :session"),
        @NamedQuery(name = "deleteBySession", query = "delete user u where u.session = :session"),
        @NamedQuery(name = "deleteByName", query = "delete user u where u.username = :username"),
        @NamedQuery(name = "getAuthUser", query = "select count(*) from user u where u.session IS NOT NULL")
})

@Entity(name="user")
@Table(name = "user")
public class UserDataSet implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "score")
    private long score;

    @Column(name = "session")
    private String session;

    public UserDataSet() {

    }
    public UserDataSet(String username, String email, String password, long score, String session) {
        this.id         =   -1;
        this.username   =   username;
        this.email      =   email;
        this.password   =   password;
        this.score      =   score;
        this.session    =   session;
    }


    public String getName() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void addScore(long score) {
        this.score += score;
    }

}
