package models;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "bot_user")
public class BotUser {
    private BigInteger userId;
    private String botUserName;
    private String userState;

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bot_user")
    public BigInteger getUserId() {
        return userId;
    }
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    @Column(name = "bot_user_name")
    public String getBotUserName() {
        return botUserName;
    }
    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    @Column(name = "user_state")
    public String getUserState() {
        return userState;
    }
    public void setUserState(String userState) {
        this.userState = userState;
    }

    public BotUser(){};
    public BotUser(String botUserName, BigInteger userId){this.botUserName = botUserName;
    this.userId=userId;}
    public BotUser(String botUserName, BigInteger userId, String userState){this.botUserName = botUserName;
        this.userId=userId; this.userState=userState;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BotUser)) return false;
        BotUser botUser = (BotUser) o;
        return Objects.equals(getBotUserName(), botUser.getBotUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBotUserName());
    }
    @Override
    public String toString() {
        return "BotUser{" +
                "botUserName='" + botUserName + '\'' +
                ", userState='" + userState + '\'' +
                '}';
    }

}
