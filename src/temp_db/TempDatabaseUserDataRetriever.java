package temp_db;

import java.util.ArrayList;
import java.util.List;

import data_retrievers.IUserDataRetriever;
import user.User;
import util.AdditionalDataConverter;

public class TempDatabaseUserDataRetriever implements IUserDataRetriever{

	private List<User> listOfUsers = new ArrayList<>();

	public TempDatabaseUserDataRetriever() {

	}

	@Override
	public User getUser(String userId) throws Exception {
		System.out.println("all users: "+listOfUsers);
		for(User user : listOfUsers) {
			if(user.getUserIdentification().equals(userId)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public boolean updateUserEyeLevel(String userId, int eyeLevel) throws Exception {
		for(User user : listOfUsers) {
			if(user.getUserIdentification().equals(userId)) {
				user.setCurrentAggroLevel(eyeLevel);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateUserAdditionalData(String userId, String additionalData) throws Exception {
		for(User user : listOfUsers) {
			if(user.getUserIdentification().equals(userId)) {
				user.setAdditionalData(AdditionalDataConverter.stringToMap(additionalData));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addUser(String userId, int eyeLevel, String additionalData) throws Exception {
		System.out.println("Any add users or ");
		User user = new User(userId, eyeLevel, additionalData);
		listOfUsers.add(user);
		return true;
	}

	@Override
	public boolean userExists(String userId) throws Exception {
		for(User user : listOfUsers) {
			if(user.getUserIdentification().equals(userId)) {
				return true;
			}
		}
		return false;
	}

}
