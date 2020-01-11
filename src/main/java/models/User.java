package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    private int userId;
    private String userName;
    private List<Race> userRaces = new ArrayList<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_race",
            joinColumns = @JoinColumn(name = "fok_user_id"),
            inverseJoinColumns = @JoinColumn(name = "fok_race_id"))
    public List<Race> getUserRaces(){
        return userRaces;
    }
    public void setUserRaces(List<Race> userRaces){
        this.userRaces = userRaces;
    }
    public void addRaceToUser(Race race){
        userRaces.add(race);
    }


    public User() {
    }
    public User(String user_name) {
        this.userName = user_name;
    }

    @Override
    public String toString(){
        return  "user_name: " + userName;
    }
}
