package datenbank.view

import datenbank.model.Variables

import javafx.collections.FXCollections;
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVFormat

import org.controlsfx.control.spreadsheet.GridBase
import org.controlsfx.control.spreadsheet.SpreadsheetCell
import org.controlsfx.control.spreadsheet.SpreadsheetView
import org.controlsfx.control.spreadsheet.SpreadsheetCellType

class Spreadsheet {

	def setup(file) {

		def lst
		file.withReader { reader ->

			def csv = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(Variables.csvSeperator as char))
			lst = csv.asList()

		}
		
		int rowCount = lst.size();
		int columnCount = 0;
		lst.each {			
			if(it.size() > columnCount)
				columnCount = it.size()
		}


		GridBase grid = new GridBase(rowCount, columnCount);

		def rows = FXCollections.observableArrayList();

		for (int row = 0; row < grid.getRowCount(); ++row) {

			final def list = FXCollections.observableArrayList();

			for (int column = 0; column < grid.getColumnCount(); ++column) {

				list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,lst[row][column]));
			}

			rows.add(list);
		}

		grid.setRows(rows);



		SpreadsheetView spv = new SpreadsheetView(grid);
		return spv
	}
}
