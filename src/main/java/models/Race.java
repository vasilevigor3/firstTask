package models;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "races")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int race_id;

    @Column(name = "date_of_race")
    private java.util.Date date_of_race;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_races",
    joinColumns = @JoinColumn(name = "u_race_id"),
    inverseJoinColumns = @JoinColumn(name = "u_user_id"))


    private List<User> ListOfUsersOfOneRace = new ArrayList<>();
    public List<User> getListOfUsersOfOneRace(){
        return ListOfUsersOfOneRace;
    }
    public void setListOfUsersOfOneRace(ArrayList<User> userList) {
        this.ListOfUsersOfOneRace = userList;
    }
    public void addUserToTheRace(User user){
        ListOfUsersOfOneRace.add(user);
    }

    public Race() {
    }
    public Race(Date date_of_race) {
        this.date_of_race = date_of_race;
    }

    public int getRace_id() {
        return race_id;
    }

    public java.util.Date getDate_of_race() {
        return date_of_race;
    }

    public void setDate_of_race(java.util.Date date_of_race) {
        this.date_of_race = date_of_race;
    }

    @Override
    public String toString(){
        return  "date_of_race: " + date_of_race;
    }

}

