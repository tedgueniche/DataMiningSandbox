package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * This is a class that holds a series of numeric (double) vectors.
 * 
 * It maintains information on the series (ex. mean, average, sd).
 *
 * A {@link #DataFrame()} presumes that all rows it is asked to {@link #addRow(SV) insert}
 * are of the same dimension and that its rows aren't altered after insertion.
 * The behaviour when this is not true is unspecified.
 */
public class DataFrame {
	/** The rows of the data frame */
	private List<SV>				rows				= new ArrayList<>();
	/** The data, as given in {@link #rows} but in columnar form */
	private HashMap<Integer, SV>	columnarData		= new HashMap<>();
	
	private Function<Integer, SV>	createNewSVIfAbsent	= (t) -> new SV();
	
	/** Creates an empty data frame */
	public DataFrame() {
	}
	
	/**
	 * Adds a new row to this data frame.
	 * 
	 * @param newRow
	 *            The row to add.
	 */
	public void addRow(SV newRow) {
		assert (rows.size() > 0 ? newRow.size() == columnarData.size() : true) : "Row did not have the same number of columns as previous rows inserted in the model.";
		for (int curDim = 0; curDim < newRow.getCount(); curDim++) {
			SV thisColumn = columnarData.computeIfAbsent(curDim, createNewSVIfAbsent);
			Double thisDimValue = newRow.get(curDim);
			thisColumn.append(thisDimValue);
		}
		rows.add(newRow);
	}
	
	/**
	 * Get the number of columns in each row.
	 * 
	 * @return Number of columns
	 */
	public int getNumberOfColumns() {
		return columnarData.size();
	}
	
	/**
	 * Get the number of columns in each row.
	 * 
	 * @return Number of columns
	 */
	public int getNumberOfRows() {
		return rows.size();
	}
	
	/**
	 * Returns a cell value from this table. The rows and column are zero-indexed.
	 * 
	 * @param row
	 *            Row to select
	 * @param column
	 *            Column to select
	 * @return The value of the cell
	 */
	public Double get(int row, int column) {
		return rows.get(row).get(column);
	}
	
	/**
	 * Returns the {@link utilities.SV} that represents the given column. The columns are zero-indexed.
	 * 
	 * @param column
	 *            Column number to select
	 * @return The {@link utilities.SV} that represents the given row.
	 */
	public SV getColumnData(int column) {
		return columnarData.get(column);
	}
	
	/**
	 * Returns the {@link utilities.SV} that represents the given row. The rows are zero-indexed.
	 * 
	 * @param row
	 *            Row number to select
	 * @return The {@link utilities.SV} that represents the given row.
	 */
	public SV getRowData(int row) {
		return rows.get(row);
	}
}
