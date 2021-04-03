package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 *
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;

	//Constructors
	public CityMarker(Location location) {
		super(location);
	}
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}

	//Draws marker on the map - abstract method defined in the parent class
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(96, 32, 33);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		pg.popStyle();
	}
	
	//Shows the title of the city if this marker is selected
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		pg.fill(0, 0, 0);
		pg.text(getCity() + ", " + getCountry() + ", " + getPopulation(), x, y);
	}
	

	//Getters
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
