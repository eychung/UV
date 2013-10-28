import java.io.IOException;

import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;


public class Main {

	/**
	 * @param args
	 * @throws TransformException 
	 * @throws FactoryException 
	 * @throws IOException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws MismatchedDimensionException 
	 */
	public static void main(String[] args) throws MismatchedDimensionException, NoSuchAuthorityCodeException, IOException, FactoryException, TransformException {
		Area a = new Area();
		a.getBuildingsWithinArea();
	}

}
