package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.*;


public interface UserMapper {
    /**
     * 新增用戶
     * @param user
     * @return
     * @throws Exception
     */
    public int insertUser(User user) throws Exception;
    /**
     * 修改用戶
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    public int updateUser (User user,int id) throws Exception;
     /**
      * 刪除用戶
      * @param id
      * @return
      * @throws Exception
      */
    public int deleteUser(int id) throws Exception;
    /**
     * 根据id查询用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public User selectUserById(int id) throws Exception;
    
    public User selectUserByUsername(String username) throws Exception;
    
     /**
      * 查询所有的用户信息
      * @return
      * @throws Exception
      */
    public List<User> selectAllUser() throws Exception;
    
    /**
     * 根据用户名 密码查询用户
     * 
     */
    public User selectUserByUsernameAndPassword(@Param("username")String username, @Param("password")String password) throws Exception;

    // 添加用户相关的空间
    public int insertUserRoom(UserRoom userRoom) throws Exception;
    
    // 根据roomId ，type选择用户空间， 即选择喜欢该空间的用户
    public List<UserRoom> selectUserRoomByRoomIdAndType(@Param("roomId")int roomId, @Param("type") int type) throws Exception;
    
    //根据userId选择其相关的空间
    public List<UserRoom> selectUserRoomByUserId(int userId) throws Exception;
    
    public List<UserRoom> selectUserRoomByRoomIdAndTypeAndUserId(@Param("roomId")int roomId, @Param("type") int type,@Param("userId")int userId) throws Exception;
    
    public List<UserRoom> selectUserRoomByUserIdAndType(@Param("userId")int userId, @Param("type") int type) throws Exception;
    
    public List<UserRoom> selectUserRoomByType(int type) throws Exception;
    
}