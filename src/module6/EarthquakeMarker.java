package module6;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 *
 */
public abstract class EarthquakeMarker extends CommonMarker implements Comparable<EarthquakeMarker> {
	protected boolean isOnLand;
	protected float radius;
	protected static final float kmPerMile = 1.6f;
	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;
	public static final float THRESHOLD_INTERMEDIATE = 70;
	public static final float THRESHOLD_DEEP = 300;


	// Abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// Constructor
	public EarthquakeMarker (PointFeature feature) {
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
	}
	

	// Sorts earthquakes in reverse order of magnitude
	public int compareTo (EarthquakeMarker other) {
		Float magnitude = this.getMagnitude();
		Float otherMagnitude = other.getMagnitude();

		return magnitude.compareTo(otherMagnitude);
	}


	//Draws marker on the map - abstract method defined in the parent class
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.pushStyle();
		colorDetermine(pg);
		drawEarthquake(pg, x, y);

		String age = getStringProperty("age");
		if ("Past Hour".equals(age) || "Past Day".equals(age)) {
			pg.strokeWeight(1);
			pg.stroke(38, 38, 38);
			int buffer = 2;
			pg.line(x-(radius+buffer), 
					y-(radius+buffer), 
					x+radius+buffer, 
					y+radius+buffer);
			pg.line(x-(radius+buffer), 
					y+(radius+buffer), 
					x+radius+buffer, 
					y-(radius+buffer));
			
		}
		pg.popStyle();
	}



	//Shows the title of the earthquake if this marker is selected
	public void showTitle(PGraphics pg, float x, float y) {
		String title = getTitle();
		pg.pushStyle();

		//Box
		pg.rectMode(PConstants.CORNER);
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);

		//Text
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);

		pg.popStyle();
	}



	// Returns the "threat circle" radius, or distance up to which this earthquake can affect things, for this
	// earthquake. DISCLAIMER: this formula is for illustration purposes only and is not intended to be used for
	// safety-critical or predictive applications.
	public double threatCircle() {	
		double miles = 20.0f * Math.pow(1.8, 2*getMagnitude()-5);
		double km = (miles * kmPerMile);
		return km;
	}

	//Determines color of marker based on earthquake's depth
	private void colorDetermine(PGraphics pg) {
		float depth = getDepth();
		
		if (depth < THRESHOLD_INTERMEDIATE) {
			pg.fill(247, 176, 0);
		}
		else if (depth < THRESHOLD_DEEP) {
			pg.fill(17, 56, 113);
		}
		else {
			pg.fill(179, 22, 27);
		}
	}


	// Returns the string representation of an earthquake marker.
	public String toString() {
		return getTitle();
	}

	// Getters
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	public String getTitle() {
		return (String) getProperty("title");
	}
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	public boolean isOnLand() {
		return isOnLand;
	}

}
