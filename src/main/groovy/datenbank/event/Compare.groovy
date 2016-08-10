package datenbank.event

import datenbank.engine.Executor
import datenbank.model.TestCase
import javafx.event.ActionEvent
import javafx.event.EventHandler

class Compare implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		def testCase = (TestCase) init.ui.tv.getSelectionModel().getSelectedItem();
		if(testCase) {
			Thread.start {

				init.ui.btnUpdate(true)
				init.rt.runOne(testCase)
				init.ui.btnUpdate(false)
			}
		} else {
			init.ui.alert("Couldn't compare", "No test case selected.")
		}
	}
}
