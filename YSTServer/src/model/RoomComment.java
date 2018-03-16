package model;

public class RoomComment {
	public RoomComment(){}
	public RoomComment(String username,String comment,int roomId){
		this.username = username;
		this.comment = comment;
		this.roomId = roomId;
	}
	private int id;
	private int roomId;
	private String username;
	private String comment;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
