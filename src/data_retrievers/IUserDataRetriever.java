package data_retrievers;

import user.User;

public interface IUserDataRetriever {

	public User getUser(String userId) throws Exception;
	
	public boolean updateUserEyeLevel(String userId, int eyeLevel) throws Exception;
	
	public boolean updateUserAdditionalData(String userId, String additionalData) throws Exception;
	
	public boolean addUser(String userId, int eyeLevel, String additionalData) throws Exception;
	
	public boolean userExists(String userId) throws Exception;
}
