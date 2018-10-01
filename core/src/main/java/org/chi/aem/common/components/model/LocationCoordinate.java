/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.Default;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;



@Model(adaptables = Resource.class)
public class LocationCoordinate {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(LocationCoordinate.class);
	    
	    public static final String PROP_LOCATION_ADDRESS = "address";
		public static final String PROP_LOCATION_ADDRESS_PLACE_ID = "addressPlaceId";

		@Inject
		private ResourceResolver resourceResolver;
	
		@Inject
		@Named(PROP_LOCATION_ADDRESS)
		@Optional
		@Default(values="11045 East Lansing Circle, Englewood, CO")
		private String address;

		@Inject
		@Named(PROP_LOCATION_ADDRESS_PLACE_ID)
		@Optional
		private String addressPlaceId;

		@SlingObject
		private Resource resource;
		
		private String latCord;
		private String lngCord;
		private String placeId = "";
		private int hash = Math.abs((int)Math.random());
	
		@PostConstruct
		protected void init() {
            InputStream inputStream = null;
            String json = "";

            // get hash code
			if (resource != null) {
				hash = Math.abs(resource.getPath().hashCode());
			} else if (address != null){
				hash = Math.abs(address.hashCode());
			}

            try {           
                // HttpClient client = new DefaultHttpClient();  
                HttpClient client = HttpClientBuilder.create().build();
                // HttpPost post = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address=10115,germany");
                HttpPost post = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(address, "UTF-8") + "&key=AIzaSyDZub6OMs7Qcg_lzqxXgL3nvobpcFK69q8");
                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
            } catch(Exception e) {
            	LOGGER.error("Inside httpCall Exception :");
            	e.printStackTrace();
            }

            try {           
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
                StringBuilder sbuild = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sbuild.append(line);
                }
                inputStream.close();
                json = sbuild.toString();      
                LOGGER.debug("Location json:" + json);

	            //now parse
	            // JSONParser parser = new JSONParser();
	            // Object obj = parser.parse(json);
	            JSONObject jb = new JSONObject(json);
	
	            //now read
	            JSONArray jsonObject1 = (JSONArray) jb.get("results");
	            JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
	            JSONObject jsonObject3 = (JSONObject)jsonObject2.get("geometry");

	            JSONObject location = (JSONObject) jsonObject3.get("location");
	
	            latCord = location.get("lat").toString();
	            LOGGER.debug("latCord:" + latCord);
	            lngCord = location.get("lng").toString();
	            LOGGER.debug("lngCord:" + lngCord);

	            if (StringUtils.isEmpty(addressPlaceId)) {
					placeId = jsonObject2.getString("place_id");
				} else {
					placeId = addressPlaceId;
				}

            } catch(Exception e) {
            	LOGGER.error("Inside json read Exception");
            	e.printStackTrace();
            }
		}

		public int getHash() {
			return hash;
		}
	
		public String getLatCord() {
			return latCord;
		}
	
		public String getLngCord() {
			return lngCord;
		}

		public String getPlaceId() {
		return placeId;
	}
}