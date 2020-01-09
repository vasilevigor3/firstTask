package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(name = "user_name")
    private String user_name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_races",
    joinColumns = @JoinColumn(name = "u_user_id"),
    inverseJoinColumns = @JoinColumn(name = "u_race_id"))


    private List<Race> UserRaces = new ArrayList<>();
    public List<Race> getUserRaces(){
        return UserRaces;
    }
    public void setUserRaces(ArrayList<Race> userRaces){
        this.UserRaces = userRaces;
    }
    public void addRaceToUser(Race race){
        UserRaces.add(race);
    }

//    private Race race;
//    public Race getRace() {
//        return race;
//    }
//    public void setRace(Race race) {
//        this.race = race;
//    }
//    @JoinTable(name = "users_race",
//    joinColumns = @JoinColumn(name = "user_name"),
//    inverseJoinColumns = @JoinColumn(name = "user_name2"))
    public User() {
    }
    public User(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString(){
        return  "user_name: " + user_name;
    }
}
