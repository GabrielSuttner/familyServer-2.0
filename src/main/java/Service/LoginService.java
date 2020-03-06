package Service;

import DAO.AuthTokenAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Response.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest r)  {
        DataBase db = new DataBase();
        UserAO ua = new UserAO();
        AuthTokenAO at = new AuthTokenAO();
        LoginResponse lp = new LoginResponse();
        try {
            User u = ua.getUser(db.getUserConnection(), r.getUserName());
            String password = r.getPassword();
            if(password.equals(u.getPassword())) {
                try {
                    db.closeUserConnection(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AuthToken token = new AuthToken(u.getUserName());
                at.addToken(db.getAuthConnection(), token);
                try {
                    db.closeAuthConnection(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lp.setSuccess(true);
                lp.setPersonID(u.getPersonID());
                lp.setUserName(u.getUserName());
                lp.setAuthToken(token.getTokenID());
            } else {
                lp.setSuccess(false);
                lp.setMessage("error: Username or Password Incorrect, please try again. ");
            }
        } catch (DataAccessException e) {
            lp.setSuccess(false);
            lp.setMessage("error: Username or Password Incorrect, please try again. ");
            try {
                db.closeUserConnection(false);
                db.closeAuthConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }
        return lp;
    }
}
