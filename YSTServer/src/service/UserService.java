package service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import tools.DBTools;
import mapper.RoomMapper;
import mapper.UserMapper;
import model.*;

public class UserService {
	// 单例
	private UserService() {
	}

	private static volatile UserService instance;

	public static UserService getIstance() {
		if (instance == null) {
			synchronized (RoomService.class) {
				if (instance == null) {
					instance = new UserService();
				}
			}
		}
		return instance;
	}
	private SqlSession session = DBTools.getSession();
	private UserMapper mapper = session.getMapper(UserMapper.class);
	/**
	 * 新增用户
	 */
	public int insertUser(User user) {
		 
		 
		int res = -1;
		try {
			User tmp = mapper.selectUserByUsername(user.getUsername());
			if (tmp != null) {
				return res;
			}
			res = mapper.insertUser(user);
			System.out.println(user.toString());
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}

		return res;
	}

	/**
	 * 根据id查询用户
	 */
	public User selectUserById(int id) {
		 
		 
		User user = null;
		try {
			user = mapper.selectUserById(id);

			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return user;
	}

	/**
	 * 查询所有的用户
	 */
	public List<User> selectAllUser() {
		 
		 
		List<User> users = null;
		try {
			users = mapper.selectAllUser();
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return users;
	}

	public User selectUserByUsernameAndPassword(String username, String password) {
		 
		 
		User user = null;
		try {
			user = mapper.selectUserByUsernameAndPassword(username, password);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return user;

	}

	public int insertUserRoom(UserRoom userRoom) {
		 
		 
		int res = -1;

		try {
			List<UserRoom> tmp = mapper.selectUserRoomByRoomIdAndTypeAndUserId(
					userRoom.getRoomId(), userRoom.getType(),
					userRoom.getUserId());
			System.out.println("SIZE:" + tmp.size());
			if (tmp.size() != 0) {
				res = -2;
				return res;
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}

		try {
			res = mapper.insertUserRoom(userRoom);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}

		return res;
	}

	public List<UserRoom> selectUserRoomByUserIdAndType(int userId, int type) {
		 
		 
		List<UserRoom> userRooms = null;
		try {
			userRooms = mapper.selectUserRoomByUserIdAndType(userId, type);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}

		return userRooms;
	}

	public List<UserRoom> selectUserRoomByUserId(int userId) {
		 
		 
		List<UserRoom> userRooms = null;
		try {
			userRooms = mapper.selectUserRoomByUserId(userId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return userRooms;

	}

	public List<UserRoom> selectUserRoomByType(int type) {
		 
		 
		List<UserRoom> userRooms = null;
		try {
			userRooms = mapper.selectUserRoomByType(type);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return userRooms;

	}

	public List<UserRoom> selectUserRoomByRoomIdAndType(int roomId, int type) {
		 
		 
		List<UserRoom> userRooms = null;
		try {
			userRooms = mapper.selectUserRoomByRoomIdAndType(roomId, type);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return userRooms;

	}

}