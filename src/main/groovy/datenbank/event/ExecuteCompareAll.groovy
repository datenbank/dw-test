package datenbank.event

import datenbank.model.Variables
import datenbank.engine.Executor
import javafx.event.ActionEvent
import javafx.event.EventHandler

class ExecuteCompareAll implements EventHandler<ActionEvent> {
	
	def init
		
	@Override
	public void handle(ActionEvent arg0) {
		Thread.start {
			
			init.ui.progressStart(init.summary.testCases.size())
			
			
			
			if(init.summary.testCases.size() > 0) {
				init.ui.btnUpdate(true)
				
				def i=0
								
				init.summary.testCases.each { testCase ->
					
					i++
					while(i>Variables.degreeOfparallelism) {
						println "wait for $testCase.name $i>$Variables.degreeOfparallelism"
						Thread.sleep(100)
					}
					Thread.start {
						if(!init.ui.cancel) {
							init.ex.runOne(testCase)
							init.rt.runOne(testCase)
							
						}
						i--
						init.ui.progressIncrement()
					}
					
				}
				//init.summary.ready()
				//init.ui.btnUpdate(false)
			} else {
				init.ui.alert("No test cases", "No test cases to execute/compare.")
			}
			//init.ui.progressStop()
		}
		
	}

}
