package com.splitit.splitit.backend.Notifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onesignal.client.ApiClient;
import com.onesignal.client.ApiException;
import com.onesignal.client.Configuration;
import com.onesignal.client.auth.*;
import com.onesignal.client.model.*;
import com.onesignal.client.model.Notification.TargetChannelEnum;
//import com.onesignal.client.model.BasicNotification.TargetChannelEnum;
import com.onesignal.client.model.SubscriptionObject.TypeEnum;
import com.splitit.splitit.backend.DAO.Parametro;
import com.splitit.splitit.backend.DTO.DtGastoCompartido;
import com.splitit.splitit.backend.Repository.ParametroRepository;
import com.onesignal.client.api.DefaultApi;

//@org.springframework.context.annotation.Configuration
//@PropertySource("classpath:/resources/application.properties")
public class OneSignal {
	
//	@Autowired
//    Environment env;
	
	private static String appId = "76b3ef2a-9820-46bf-99d4-cd2475005fd4";
	private static String appKeyToken = "MWQ5MmI5MTEtZjg2Yi00MTU4LWFlMWUtOWYzMTEwZjk5OTYy";
//	private static final String userKeyToken = "YOUR_USER_TOKEN";
	
	private DefaultApi api;
	
	
	public OneSignal() {
		
		//Setting up the client
	    ApiClient defaultClient = Configuration.getDefaultApiClient();
	    HttpBearerAuth appKey = (HttpBearerAuth) defaultClient.getAuthentication("app_key");
	    appKey.setBearerToken(appKeyToken);
	    
	    this.api = new DefaultApi(defaultClient);
	}
	
	public void createUser(String email) {
	    //Creo un usuario
	    User user = new User(); // User | 
	    Map<String, Object> identity = new HashMap<String, Object>();
	    identity.put("external_id", email);
	    user.setIdentity(identity);
	    try {
	        User result = api.createUser(appId, user);
	        System.out.println("OneSignal - Usuario creado con exito");
	        System.out.println(result);
	        
//	        crearSubscripcion(email);
	      } catch (ApiException e) {
	        System.err.println("Exception when calling DefaultApi#createUser");
	        System.err.println("Status code: " + e.getCode());
	        System.err.println("Reason: " + e.getResponseBody());
	        System.err.println("Response headers: " + e.getResponseHeaders());
	        e.printStackTrace();
	      }
	}
	
	private void crearSubscripcion(String email) {
		//Le creo una suscripcion al usuario con:
//			external_id: email
//			type: inicial
		String aliasLabel = "external_id"; // String | 
		String aliasId = email; // String | 
		CreateSubscriptionRequestBody createSubscriptionRequestBody = new CreateSubscriptionRequestBody(); // CreateSubscriptionRequestBody |
		SubscriptionObject subsParms = new SubscriptionObject();
		subsParms.type(TypeEnum.ANDROIDPUSH);
		subsParms.token("inicial");
		createSubscriptionRequestBody.setSubscription(subsParms);
		try {
			InlineResponse201 result = api.createSubscription(appId, aliasLabel, aliasId, createSubscriptionRequestBody);
			System.out.println("OneSignal - Suscripcion inicial creada con exito.");
			System.out.println(result);
		} catch (ApiException e) {
			System.err.println("Exception when calling DefaultApi#createSubscription");
			System.err.println("Status code: " + e.getCode());
			System.err.println("Reason: " + e.getResponseBody());
			System.err.println("Response headers: " + e.getResponseHeaders());
			e.printStackTrace();
		}
	}
	
	public void createNotification(TipoInternoNotificacion tipoNotificacion, List<String> listaMailsParticipantes, String titulo) {
		try {
			List<String> listaExternalIds = new ArrayList<String>();
			for (String usrMail : listaMailsParticipantes) {
		    	if (api.fetchUser(appId, "external_id", usrMail).getSubscriptions() != null) {
					listaExternalIds.add(usrMail);
				}
		    }
			if (listaExternalIds.isEmpty()) {
				return;
			}
			
			Notification notification = new Notification();
		    notification.setAppId(appId);
		    notification.setIsAndroid(true);	    
		    notification.setIncludeExternalUserIds(listaExternalIds);
		    
		    StringMap content = new StringMap();
//		    switch (tipoNotificacion) {
//		    	case NUEVOGASTO:
////		    		DtGastoCompartido datosGasto = (DtGastoCompartido) data;
//		    		content.en("Nuevo gasto en grupo");
//		    	    break;
//				case PAGOENVIADO:
//					content.en("Has realizado un pago");
//					break;
//				case PAGORECIBIDO:
//					content.en("Has recibido un pago");
//					break;
//		    }
		    content.en(titulo);
		    notification.setContents(content);
//		    notification.setData(data);
		    	    
		    notification.setTargetChannel(TargetChannelEnum.PUSH);
		
		    // Sending the request
			api.createNotification(notification);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
	}
}
