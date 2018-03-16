package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import tools.DBTools;
import mapper.*;
import model.*;

public class RoomService {
	// 单例
	private RoomService() {
	}

	private static volatile RoomService instance;

	public static RoomService getIstance() {
		if (instance == null) {
			synchronized (RoomService.class) {
				if (instance == null) {
					instance = new RoomService();
				}
			}
		}
		return instance;
	}

	private SqlSession session = DBTools.getSession();
	private RoomMapper mapper = session.getMapper(RoomMapper.class);
	public int insertRoom(Room room) {
		int result = -1;
		
		
		try {
			result = mapper.insertRoom(room);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		System.out.println(result);
		return result;

	}

	public int insertRoomDescription(RoomDescription roomDescription) {
		int result = -1;
		
		
		try {
			result = mapper.insertRoomDescription(roomDescription);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	public List<Room> selectAllRoom() {
		
		
		List<Room> rooms = null;
		try {
			rooms = mapper.selectAllRoom();
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return rooms;
	}

	public List<RoomComment> selectAllRoomComment() {
		
		
		List<RoomComment> roomComments = null;
		try {
			roomComments = mapper.selectAllRoomComment();
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomComments;
	}

	public List<RoomImage> selectAllRoomImage() {
		
		
		List<RoomImage> roomImages = null;
		try {
			roomImages = mapper.selectAllRoomImage();
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomImages;
	}

	public List<RoomDescription> selectAllRoomDescription() {
		
		
		List<RoomDescription> roomDescriptions = null;
		try {
			roomDescriptions = mapper.selectAllRoomDescription();
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomDescriptions;
	}

	public Room selectRoomById(int id) {
		
		
		Room room = null;
		try {
			room = mapper.selectRoomById(id);
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return room;
	}

	public List<RoomImage> selectRoomImageByUsername(String username) {
		
		
		List<RoomImage> roomImages = null;
		try {
			roomImages = mapper.selectRoomImageByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomImages;
	}

	public List<RoomComment> selectRoomCommentByUsername(String username) {
		
		
		List<RoomComment> roomComments = null;
		try {
			roomComments = mapper.selectRoomCommentByUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomComments;
	}

	public List<RoomDescription> selectRoomDescriptionByRoomId(int roomId) {
		
		
		List<RoomDescription> roomDescriptions = null;
		try {
			roomDescriptions = mapper.selectRoomDescriptionByRoomId(roomId);
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomDescriptions;
	}

	public List<RoomComment> selectRoomCommentByRoomId(int roomId) {
		
		
		List<RoomComment> roomComment = null;
		try {
			roomComment = mapper.selectRoomCommentByRoomId(roomId);
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomComment;
	}

	public int insertRoomComment(RoomComment roomComment) {
		int result = -1;
		
		
		try {
			result = mapper.insertRoomComment(roomComment);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	public List<Room> selectRoomsByIdList(List<Integer> idList) {
		
		
		List<Room> rooms = null;
		try {
			rooms = mapper.selectRoomsByIdList(idList);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return rooms;
	}

	public int insertRoomImage(RoomImage roomImage) {
		int result = -1;
		
		
		try {
			result = mapper.insertRoomImage(roomImage);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	public List<RoomImage> selectRoomImageByRoomId(int roomId) {
		
		
		List<RoomImage> roomImages = null;
		try {
			roomImages = mapper.selectRoomImageByRoomId(roomId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomImages;
	}

	public List<Room> selectRoomByKeyWord(String keyword) {
		
		
		List<Room> rooms_by_name = new ArrayList<Room>();
		List<RoomComment> rooms_by_comment = new ArrayList<RoomComment>();
		List<RoomDescription> rooms_by_description = new ArrayList<RoomDescription>();
		Set<Integer> id_set = new HashSet<Integer>();
		List<Room> rooms = new ArrayList<Room>();
		keyword = "%" + keyword + "%";
		System.out.println("Search keyword:" + keyword);
		try {
			rooms_by_name = mapper.selectRoomByKeyWord(keyword);
			rooms_by_comment = mapper.selectRoomCommentByKeyWord(keyword);
			rooms_by_description = mapper
					.selectRoomDescriptionByKeyWord(keyword);
			for (Room room : rooms_by_name) {
				id_set.add(room.getId());
			}

			for (RoomComment rc : rooms_by_comment) {
				id_set.add(rc.getRoomId());
			}

			for (RoomDescription rd : rooms_by_description) {
				id_set.add(rd.getRoomId());
			}
			List<Integer> ids = new ArrayList<Integer>();
			ids.addAll(id_set);
			rooms = mapper.selectRoomsByIdList(ids);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return rooms;
	}

	/*
	 * ROOM POINT
	 */

	public List<RoomPoint> selectRoomPointByRoomId(int roomId) {
		
		
		List<RoomPoint> roomPoints = null;
		try {
			roomPoints = mapper.selectRoomPointByRoomId(roomId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomPoints;
	}

	public List<RoomPoint> selectRoomPointByUserId(int userId) {
		
		
		List<RoomPoint> roomPoints = null;
		try {
			roomPoints = mapper.selectRoomPointByUserId(userId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomPoints;
	}
	
	public int insertRoomPoint(RoomPoint roomPoint,int userId,int roomId){
		int result = -1;
		
		
		try {
			RoomPoint tmp = mapper.selectRoomPointByUserIdAndRoomId(userId, roomId);
			if(tmp != null){
				return -2;
			}
			result = mapper.insertRoomPoint(roomPoint);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}
	
	public RoomPoint selectRoomPointByUserIdAndRoomId(int userId,int roomId) {
		
		
		RoomPoint roomPoint = null;
		try {
			roomPoint = mapper.selectRoomPointByUserIdAndRoomId(userId, roomId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return roomPoint;
	}
	

}
