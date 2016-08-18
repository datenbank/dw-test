package datenbank.event

import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.application.Platform;

class SpreadsheetSave implements EventHandler<ActionEvent> {

	def init

	def file
	def spreadsheet
	
	def stage
	def fileMenu
	def saveItem
	
	def save() {
		Thread.start {
			try {
				
				Platform.runLater(new Runnable() {
					@Override public void run() {
						stage.getScene().setCursor(Cursor.WAIT);
						fileMenu.setDisable(true)
						saveItem.setDisable(true)
					}
				})
				
				
				
				file.write("")

				spreadsheet.getData().each { row ->
					def i = row.size()
					def j = 1
					row.each { col ->

						def attr = col.item ? col.item : ""

						if(attr.contains("${Variables.csvSeperator}"))
							attr = "\"${attr}\""

						if(i>j)
							file << "${attr}${Variables.csvSeperator}"
						else
							file << "${attr}\r\n"
						j++
					}
				}
				
				Platform.runLater(new Runnable() {
					@Override public void run() {
						stage.getScene().setCursor(Cursor.DEFAULT);
						fileMenu.setDisable(false)
						saveItem.setDisable(false)
					}
				})
				
				init.ui.confirm("Saved", "The file is saved.")
			} catch(all) {
				init.ui.alert("Saved", "The file didn't save properly.\n $all")
				Platform.runLater(new Runnable() {
					@Override public void run() {
						stage.getScene().setCursor(Cursor.DEFAULT);
						fileMenu.setDisable(false)
						saveItem.setDisable(false)
					}
				})
			}
		}
	}
	

	@Override
	public void handle(ActionEvent arg0) {
		save()
	}
}
