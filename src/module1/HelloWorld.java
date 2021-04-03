package module1;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Krystyna Garwol
  * Date: December 20, 2020
  * */

public class HelloWorld extends PApplet {
	UnfoldingMap map1;
	UnfoldingMap map2;

	public void setup() {
		// Set up the Applet window to be 850x600 and processing library's 2D drawing
		size(850, 600, P2D);

		// Set the background color for the Applet (rgb)
		this.background(255, 245, 245);

		// Select a map provider and zoom level
		AbstractMapProvider provider = new OpenStreetMap.OpenStreetMapProvider();
		int zoomLevel = 10;

		// 1. Create a new UnfoldingMap to be displayed in this window.
		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);
		map2 = new UnfoldingMap(this, 450, 50, 350, 500, provider);

		// 2. Zoom in and center the map at 32.9 (latitude) and -117.2 (longitude) - aka create location
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		map2.zoomAndPanTo(zoomLevel, new Location(51.3f, -0.3f));

		// 3. Make the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		MapUtils.createDefaultEventDispatcher(this, map2);

	}

	/** Draw the Applet window.  */
	public void draw() {
		map1.draw();
		map2.draw();
	}
}
