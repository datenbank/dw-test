package datenbank.event

import datenbank.engine.ResultTester
import javafx.event.ActionEvent
import javafx.event.EventHandler

class CompareAll implements EventHandler<ActionEvent> {
	
	def init
		
	@Override
	public void handle(ActionEvent arg0) {
		Thread.start {
			
			init.ui.progressStart(init.summary.testCases.size())
			
			if(init.summary.testCases.size() > 0) {
				init.ui.btnUpdate(true)
				

				init.summary.testCases.each { testCase ->
					if(!init.ui.cancel) {
						init.rt.runOne(testCase)
						init.ui.progressIncrement()
					}
					
				}
				
				init.summary.ready()
				
				
				init.ui.btnUpdate(false)
				
			} else {
				init.ui.alert("No test cases", "No test cases to compare.")
			}
			init.ui.progressStop()
		}
		
	}

}
