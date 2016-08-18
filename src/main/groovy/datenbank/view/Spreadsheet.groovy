package datenbank.view

import datenbank.model.Variables

import javafx.collections.FXCollections;
import javafx.event.EventHandler
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVFormat

import org.controlsfx.control.spreadsheet.GridBase
import org.controlsfx.control.spreadsheet.GridChange
import org.controlsfx.control.spreadsheet.SpreadsheetCell
import org.controlsfx.control.spreadsheet.SpreadsheetView
import org.controlsfx.control.spreadsheet.SpreadsheetCellType

class Spreadsheet {

	SpreadsheetView spv
	
	def wasChanged = false
	
	def getData() {
		
		spv.getGrid().getRows()
	}
	
	def Spreadsheet(file) {
		
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

		int columnAdd = 10 - columnCount  
		if(columnAdd < 0)
			columnAdd = 0
		
		int rowAdd = 20 - rowCount
		if(rowAdd < 0)
			rowAdd = 0
				
		GridBase grid = new GridBase(rowCount+rowAdd, columnCount+columnAdd);


		def rows = FXCollections.observableArrayList();

		for (int row = 0; row < grid.getRowCount(); ++row) {

			final def list = FXCollections.observableArrayList();

			for (int column = 0; column < grid.getColumnCount(); ++column) {
				
				if(row < rowCount && column <columnCount)
					list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1, lst[row][column]));
				else 
					list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,""));
			}

			rows.add(list);
		}

		grid.setRows(rows);



		spv = new SpreadsheetView(grid);
		
		spv.getColumns().each {
			it.setPrefWidth(100) 
		}

		grid.addEventHandler(GridChange.GRID_CHANGE_EVENT, new EventHandler<GridChange>() {
			
			public void handle(GridChange change) {
				 wasChanged = true
				}
			});
   
		
		
	}
}
