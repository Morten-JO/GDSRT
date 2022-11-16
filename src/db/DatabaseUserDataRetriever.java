package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data_retrievers.IUserDataRetriever;
import user.User;

public class DatabaseUserDataRetriever implements IUserDataRetriever{

	private final String tableName;
	
	private DatabaseController dbController;
	
	public DatabaseUserDataRetriever(DatabaseController dbController) {
		this.dbController = dbController;
		tableName = "users";
	}
	
	@Override
	public User getUser(String userId) throws SQLException {
		PreparedStatement statement = dbController.prepareSafeStatement("select * from " + dbController.getDatabaseName() + "."+tableName+" where ITEM_ID = ?");
		statement.setString(1, userId);
		ResultSet userData = statement.executeQuery();
		if(userData.next()) {
			User user = new User(userData.getString(1), userData.getInt(2), userData.getString(2));
			return user;
		}
		return null;
	}

	@Override
	public boolean updateUserEyeLevel(String userId, int eyeLevel) throws SQLException {
		if(userExists(userId)) {
			PreparedStatement statementUpdate = dbController.prepareSafeStatement("update " + dbController.getDatabaseName() + ".users set EYE_LEVEL = ? where USER_ID = ?");
			statementUpdate.setInt(1, eyeLevel);
			statementUpdate.setString(2, userId);
			int res = statementUpdate.executeUpdate();
			if(res != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateUserAdditionalData(String userId, String additionalData) throws SQLException {
		if(userExists(userId)) {
			PreparedStatement statementUpdate = dbController.prepareSafeStatement("update " + dbController.getDatabaseName() + ".users set ADD_DATA = ? where USER_ID = ?");
			statementUpdate.setString(1, additionalData);
			statementUpdate.setString(2, userId);
			int res = statementUpdate.executeUpdate();
			if(res != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addUser(String userId, int eyeLevel, String additionalData) throws SQLException {
		if(userExists(userId)) {
			return false;
		}
		PreparedStatement statement = dbController.prepareSafeStatement("insert into " + dbController.getDatabaseName() + ".users (USER_ID, EYE_LEVEL, ADD_DATA) values(?,?,?)");
		statement.setString(1, userId);
		statement.setInt(2, eyeLevel);
		statement.setString(3, additionalData);
		dbController.getConnection().setAutoCommit(false);
		statement.executeUpdate();
		dbController.getConnection().commit();
		dbController.getConnection().setAutoCommit(true);
		return true;
	}

	@Override
	public boolean userExists(String userId) throws SQLException {
		PreparedStatement statementCheckPlayerOne;
		statementCheckPlayerOne = dbController.prepareSafeStatement(
				"select * from " + dbController.getDatabaseName() + ".users where USER_ID = ?");
		statementCheckPlayerOne.setString(1, userId);
		ResultSet setCheckPlayerOne = statementCheckPlayerOne.executeQuery();
		if (setCheckPlayerOne.next()) {
			return true;
		}
		return false;
	}

}
