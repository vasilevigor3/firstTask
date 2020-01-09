package services;

import dao.DAO;
import dao.RaceDAOImpl;
import models.Race;

import java.util.List;

public class RaceService {

    private DAO <Race> raceDAOImpl = new RaceDAOImpl();

    public RaceService() {
    }

    public Race findRace(int id) {
        return raceDAOImpl.findById(id);
    }

    public void saveRace(Race race) {
        raceDAOImpl.save(race);
    }

    public void deleteRace(Race race) {
        raceDAOImpl.delete(race);
    }

    public void updateRace(Race race) {
        raceDAOImpl.update(race);
    }

    public List<Race> findAllRaces() {
        return raceDAOImpl.getAll();
    }
}
