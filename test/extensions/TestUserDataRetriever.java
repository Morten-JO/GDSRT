package extensions;

import java.util.ArrayList;
import java.util.List;

import data_retrievers.IUserDataRetriever;
import user.User;

public class TestUserDataRetriever implements IUserDataRetriever{

	public List<User> users = new ArrayList<>();
	
	@Override
	public User getUser(String userId) {
		for(User u : users) {
			if(u.getUserIdentification().equals(userId)) {
				return u;
			}
		}
		return null;
	}

}
