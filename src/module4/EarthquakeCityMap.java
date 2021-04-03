package module4;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 * Date: December 28, 2020
 * */
public class EarthquakeCityMap extends PApplet {

	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
//	private String earthquakesURL;
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	private UnfoldingMap map;
	private List<Marker> cityMarkers;
	private List<Marker> quakeMarkers;
	private List<Marker> countryMarkers;

	public void setup() {
		//1. Setting the size of the applet
		size(900, 700, OPENGL);

		//2. Creating a new map with the OpenStreetMap provider and adding interactivity with the event dispatcher method
		map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//FOR TESTING
//		earthquakesURL = "test1.atom";
//		earthquakesURL = "test2.atom";
//		earthquakesURL = "quiz1.atom";
		
		
		//3. Reading in earthquake data and geometric properties
	    //STEP 1:
		//-Locations: Importing country and city features from the respective json file and storing them in the Feature
		// List
		//-Markers: Creating markers for the countries' and cities' locations
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//STEP 2: Reading in earthquake RSS feed and storing it in the PointFeature List
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    printQuakes();

	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	}

	public void draw() {
		background(89, 78, 78);
		map.draw();
		addMapLegend();
	}

	//HELPER METHODS
	private void addMapLegend() {
		//Rectangle
		fill(255, 255, 255);
		rect(10, 50, 160, 260);
		//Text
		fill(0, 0, 0);
		text("Earthquake Data", 20, 80);
		text("City Marker", 40, 110);
		text("Land Quake", 40, 134);
		text("Ocean Quake", 40, 160);
		text("Size - Magnitude", 20, 190);
		text("Shallow", 40, 220);
		text("Intermediate", 40, 244);
		text("Deep", 40, 268);
		text("Past hour", 40, 292);
		//Shapes
		fill(96, 32, 33);
		triangle(20, 110, 26, 98, 32, 110);
		fill(255, 255, 255);
		ellipse(26, 130, 14, 14);
		rect(20, 150, 12, 12);
		fill(255, 255, 0);
		ellipse(26, 216, 14, 14);
		fill(0, 0, 255);
		ellipse(26, 240, 14, 14);
		fill(255, 0, 0);
		ellipse(26, 264, 14, 14);
		fill(255, 255, 255);
		ellipse(26, 288, 14, 14);
		strokeWeight((float) 2.0);
		line(20, 295, 32, 280);
		line(20, 280, 32, 295);
	}

	//This method checks if the earthquake occurred on land. If it did, it sets the "country" property of its
	//PointFeature to the country where it occurred (done by the isInCountry helper method) and returns true.
	private boolean isLand(PointFeature earthquake) {
		for (Marker marker : countryMarkers) {
			if(isInCountry(earthquake, marker)) {
				return true;
			}
		}
		return false;
	}

	//This method prints the number of the earthquakes that occurred on land and number of the earthquakes that
	// occurred in the ocean. There are 2 solutions included in here:
	//1. Using hashMap - more effective solution as we are looping only through the quakeMarkers
	//2. Using nested loop as suggested in the assignment - less effective as we are looping through the
	// countryMarkers as well as quakeMarkers - this solution is commented out below
	private void printQuakes() {
		//Creating an empty quakeMap to store filtered data from the quakeMarkers list
		Map<String, Integer> quakeMap = new LinkedHashMap<String, Integer>();
		//Setting the initial value for ocean quakes so can add values to it later
		quakeMap.put("OCEAN QUAKES: ", 0);

		for(Marker quakeMarker: quakeMarkers) {
			if(quakeMarker instanceof LandQuakeMarker) {
				String countryName = (String) quakeMarker.getProperty("country");
				//Checking if the name already exist in the quakeMap, if it does, we are increasing earthquake value
				// for this country
				if(quakeMap.containsKey(countryName)) {
					int value = quakeMap.get(countryName);
					quakeMap.put(countryName, ++value);
				} else {
					quakeMap.put(countryName, 1);
				}
			} else {
				int value = quakeMap.get("OCEAN QUAKES: ");
				quakeMap.put("OCEAN QUAKES: ", ++value);
			}
		}
		for(String key: quakeMap.keySet()) {
			System.out.println(key + ": " + quakeMap.get(key));
		}

//		System.out.println("...");
//		System.out.println("LAND QUAKES:");
//		int totalQuakeCount = 0;
//		for(Marker cm: countryMarkers) {
//			String name = (String) cm.getProperty("name");
//			int quakeCount = 0;
//			for(Marker qm: quakeMarkers) {
//				String country = (String) qm.getProperty("country");
//				if(qm instanceof LandQuakeMarker) {
//					if(country.equals(name)) {
//						quakeCount++;
//					}
//				}
//			}
//			if(quakeCount >= 1) System.out.println(name + ": " + quakeCount);
//			totalQuakeCount += quakeCount;
//		}
//		System.out.println("...");
//		System.out.println("OCEAN QUAKES: " + (quakeMarkers.size() - totalQuakeCount));
	}

	// Checks whether a given earthquake is in a given country. We are comparing here the earthquake
	// data from the RSS feed with the countryMarkers list, created from the countries json file. This method
	// also adds the country property to the properties of the earthquake feature if it's in one of the countries.
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		Location checkLoc = earthquake.getLocation();
		if(country.getClass() == MultiMarker.class) {
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
					return true;
				}
			}
		}
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			return true;
		}
		return false;
	}

}