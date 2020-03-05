package Service;

import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Response.ClearResponse;

public class ClearService {
    public ClearResponse clear() {
        ClearResponse response = new ClearResponse();
        DataBase db = new DataBase();

        try {
            db.clearTables();
            response.setMessage("Clear Succeeded.");
            response.setSuccess(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
