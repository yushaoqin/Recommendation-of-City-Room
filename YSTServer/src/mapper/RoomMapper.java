package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.*;

public interface RoomMapper {

	/*
	 * ROOM
	 */
	public int insertRoom(Room room) throws Exception;

	public Room selectRoomById(int id) throws Exception;

	public List<Room> selectAllRoom() throws Exception;

	public List<Room> selectRoomsByIdList(List<Integer> list) throws Exception;

	public List<Room> selectRoomByKeyWord(@Param("keyword") String keyword)
			throws Exception;

	/*
	 * ROOM IMAGE
	 */

	public List<RoomImage> selectRoomImageByUsername(String username)
			throws Exception;

	public int insertRoomImage(RoomImage roomImage) throws Exception;

	public List<RoomImage> selectRoomImageByRoomId(int roomId) throws Exception;

	public List<RoomImage> selectAllRoomImage() throws Exception;

	/*
	 * ROOM COMMENT
	 */
	public List<RoomComment> selectRoomCommentByKeyWord(
			@Param("keyword") String keyword) throws Exception;

	public List<RoomComment> selectRoomCommentByUsername(String username)
			throws Exception;

	public List<RoomComment> selectRoomCommentByRoomId(int roomId)
			throws Exception;

	public int insertRoomComment(RoomComment roomComment) throws Exception;

	public List<RoomComment> selectAllRoomComment() throws Exception;

	/*
	 * Room Description
	 */
	public int insertRoomDescription(RoomDescription roomDescription)
			throws Exception;

	public List<RoomDescription> selectRoomDescriptionByRoomId(int roomId)
			throws Exception;

	public List<RoomDescription> selectRoomDescriptionByKeyWord(
			@Param("keyword") String keyword) throws Exception;

	public List<RoomDescription> selectAllRoomDescription() throws Exception;

	/*
	 * Room Point
	 */

	public List<RoomPoint> selectRoomPointByRoomId(int roomId) throws Exception;

	public List<RoomPoint> selectRoomPointByUserId(int userId) throws Exception;
	
	public int insertRoomPoint(RoomPoint roomPoint) throws Exception;
	
	public RoomPoint selectRoomPointByUserIdAndRoomId(@Param("userId") int userId,@Param("roomId")int roomId) throws Exception;
}
