package extensions;

import java.util.ArrayList;
import java.util.List;

import data_retrievers.IUserDataRetriever;
import user.User;
import util.AdditionalDataConverter;

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

	@Override
	public boolean updateUserEyeLevel(String userId, int eyeLevel) throws Exception {
		for(User u : users) {
			if(u.getUserIdentification().equals(userId)) {
				u.setCurrentAggroLevel(eyeLevel);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateUserAdditionalData(String userId, String additionalData) throws Exception {
		for(User u : users) {
			if(u.getUserIdentification().equals(userId)) {
				u.setAdditionalData(AdditionalDataConverter.stringToMap(additionalData));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addUser(String userId, int eyeLevel, String additionalData) throws Exception {
		users.add(new User(userId, eyeLevel, additionalData));
		return true;
	}

	@Override
	public boolean userExists(String userId) throws Exception {
		for(User u : users) {
			if(u.getUserIdentification().equals(userId)) {
				return true;
			}
		}
		return false;
	}

}
