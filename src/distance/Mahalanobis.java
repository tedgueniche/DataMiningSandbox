package distance;

import java.util.ArrayList;
import java.util.List;

import utilities.DataFrame;
import utilities.SV;

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
	private final double[][]	covarianceMatrix;
	private final double[][]	invertedCovarianceMatrix;
	private SV meanVector = new SV();
	public Mahalanobis(DataFrame dataset) {
		for(int col = 0; col < dataset.getNumberOfColumns(); col++){
			meanVector.add(dataset.getColumnData(col).mean());
		}
		
		int numCols = dataset.getNumberOfColumns();
		covarianceMatrix = new double[numCols][numCols];
		
		/*
		 * This algorithm are adapted from
		 * http://itl.nist.gov/div898/handbook/pmc/section5/pmc541.htm
		 */
		for (int variableX = 0; variableX < numCols; variableX++) {
			covarianceMatrix[variableX][variableX] = dataset.getColumnData(variableX).var();
			for (int variableY = variableX + 1; variableY < numCols; variableY++) {
				
				double COV = 0;
				for (int row = 0; row < dataset.getNumberOfRows(); row++) {
					double xCellDiff = dataset.get(row, variableX) - dataset.getColumnData(variableX).mean();
					double yCellDiff = dataset.get(row, variableY) - dataset.getColumnData(variableY).mean();
					COV = COV + xCellDiff * yCellDiff;
				}
				COV = COV / (dataset.getNumberOfRows() - 1);
				
				covarianceMatrix[variableX][variableY] = COV;
				covarianceMatrix[variableY][variableX] = COV;
			}
		}
		
		invertedCovarianceMatrix = getInvertedMatrix(covarianceMatrix);
	}
	
	/**Gets the mahalanobis distance from the given point to the mean vector
	 * 
	 * @param fromPoint Vector to get difference of the mean from
	 * @return Distance to mean
	 */
	public double getDistance(SV fromPoint){
		return getDistance(fromPoint, meanVector);
	}
	
	/**Returns the mahalanobis distance from the given two vectors
	 * 
	 * @param pointA First vector 
	 * @param pointB Second vector
	 * @return Difference of two vectors given
	 */
	public double getDistance(SV pointA, SV pointB){
		double[][] inverstedCovariance = this.getCovarianceMatrix();
		
		return 0;
	}
	
	/**Returns the covariance matrix, it is expected that callers do not modify this*/
	public double[][] getCovarianceMatrix() {
		return covarianceMatrix;
	}
	
	/**
	 * Calculates the inverted covariance matrix using the Gauss-Jordan
	 * Elimination method
	 * 
	 * @return Inverted Covariance matrix
	 */
	public double[][] getInvertedCovarianceMatrix() {
		return invertedCovarianceMatrix;
	}
	
	/**
	 * 
	 Returns the inverted matrix of the given matrix. Does not mutate the
	 * provided matrix.
	 * 
	 * @param matrixToInvert
	 *            Matrix to invert
	 * @return Inversion of give matrix
	 */
	public double[][] getInvertedMatrix(double[][] matrixToInvert) {
		double[][] toTransform = new double[matrixToInvert.length][matrixToInvert.length];
		
		// create our right and left matrixes for Gauss-Jordan Elimination
		for (int row = 0; row < matrixToInvert.length; row++) {
			for (int col = 0; col < matrixToInvert[row].length; col++) {
				toTransform[row][col] = matrixToInvert[row][col];
			}
		}
		
		double[][] invertedCovariance = new double[matrixToInvert.length][matrixToInvert.length];
		for (int major = 0; major < invertedCovariance.length; major++) {
			invertedCovariance[major][major] = 1.0d;
		}
		
		// Clear each diagonal
		for (int rowNum = 0; rowNum < toTransform.length; rowNum++) {
			double[] transformRow = toTransform[rowNum];
			double[] invertedRow = invertedCovariance[rowNum];
			double diagValue = transformRow[rowNum];
			divideRow(diagValue, transformRow);
			divideRow(diagValue, invertedRow);
			
			for (int otherRowNum = 0; otherRowNum < toTransform.length; otherRowNum++) {
				if (otherRowNum != rowNum) {
					double[] toSubLeft = toTransform[otherRowNum];
					double[] toSubRight = invertedCovariance[otherRowNum];
					double whatToSubtract = toSubLeft[rowNum];
					subPxVFromQ(whatToSubtract, transformRow, toSubLeft);
					subPxVFromQ(whatToSubtract, invertedRow, toSubRight);
				}
			}
		}
		
		return invertedCovariance;
	}
	
	/**
	 * Takes a scalar (p) and two vectors v and q, returns the result of q - p*v
	 * 
	 * @param multiplier
	 *            Scalar to multiply toSubstract
	 * @param toSubstract
	 * @param toSubFrom
	 */
	private void subPxVFromQ(double multiplier, double[] toSubstract, double[] toSubFrom) {
		for (int col = 0; col < toSubstract.length; col++) {
			toSubFrom[col] = toSubFrom[col] - multiplier * toSubstract[col];
		}
	}
	
	/**
	 * Modifies a row, in place, by dividing it by the given scalar.
	 * 
	 * @param diagValue
	 *            value to divide the row from
	 * @param rowToModify
	 *            row to modify in place
	 */
	private void divideRow(double diagValue, double[] rowToModify) {
		for (int col = 0; col < rowToModify.length; col++) {
			rowToModify[col] = rowToModify[col] / diagValue;
		}
	}
}
