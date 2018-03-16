package recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.*;
import service.RoomService;
import service.UserService;

public class UserModel {
	public static <K, V extends Comparable<? super V>> Map<K, V>   
    sortMapByValue( Map<K, V> map )  
{  
    List<Map.Entry<K, V>> list =  
        new LinkedList<Map.Entry<K, V>>( map.entrySet() );  
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()  
    {  
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )  
        {  
            return (o1.getValue()).compareTo( o2.getValue() );  
        }  
    } );  

    Map<K, V> result = new LinkedHashMap<K, V>();  
    for (Map.Entry<K, V> entry : list)  
    {  
        result.put( entry.getKey(), entry.getValue() );  
    }  
    return result;  
}  
	
	/*
	 * @params  用户id， 用户名
	 * @return	SCORE MAP<ROOMID,SCORE>
	 */
	public Map<Integer,Double> analysisUser(int user_id,String username){
		System.out.println("------------ANALYSIS USER START--------");
		UserService usv = UserService.getIstance();
		RoomService rsv = RoomService.getIstance();
		System.out.println("用户名："+username+"用户id："+user_id);
		User user = usv.selectUserById(user_id);
		List<RoomComment> room_comments = rsv.selectRoomCommentByUsername(username);
		List<RoomImage> room_images = rsv.selectRoomImageByUsername(username);
		List<UserRoom> room_likes = usv.selectUserRoomByUserIdAndType(user_id, 2);
		List<UserRoom> room_shares = usv.selectUserRoomByUserIdAndType(user_id, 2);
		Set<Integer> comment_room_id_set = new HashSet<Integer>();
		Set<Integer> images_room_id_set = new HashSet<Integer>();
		Set<Integer> likes_room_id_set = new HashSet<Integer>();
		Set<Integer> shares_room_id_set = new HashSet<Integer>();
		if(room_comments != null){
			for(RoomComment rc : room_comments){
				comment_room_id_set.add(rc.getRoomId());
			}
		}
		if(room_images != null){
			for(RoomImage ri : room_images){
				images_room_id_set.add(ri.getRoomId());
			}
		}
		
		if(room_likes != null){
			for(UserRoom ur : room_likes){
				likes_room_id_set.add(ur.getRoomId());
			}
		}
		
		if(room_shares != null){
			for(UserRoom ur : room_shares){
				shares_room_id_set.add(ur.getRoomId());
			}
		}
		
		Map<Integer,Set<Integer>> result = new HashMap<Integer,Set<Integer>>();
		result.put(0, comment_room_id_set);
		result.put(1, shares_room_id_set);
		result.put(2, likes_room_id_set);
		result.put(3, images_room_id_set);
		
		//获得所有相关room ID 去重
		Set<Integer> all_id = new HashSet<Integer>();
		for (Map.Entry<Integer, Set<Integer>> entry : result.entrySet()) { 
			for(int i:entry.getValue()){
				all_id.add(i);
			}
		}
		
		Map<Integer,Double> score = new HashMap<Integer,Double>();
		//初始化得分表
		for(int i : all_id){
			score.put(i, 0.0);
		}
		
		//计算各ROOM 得分
		for (Map.Entry<Integer, Set<Integer>> entry : result.entrySet()) { 
			// 0 comment  1 share 2 like 3 image
	//	    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
		    Set<Integer> tmp = entry.getValue();
	    	Double times = 1.0;
	    	switch(entry.getKey()){
	    	case 0:{times = 1.5;break;}
	    	case 1:{times = 1.0;break;}
	    	case 2:{times = 1.5;break;}
	    	case 3:{times = 0.5;break;}
	    	default:{times = 1.0;break;}
	    	}
		    for(int i:tmp){
		    	score.put(i, score.get(i)+1*times);
		    	}
		} 
		score = sortMapByValue(score);
		for (Map.Entry<Integer, Double> entry : score.entrySet()) { 
			System.out.println("ROOM ID = " + entry.getKey() + ", Value = " + entry.getValue()); 
		}

		System.out.println("------------ANALYSIS USER END--------");
		return score;
	}

	/*
	 * @params  用户id， 用户名
	 * @return	DISTANCE MAP<USERID,SIMILAR_LEVEL>
	 */
	public Map<Integer, Double> getMostSimilarUsers(int user_id,String username){

		System.out.println("------------GET MOST SIMILAR USER START--------");
		UserService usv = UserService.getIstance();
		RoomService rsv = RoomService.getIstance();
		List<User> all_users = usv.selectAllUser();
		Map<Integer,Double> now_user_score_list = analysisUser(user_id,username);
		Map<Integer,Map<Integer,Double>> other_user_score_lists = new HashMap<Integer,Map<Integer,Double>>();//userid, <roomID,SCORE>
		
		for(User user:all_users){
			String username_tmp = user.getUsername();
			int user_id_tmp = user.getId();
			if(user_id_tmp == user_id){continue;}
			
			other_user_score_lists.put(user_id_tmp, analysisUser(user_id_tmp,username_tmp));
		}
		//判断关注的相同room的个数
		
		/*
		 *	1	2	3	4	5
		 *a		1		1		
		 *b	1		1		1
		 *c 1	1		1	
		 *d	1	1	1	1	1
		 *e 	1	1	1
		 *f 		1	1	1
		 */
		//得到用户关注的room
		Set<Integer> now_user_rooms_id = new HashSet<Integer>();
		Map<Integer,Double> distances = new HashMap<Integer,Double>();
		for(Map.Entry<Integer, Double>entry: now_user_score_list.entrySet()){
			now_user_rooms_id.add(entry.getKey());
		}

		
		for (Map.Entry<Integer, Map<Integer,Double>> entry : other_user_score_lists.entrySet()) { 
			int compare_user_id = entry.getKey();
			double same_room_count = 0;
			 Map<Integer,Double> compare_user_score_list = entry.getValue();
				Set<Integer> compare_user_rooms_id = new HashSet<Integer>();
				for(Map.Entry<Integer, Double>entry1: compare_user_score_list.entrySet()){
					compare_user_rooms_id.add(entry1.getKey());
					if(now_user_rooms_id.contains(entry1.getKey())){same_room_count++;} //如果被比较的用户也关注过相同的room ，count++
				}
				
				if(same_room_count != 0 ){
					double distance = same_room_count/Math.sqrt((now_user_rooms_id.size()*compare_user_rooms_id.size()));
					distances.put(compare_user_id, distance);
				}		 
		}
		
		distances = sortMapByValue(distances); //排序
		for (Map.Entry<Integer, Double> entry : distances.entrySet()) { 
			System.out.println("和user id = " + entry.getKey() + "相比, 相似度为 = " + entry.getValue()); 
		}

		System.out.println("------------GET MOST SIMILAR USER END--------");
		return distances;
		
	}
	/*
	 * @params  用户id， 用户名
	 * @return	INTEREST MAP<ROOMID,INTEREST_LEVEL>
	 */
	public Map<Integer, Double> getRoomByKSimiliarUser(int user_id,String username,int k){
		UserService usv = UserService.getIstance();
		RoomService rsv = RoomService.getIstance();
		List<Room> all_rooms = rsv.selectAllRoom();
		Map<Integer, Double> users_distances = getMostSimilarUsers(user_id,username);
		List<Integer> all_similar_users_id = new ArrayList<Integer>();
		List<Integer> similar_users_id = new ArrayList<Integer>();
		List<Double> similar_users_similar_level = new ArrayList<Double>();
		int count_tmp = 0;
		//获取K个距离最小的
		for(Map.Entry<Integer, Double>entry:users_distances.entrySet()){
			all_similar_users_id.add(entry.getKey());
		}
		for(int i = all_similar_users_id.size()-1;i>=0;i--){
			if(count_tmp == k){break;}
			int id = all_similar_users_id.get(i);
			similar_users_id.add(id);
			similar_users_similar_level.add(users_distances.get(id));
		}
		
		//收集这K个最相似用户的关注room信息
		Map<Integer,Map<Integer,Double>> similar_users_rooms = new HashMap<Integer,Map<Integer,Double>>();
		for(int id:similar_users_id){
			User user = usv.selectUserById(id);
			similar_users_rooms.put(id, analysisUser(id,user.getUsername()));
		}
		
		count_tmp = 0;
		Map<Integer,Double> interest_levels = new HashMap<Integer,Double>(); //Map<RoomID,INTEREST_LEVEL>
		for(Room room : all_rooms){
			int room_id = room.getId();
			double interest_level = 0.0;
			//判断相似用户是否也关注过这个room
			List<Integer> focus_room_similar_user_id = new ArrayList<Integer>();
			List<Integer> tmp = new ArrayList<Integer>();
			Map<Integer,Double>  tmp1 = new HashMap<Integer,Double>();
			for(Map.Entry<Integer, Map<Integer,Double>> entry : similar_users_rooms.entrySet()){//分析每个相似用户，记录关注过这个ROOM的user id
				tmp1 = entry.getValue();
				for(Map.Entry<Integer, Double> entry1 :tmp1.entrySet()){//获得当前相似用户的所有关注room id
					tmp.add(entry1.getKey());
				}
				if(tmp1.containsKey(room_id)){//如果相似用户也关注当前room，则记录
					focus_room_similar_user_id.add(entry.getKey()); 
				}
			}
			for(int similar_user_id : focus_room_similar_user_id){
				int index = similar_users_id.indexOf(similar_user_id);
				interest_level += similar_users_similar_level.get(index);
			}
			if(interest_level !=0){
				interest_levels.put(room_id, interest_level);
			}
		}
		List<Integer> alreay_focus_rooms_id = new ArrayList<Integer>();
		Map<Integer, Double> users_focus_map = analysisUser(user_id,username);
		for(Map.Entry<Integer, Double> entry:interest_levels.entrySet()){
			if(users_focus_map.containsKey(entry.getKey())){
				alreay_focus_rooms_id.add(entry.getKey());
			}
		}
		for(int id:alreay_focus_rooms_id){
			interest_levels.remove(id);
		}
		interest_levels = sortMapByValue(interest_levels);
		
		for(Map.Entry<Integer, Double> entry:interest_levels.entrySet()){
			System.out.println("感兴趣的roomid："+entry.getKey()+"程度："+entry.getValue());
		}
		
		return interest_levels;
		
	}
	
	
	public Map<Integer, Double> forecast(Map<Integer,Double> score,Set<Integer>rooms_id){
		//计算关注过的room中tag的情况
		RoomService rsv = RoomService.getIstance();
		List<Integer> ids = new ArrayList<Integer>(rooms_id);
		List<Room> rooms = rsv.selectRoomsByIdList(ids);
		int [] tags = new int[6]; //0 all,1,娱乐 2美食 3游览 4办公 5运动
		tags[0] = 0;tags[1] = 0;tags[2] = 0;tags[3] = 0;tags[4] = 0;tags[5] = 0;
		for(Room room:rooms){
			String[]tag = room.getTags().split(",");
			for(String tmp:tag){
				if("娱乐".equals(tmp)){
					tags[1]++;
				}else if("美食".equals(tmp)){
					tags[2]++;
				}else if("游览".equals(tmp)){
					tags[3]++;
				}else if("办公".equals(tmp)){
					tags[4]++;
				}else if("运动".equals(tmp)){
					tags[5]++;
				}
			}
		}
		tags[0] =tags[1]+tags[2]+tags[3]+tags[4]+tags[5];
		
		/*
		 * 获取所有含有comment的room的情感分析
		 */
//		Map<Integer,Double> rooms_sa = sa_room();
		Map<Integer,Double> rooms_sa = null;
		if(rooms_sa.size() == 0){
			return score;
		}
		
		for(Map.Entry<Integer, Double>entry:score.entrySet()){
			if(entry.getValue() == 1.0){
				//已关注过的 room不处理
				continue;
			}
			//对于未关注过的room
			int room_id = entry.getKey();
			if(rooms_sa.containsKey(room_id) == false){
				//这个room没有sa结果
				continue;
			}else{
				//根据这个room tags的情况以及user tags的情况结合 sa结果进行预测
				/*
				 * 可能性 = tag in tags -->+=(tag_percent* sa)
				 */
				Room room = rsv.selectRoomById(room_id);
				String []tag = room.getTags().split(",");
				List<Integer> tag_id = new ArrayList<Integer>(); //这个room含有的tag
				for(String tmp:tag){
					if("娱乐".equals(tmp)){
						tag_id.add(1);
					}else if("美食".equals(tmp)){
						tag_id.add(2);
					}else if("游览".equals(tmp)){
						tag_id.add(3);
					}else if("办公".equals(tmp)){
						tag_id.add(4);
					}else if("运动".equals(tmp)){
						tag_id.add(5);
					}
				}
				double posibility = 0.0;
				for(int i:tag_id){
					int a = tags[i];
					int b = tags[0];
					double c = rooms_sa.get(room_id);
					posibility += ((double)a/b*c);
				}
				
				score.put(room_id, (double)posibility);
				
			}
			
		}
		return score;
	}
	
	
	public static void main(String[] args){
		UserModel um = new UserModel();
		um.getRoomByKSimiliarUser(9,"qwer",3);
	}
}
