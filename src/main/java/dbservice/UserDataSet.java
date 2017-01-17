package dbservice;

import java.io.Serializable;

import javax.persistence.*;
/**
 * Created by uschsh on 29.11.15.
 */
@NamedQueries({
        @NamedQuery(name = "userByName", query = "from user u where u.username = :username"),
        @NamedQuery(name = "deleteByName", query = "delete user u where u.username = :username")
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


    public UserDataSet() {

    }
    public UserDataSet(String username, String email, String password, long score) {
        this.id         =   -1;
        this.username   =   username;
        this.email      =   email;
        this.password   =   password;
        this.score      =   score;
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

    public void addScore(long _score) {
        this.score += _score;
    }

}
