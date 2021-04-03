package module6;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LifeExpectancy extends PApplet {

	private UnfoldingMap map;
	Map<String, Float> lifeExpByCountry;
	List<Feature> countries;
	List<Marker> countryMarkers;

	public void setup() {
		//1. Setting the size of the applet window
		size(800, 600, OPENGL);

		//2. Creating a new map and allowing user to interact with the map with event dispatcher
		map = new UnfoldingMap(this, 50, 50, 700, 500, new OpenStreetMap.OpenStreetMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);

		//3. Importing the data:
		//Life expectancy data from the csv file
		lifeExpByCountry = loadLifeExpectancyFromCSV("LifeExpectancyWorldBankModule3.csv");
		//Importing Locations for the countries from the json file
		countries = GeoJSONReader.loadData(this, "countries.geo.json");

		//4. Creating the markers for the locations using in-built method. This in-built method adds the background
		// colour to the country rather than adding simple point marker to the country.
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);

		//5. Adding colours to the markers
		shadeCountries();
	}

	//Helper methods
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) {
		//Create a HashMap object that will store the countries ID and life expectancy
		Map<String, Float> lifeExpMap = new HashMap<String, Float>();
		//Read the data from the file in form of string rows
		String[] rows = loadStrings(fileName);

		//Iterate through the rows array and retrieve the relevant data from the columns
		for(String row: rows) {
			//Split the rows into columns, store them as String in the array so we can then retrieve data by e.g. columns[4]
			String[] columns = row.split(",");
			if (columns.length == 6 && !columns[5].equals("..")) {
				lifeExpMap.put(columns[4], Float.parseFloat(columns[5]));
			}
		}
		return lifeExpMap;
	}

	private void shadeCountries() {
		//1. Because we are adding the colours to the markers we need to go through the markers array
		//2. Get the country id for each marker from the markers array(because markers have been added to the
		// countries locations they have got locations data and all the properties from the json file)
		//3. Check if the csv file(life expectancy data) contains the country id from the markers array. If it does:
		//- Get the life expectancy from the csv file
		//- Create a color level by translating the life expectancy value range to the rgb color range using in-build
		// map() function
		// - Set the colour, otherwise if there is not id found in the csv file, set the default color

		for(Marker marker: countryMarkers) {
			String countryId = marker.getId();

			if(lifeExpByCountry.containsKey(countryId)) {
				float lifeExp = lifeExpByCountry.get(countryId);
				int colorLevel = (int) map(lifeExp, 40, 90, 10, 255);
				marker.setColor(color(255-colorLevel, 100, colorLevel));
			} else {
				marker.setColor(color(150, 150, 150));
			}
		}

	}

	public void draw() {
		background(89, 78, 78);
		map.draw();
	}

}
