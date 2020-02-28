package Service;

import DAO.AuthTokenAO;
import DAO.UserAO;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Model.AuthToken;
import Model.User;
import RequestResult.LoginRequest;
import RequestResult.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest r)  {
        DataBase db = new DataBase();
        UserAO ua = new UserAO();
        AuthTokenAO at = new AuthTokenAO();


        LoginResponse lp = new LoginResponse();
        try {
            User u = ua.getUser(db.getUserConnection(), r.getUserName());
            AuthToken token = new AuthToken(u.getUserName());
            lp.setSuccess(true);
            lp.setPersonID(u.getPersonID());
            lp.setUserName(u.getUserName());
            lp.setAuthToken(token.getTokenID());
        } catch (DataAccessException e) {
            lp.setSuccess(false);
            lp.setMessage(e.getMessage());
        }
        return lp;
    }
}
