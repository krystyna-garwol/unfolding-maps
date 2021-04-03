package module5;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Krystyna Garwol
 *
 */
// CommonMarker has been introduced because in this assignment there will be some drawing functionality that is common
// to all markers on our map, so we didn’t want to have to duplicate code between EarthquakeMarker and CityMarker.
// This process of restructuring our code is known as refactoring and it is very common in software engineering.
public abstract class CommonMarker extends SimplePointMarker {

	protected boolean clicked = false;

	// Constructors
	public CommonMarker(Location location) {
		super(location);
	}
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}

	// Getters
	public boolean getClicked() {
		return clicked;
	}
	
	//Setters
	public void setClicked(boolean state) {
		clicked = state;
	}

	// Method that draws marker on the map.
	public void draw(PGraphics pg, float x, float y) {
		if (!hidden) {
			drawMarker(pg, x, y);
			if (selected) {
				showTitle(pg, x, y);
			}
		}
	}

	// Abstract methods implemented in derived classes
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
}