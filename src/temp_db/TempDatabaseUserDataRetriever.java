package temp_db;

import data_retrievers.IUserDataRetriever;
import db.DatabaseController;
import user.User;

public class TempDatabaseUserDataRetriever implements IUserDataRetriever{

	public TempDatabaseUserDataRetriever() {
		
	}
	
	@Override
	public User getUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUserEyeLevel(String userId, int eyeLevel) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUserAdditionalData(String userId, String additionalData) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addUser(String userId, int eyeLevel, String additionalData) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean userExists(String userId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
