package data_retrievers;

import user.User;

public interface IUserDataRetriever {

	public User getUser(String userId);
	
	public boolean updateUser(String userId)
}
