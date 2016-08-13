package com.ov.exercice.sncf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Consigne:
 * Enregistrez vous sur https://data.sncf.com/api (http://www.navitia.io/register/) pour récupérer une clé d'API.
 * Ce code va servir à récupérer les horaires de trains au départ de montparnasse.
 * Puis ensuite les afficher sous forme Heure : destination
 * Vous utiliserez votre clé dans l'url d'appel de l'API.
 * Il ne respecte pas les standards et doit être nettoyé puis refactoré pour
 * être réutilisable et compréhensible.
 */

public class gettrajets {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			HttpURLConnection c = (HttpURLConnection) (new URL("https://api.sncf.com/v1/coverage/sncf/stop_areas/stop_area:OCE:SA:87391003/departures?datetime=20160729T150423")).openConnection();
			c.setRequestProperty("Authorization", "Basic " + (Base64.getUrlEncoder().encodeToString("374817c5-d83d-4eb0-a42e-2ad1811d6794:".getBytes())));
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.connect();
			int b = c.getResponseCode();
			String json = "";
			switch (b) {
			case 200:
			case 201:
				BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
				StringBuilder a = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					a.append(line+"\n");
				}
				r.close();
				json = a.toString();
				JSONArray f = (JSONArray) (((JSONObject)(new JSONParser()).parse(json)).get("departures"));
				System.out.println("Prochains départs de Montparnasse :");
				for (int i=0 ; i<f.size()-1 ; i++) {
					System.out.println(((JSONObject)((JSONObject)f.get(i)).get("stop_date_time")).get("departure_date_time")+" : "+((JSONObject)((JSONObject)((JSONObject)f.get(i)).get("route")).get("line")).get("name"));
				}
			}
		} catch (MalformedURLException iException) {
			// TODO Auto-generated catch block
			System.err.println(iException);
		} catch (IOException iException) {
			// TODO Auto-generated catch block
			System.err.println(iException);
		} catch (ParseException iException) {
			// TODO Auto-generated catch block
			System.err.println(iException);
		}
	}
}
