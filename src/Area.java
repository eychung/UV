import java.io.File;
import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @author Eric
 */
public class Area {

	// Generic conversion method for different coordinates systems.
	public double[] convertCRS(CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs, double x, double y) throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException, TransformException
	{
		boolean lenient = true; // relax accuracy
		MathTransform mathTransform = CRS.findMathTransform(sourceCrs, targetCrs, lenient);

		DirectPosition2D srcDirectPosition2D = new DirectPosition2D(sourceCrs, x, y);
		DirectPosition2D destDirectPosition2D = new DirectPosition2D();
		mathTransform.transform(srcDirectPosition2D, destDirectPosition2D);

		double[] coordinates = {destDirectPosition2D.x, destDirectPosition2D.y};

		return coordinates;
	}

	// Will take in Jerrid's BoundingBox as param to replace hard coded LatLong values.
	public void getBuildingsWithinArea() throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException, FactoryException, TransformException {

		// 34.192312,-118.584052
		/*double srcLat = 34.19394633058208;
		double srcLong = -118.58030233587625;
		double dstLat = 34.192312;
		double dstLong = -118.584052;*/

		// Bounds	(6385149.28,1892822.25)
		double srcLat = 6386184.71;
		double srcLong = 1893469.44;
		double dstLat = 6385149.28;
		double dstLong = 1892822.25;

		File file = new File("resources/lariac_buildings_2008.shp");
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		FeatureSource source = store.getFeatureSource();

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
		FeatureType schema = source.getSchema();
		String geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
		CoordinateReferenceSystem crs = schema.getGeometryDescriptor().getCoordinateReferenceSystem();
		ReferencedEnvelope bbox = new ReferencedEnvelope(srcLat, dstLat, srcLong, dstLong, crs);
		System.out.println("BBOX area: " + bbox.getArea());
		Filter filter = ff.bbox(ff.property(geometryPropertyName), bbox);

		// filters buildings within the bounding box of the start and end points
		FeatureCollection collection = source.getFeatures(filter);
		System.out.println("Collection size: " + collection.size());
		FeatureIterator iterator = collection.features();

		try {
			while( iterator.hasNext() ){
				Feature feature = iterator.next();
				System.out.println(feature.getIdentifier());
				System.out.println("Building ID & height: " + feature.getProperty("BLD_ID").getValue() + " & " + feature.getProperty("HEIGHT").getValue());

				/*double x = 6386184.71;
				double y = 1893469.44;
				CoordinateReferenceSystem sourceCrs = source.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
				CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:4326");

				double x = 34.19394633058208;
				double y = -118.58030233587625;
				CoordinateReferenceSystem sourceCrs = CRS.decode("EPSG:4326");
				CoordinateReferenceSystem targetCrs = source.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();

				double[] temp = convertCRS(sourceCrs, targetCrs, x, y);
				System.out.println(temp[0] + " & " + temp[1]);*/


				// TODO: create buildings from feature and store into array
			}
		}
		finally {
			iterator.close();
		}
	}

}
