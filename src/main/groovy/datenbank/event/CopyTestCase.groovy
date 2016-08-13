package datenbank.event

import datenbank.model.TestCase
import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class CopyTestCase implements EventHandler<ActionEvent> {

	def init


	@Override
	public void handle(ActionEvent arg0) {
		try {
			
			def testCase = init.ui.testCaseCopy
			
			if(testCase) {
				
				def tgtSQL = new File("${Variables.path}Target/${testCase.name}.sql")
				def srcSQL = new File("${Variables.path}Source/${testCase.name}.sql")
				
				def after = new File("${Variables.path}Target/${testCase.name}_After.bat")
				def before = new File("${Variables.path}Target/${testCase.name}_Before.bat")
				
				def tmp = new File("${Variables.path}Target/${testCase.name}_Copy.sql")
				def i = 1
				def newName = "_Copy"
				
				while(tmp.exists()) {
					tmp = new File("${Variables.path}Target/${testCase.name}_Copy_${i}.sql")
					
					newName = "_Copy_${i}"
					
					i++
				}
								
				if(tgtSQL.exists()) {
					def tgtSQLCopy = new File("${Variables.path}Target/${testCase.name}${newName}.sql")
					tgtSQLCopy << tgtSQL.text
				}
				if(srcSQL.exists()) {
					def srcSQLCopy = new File("${Variables.path}Source/${testCase.name}${newName}.sql")
					srcSQLCopy << srcSQL.text
				}
				if(after.exists()) {
					def afterCopy = new File("${Variables.path}Source/${testCase.name}${newName}_After.sql")
					afterCopy << after.text
				}
				if(before.exists()) {
					def beforeCopy = new File("${Variables.path}Source/${testCase.name}${newName}_Before.sql")
					beforeCopy << before.text
				}
				
				init.summary.testCases << new TestCase(name: testCase.name+"${newName}")
				
				init.summary.ready()
				init.ui.menu()
				
			} else {
					init.ui.alert("Paste test case", "No test case in clip board.")
			}
		} catch(all) {
			init.ui.alert("Paste test case", "Couldn't paste: $all")
		}
	}
}
