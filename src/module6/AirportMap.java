package module6;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private CommonMarker lastClicked;
	
	public void setup() {
		// 1. Setting the size of the applet
		size(850,700, OPENGL);

		// 2. Creating a new map and adding interactivity with the event dispatcher method
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// 3. Getting features from airport data file
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// 4. Creating markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();

		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		}
		
		
		// 5. Parsing a route data and adding route markers
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			routeList.add(sl);
		}

//		map.addMarkers(routeList);
		map.addMarkers(airportList);
		System.out.println(airportList.get(0).getProperties());

	}
	
	public void draw() {
		background(0);
		map.draw();
	}

	@Override
	public void mouseClicked() {
		//This captures the click anywhere on the map and unhides all the markers
		if(lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		} else {
			airportClicked();
		}
	}

	// This method shows only airports belonging to the country which marker was clicked, hiding all other airports
	private void airportClicked() {
		if(lastClicked != null) return;
		for(Marker marker : airportList) {
			if(marker.isInside(map, mouseX, mouseY)){
				lastClicked = (CommonMarker) marker;
				for(Marker mhide : airportList) {
					String markerCountry = marker.getProperty("country").toString();
					String mhideCountry = mhide.getProperty("country").toString();
					if(!mhideCountry.equals(markerCountry)) {
						mhide.setHidden(true);
					}
				}
			}
		}
	}

	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}

}
