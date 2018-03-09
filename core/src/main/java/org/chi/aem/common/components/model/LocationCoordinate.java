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
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.Default;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;



@Model(adaptables = Resource.class)
public class LocationCoordinate {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(LocationCoordinate.class);
	    
	    public static final String PROP_LOCATION_ADDRESS = "address";

		@Inject
		private ResourceResolver resourceResolver;
	
		@Inject
		@Named(PROP_LOCATION_ADDRESS)
		@Optional
		@Default(values="11045 East Lansing Circle, Englewood, CO")
		private String address;
		
		private String latCord;
		private String lngCord;
	
	
		@PostConstruct
		protected void init() {
            InputStream inputStream = null;
            String json = "";

            try {           
                HttpClient client = new DefaultHttpClient();  
                LOGGER.info("After client" + address + client);
                // HttpPost post = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address=10115,germany");
                HttpPost post = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address="+URLEncoder.encode(address, "UTF-8") + "&sensor=false");
                LOGGER.info("After post");
                HttpResponse response = client.execute(post);
                LOGGER.info("After response");
                HttpEntity entity = response.getEntity();
                LOGGER.info("After entity");
                inputStream = entity.getContent();
                LOGGER.info("After inputStream");
            } catch(Exception e) {
            	
            	LOGGER.info("Inside Exception :" + inputStream);

            }

            try {           
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
                StringBuilder sbuild = new StringBuilder();
                String line = null;
                LOGGER.info("After line");
                while ((line = reader.readLine()) != null) {
                    sbuild.append(line);
                }
                inputStream.close();
                json = sbuild.toString();      
                LOGGER.info("json:" + json);

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
            LOGGER.info("latCord:" + latCord);
            lngCord = location.get("lng").toString();
            LOGGER.info("lngCord:" + lngCord);
            } catch(Exception e) {
            	LOGGER.info("Inside Exception latCord:" + latCord);
            	LOGGER.info("Inside Exception  lngCord:" + lngCord);
            }
		}
	
		public String getLatCord() {
			return latCord;
		}
	
		public String getLngCord() {
			return lngCord;
		}
}