package module3;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;


/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 * Date: December 24, 2020
 * */
public class EarthquakeCityMap extends PApplet {

	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;
	private UnfoldingMap map;
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		//1. Setting the size of the applet
		size(950, 600, OPENGL);

		//2. Creating a new map with the OpenStreetMap provider
		map = new UnfoldingMap(this, 200, 50, 700, 500, new OpenStreetMap.OpenStreetMapProvider());

		//3. Setting the zoom level and adding interactivity with the event dispatcher method
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);

	    //4. Locations: importing the locations from the RSS feed and storing them in the PointFeature List
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

	    //5. Markers: creating the markers ArrayList to store all the styled markers added to the PointFeature
		// locations.
		// In here we are looping through the PointFeature array and adding a styled marker to each earthquake stored
		// in this array. createMarker helper method is responsible for creating a marker and adding a color to it.
		List<Marker> markers = new ArrayList<Marker>();
		for(PointFeature earthquake: earthquakes) {
			markers.add(createMarker(earthquake));
		}

	    //6. Adding the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}
		
	//Helper methods
	private SimplePointMarker createMarker(PointFeature feature) {
		//Creating a new marker
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation(), feature.getProperties());

		//Getting the magnitude property from the PointFeature list
		float magnitude = (float) feature.getProperty("magnitude");

		if(magnitude >= THRESHOLD_MODERATE) {
			marker.setColor(color(255, 0, 0));
		} else if(magnitude >= THRESHOLD_LIGHT) {
			marker.setColor(color(255, 255, 0));
		} else {
			marker.setColor((color(0, 0, 255)));
		}

	    return marker;
	}

	private void addMapLegend() {
		//Rectangle
		fill(255, 255, 255);
		rect(10, 50, 160, 200);
		//Text
		fill(0, 0, 0);
		text("Earthquake Data", 20, 80);
		text("5.0+ Magnitude", 34, 114);
		text("4.0+ Magnitude", 34, 144);
		text("Below 4.0 Magnitude", 34, 174);
		//Circles
		fill(255, 0, 0);
		ellipse(24, 110, 10, 10);
		fill(255, 255, 0);
		ellipse(24, 140, 10, 10);
		fill(0, 0, 255);
		ellipse(24, 170, 10, 10);
	}

	public void draw() {
		background(89, 78, 78);
	    map.draw();
	    addMapLegend();
	}
}
