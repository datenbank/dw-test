package datenbank.event

import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class NewTestCase implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		try {
			def tcName = init.ui.input("New test case", "Enter name")

			def file = new File("${Variables.path}Target/${tcName}.sql")
			if(!file.exists()) {
				file << ""
				init.summary = init.init()
			} else {
				init.ui.alert("New test case", "A test case with that name already exists.")
			}
		} catch(all) {
			init.ui.alert("New test case", "Was not created")
		}
	}
}
