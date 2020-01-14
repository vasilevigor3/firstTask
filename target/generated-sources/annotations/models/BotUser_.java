package models;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BotUser.class)
public abstract class BotUser_ {

	public static volatile SingularAttribute<BotUser, String> botUserName;
	public static volatile SingularAttribute<BotUser, String> userState;
	public static volatile SingularAttribute<BotUser, BigInteger> userId;

	public static final String BOT_USER_NAME = "botUserName";
	public static final String USER_STATE = "userState";
	public static final String USER_ID = "userId";

}

