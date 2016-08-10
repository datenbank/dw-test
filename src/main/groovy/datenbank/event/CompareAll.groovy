package datenbank.event

import datenbank.engine.ResultTester
import javafx.event.ActionEvent
import javafx.event.EventHandler

class CompareAll implements EventHandler<ActionEvent> {
	
	def init
		
	@Override
	public void handle(ActionEvent arg0) {
		Thread.start {
			if(init.summary.testCases.size() > 0) {
				init.ui.btnUpdate(true)
				init.rt.runAll(init.summary)
				init.ui.btnUpdate(false)
			} else {
				init.ui.alert("No test cases", "No test cases to compare.")
			}
		}
		
	}

}
