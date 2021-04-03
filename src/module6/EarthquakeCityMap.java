package module6;

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
import java.util.Collections;
import java.util.List;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 * Date: January 5, 2021
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
	private CommonMarker lastSelected; // for the marker where the mouse was selected/hovered (not clicked)
	private CommonMarker lastClicked; // for the marker where there was a mouse click

	
	public void setup() {
		// 1. Setting the size of the applet
		size(900, 700, OPENGL);
		background(179, 179, 179);

		// 2. Creating a new map with the OpenStreetMap provider and adding interactivity with the event dispatcher
		// method
		map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING:
//		earthquakesURL = "test1.atom";
//		earthquakesURL = "test2.atom";
//		earthquakesURL = "quiz2.atom";


		// 3. Reading in earthquake data and geometric properties
		// STEP 1:
		// -Locations: Importing country and city features from the respective json files and storing them in the
		// Feature Lists
		// -Markers: Creating markers for the countries' and cities' locations
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}

		// STEP 2: Reading in earthquake RSS feed and storing it in the PointFeature List
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

//	    printQuakes();
//	    sortAndPrint(10);

	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	}


	public void draw() {
//		background(179, 179, 179);
		map.draw();
		addMapLegend();
	}



	// Prints earthquake markers sorted in reverse order
	private void sortAndPrint(int numToPrint) {
		ArrayList<EarthquakeMarker> quakeArray = new ArrayList<EarthquakeMarker>();
		for(Marker quake : quakeMarkers) {
			quakeArray.add((EarthquakeMarker) quake);
		}
		Collections.sort(quakeArray, Collections.reverseOrder());
		if(numToPrint > quakeArray.size()) {
			for(int i = 0; i < quakeArray.size(); i++) {
				System.out.println(quakeArray.get(i));
			}
		} else {
			for(int i = 0; i < numToPrint; i++) {
				System.out.println(quakeArray.get(i));
			}
		}
	}

	// Event handler that changes background color on key press
	@Override
	public void keyPressed() {
		switch(key) {
			case 'n':
				background(105, 122, 150);
				break;
			case 'g':
				background(129, 141, 112);
				break;
			case 'b':
				background(181, 152, 125);
				break;
				case 'p':
					for(Marker cm : cityMarkers) {
						String popStr = cm.getProperty("population").toString();
						float population = Float.parseFloat(popStr);
						if(population > 1) {
							cm.setHidden(true);
						}
					}
					break;
			default:
				background(179, 179, 179);
				for(Marker cm : cityMarkers) {
					unhideMarkers();
				}
		}
	}

	// Event handler that gets called automatically when the mouse moves.
	@Override
	public void mouseMoved() {
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);

	}

	// This method selects the marker on mouse hover. If there is a marker under the cursor, and there is no marker
	// selected, then set the selection to true on the marker.
	private void selectMarkerIfHover(List<Marker> markers) {
		// Abort if there's already a marker selected
		if (lastSelected != null) return;
		
		for (Marker m : markers) {
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

	// Event handler that gets called automatically when there is a mouse click.
	@Override
	public void mouseClicked() {
		//This captures the click anywhere on the map and unhides all the markers
		if(lastClicked != null) {
			unhideMarkers();
			hideCityPopUp();
			lastClicked = null;
		} else {
			quakeClicked();
			cityClicked();
			drawCityPopUp();
		}
	}


	//When user clicks on the earthquake marker, all other markers disappear except for the city markers that
	// are within the threat circle of this earthquake.
	private void quakeClicked() {
		//If there is already a marker (city or quake) clicked, then abort
		if(lastClicked != null) return;
		//If no marker is clicked or selected, execute below:
		for(Marker mk: quakeMarkers) {
			EarthquakeMarker marker = (EarthquakeMarker)mk;
			if(marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				for(Marker mkhide: quakeMarkers) {
					if(mkhide != lastClicked) {
						mkhide.setHidden(true);
					}
				}
				for (Marker cm : cityMarkers) {
					if (cm.getDistanceTo(marker.getLocation()) > marker.threatCircle()) {
						cm.setHidden(true);
					}
				}
				// needed in here so no more earthquakes are looped through if we already found one that is clicked
				return;
			}
		}
	}

	//When user clicks on the city marker, all other markers disappear except for the earthquakes that contain that
	// city in their threat circle.
	private void cityClicked() {
		//If there is already a marker (city or quake) clicked, then abort.
		if(lastClicked != null) return;
		//If no marker is clicked or selected, execute below:
		for(Marker marker : cityMarkers) {
			if(marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker) marker;
				for(Marker cmhide : cityMarkers) {
					if(cmhide != lastClicked) {
						cmhide.setHidden(true);
					}
				}
				for (Marker mhide : quakeMarkers) {
					EarthquakeMarker quakeMarker = (EarthquakeMarker)mhide;
					if (quakeMarker.getDistanceTo(marker.getLocation())
							> quakeMarker.threatCircle()) {
						quakeMarker.setHidden(true);
					}
				}
				// needed in here so no more cities are looped through if we already found one that is clicked
				return;
			}
		}
	}

	// Draws a city pop up window as a map legend
	private void drawCityPopUp() {
		List<EarthquakeMarker> magArr = new ArrayList<>();
		for(Marker marker : cityMarkers) {
			if(marker.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker) marker;
				fill(255, 255, 255);
				rect(10, 350, 160, 100);
				fill(0, 0, 0);
				String cityLocation = marker.getProperty("name").toString() + ", " + marker.getProperty("country").toString();
				text(cityLocation, 20, 380);

				for(Marker qm : quakeMarkers) {
					EarthquakeMarker quakeMarker = (EarthquakeMarker) qm;
					if(quakeMarker.getDistanceTo(marker.getLocation()) < quakeMarker.threatCircle()) {
						magArr.add(quakeMarker);
					}
				}
			}
		}
		String count = "Nearby earthquakes: " + magArr.size();
		double floatSum = magArr.stream().mapToDouble(quake -> quake.getMagnitude()).sum();
		String avgMag = "Average magnitude: " + (float) (floatSum / magArr.size());
		if(magArr.size() > 0) {
			text(count, 20, 400);
			text(avgMag, 20, 420);
		}
	}

	private void hideCityPopUp() {
		fill(179, 179, 179);
		strokeWeight(0);
		rect(10, 350, 160, 200);
	}


	
	// Loops over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}

	private void addMapLegend() {
		//Rectangle
		fill(255, 255, 255);
		stroke(179, 179, 179);
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
		fill(75, 104, 36);
		triangle(20, 110, 26, 98, 32, 110);
		fill(255, 255, 255);
		ellipse(26, 130, 14, 14);
		rect(20, 150, 12, 12);
		fill(247, 176, 0);
		ellipse(26, 216, 14, 14);
		fill(17, 56, 113);
		ellipse(26, 240, 14, 14);
		fill(179, 22, 27);
		ellipse(26, 264, 14, 14);
		fill(255, 255, 255);
		ellipse(26, 288, 14, 14);
		strokeWeight(1);
		line(20, 295, 32, 280);
		line(20, 280, 32, 295);
	}


	//This method checks if the earthquake occurred on land. If it did, it sets the "country" property of its
	//PointFeature to the country where it occurred (done by the isInCountry helper method) and returns true.
	private boolean isLand(PointFeature earthquake) {
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		return false;
	}


	//This method prints the number of the earthquakes that occurred on land and number of the earthquakes that
	// occurred in the ocean.
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
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
		} else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			return true;
		}
		return false;
	}

}
