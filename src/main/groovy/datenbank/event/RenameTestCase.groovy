package datenbank.event

import datenbank.model.TestCase
import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class RenameTestCase implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		try {

			def testCase = (TestCase) init.ui.tv.getSelectionModel().getSelectedItem();
			if(testCase) {
				
				
				def tcName = init.ui.input("Rename test case", "Enter name", testCase.name)
				
				def tgtSQL = new File("${Variables.path}Target/${testCase.name}.sql")
				def tgtCSV = new File("${Variables.path}Target/Result/${testCase.name}.csv")
				def srcSQL = new File("${Variables.path}Source/${testCase.name}.sql")
				def srcCSV = new File("${Variables.path}Source/Result/${testCase.name}.csv")
				def compCSV = new File("${Variables.path}Report/${testCase.name}.csv")

				def after = new File("${Variables.path}Target/${testCase.name}_After.bat")
				def before = new File("${Variables.path}Target/${testCase.name}_Before.bat")
				
				if(tgtSQL.exists())
					tgtSQL.renameTo("${Variables.path}Target/${tcName}.sql")
				if(srcSQL.exists())
					srcSQL.renameTo("${Variables.path}Source/${tcName}.sql")
				if(compCSV.exists())
					compCSV.renameTo("${Variables.path}Report/${tcName}.csv")
				if(srcCSV.exists())
					srcCSV.renameTo("${Variables.path}Source/Result/${tcName}.csv")
				if(tgtCSV.exists())
					tgtCSV.renameTo("${Variables.path}Target/Result/${tcName}.csv")
				
				if(after.exists())
					after.renameTo("${Variables.path}Target/Result/${tcName}_After.bat")
					
				if(before.exists())
						before.renameTo("${Variables.path}Target/Result/${tcName}_Before.bat")
				testCase.name = tcName
				init.summary.ready()
				init.ui.menu()
				
			} else {
					init.ui.alert("Rename test case", "No test case selected.")
			}
		} catch(java.util.NoSuchElementException ne) {
			//just cancelled
		} catch(all) {
			init.ui.alert("Rename test case", "Couldn't rename: $all")
		}
	}
}
