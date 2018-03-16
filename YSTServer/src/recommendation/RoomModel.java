package recommendation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.Room;
import model.RoomComment;
import model.RoomDescription;
import model.RoomImage;
import model.UserRoom;
import service.RoomService;
import service.UserService;
import NLPIR.CLibrarySentiment;
//import NLPIR.LJSentimentAnalysisLibrary.CLibrarySentimentAnalysis;
//import NLPIR.Nlpir.CLibrary;

public class RoomModel {
	private  Map<String,ArrayList<String>> getLevel(String filePath,String encoding){
		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		ArrayList<String> level1 = new ArrayList<String>();
		ArrayList<String> level2 = new ArrayList<String>();
		ArrayList<String> level3 = new ArrayList<String>();
		ArrayList<String> level4 = new ArrayList<String>();
		ArrayList<String> level5 = new ArrayList<String>();
        try {
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    int flag = 0;
                    while((lineTxt = bufferedReader.readLine()) != null&& !"".equals(lineTxt)){
                        if(lineTxt.charAt(0)=='1'){flag = 1;continue;}
                        if(lineTxt.charAt(0)=='2'){flag = 2;continue;}
                        if(lineTxt.charAt(0)=='3'){flag = 3;continue;}
                        if(lineTxt.charAt(0)=='4'){flag = 4;continue;}
                        if(lineTxt.charAt(0)=='5'){flag = 5;continue;}
                        if(lineTxt.charAt(0)=='6'){flag = 1;continue;}
                        switch(flag){
                        case 1:{level1.add(lineTxt);break;}
                        case 2:{level2.add(lineTxt);break;}
                        case 3:{level3.add(lineTxt);break;}
                        case 4:{level4.add(lineTxt);break;}
                        case 5:{level5.add(lineTxt);break;}
                        }
                    }
                    read.close();
                    map.put("1", level1);
                    map.put("2", level2);
                    map.put("3", level3);
                    map.put("4", level4);
                    map.put("5", level5);
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
		return map;
     
    }
	private ArrayList<String> getNotDict(){
		ArrayList<String> notDict = new ArrayList<String>();
		notDict.add("没有");
		notDict.add("不");
		notDict.add("否");
		notDict.add("勿");
		notDict.add("无");
		notDict.add("莫");
		notDict.add("未");
		notDict.add("非");
		notDict.add("没");
		notDict.add("不是");
		return notDict;
	} 
	private boolean isNotBefore(String []fenci_result,int index,ArrayList<String>notDict){

		if(index >0){
			String word = fenci_result[index-1].split("/")[0];
			if("是".equals(word)){
				if(index > 1){
					word = fenci_result[index-2].split("/")[0];
				}
			}
			if(notDict.contains(word)){
				return true;
			}
		}
		return false;
	}
	private ArrayList<String> getWordTypeToSA(){
		ArrayList<String> wordType = new ArrayList<String>();
		wordType.add("a");
		wordType.add("an");
		wordType.add("v");
		wordType.add("vi");
		wordType.add("vn");
		return wordType;
	}
//	public double sentimentAnalysis(CLibrary cInstane,CLibrarySentimentAnalysis instance,String input){
//		
//		input = input.replaceAll(" +",""); //去除所有空格
//		double result = 0;
//		ArrayList<Double> score = new ArrayList<Double>();
//		Map<String,ArrayList<String>> levelMap = getLevel("D://program/eclipse-4.4-workspcae/YSTServer/dict/level.txt","UTF-8");
////		Map<String,ArrayList<String>> levelMap = getLevel("/root/NLPIR_IN_PROJECT/level.txt","UTF-8");
//		String fenci_tmp = cInstane.NLPIR_ParagraphProcess(input, 1);  //分词
//		
//		String []fenci_result = fenci_tmp.split(" "); //将结果分解为形如    ["历史/n", "十分/d", "悠久/a" ,"，/wd"]的数组
//		ArrayList<Integer> indexs_in_fenci_result = new ArrayList<Integer>();
//		ArrayList<String> wordType = getWordTypeToSA();
//		for(int fenci_result_index =0;fenci_result_index<fenci_result.length;fenci_result_index++){
//			String word_result = fenci_result[fenci_result_index];
//			String[] word = word_result.split("/");  // 将单词分解为形如 ["历史","n"]的形式
//			if(wordType.contains(word[1])){ //形容词或者动词，需要进行情感分析
//				indexs_in_fenci_result.add(fenci_result_index);//记录单词在分词结果中的index
//				
//				/*
//				 * println
//				 */
//				System.out.print(fenci_result_index+",");
//			}
//		}
//		/*
//		 * println
//		 */
//		System.out.println("");
//		
//		
//		for(int i =0;i<indexs_in_fenci_result.size();i++){
//			String word_result = fenci_result[indexs_in_fenci_result.get(i)];
//			String[] word = word_result.split("/");  // 将单词分解为形如 ["历史","n"]的形式
//			double score_tmp = 0; //这个单词最终情感得分。
//			byte[] sa_result_tmp = new byte[150]; //存放单个词语的情感分析结果
//			instance.LJST_GetParagraphSent(word[0], sa_result_tmp);//将该单词进行分析
//			String sa_result = new String(sa_result_tmp);
//			sa_result_tmp = null;
//				/*
//				 * sa_result格式为：
//				 * EMOTION_HAPPY/0
//				 * EMOTION_GOOD/1
//				 * EMOTION_ANGER/0
//				 * EMOTION_SORROW/0
//				 * EMOTION_FEAR/0
//				* EMOTION_EVIL/0
//				* EMOTION_SURPRISE/0
//				 */
//		//	System.out.print("分析单词为："+word[0]+" "+word[1]+" 结果为：\n"+sa_result+"\n");
//				/*
//				 * 将 sa_result分解为数组形式，并判断情感正负，若 EMOTION_HAPPY或者EMOTION_GOOD 为1，则情感值为1，否则为-1	
//				 */
//			String []sa_results = sa_result.split("\n"); 
//			for(int sa_results_index =0;sa_results_index<7;sa_results_index++){  //判断是7个标签中的哪一个
//				String [] tmp = sa_results[sa_results_index].split("/"); //tmp为： [EMOTION_X,0]
//
//				if("1".equals(tmp[1])){
//					switch(tmp[0]){
//					case "EMOTION_HAPPY":{score_tmp = 1;break;}
//					case "EMOTION_GOOD":{score_tmp = 1;break;}
//					case "EMOTION_ANGER":{score_tmp = -1;break;}
//					case "EMOTION_SORROW":{score_tmp = -1;break;}
//					case "EMOTION_FEAR":{score_tmp = -1;break;}
//					case "EMOTION_EVIL":{score_tmp = -1;break;}
//					} //switch
//				} // if 			
//			} //for
//			if(score_tmp == 0){continue;} //得分为0 ，说明该单词不带感情色彩为中性词。继续分析下一个单词。
//			
//			/*
//			 * println
//			 */
//			System.out.println("-------------"+word[0]+" 在原文中的序号为："+indexs_in_fenci_result.get(i)+"-------------");
//			
//			ArrayList<String> notDict = getNotDict();
//			int number_of_not = 0 ;
//			/*
//			 * 若第X-1个和第X个被分析的单词之间，存在偶数个否定词，则为原感情，否则为否定感情
//			 */
//			if(i == 0 ){ //是第一个被分析的词
//				for(int j = 0 ;j<indexs_in_fenci_result.get(0);j++){
//					String word_in_content = fenci_result[j].split("/")[0];
//					if(notDict.contains(word_in_content)){number_of_not++;}
//				}
//			}else{
//				for(int j = indexs_in_fenci_result.get(i-1);j<indexs_in_fenci_result.get(i);j++){
//					String word_in_content = fenci_result[j].split("/")[0];
//					if(notDict.contains(word_in_content)){number_of_not++;}
//				}
//			}
//			if(number_of_not%2 != 0 ){score_tmp *= -1;}//奇数个否定词，感情取反，例如： 不好吃
//			
//			//根据程度副词不同，表达感情的强烈程度不同。分为5类， 权重分别为 1.5,1.3,1.1，0.8,0.5
//			
//			int number_of_chengdu = 0 ;
//			double times = 1;
//			System.out.println("分析："+fenci_result[indexs_in_fenci_result.get(i)]+"的程度词：");
//			if(i == 0 ){ //是第一个被分析的词
//				for(int j = 0 ;j<indexs_in_fenci_result.get(0);j++){
//					double times_tmp = 1;
//					String word_in_content = fenci_result[j].split("/")[0];
//		
//					if(levelMap.get("1").contains(word_in_content)){
//						times_tmp = 1.5;	
//						number_of_chengdu++;
//					}else if(levelMap.get("2").contains(word_in_content)){
//						times_tmp = 1.3;
//						number_of_chengdu++;
//					}else if(levelMap.get("3").contains(word_in_content)){
//						times_tmp = 1.1;
//						number_of_chengdu++;
//					}else if(levelMap.get("4").contains(word_in_content)){
//						times_tmp = 0.8;
//						number_of_chengdu++;
//					}else if(levelMap.get("5").contains(word_in_content)){
//						times_tmp = 0.5;
//						number_of_chengdu++;
//					}else{times *=1;}
//					if(times_tmp != 1){//判断程度副词前是否有否定词，若有，则倍数应为0.8， 例如 不很高兴
//						System.out.println("程度词："+word_in_content);
//						if(isNotBefore(fenci_result,j,notDict)){
//							if((times_tmp == 0.5|| times_tmp ==0.8)){ //没有 一点，没有 丝毫等
//								times_tmp = 1.5;	
//							}else{ //没有很 ，没有特别 等
//								times_tmp = 0.8;
//							}
//						}
//					}
//					times *= times_tmp;
//					/*
//					 * println
//					 */
//				//	System.out.println("程度副词分析:"+word_in_content+"  倍数为："+times_tmp);
//				}
//			}else{
//				for(int j = indexs_in_fenci_result.get(i-1);j<indexs_in_fenci_result.get(i);j++){
//					double times_tmp =1;
//					String word_in_content = fenci_result[j].split("/")[0];
//					if(levelMap.get("1").contains(word_in_content)){
//						times_tmp = 1.5;	
//						number_of_chengdu++;
//					}else if(levelMap.get("2").contains(word_in_content)){
//						times_tmp = 1.3;
//						number_of_chengdu++;
//					}else if(levelMap.get("3").contains(word_in_content)){
//						times_tmp = 1.1;
//						number_of_chengdu++;
//					}else if(levelMap.get("4").contains(word_in_content)){
//						times_tmp = 0.8;
//						number_of_chengdu++;
//					}else if(levelMap.get("5").contains(word_in_content)){
//						times_tmp = 0.5;
//						number_of_chengdu++;
//					}else{times *=1;}
//					if(times_tmp != 1){//判断程度副词前是否有否定词，若有，则倍数应为0.8， 例如 不很高兴
//						System.out.println("程度词："+word_in_content+" 权重："+times_tmp);
//						if(isNotBefore(fenci_result,j,notDict)){
//							if((times_tmp == 0.5|| times_tmp ==0.8)){ //没有 一点，没有 丝毫等
//								times_tmp = 1.5;	
//							}else{ //没有很 ，没有特别 等
//								times_tmp = 0.8;
//							}
//						}
//					}
//					times *= times_tmp;
//					/*
//					 * println
//					 */
//				//	System.out.println("程度副词分析:"+word_in_content+"  倍数为："+times_tmp);
//				}
//			}
//			score_tmp *= times ;
//			score.add(score_tmp);
//			/*
//			 * println
//			 */
//			System.out.println("最终 "+ word[0] +" 得分："+score_tmp+",具有"+number_of_not+"个否定词"+" 具有"+number_of_chengdu+"个程度词");
//	
//
//		
//		}
//		//没有一个具有情感色彩的词语
//		if(score.size() == 0){
//			return 0 ;
//		}
//		for(double s : score){
//			System.out.println(s);
//			result+= s;
//		}
//		result = result/score.size();
//		return result;
//	}
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
	

	
	public double[] NLPIR_sentimentAnalysis(CLibrarySentiment instance,String input){


	        String result = CLibrarySentiment.Instance.ST_GetSentencePoint(input);
//	        System.out.println("-----------\n"+result);
	        int index1 = result.indexOf("<polarity>");
	        int index2 = result.indexOf("</polarity>");
//	        System.out.println(index1+":"+index2);
	        double polarity = Double.parseDouble(result.substring(index1+10, index2));
	        System.out.println(polarity);
	        
	        int index3 = result.indexOf("<positivepoint>");
	        int index4 = result.indexOf("</positivepoint>");
//	        System.out.println(index3+":"+index4);
	        double positivepoint = Double.parseDouble(result.substring(index3+15, index4));
	        System.out.println(positivepoint);
	        
	        int index5 = result.indexOf("<negativepoint>");
	        int index6 = result.indexOf("</negativepoint>");
//	        System.out.println(index3+":"+index4);
	        double negativepoint = Double.parseDouble(result.substring(index5+15, index6));
	        System.out.println(negativepoint);
	        double[] res =new double[3];
	        res[0] = polarity;
	        res[1] = positivepoint;
	        res[2] = negativepoint;
		return res;
	}
	public Map<Integer, Double> sa_room(CLibrarySentiment instance){
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
			double [] oneres = NLPIR_sentimentAnalysis(instance,rc.getComment());
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


	
	
	
	
	
	
//	public List<Integer> recommendRoomByComment(CLibrary cInstance,CLibrarySentimentAnalysis csInstance){
//		Map<Integer,ArrayList<Double>> scores = new HashMap<Integer,ArrayList<Double>>();
//		RoomService rsv = RoomService.getIstance();
//		
//		List<RoomComment> rcs = rsv.selectAllRoomComment();
//		
//		if(rcs == null ){return null;}
//		
//		Set<Integer> room_id_set = new HashSet<Integer>(); 
//		for(RoomComment rc : rcs){
//			room_id_set.add(rc.getRoomId());
//		}
//		
//		//初始化 result
//		for(int room_id : room_id_set){
//			ArrayList<Double> tmp = new ArrayList<Double>();
//			scores.put(room_id, tmp);
//		}
//		//逐条分析comment，并给出情感得分
//		for(RoomComment rc : rcs){
//			System.out.println("正在分析："+rc.getComment());
//			Double tmp_score = sentimentAnalysis(cInstance,csInstance,rc.getComment());
//			scores.get(rc.getRoomId()).add(tmp_score);
//		}
//		
//		//计算平均得分，得出含有comment的空间的平均情感得分
//		
//		Map<Integer,Double> avg_score_for_room = new HashMap<Integer,Double>();
//		for(int room_id : room_id_set){
//			double avg_score = 0;
//			List<Double> score_list = scores.get(room_id);
//			for(Double tmp : score_list){
//				avg_score += tmp;
//			}
//			avg_score /= score_list.size();
//			avg_score_for_room.put(room_id, avg_score);
//		}
//		
//		avg_score_for_room = sortMapByValue(avg_score_for_room);//将记录得分的map按照value排序
//		int number_of_room_recommend = (room_id_set.size()>5)?5:room_id_set.size(); //推荐的数量
//		int count_tmp = 0;
//		List<Integer> room_id_to_recommend = new ArrayList<Integer>();
//		for (Map.Entry<Integer, Double> entry : avg_score_for_room.entrySet()) {  
//			 if(count_tmp == number_of_room_recommend){break;}
//		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
//		    room_id_to_recommend.add(entry.getKey());
//		    count_tmp ++;
//		  
//		}  
//		return room_id_to_recommend;
//		
//
//		
//	}
	public Map<Integer,Integer> getNumberOfRoomInfo(int type){
		/*
		 * 0  likes number
		 * 1  img number
		 * 2  description number
		 * 3  content number
		 */
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		UserService usv = UserService.getIstance();
		RoomService rsv = RoomService.getIstance();
		List<Integer> rooms_id = new ArrayList<Integer>();
		Set<Integer> room_id_set = new HashSet<Integer>(); 
		if(type == 0){ //liked
			List<UserRoom> roomsBeLiked = usv.selectUserRoomByType(2);
			for(UserRoom ur : roomsBeLiked){
				rooms_id.add(ur.getRoomId());	
				room_id_set.add(ur.getRoomId());
			}
		}else if(type == 1){ //img
			List<RoomImage> roomImages = rsv.selectAllRoomImage();
			for(RoomImage ri : roomImages){
				rooms_id.add(ri.getRoomId());	
				room_id_set.add(ri.getRoomId());
			}	
		}else if(type == 2){ //description
			List<RoomDescription> roomDescriptions = rsv.selectAllRoomDescription();
			for(RoomDescription rd : roomDescriptions){
				rooms_id.add(rd.getRoomId());	
				room_id_set.add(rd.getRoomId());
			}	
		}else if(type == 3){//content
			List<RoomComment> roomComments = rsv.selectAllRoomComment();
			for(RoomComment rc : roomComments){
				rooms_id.add(rc.getRoomId());	
				room_id_set.add(rc.getRoomId());
			}	
		}
		for(int i: room_id_set){
			//初始化
			result.put(i, 0);
		}
		for(int i=0;i<rooms_id.size();i++){
			int id = rooms_id.get(i);
			int count = result.get(id)+1;
			result.put(id,count );
		}
		
		result = sortMapByValue(result);
//		for (Map.Entry<Integer, Integer> entry : result.entrySet()) {  
//		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
//		}  	
		
		return result;
	}
	public List<Integer> recommendRoomByAllInfo(){
		
		Map<Integer,Double> scores = new HashMap<Integer,Double>();
		Map<Integer,Integer> likes = getNumberOfRoomInfo(0);
		Map<Integer,Integer> imgs = getNumberOfRoomInfo(1);
		Map<Integer,Integer> descriptions = getNumberOfRoomInfo(2);
		Map<Integer,Integer> comments = getNumberOfRoomInfo(3);
		
		Set<Integer> room_id_set = new HashSet<Integer>(); 
		for (Map.Entry<Integer, Integer> entry : likes.entrySet()) {  
		    room_id_set.add(entry.getKey());   
		}  	
		for (Map.Entry<Integer, Integer> entry : imgs.entrySet()) {  
		    room_id_set.add(entry.getKey());   
		}  	
		for (Map.Entry<Integer, Integer> entry : descriptions.entrySet()) {  
		    room_id_set.add(entry.getKey());   
		}  	
		for (Map.Entry<Integer, Integer> entry : comments.entrySet()) {  
		    room_id_set.add(entry.getKey());   
		}  	
		for(int i :room_id_set){
			scores.put(i, 0.0);//初始化 score
		}
		//likes的权重为1.5
		double weight = 1.5;
		for (Map.Entry<Integer, Integer> entry : likes.entrySet()) {  
			double old = scores.get(entry.getKey());
			double value = entry.getValue()*weight + scores.get(entry.getKey());
		    scores.put(entry.getKey(), value) ;
		    
		}  
		//img的权重为1.5
		weight = 1.5;
		for (Map.Entry<Integer, Integer> entry : imgs.entrySet()) {
			double old = scores.get(entry.getKey());  
			double value = entry.getValue()*weight + scores.get(entry.getKey());
		    scores.put(entry.getKey(), value) ;
		}  
		//desciption的权重为1
		weight = 1;
		for (Map.Entry<Integer, Integer> entry : descriptions.entrySet()) {
			double old = scores.get(entry.getKey());  
			double value = entry.getValue()*weight + scores.get(entry.getKey());
		    scores.put(entry.getKey(), value) ;
		}  
		//comment的权重为0.5
		weight = 0.5;
		for (Map.Entry<Integer, Integer> entry : comments.entrySet()) {
			double old = scores.get(entry.getKey());  
			double value = entry.getValue()*weight + scores.get(entry.getKey());
		    scores.put(entry.getKey(), value) ;
		}  
		
		scores = sortMapByValue(scores);
		System.out.println("");
		List<Integer> rooms_id = new ArrayList<Integer>();
		int count_tmp = 0;
		for (Map.Entry<Integer, Double> entry : scores.entrySet()) {  
			if(count_tmp == 5){break;}
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
		    rooms_id.add(entry.getKey());
		    count_tmp++;
		    
		}  
		return rooms_id;
		
	}
	
	public static void main(String[]args){
		/*
		 * NLPIR 分词初始化
		 */
//		String argu = "D:\\program\\eclipse-4.4-workspcae\\YSTServer";
//		// String system_charset = "GBK";//GBK----0
//		String system_charset = "UTF-8";
//		int charset_type = 1;
//		CLibrary instance = CLibrary.Instance;
//		int init_flag = instance.NLPIR_Init(argu, charset_type, "0");
//		System.out.println("分词系统初始化："+init_flag);
		
		
//		String res = instance.NLPIR_GetKeyWords(input, 5, true);
//		System.out.println("关键词："+res);
//		try{
//
//			String res = instance.NLPIR_ParagraphProcess("差评", 1);
//			System.out.println("分词后："+res);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		String[] fenci_jieguo = res.split(" ");
//		for(String tmp : fenci_jieguo){
//			System.out.println(tmp);
//		}
		
		

	//	String input ="心情不是特别好心情不是特别好心情不是特别好心情不是特别好心情不是特别好心情不是特别好";
		/*
		 * 情感分析
		 */
		// 初始化
//		CLibrarySentimentAnalysis caInstance = CLibrarySentimentAnalysis.Instance;
//		int flag = caInstance.LJST_Inits(
//				"D:\\program\\eclipse-4.4-workspcae\\YSTServer\\sentimentAnalysisData", 1, "");
//		if (flag == 0) {
//			System.out.println("SentimentAnalysis初始化失败");
//			System.exit(0);
//		} else {
//			System.out.println("SentimentAnalysis初始化成功");
//		}


        System.out.println("初始化开始...");
        CLibrarySentiment instance = CLibrarySentiment.Instance;
        if (instance.ST_Init("", 1, "") == 0) {
            System.out.println(CLibrarySentiment.Instance.ST_GetLastErrMsg());
            System.out.println("初始化失败");
            System.exit(1);
        } else {
            System.out.println("初始化成功");
        }
		   int num = instance.ST_ImportUserDict("./dict/sentilexicon.txt", 0);
	        System.out.printf("添加用户单词%d个\n", num);
		
		RoomService rsv = RoomService.getIstance();
		try{
			RoomModel rm = new RoomModel();
			Map<Integer, Double> res = rm.sa_room(instance);
			for(Entry<Integer, Double> entry:res.entrySet()){
				System.out.println("ROOM ID:"+entry.getKey()+"  GOOD RATE:"+entry.getValue());
			}
		}catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}finally{
			
//			caInstance.LJST_Exits();
//			instance.NLPIR_Exit();
//			CLibrary.Instance.NLPIR_Exit();
//			CLibrarySentimentAnalysis.Instance.LJST_Exits();
			CLibrarySentiment.Instance.ST_Exit();
			System.out.println("EXIT ALL FINISHED");
		}

		// 退出，释放资源
		

		
	}
}
