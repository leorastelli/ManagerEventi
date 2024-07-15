package com.managereventi.managereventi.services.Config;

import com.managereventi.managereventi.model.dao.DAOFactory;

import java.util.Calendar;
import java.util.logging.Level;

public class Configuration {

    /* Database Configruation */
    public static final String DAO_IMPL=DAOFactory.MYSQLJDBCIMPL;
    public static final String DATABASE_DRIVER="com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE=Calendar.getInstance().getTimeZone().getID();
    public static final String
           // DATABASE_URL="jdbc:mysql://localhost/ManagerEventi?user=root&password=Leorasta29&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE;
            DATABASE_URL="jdbc:mysql://localhost/ManagerEventi?user=root&password=ANNAbella02&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone="+SERVER_TIMEZONE;

    /* Session Configuration */
    public static final String COOKIE_IMPL=DAOFactory.COOKIEIMPL;

    /* Logger Configuration */
    public static final String GLOBAL_LOGGER_NAME="ManagerEventi";
    //public static final String GLOBAL_LOGGER_FILE="/Users/leonardorastelli/logs/ManagerEventi_log.txt";
    public static final String GLOBAL_LOGGER_FILE="/Users/annaferri/logs/ManagerEventi.txt";
    public static final Level GLOBAL_LOGGER_LEVEL=Level.ALL;

}
