package distance;

import utilities.DataFrame;

/**
 * 
 * This class is used to calculate the <a
 * href="http://en.wikipedia.org/wiki/Mahalanobis_distance">Mahalanobis
 * distance</a> It is given a {@link utilities.DataFrame DataFrame}, calculates
 * the covariance matrix, and responds to requests for either the covariance
 * matrix or to do distance calculation.
 *
 */
public class Mahalanobis {
	private final double[][] covarianceMatrix;
	public Mahalanobis(DataFrame dataset) {
		int numCols = dataset.getNumberOfColumns();
		covarianceMatrix = new double[numCols][numCols];
		
		/*This algorithm are adapted from http://itl.nist.gov/div898/handbook/pmc/section5/pmc541.htm*/
		for(int variableX = 0; variableX < numCols; variableX ++){
			covarianceMatrix[variableX][variableX] = dataset.getColumnData(variableX).var();
			for(int variableY = variableX + 1; variableY < numCols; variableY++){
				
				double COV = 0;
				for(int row = 0; row < dataset.getNumberOfRows(); row++){
					double xCellDiff = dataset.get(row, variableX) - dataset.getColumnData(variableX).mean();
					double yCellDiff = dataset.get(row, variableY) - dataset.getColumnData(variableY).mean();
					COV = COV + xCellDiff*yCellDiff;
				}
				COV = COV / (dataset.getNumberOfRows() -1);
				
				covarianceMatrix[variableX][variableY] = COV;
				covarianceMatrix[variableY][variableX] = COV;
			}
		}
		
	}
	
	public double[][] getCovarianceMatrix(){
		return covarianceMatrix;
	}
}
