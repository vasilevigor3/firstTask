package models;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Race.class)
public abstract class Race_ {

	public static volatile SingularAttribute<Race, Integer> raceId;
	public static volatile SingularAttribute<Race, LocalDate> dateOfRace;
	public static volatile ListAttribute<Race, User> listOfUsersOfOneRace;

	public static final String RACE_ID = "raceId";
	public static final String DATE_OF_RACE = "dateOfRace";
	public static final String LIST_OF_USERS_OF_ONE_RACE = "listOfUsersOfOneRace";

}

