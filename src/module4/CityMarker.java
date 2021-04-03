package module4;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 *
 */
public class CityMarker extends SimplePointMarker {

	public static final int TRI_SIZE = 6;

	//Constructors
	public CityMarker(Location location) {
		super(location);
	}
	//In here we are using a Feature object as a parameter (all the cities are stored in the Feature List)
	//We can pass an object as a parameter as long as it contains the properties that match the parameters used in
	// the constructor of the parent class we are extending. We would not be able to use in here another 3rd
	// constructor e.g. city.getId() because simply it is not included in the constructor of the parent class:
	// SimplePointMaker
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}

	//Method that draws marker on the map. This is called automatically for the SimplePointMarker, so there is no
	// need to call it explicitly in the code.
	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(96, 32, 33);
		pg.triangle(x - TRI_SIZE, y + TRI_SIZE, x, y - TRI_SIZE, x + TRI_SIZE, y + TRI_SIZE);
		pg.popStyle();
	}
	
	//Getters - because we are extending SimplePointMarker that has getProperties method (calling it in super)  we can
	// now access all the properties in the city json file
	public String getCity() {
		return getStringProperty("name");
	}
	public String getCountry() {
		return getStringProperty("country");
	}
	public float getPopulation() {
		return Float.parseFloat(getStringProperty("population"));
	}
}
