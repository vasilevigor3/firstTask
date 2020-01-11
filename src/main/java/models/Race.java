package models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "races")
public class Race {
    private int raceId;
    private LocalDate dateOfRace;
    private List<User> listOfUsersOfOneRace = new ArrayList<>();



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "race_id")
    public int getRaceId() {
        return raceId;
    }
    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    @Column(name = "date_of_race")
    public LocalDate getDateOfRace() {
        return dateOfRace;
    }
    public void setDateOfRace(LocalDate dateOfRace) {
        this.dateOfRace = dateOfRace;
    }


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_race",
            joinColumns = @JoinColumn(name = "fok_race_id"),
            inverseJoinColumns = @JoinColumn(name = "fok_user_id"))
    public List<User> getListOfUsersOfOneRace(){
        return listOfUsersOfOneRace;
    }
    public void setListOfUsersOfOneRace(List<User> listOfUsersOfOneRace) {
        this.listOfUsersOfOneRace = listOfUsersOfOneRace;
    }
    public void addUserToTheRace(User user){
        listOfUsersOfOneRace.add(user);
    }

    public Race() {
    }
    public Race(LocalDate dateOfRace) {
        this.dateOfRace = dateOfRace;
    }
    @Override
    public String toString(){
        return  dateOfRace.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Race)) return false;
        Race race = (Race) o;
        return Objects.equals(getDateOfRace(), race.getDateOfRace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateOfRace(), getListOfUsersOfOneRace());
    }
}

