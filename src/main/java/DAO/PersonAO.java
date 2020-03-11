package DAO;

import DataAccess.DataAccessException;
import Model.Person;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PersonAO {

    /**
     * add Person to Database
     * @param connection
     * @param person
     * @throws SQLException
     */
    public void addPerson(Connection connection, Person person) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO persons (Person_ID, " +
                    "Username, First_Name, Last_Name, Gender," +
                    "Father_ID, Mother_ID, Spouse_ID) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            if(stmt.executeUpdate() == 1) {
                System.out.println("Added Person: " + person.getFirstName() + " " + person.getLastName());
            } else {
                System.out.println("Failed to add Person: " + person.getPersonID());
            }

        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error adding " +person.getFirstName() +" to Persons database");
        }
        finally
         {
             try {
                 if (stmt != null) {
                     stmt.close();
                 }
             } catch (SQLException e) {
                 e.printStackTrace();
                 throw new DataAccessException("Error closing STMT in addPerson");
             }
        }
    }

    /**
     * Add list of Person to Database.
     * @param connection
     * @param persons
     * @throws DataAccessException
     */
    public void addPersons(Connection connection, List<Person> persons) throws DataAccessException {
        PreparedStatement stmt = null;

        try {
            for (Person person : persons){
                String sql = "INSERT INTO persons (Person_ID, " +
                        "Username, First_Name, Last_Name, Gender," +
                        "Father_ID, Mother_ID, Spouse_ID) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql);

                stmt.setString(1, person.getPersonID());
                stmt.setString(2, person.getUsername());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, person.getGender());
                stmt.setString(6, person.getFatherID());
                stmt.setString(7, person.getMotherID());
                stmt.setString(8, person.getSpouseID());

                if (stmt.executeUpdate() == 1) {
                    System.out.println("Added Person: " + person.getFirstName() + " " + person.getLastName());
                } else {
                    System.out.println("Failed to add Person: " + person.getPersonID());
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error adding  to Persons database in addPersons method.");
        }
        finally
        {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DataAccessException("Error closing STMT in addPerson");
            }
        }
    }
    /**
     * Delete person by given personID
     * @param connection
     * @param personID
     * @throws SQLException
     */
    public void deletePerson(Connection connection, String personID) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE from persons WHERE Person_ID = '" + personID +"';";
            stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
            System.out.printf("Deleted %s.\n", personID);
        } catch(SQLException e) {
            throw new DataAccessException("Failed to delete " + personID);

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Delete all Persons that have given associatedUsername.
     * @param connection
     * @param associatedUsername
     * @throws DataAccessException
     */
    public void deletePersonByAssociated(Connection connection, String associatedUsername) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE from persons WHERE Username = '" + associatedUsername +"';";
            stmt = connection.prepareStatement(sql);

            stmt.executeUpdate();
            System.out.printf("Deleted %s.\n", associatedUsername);
        } catch(SQLException e) {
            throw new DataAccessException("Failed to delete " + associatedUsername);

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Update a person, uses the personID to update.
     * @param connection
     * @param person
     * @throws SQLException
     */
    public void updatePerson(Connection connection, Person person) throws DataAccessException {
        PreparedStatement stmt = null;
        try{
            String sql = "UPDATE persons" +
                    " SET Person_ID = ?, Username = ?, First_Name = ?, Last_Name = ?," +
                    " Gender = ?, Father_ID = ?, Mother_ID = ?, Spouse_ID = ?" +
                    " WHERE Person_ID = '" + person.getPersonID() + "';";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
            System.out.println("Updated Person: " + person.getFirstName() + " " + person.getLastName());

        } catch (SQLException e){
            System.out.println("Failed to update Person: " + person.getPersonID());
            throw new DataAccessException("Error updating " + person.getFirstName());
        } finally {
            if(stmt!= null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get list of All persons in database.
     * @param connection
     * @return
     * @throws SQLException
     */
    public List<Person> getPersons(Connection connection) throws DataAccessException {
        List<Person> people = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Person_ID, Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID FROM persons";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {

                String personId = rs.getString(1);
                String userName = rs.getString(2);
                String fName = rs.getString(3);
                String lName = rs.getString(4);
                String gender = rs.getString(5);
                String dad = rs.getString(6);
                String mom = rs.getString(7);
                String spouse = rs.getString(8);

                people.add(new Person(personId, userName, fName, lName, gender, dad, mom, spouse));
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error retreiving all data from Persons table");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return people;
    }

    /**
     * get Person from database by Person_ID
     * @param connection
     * @param personID
     * @return
     * @throws DataAccessException
     */
    public Person getPerson(Connection connection, String personID) throws DataAccessException {
        Person person = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Person_ID, Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID " +
                    "FROM persons " +
                    "WHERE Person_ID = '" + personID + "';";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            String personId = rs.getString(1);
            String userName = rs.getString(2);
            String fName = rs.getString(3);
            String lName = rs.getString(4);
            String gender = rs.getString(5);
            String dad = rs.getString(6);
            String mom = rs.getString(7);
            String spouse = rs.getString(8);


            person = new Person(personId, userName, fName, lName, gender, dad, mom, spouse);
        } catch(SQLException e ){
            throw new DataAccessException("Error finding Person");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error closing Result Set or PreparedStatement");
            }
        }
        return person;
    }


    /**
     * retreive all persons from database that have associatedUsername.
     * @param connection
     * @param username
     * @return
     * @throws DataAccessException
     */
    public List<Person> getPersonByAssociated(Connection connection, String username) throws DataAccessException {
        List<Person> people = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Person_ID, Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID " +
                    "FROM persons WHERE Username = '" + username+ "';";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()) {

                String personId = rs.getString(1);
                String userName = rs.getString(2);
                String fName = rs.getString(3);
                String lName = rs.getString(4);
                String gender = rs.getString(5);
                String dad = rs.getString(6);
                String mom = rs.getString(7);
                String spouse = rs.getString(8);

                people.add(new Person(personId, userName, fName, lName, gender, dad, mom, spouse));
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error retreiving all data from Persons table");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return people;
    }
}
