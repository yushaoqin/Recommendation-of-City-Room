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
import java.util.Map.Entry;

import model.Room;
import model.RoomComment;
import model.RoomImage;
import model.RoomPoint;
import model.User;
import model.UserRoom;
import service.RoomService;
import service.UserService;
import NLPIR.CLibrarySentiment;

public class UserCF {
	
	private UserCF() {
		initNLPIR();
	}

	private static volatile UserCF instance;

	public static UserCF getIstance() {
		if (instance == null) {
			synchronized (RoomService.class) {
				if (instance == null) {
					instance = new UserCF();
				}
			}
		}
		return instance;
	}
	
	
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
	 * @params String input 待分析文本  CLibrarySentiment instance:MLPIR 实例
	 * @return double[4] : 总分，正分，负分，正比例
	 */
	public double[] NLPIR_sentimentAnalysis(String input){


	        String result = CLibrarySentiment.Instance.ST_GetSentencePoint(input);
	        int index1 = result.indexOf("<polarity>");
	        int index2 = result.indexOf("</polarity>");
//	        System.out.println(index1+":"+index2);
	        double polarity = Double.parseDouble(result.substring(index1+10, index2));
//	        System.out.println(polarity);
	        
	        int index3 = result.indexOf("<positivepoint>");
	        int index4 = result.indexOf("</positivepoint>");
//	        System.out.println(index3+":"+index4);
	        double positivepoint = Double.parseDouble(result.substring(index3+15, index4));
//	        System.out.println(positivepoint);
	        
	        int index5 = result.indexOf("<negativepoint>");
	        int index6 = result.indexOf("</negativepoint>");
//	        System.out.println(index3+":"+index4);
	        double negativepoint = Double.parseDouble(result.substring(index5+15, index6));
//	        System.out.println(negativepoint);
	        double[] res =new double[3];
	        res[0] = polarity;
	        res[1] = positivepoint;
	        res[2] = negativepoint;
		return res;
	}
	
	
	/*
	 * @return  roomid: good rate
	 */
	public Map<Integer, Double> sa_room(){
		Map<Integer,double[]> result = new HashMap<Integer,double[]>();
		RoomService rsv = RoomService.getIstance();
		List<RoomComment> rcs = rsv.selectAllRoomComment();
		Set<Integer> all_ids = new HashSet<Integer>();
		if(rcs!=null && rcs.size()!=0){
			for(RoomComment rc:rcs){
				//获取所有含有comment的room id
				all_ids.add(rc.getRoomId());
			}
		}else{
			System.out.println("没有拥有 comment的room");
			return null;
		}
		for(int id:all_ids){
			//初始化result
			double [] tmp = new double[4];
			tmp[0] = 0.0;
			tmp[1] = 0.0;
			tmp[2] = 0.0;
			tmp[3] = 0.0;
			result.put(id, tmp);
		}
		for(RoomComment rc:rcs){
			//获取所有含有comment的room id
			double [] oneres = NLPIR_sentimentAnalysis(rc.getComment());
			double [] oldV = result.get(rc.getRoomId());
			double [] newV = new double[4];
			newV[0] = oldV[0] + oneres[0];
			newV[1] = oldV[1] + oneres[1];
			newV[2] = oldV[2] + oneres[2];
			newV[3] = 0.0;
			result.put(rc.getRoomId(), newV);
		}
		Map<Integer,Double> rate = new HashMap<Integer,Double>();
		//计算好评差评比例
		for(Map.Entry<Integer, double[]> entry:result.entrySet()){
			int id = entry.getKey();
			double [] res = entry.getValue();
			if(res[1] == 0.0 && res[2] == 0.0){
				res[3] = 0.0;
			}else{
				res[3] = res[1] / (res[1]-res[2]);	
			}
			result.put(id, res);
			rate.put(id, res[3]);
		}
		rate = sortMapByValue(rate);
		return rate;
	}
	
	
	
	/*
	 * @params  用户id， 用户名
	 * @return	POINT MAP<ROOMID,POINT>
	 */
	public Map<Integer,Double> getUserRoomPoint(int user_id,String username){
		RoomService rsv = RoomService.getIstance();
		List<RoomPoint> roomPoints = rsv.selectRoomPointByUserId(user_id);
		Map<Integer,Double> roomPoint = new HashMap<Integer,Double>();
		for(RoomPoint rp:roomPoints){
			roomPoint.put(rp.getRoomId(), rp.getValue());
		}
		
		//收藏视为5分评价
		UserService usv = UserService.getIstance();
		List<UserRoom> roomLikes = usv.selectUserRoomByUserIdAndType(user_id, 2);
		for(UserRoom rl : roomLikes){
			roomPoint.put(rl.getRoomId(), 5.0);
		}
		
		return roomPoint;
	}
	
	/*
	 * @params  room id
	 * @return	POINT MAP<USERID,POINT>
	 */
	public Map<Integer,Double> getRoomUserPoint(int room_id){
		RoomService rsv = RoomService.getIstance();
		List<RoomPoint> roomPoints = rsv.selectRoomPointByRoomId(room_id);
		Map<Integer,Double> roomPoint = new HashMap<Integer,Double>();
		for(RoomPoint rp:roomPoints){
			roomPoint.put(rp.getUserId(), rp.getValue());
		}
		UserService usv = UserService.getIstance();
		List<UserRoom> roomLikes = usv.selectUserRoomByRoomIdAndType(room_id, 2);
		for(UserRoom rl : roomLikes){
			roomPoint.put(rl.getUserId(), 5.0);
		}

		return roomPoint;
	}
	
	/*
	 * 预测user对于并集中未评分的room的评分
	 */
	public Map<Integer, Double> forecast(Map<Integer,Double> score,Set<Integer>rooms_id){
		Set<Integer> no_point_room_id = new HashSet<Integer>();
		Set<Integer> yes_point_room_id = new HashSet<Integer>();
		for(Map.Entry<Integer, Double>entry:score.entrySet()){
			if(entry.getValue() != 0.0){
				yes_point_room_id.add(entry.getKey());
			}else{
				no_point_room_id.add(entry.getKey());
			}
		}
		Map<Integer,Double> similar_room = new HashMap<Integer,Double>();
		for(int room_id: no_point_room_id){
//			similar_room = getMostSimilarRoomWithSa(room_id);
			similar_room = getMostSimilarRoom(room_id);
			//计算room similar的平均值
			double toal_room_similar_rate = 0.0;
			for(Map.Entry<Integer,Double>entry:similar_room.entrySet()){
				if(entry.getValue() >=0.0){
					toal_room_similar_rate += entry.getValue();
				}else{
					toal_room_similar_rate += (-entry.getValue());
				}
			}
		//	avg_room_similar_rate = (similar_room.size()!=0)?avg_room_similar_rate/similar_room.size() :0.0;
			
			
			double up = 0.0; //分子
			//根据room相似度预测user评分
			for(int i:yes_point_room_id){
				if(similar_room.containsKey(i)){//若相似room中有user曾经评分过的项目
						up += similar_room.get(i) * score.get(i); //分子等于  相似度* 用户评分 之和
				}
			}
			
			double possible_point = (toal_room_similar_rate !=0)?up/toal_room_similar_rate:0.0;
			score.put(room_id, possible_point);
		}
		score = sortMapByValue(score);
		return score;
	}
	
	
	/*
	 * 修正的余弦相似性计算用户相似度
	 */
	public double improvedCosine(Map<Integer,Double>u,Map<Integer,Double>j,Set<Integer> all_index){
		if(u.size() == 0 || j.size() ==0 || all_index.size() == 0){
			return 0.0;
		}
		//通过相似相关性 计算sim（i,j）
		//平均关注度
		double avg_u = 0.0;
		double avg_j = 0.0;
		//分子
		double up =0.0;
		//分母
		double down_u = 0.0;
		double down_j = 0.0;
		
		//计算平均数
		for(int i:all_index){
			avg_u +=u.get(i);
			avg_j += j.get(i);
		}
		
		avg_u /= all_index.size();
		avg_j /= all_index.size();
		
		//计算分子
		for(int i:all_index){
			up += (u.get(i)- avg_u) *(j.get(i)- avg_j);
			down_u += (u.get(i)- avg_u) *(u.get(i)- avg_u);
			down_j +=(j.get(i)- avg_j) *(j.get(i)- avg_j);
		}
		
		down_u = Math.sqrt(down_u);
		down_j = Math.sqrt(down_j);
		if(down_u == 0.0 ||down_j == 0.0){
			return 0.0;
		}
		double similar = up/(down_u *down_j);
		
		return similar;
		
		//计算分母
		
	}
	
	/*
	 * @param 待分析的 room id
	 * @return SIMILAR RATE MAP<ROOMID,SIMILAR RATE>
	 */
	public  Map<Integer, Double> getMostSimilarRoom(int room_id){
		UserService usv = UserService.getIstance();
		RoomService rsv = RoomService.getIstance();
		List<Room> all_rooms = rsv.selectAllRoom();
		//得到当前room的所有user评分情况
		Map<Integer,Double> now_room_score_list = getRoomUserPoint(room_id);
		Map<Integer,Map<Integer,Double>> other_room_score_lists = new HashMap<Integer,Map<Integer,Double>>();//roomId, <userId,SCORE>
		
		for(Room room:all_rooms){
			int room_id_tmp = room.getId();
			if(room_id_tmp == room_id){continue;} //排除自身
			
			other_room_score_lists.put(room_id_tmp, getRoomUserPoint(room_id_tmp));
		}
		//得到user-room 的二阶矩阵
		
		/*
		 *	1	2	3	4	5
		 *a		2		3		
		 *b	2	1	3		5
		 *c 1	4		1	
		 *d	1	2	1	4	1
		 *e 	1	3	1
		 *f 		1	3	2
		 */
		//得到用户关注的room
	
		/*
		 * now_room_users_id: 评分过 当前 room 的 user ids
		 */
		Set<Integer> now_room_users_id = new HashSet<Integer>();
		for(Map.Entry<Integer, Double>entry: now_room_score_list.entrySet()){
			now_room_users_id.add(entry.getKey());
		}
		/*
		 * 其他room 被 user 评分的 user ids
		 */
		Map<Integer,Set<Integer>> other_room_users_id = new HashMap<Integer,Set<Integer>>();//roomId, <userId>
		for(Map.Entry<Integer, Map<Integer,Double>>entry: other_room_score_lists.entrySet()){
			Set<Integer> tmp1 = new HashSet<Integer>();
			Map<Integer,Double> tmp = entry.getValue();
			for(Map.Entry<Integer, Double>entry1:tmp.entrySet()){
				tmp1.add(entry1.getKey());
				other_room_users_id.put(entry.getKey(), tmp1);
			}
		}
		/*
		 * 计算每个room的相似度
		 */
		//当前分析room的得分表   userId,point
		Map<Integer,Double> now_room_score = new HashMap<Integer,Double>(); 
		//当前比较room的得分表   userId,point
		Map<Integer,Double> oneof_other_room_score = new HashMap<Integer,Double>();
		
		//返回结果Map<other room id , similar>
		Map<Integer,Double> result = new HashMap<Integer,Double>();
		
		for(Room room:all_rooms){
		
			int room_id_tmp = room.getId();
			if(other_room_users_id.containsKey(room_id_tmp) == false){
				//room 没有被评分过
				continue;
			}
			if(room_id_tmp == room_id){continue;}//排除自身
			
			//获得当前比较room 的评分user ids
			Set<Integer> oneof_other_room_users_id = other_room_users_id.get(room_id_tmp);
			//当前分析room，当前比较room的评分user id的并集
			Set<Integer> union_users_id = new HashSet<Integer>();
			union_users_id.addAll(oneof_other_room_users_id);
			union_users_id.addAll(now_room_users_id);
			//初始化
			for(int i :union_users_id){
				now_room_score.put(i, 0.0);
				oneof_other_room_score.put(i,0.0);
			}
			for(int i :oneof_other_room_users_id){
				oneof_other_room_score.put(i, other_room_score_lists.get(room_id_tmp).get(i));
			}
			
			for(int i:now_room_users_id){
				now_room_score.put(i,now_room_score_list.get(i));
			}
			
			
			double similar = improvedCosine(now_room_score,oneof_other_room_score,union_users_id);
			result.put(room_id_tmp, similar);
		}
		result = sortMapByValue(result);
		return result;
		
	}
	
	/*
	 * @param 用户id， 用户名
	 * @return	SIMILAR RATE MAP<USERID,SIMILAR_LEVEL>
	 */
	public Map<Integer, Double> getMostSimilarUsers(int user_id,String username){
		UserService usv = UserService.getIstance();
		List<User> all_users = usv.selectAllUser();
		Map<Integer,Double> now_user_score_list = getUserRoomPoint(user_id,username);
		Map<Integer,Map<Integer,Double>> other_user_score_lists = new HashMap<Integer,Map<Integer,Double>>();//userid, <roomID,SCORE>
		
		for(User user:all_users){
			String username_tmp = user.getUsername();
			int user_id_tmp = user.getId();
			if(user_id_tmp == user_id){continue;}
			
			other_user_score_lists.put(user_id_tmp, getUserRoomPoint(user_id_tmp,username_tmp));
		}
		//得到user-room 的二阶矩阵
		
		/*
		 *	1	2	3	4	5
		 *a		2		3		
		 *b	2	1	3		5
		 *c 1	4		1	
		 *d	1	2	1	4	1
		 *e 	1	3	1
		 *f 		1	3	2
		 */
		//得到用户关注的room
	
		/*
		 * now_user_rooms_id: 用户关注的room id
		 */
		Set<Integer> now_user_rooms_id = new HashSet<Integer>();
		for(Map.Entry<Integer, Double>entry: now_user_score_list.entrySet()){
			now_user_rooms_id.add(entry.getKey());
		}
		/*
		 * 其他用户关注的room id
		 */
		Map<Integer,Set<Integer>> other_user_rooms_id = new HashMap<Integer,Set<Integer>>();//userid, <roomID,SCORE>
		for(Map.Entry<Integer, Map<Integer,Double>>entry: other_user_score_lists.entrySet()){
			Set<Integer> tmp1 = new HashSet<Integer>();
			Map<Integer,Double> tmp = entry.getValue();
			for(Map.Entry<Integer, Double>entry1:tmp.entrySet()){
				tmp1.add(entry1.getKey());
				other_user_rooms_id.put(entry.getKey(), tmp1);
			}
		}
		/*
		 * 计算每个用户的相似度
		 */
		//当前分析用户 关注可能 表
		Map<Integer,Double> now_user_score = new HashMap<Integer,Double>();
		//当前比较用户 关注可能 表
		Map<Integer,Double> oneof_other_user_score = new HashMap<Integer,Double>();
		
		//返回结果Map<other user id , similar>
		Map<Integer,Double> result = new HashMap<Integer,Double>();
		
		for(User user:all_users){
		
			int user_id_tmp = user.getId();
			if(other_user_rooms_id.containsKey(user_id_tmp) == false){
				continue;
			}
			if(user_id_tmp == user_id){continue;}//排除自身
			
			//获得当前比较用户 关注的rooms id
			Set<Integer> oneof_other_user_rooms_id = other_user_rooms_id.get(user_id_tmp);
			//当前分析用户，当前比较用户关注rooms id的并集
			Set<Integer> union_rooms_id = new HashSet<Integer>();
			union_rooms_id.addAll(oneof_other_user_rooms_id);
			union_rooms_id.addAll(now_user_rooms_id);
			//初始化
			for(int i :union_rooms_id){
				now_user_score.put(i, 0.0);
				oneof_other_user_score.put(i,0.0);
			}
			//other_user_score_lists.put(user_id_tmp, getUserRoomPoint(user_id_tmp,username_tmp));
			for(int i :oneof_other_user_rooms_id){
				oneof_other_user_score.put(i, other_user_score_lists.get(user_id_tmp).get(i));
			}
			for(int i:now_user_rooms_id){
				now_user_score.put(i,now_user_score_list.get(i));
			}
			
			//推测 并集 中用户未评分过的项目的得分,以解决相似相关系数方法数据过少的情况
			now_user_score = forecast(now_user_score,now_user_rooms_id);
			oneof_other_user_score = forecast(oneof_other_user_score,oneof_other_user_rooms_id);
			
			double similar = improvedCosine(now_user_score,oneof_other_user_score,union_rooms_id);
			result.put(user_id_tmp, similar);
			
		}
		result = sortMapByValue(result);
		return result;
		
	}
	
	public Map<Integer,Double> getMostSimilarRoomWithSa(int room_id){
		Map<Integer,Double> similar_room = getMostSimilarRoom(room_id);
		Map<Integer,Double> rate = sa_room();
		double avg_rate = 0.0;
		for(Map.Entry<Integer,Double>entry:rate.entrySet()){
			avg_rate += entry.getValue();
		}
		avg_rate = avg_rate/((double)rate.size());
		double this_room_rate = avg_rate;
		if(rate.containsKey(room_id)){
			this_room_rate = rate.get(room_id);
		}
		
		Map<Integer,Double> result = new HashMap<Integer,Double>();
		for(Map.Entry<Integer, Double> entry:similar_room.entrySet()){
			int tmp_room_id = entry.getKey();
			double tmp_similar = entry.getValue();
			double tmp_rate = avg_rate;
			if(rate.containsKey(tmp_room_id)){
				tmp_rate = rate.get(tmp_room_id);
			}
			if(this_room_rate == 0.0 ){this_room_rate+=0.01;}
			if(tmp_rate == 0.0 ){tmp_rate+=0.01;}
			double times = (this_room_rate>tmp_rate)?(tmp_rate/this_room_rate ):(this_room_rate/tmp_rate);
			tmp_similar *= times;
			result.put(tmp_room_id, tmp_similar);
		}
		result = sortMapByValue(result);
		return result;
	}
	
	public Map<Integer,Double> getRecommendationByKSimilar(int user_id,String username){
		int k = 5; //取3个最近的用户
		UserService usv = UserService.getIstance();
		RoomService rsv = RoomService.getIstance();
		Map<Integer,Double> similar_users = getMostSimilarUsers(user_id,username); //获得相似用户的情况
		int num_of_similar_users = similar_users.size();
		k = (num_of_similar_users>=3)? 3:num_of_similar_users; //判断相似用户数量
		List<Integer> similar_users_id = new ArrayList<Integer>();
		for(Map.Entry<Integer, Double>entry:similar_users.entrySet()){
			similar_users_id.add(entry.getKey());
		}
		Map<Integer,Double> user_score = new HashMap<Integer,Double>();
		double avg_user  = 0.0;
		double avg_other = 0.0;
		user_score = getUserRoomPoint(user_id,username);
		for(Map.Entry<Integer, Double>entry:user_score.entrySet()){
			avg_user += entry.getValue();
		}
		avg_user /= ((double)user_score.size());
		
		List<Room> rooms = rsv.selectAllRoom();
		Map<Integer,Double> score = new HashMap<Integer,Double>();
		Map<Integer,Double> result = new HashMap<Integer,Double>(); //roomid, possibility_point
		for(Room room:rooms){
			int roomId = room.getId();
			if(user_score.containsKey(roomId)){
				continue;
			}
			result.put(roomId, 0.0);
			
			double possibility_point = 0.0;
			/*
			 * 计算相似用户的平均评分
			 */
			
			//计算分子
			double up = 0.0;
			for(int i =similar_users_id.size()-1;i>= similar_users_id.size()-k;i--){
				int tmp_user_id = similar_users_id.get(i);
				User other_user = usv.selectUserById(tmp_user_id);
				score = getUserRoomPoint(other_user.getId(),other_user.getUsername());
				for(Map.Entry<Integer, Double>entry:score.entrySet()){
					avg_other += entry.getValue();
				}
				avg_other /= ((double)score.size());
				//预测当前ROOM 评分
				
				//若当前的相似用户没有对此room进行评分，则取0
				double tmp_point = 0.0;
				if(score.containsKey(roomId) == true){
					tmp_point = score.get(roomId);
				}
				up += similar_users.get(tmp_user_id)*(tmp_point-avg_other);
			}
			
			//计算分母
			double down = 0.0;
			for(int i =similar_users_id.size()-1;i>= similar_users_id.size()-k;i--){
				down+= Math.abs(similar_users.get(similar_users_id.get(i))); //k个用户相似度的绝对值之和
			}
			if(down != 0.0){
				possibility_point = avg_user + up/down;	
			}else{

				possibility_point = 0.0;
			}
			
			result.put(roomId, possibility_point);
			
		}
		
		result = sortMapByValue(result);
		return result;
		
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
			similar_users_rooms.put(id, getUserRoomPoint(id,user.getUsername()));
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
		Map<Integer, Double> users_focus_map = getUserRoomPoint(user_id,username);
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
	
	public int initNLPIR(){
		String argu = "D:\\program\\eclipse-4.4-workspcae\\YSTServer";
		String argu2 = argu+"\\dict\\sentilexicon.txt";
		
		// // String argu ="/root/NLPIR_IN_PROJECT";
		 // String argu2 = "/root/NLPIR_IN_PROJECT/sentilexicon.txt";
		
		
		  int flag = CLibrarySentiment.Instance.ST_Init(argu, 1, "");
		  if(flag == 0){
	            System.out.println(CLibrarySentiment.Instance.ST_GetLastErrMsg());
	            System.out.println("NLPIR初始化失败");
	            System.exit(1);
		  }else{
			  System.out.println("NLPIR初始化成功");
		  }
		  int num = CLibrarySentiment.Instance.ST_ImportUserDict(argu2, 0);
	       System.out.printf("添加用户单词%d个\n", num);
	       
	       return flag;
	}
	
	public void exitNLPIR(){
		CLibrarySentiment.Instance.ST_Exit();
		System.out.println("EXIT NLPIR FINISHED");
	}
	
	public static void main(String[]args){
//		   System.out.println("初始化开始...");
//	        CLibrarySentiment instance = CLibrarySentiment.Instance;
//	        if (instance.ST_Init("", 1, "") == 0) {
//	            System.out.println(CLibrarySentiment.Instance.ST_GetLastErrMsg());
//	            System.out.println("初始化失败");
//	            System.exit(1);
//	        } else {
//	            System.out.println("初始化成功");
//	        }
//			   int num = instance.ST_ImportUserDict("./dict/sentilexicon.txt", 0);
//		        System.out.printf("添加用户单词%d个\n", num);
		
		
			
			try{
				UserCF ucf = new UserCF();
				ucf.initNLPIR();
//				System.out.println("WITHNOT SA------------");
//				Map<Integer, Double> res1 = ucf.getMostSimilarRoom(37);
//				for(Entry<Integer, Double> entry:res1.entrySet()){
//					System.out.println("Room ID:"+entry.getKey()+"  SIMILAR:"+entry.getValue());
//				}
//				
//				System.out.println("WITH SA------------");
//				Map<Integer, Double> res2 = ucf.getMostSimilarRoomWithSa(37);
//				for(Entry<Integer, Double> entry:res2.entrySet()){
//					System.out.println("Room ID:"+entry.getKey()+"  SIMILAR:"+entry.getValue());
//				}
//				
//				System.out.println("SMILAR USER------------");
//				Map<Integer, Double> res = ucf.getMostSimilarUsers(8, "qwer");
//				for(Entry<Integer, Double> entry:res.entrySet()){
//					System.out.println("USER ID:"+entry.getKey()+"  SIMILAR:"+entry.getValue());
//				}
//				
				
				System.out.println("POSSIBILITY POINT------------");
				Map<Integer, Double> res3 = ucf.getRecommendationByKSimilar(10, "chuyc");
				for(Entry<Integer, Double> entry:res3.entrySet()){
					System.out.println("ROOM ID:"+entry.getKey()+"  POSSIBILITY POINT:"+entry.getValue());
				}
				
			}catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}finally{
				
//				caInstance.LJST_Exits();
//				instance.NLPIR_Exit();
//				CLibrary.Instance.NLPIR_Exit();
//				CLibrarySentimentAnalysis.Instance.LJST_Exits();
				CLibrarySentiment.Instance.ST_Exit();
				System.out.println("EXIT ALL FINISHED");
			}
	}
}
