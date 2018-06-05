package com.weavers.duqhan.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.weavers.duqhan.business.impl.AndroidPushNotificationsService;
import com.weavers.duqhan.dao.GuestFcmTokenDao;
import com.weavers.duqhan.dao.UsersDao;
import com.weavers.duqhan.domain.GuestFcmToken;
import com.weavers.duqhan.domain.Users;
import java.util.Objects;
import java.util.Random;

@CrossOrigin
@Configuration
@EnableScheduling
@RestController
@Transactional
@RequestMapping("/push/**")
public class PushNotification {

	private final String TOPIC = "JavaSampleApproach";
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;
	
	@Autowired
	UsersDao usersDao;
	
	@Autowired
	GuestFcmTokenDao guestFcmTokenDao;
	
	@Scheduled(cron = "0 0 7 * * ?")
	//@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> send(){
		Map<String, Object> result = new HashMap<String, Object>();
		List<GuestFcmToken> users = new ArrayList<GuestFcmToken>();
		users=guestFcmTokenDao.loadAll();
		Map<String,String> offerCodes=returnRandom();
		try{
			for (GuestFcmToken users2 : users) {
				if(!users2.getToken().equals(""))
					send(users2.getToken(),offerCodes);
			}
			result.put("status", "success");
		}catch (Exception e) {
			result.put("status", "error");
			e.printStackTrace();
		}
		return result;
	}
	
	public Map<String,String> returnRandom() {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String> finalResult = new HashMap<String, String>();
		result.put("10", "grab10");
		result.put("15", "duqhan15");
		result.put("20", "special20");
		result.put("25", "boom25");
		result.put("40", "big40");
		List<String> keysAsArray = new ArrayList<String>(result.keySet());
		Random r = new Random();
		int index = r.nextInt(keysAsArray.size());
		String discount=keysAsArray.get(index);
		String code=result.get(keysAsArray.get(index));
		finalResult.put(discount, code);
		return finalResult;
	}
	
	public ResponseEntity<String> send(String deviceId,Map<String,String> offerCode) throws JSONException {
		Map.Entry<String,String> entry = offerCode.entrySet().iterator().next();		
				JSONObject body = new JSONObject();
				body.put("to",deviceId);
				body.put("priority", "high");

				JSONObject notification = new JSONObject();
				notification.put("title", "Today's Offer");
				notification.put("body", "Get "+entry.getKey()+"% discount "+"use "+entry.getValue());
				
				JSONObject data = new JSONObject();
				data.put("Key-1", "JSA Data 1");
				data.put("Key-2", "JSA Data 2");

				body.put("notification", notification);
				body.put("data", data);

				HttpEntity<String> request = new HttpEntity<>(body.toString());

				CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
				CompletableFuture.allOf(pushNotification).join();

				try {
					String firebaseResponse = pushNotification.get();
					
					return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
		
	}
}
