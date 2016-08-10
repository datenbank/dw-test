package datenbank.event

import datenbank.engine.Executor
import javafx.event.ActionEvent
import javafx.event.EventHandler

class ExecuteCompareAll implements EventHandler<ActionEvent> {
	
	def init
		
	@Override
	public void handle(ActionEvent arg0) {
		Thread.start {
			if(init.summary.testCases.size() > 0) {
				init.ui.btnUpdate(true)
				
				summary.testCases.each { testCase ->
					init.ex.runOne(testCase)
					init.rt.runOne(testCase)
				}
				
				init.ui.btnUpdate(false)
			} else {
				init.ui.alert("No test cases", "No test cases to execute/compare.")
			}
		}
		
	}

}
