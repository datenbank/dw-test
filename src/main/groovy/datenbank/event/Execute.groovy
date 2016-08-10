package datenbank.event

import datenbank.engine.Executor
import datenbank.model.TestCase
import javafx.event.ActionEvent
import javafx.event.EventHandler

class Execute implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		def testCase = (TestCase) init.ui.tv.getSelectionModel().getSelectedItem();
		if(testCase) {
			Thread.start {

				init.ui.btnUpdate(true)
				init.ex.runOne(testCase)
				init.ui.btnUpdate(false)
			}
		} else {
			init.ui.alert("Couldn't execute", "No test case selected.")
		}
	}
}
