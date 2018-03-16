package model;

public class UserRoom {
	 public UserRoom(){};
	 public UserRoom(int userId,int roomId,int type){
		 this.userId = userId;
		 this.roomId = roomId;
		 this.type = type;
	 }
	 
	 private int id;
	 private int userId;
	 private int roomId;
	 private int type; // 1是分享 2是喜欢
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	 
}
