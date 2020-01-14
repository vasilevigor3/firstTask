package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile ListAttribute<User, Race> userRaces;
	public static volatile SingularAttribute<User, String> userName;
	public static volatile SingularAttribute<User, Integer> userId;

	public static final String USER_RACES = "userRaces";
	public static final String USER_NAME = "userName";
	public static final String USER_ID = "userId";

}

