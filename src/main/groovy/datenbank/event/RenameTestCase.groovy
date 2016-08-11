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
			def tcName = init.ui.input("Rename test case", "Enter name")

			def testCase = (TestCase) init.ui.tv.getSelectionModel().getSelectedItem();
			if(testCase) {
				def tgtSQL = new File("${Variables.path}Target/${testCase.name}.sql")
				def tgtCSV = new File("${Variables.path}Target/Result/${testCase.name}.csv")
				def srcSQL = new File("${Variables.path}Source/${testCase.name}.sql")
				def srcCSV = new File("${Variables.path}Source/Result/${testCase.name}.csv")
				def compCSV = new File("${Variables.path}Report/${testCase.name}.csv")

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

				testCase.name = tcName
				
				init.summary.ready()
			}
		} catch(all) {
			init.ui.alert("Rename test case", "Couldn't rename: $all")
		}
	}
}
