package DataAccess;

import java.sql.*;

public class DataBase {
    private Connection personConnection = null;
    private Connection eventConnection = null;
    private Connection userConnection = null;
    private Connection authConnection = null;

    /**
     *
     * @return
     * @throws DataAccessException
     */
    public Connection getPersonConnection() throws DataAccessException {
        if(this.personConnection == null) {
            openPersonConnection();
        }
        return personConnection;
    }

    /**
     *
     * @return
     * @throws DataAccessException
     */
    public Connection getEventConnection() throws DataAccessException {
        if(this.eventConnection == null) {
            openEventConnection();
        }
        return eventConnection;
    }

    /**
     *
     * @return
     * @throws DataAccessException
     */
    public Connection getUserConnection() throws DataAccessException {
        if(this.userConnection == null) {
            openUserConnection();
        }
        return userConnection;
    }

    /**
     *
     * @return
     * @throws DataAccessException
     */
    public Connection getAuthConnection() throws DataAccessException {
        if(this.authConnection == null) {
            openAuthConnection();
        }
        return authConnection;
    }

    /**
     *
     * @throws DataAccessException
     */
    public void openPersonConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";
        if(this.personConnection != null) {
            return;
        }
        try {
            this.personConnection = DriverManager.getConnection(url);
            this.personConnection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to persons database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public  void openEventConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";

        try {
            this.eventConnection = DriverManager.getConnection(url);
            this.eventConnection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to Events database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void openUserConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";

        try {
            this.userConnection = DriverManager.getConnection(url);
            this.userConnection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to user database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void openAuthConnection() throws DataAccessException {
        final String url = "jdbc:sqlite:db.db";

        try {
            this.authConnection = DriverManager.getConnection(url);
            this.authConnection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to auth database");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void closeAllConnections() throws DataAccessException {
        closePersonConnection(false);
        closeAuthConnection(false);
        closeEventConnection(false);
        closeUserConnection(false);
    }

    /**
     *
     * @param commit
     * @throws DataAccessException
     */
    public void closePersonConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                this.personConnection.commit();
            } else {
                this.personConnection.rollback();
            }
            this.personConnection.close();
            this.personConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     *
     * @param commit
     * @throws DataAccessException
     */
    public void closeEventConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                this.eventConnection.commit();
            } else {
                this.eventConnection.rollback();
            }
            this.eventConnection.close();
            this.eventConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     *
     * @param commit
     * @throws DataAccessException
     */
    public void closeAuthConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                this.authConnection.commit();
            } else {
                this.authConnection.rollback();
            }
            this.authConnection.close();
            this.authConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     *
     * @param commit
     * @throws DataAccessException
     */
    public void closeUserConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                this.userConnection.commit();
            } else {
                this.userConnection.rollback();
            }
            this.userConnection.close();
            this.userConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * Attempts to close all database connections.
     * @throws DataAccessException
     */
    public void clearTables() throws DataAccessException {
       clearPersonsTable();
       clearAuthTables();
       clearEventsTables();
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearPersonsTable() throws DataAccessException {
        if(this.userConnection == null) {
            openUserConnection();
        }
        try (Statement stmt = this.personConnection.createStatement()){
            String sql = "DELETE FROM persons;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing persons tables");
        }
    }

    /**
     *
     * @throws DataAccessException
     */
    public void clearEventsTables() throws DataAccessException {
        try (Statement stmt = eventConnection.createStatement()){
            String sql = "DELETE FROM events;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing events tables");
        }
    }



    /**
     *
     * @throws DataAccessException
     */
    public void clearAuthTables() throws DataAccessException {
        try (Statement stmt = authConnection.createStatement()){
            String sql = "DELETE FROM authToken;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing authToken tables");
        }
    }

    /**
     *
     * @throws SQLException
     */
    //When database is created, it goes through and creates its own pre filled database.
    public DataBase() throws SQLException {
    }

    public void fillDB() throws DataAccessException, SQLException {
        if(this.personConnection == null) {
            openPersonConnection();
        }
        if(this.authConnection == null) {
            openAuthConnection();
        }
        if(this.userConnection == null) {
            openUserConnection();
        }
        if(this.eventConnection == null) {
            openEventConnection();
        }
        String sql = "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000064', 'cklimontovich0', 'Caterina', 'Klimontovich', 'F', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000065', 'pschubart1', 'Packston', 'Schubart', 'M', '5e5059d6fc13ae3226000066', '5e5059d6fc13ae3226000067', '5e5059d6fc13ae3226000068');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000069', 'ehandling2', 'Ellwood', 'Handling', 'M', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae322600006a', 'dfancett3', 'Darb', 'Fancett', 'M', '5e5059d6fc13ae322600006b', '5e5059d6fc13ae322600006c', '5e5059d6fc13ae322600006d');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae322600006e', 'dolivier4', 'Denney', 'Olivier', 'M', '5e5059d6fc13ae322600006f', '5e5059d6fc13ae3226000070', '5e5059d6fc13ae3226000071');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000072', 'lcasaletto5', 'Leonelle', 'Casaletto', 'F', '5e5059d6fc13ae3226000073', '5e5059d6fc13ae3226000074', '5e5059d6fc13ae3226000075');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000076', 'asutch6', 'Agnella', 'Sutch', 'F', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000077', 'dlismer7', 'Denni', 'Lismer', 'F', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000078', 'dcruz8', 'Dode', 'Cruz', 'F', '5e5059d6fc13ae3226000079', '5e5059d6fc13ae322600007a', '5e5059d6fc13ae322600007b');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae322600007c', 'srizzotto9', 'Starlin', 'Rizzotto', 'F', '5e5059d6fc13ae322600007d', '5e5059d6fc13ae322600007e', '5e5059d6fc13ae322600007f');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000080', 'kwalbridgea', 'Kyrstin', 'Walbridge', 'F', '5e5059d6fc13ae3226000081', '5e5059d6fc13ae3226000082', '5e5059d6fc13ae3226000083');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000084', 'kpanchenb', 'Karoly', 'Panchen', 'M', '5e5059d6fc13ae3226000085', '5e5059d6fc13ae3226000086', '5e5059d6fc13ae3226000087');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000088', 'wmacalessc', 'Wilbur', 'MacAless', 'M', '5e5059d6fc13ae3226000089', '5e5059d6fc13ae322600008a', '5e5059d6fc13ae322600008b');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae322600008c', 'kbrayshawd', 'Kellby', 'Brayshaw', 'M', '5e5059d6fc13ae322600008d', '5e5059d6fc13ae322600008e', '5e5059d6fc13ae322600008f');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000090', 'ljosowitze', 'Lanae', 'Josowitz', 'F', '5e5059d6fc13ae3226000091', '5e5059d6fc13ae3226000092', '5e5059d6fc13ae3226000093');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000094', 'shaskellf', 'Silas', 'Haskell', 'M', '5e5059d6fc13ae3226000095', '5e5059d6fc13ae3226000096', '5e5059d6fc13ae3226000097');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae3226000098', 'ocraigheidg', 'Otis', 'Craigheid', 'M', '5e5059d6fc13ae3226000099', '5e5059d6fc13ae322600009a', '5e5059d6fc13ae322600009b');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae322600009c', 'vbillingsleyh', 'Venita', 'Billingsley', 'F', '5e5059d6fc13ae322600009d', '5e5059d6fc13ae322600009e', '5e5059d6fc13ae322600009f');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000a0', 'lcridgei', 'Lonni', 'Cridge', 'F', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000a1', 'ddobbinj', 'Daron', 'Dobbin', 'M', '5e5059d6fc13ae32260000a2', '5e5059d6fc13ae32260000a3', '5e5059d6fc13ae32260000a4');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000a5', 'rakidk', 'Ramon', 'Akid', 'M', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000a6', 'kcornhilll', 'Kingston', 'Cornhill', 'M', '5e5059d6fc13ae32260000a7', '5e5059d6fc13ae32260000a8', '5e5059d6fc13ae32260000a9');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000aa', 'wbradbornem', 'Will', 'Bradborne', 'M', '5e5059d6fc13ae32260000ab', '5e5059d6fc13ae32260000ac', '5e5059d6fc13ae32260000ad');\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000ae', 'ajankiewiczn', 'Augustin', 'Jankiewicz', 'M', null, null, null);\n" +
                "insert into persons (Person_ID, Username, First_Name, Last_Name, gender, Father_ID, Mother_ID, Spouse_ID) values ('5e5059d6fc13ae32260000af', 'rwithamso', 'Ricki', 'Withams', 'M', '5e5059d6fc13ae32260000b0', '5e5059d6fc13ae32260000b1', '5e5059d6fc13ae32260000b2');";
        PreparedStatement stmt = null;
        try {
            stmt = this.personConnection.prepareStatement(sql);
        } finally {
            if(stmt != null)
                stmt.close();
        }

        sql = "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000032', 'dcostellow0', '5e505b89fc13ae1c4d000033', 38.9693412, -9.1822048, 'Portugal', 'Sapataria', 'Pink', 2010);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000034', 'amelrose1', '5e505b89fc13ae1c4d000035', -7.1562056, -34.9313665, null, 'Bayeux', 'Violet', 1987);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000036', 'cdoctor2', '5e505b89fc13ae1c4d000037', 39.4242713, -8.6985716, null, 'Malhou', 'Khaki', 1992);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000038', 'bparade3', '5e505b89fc13ae1c4d000039', 29.432849, 110.923502, 'China', 'Gaofeng', 'Khaki', 2008);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00003a', 'igeelan4', '5e505b89fc13ae1c4d00003b', 56.504578, 13.0422348, 'Sweden', 'Laholm', 'Orange', 2002);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00003c', 'jallkins5', '5e505b89fc13ae1c4d00003d', 42.059759, 86.574067, 'China', 'Yanqi', 'Pink', 2004);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00003e', 'umacdiarmid6', '5e505b89fc13ae1c4d00003f', -17.5952979, -65.9393064, 'Bolivia', 'Cliza', 'Turquoise', 1994);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000040', 'kgarment7', '5e505b89fc13ae1c4d000041', 55.9129296, 13.1018168, 'Sweden', 'Svalöv', 'Yellow', 2005);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000042', 'hragge8', '5e505b89fc13ae1c4d000043', -17.4652462, 49.2005539, 'Madagascar', 'Vavatenina', 'Puce', 2005);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000044', 'tcaldow9', '5e505b89fc13ae1c4d000045', 28.214419, 121.293705, 'China', 'Chumen', 'Aquamarine', 2011);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000046', 'dachrameeva', '5e505b89fc13ae1c4d000047', 49.8691189, 15.5800287, 'Czech Republic', 'Třemošnice', 'Purple', 1994);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000048', 'nhardakerb', '5e505b89fc13ae1c4d000049', 39.6003316, 20.3077691, 'Greece', 'Filiátes', 'Goldenrod', 2004);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00004a', 'emcnirlinc', '5e505b89fc13ae1c4d00004b', 42.1119268, 44.9672863, 'Georgia', 'T’ianet’i', 'Green', 2002);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00004c', 'ksaxelbyd', '5e505b89fc13ae1c4d00004d', -10.6235959, 123.409084, 'Indonesia', 'Batuidu', 'Khaki', 2011);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00004e', 'fdewdneye', 5e505b89fc13ae1c4d00004f, 15.3749395, -86.6850292, null, 'Nombre de Jesús', 'Khaki', 2007);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000050', 'yjohnseyf', '5e505b89fc13ae1c4d000051', 22.5364306, -80.9055645, 'Cuba', 'Calimete', 'Maroon', 1999);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000052', 'astendallg', '5e505b89fc13ae1c4d000053', -13.3108167, -75.4102538, 'Peru', 'Mollepampa', 'Goldenrod', 2007);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000054', 'pburrassh', '5e505b89fc13ae1c4d000055', 48.0999765, 29.124263, 'Ukraine', 'Kodyma', 'Violet', 1965);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000056', 'dspratlingi', '5e505b89fc13ae1c4d000057', 21.6837923, 104.4551361, null, 'Yên Bái', 'Mauv', 2005);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000058', 'ctuckj', '5e505b89fc13ae1c4d000059', -8.6433591, 116.4960979, 'Indonesia', 'Sukamulia', 'Khaki', 2003);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00005a', 'afrowdk', '5e505b89fc13ae1c4d00005b', 57.7298725, 13.1015839, null, 'Borås', 'Fuscia', 2003);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00005c', 'kdranl', '5e505b89fc13ae1c4d00005d', -33.2825097, 19.3247866, null, 'Ceres', 'Indigo', 1993);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d00005e', 'bcholwellm', '5e505b89fc13ae1c4d00005f', 18.0335359, -77.856738, 'Jamaica', 'Black River', 'Goldenrod', 1984);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000060', 'abodimeaden', '5e505b89fc13ae1c4d000061', 37.0388867, 9.6647636, 'Tunisia', 'Mateur', 'Goldenrod', 1990);\n" +
                "insert into events (Event_ID, Username, Person_ID, Latitude, Longitude, Country, City, EventType, Year) values ('5e505b89fc13ae1c4d000062', 'jvangoo', '5e505b89fc13ae1c4d000063', '-8.5694', '116.1325', 'Indonesia', 'Sayang Lauq', 'Aquamarine', 1993);";
        stmt = null;
        try {
            stmt = this.eventConnection.prepareStatement(sql);
        } finally {
            if(stmt != null)
                stmt.close();
        }
        sql = "insert into user (Username, Password) values ('lhariot0', '26BHKUqA2y3S');\n" +
                "insert into user (Username, Password) values ('mdrackford1', 'ShvlJm');\n" +
                "insert into user (Username, Password) values ('ncarnie2', '7d8cii2nh4Fc');\n" +
                "insert into user (Username, Password) values ('ahamberston3', 'A2HNGXXZ');\n" +
                "insert into user (Username, Password) values ('mwise4', 'Ul064fSnU8');\n" +
                "insert into user (Username, Password) values ('plundy5', 'cpZgFyDymllt');\n" +
                "insert into user (Username, Password) values ('hgosnell6', 'p8hdtUdpcB');\n" +
                "insert into user (Username, Password) values ('nmcneil7', 'nF3dypLZYc');\n" +
                "insert into user (Username, Password) values ('jcathro8', 'Brl3njGF');\n" +
                "insert into user (Username, Password) values ('bjeanenet9', 'dXz7fGucV');\n" +
                "insert into user (Username, Password) values ('lwhiteara', 'i7PbjeJpQ');\n" +
                "insert into user (Username, Password) values ('gcreaseb', '3XsFJo');\n" +
                "insert into user (Username, Password) values ('nhuttleyc', 'AzuYIPwZJ0w9');\n" +
                "insert into user (Username, Password) values ('jbeeld', 'TrCvXP');\n" +
                "insert into user (Username, Password) values ('ckrole', 'eiU9Ma4Wf');\n" +
                "insert into user (Username, Password) values ('bgroocockf', 'iX0CKllu');\n" +
                "insert into user (Username, Password) values ('mstyantg', 'suC7PRDFCdcA');\n" +
                "insert into user (Username, Password) values ('cglasspooleh', 'diAp5bP5xZU');\n" +
                "insert into user (Username, Password) values ('cslaghti', 'bnac63GEn6lt');\n" +
                "insert into user (Username, Password) values ('bauchinleckj', 'WFBO4eW1D');\n" +
                "insert into user (Username, Password) values ('ebeamsk', '19frlkEXB');\n" +
                "insert into user (Username, Password) values ('rwathanl', 'RGgxCIf83dXA');\n" +
                "insert into user (Username, Password) values ('zebym', 'BXFoMIks0An');\n" +
                "insert into user (Username, Password) values ('bbrislanen', 'V3G6t9uF');\n" +
                "insert into user (Username, Password) values ('bpencheno', 'OdeIZuaz6zPN');";
        stmt = null;
        try {
            stmt = this.userConnection.prepareStatement(sql);
        } finally {
            if(stmt != null)
                stmt.close();
        }
    }
}
