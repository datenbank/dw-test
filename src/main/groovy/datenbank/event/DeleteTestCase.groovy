package datenbank.event

import datenbank.model.TestCase
import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class DeleteTestCase implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		try {
			def go = init.ui.accept("Delete test case", "Click OK to Delete or Cancel.")
			if(go) {
				def testCase = (TestCase) init.ui.tv.getSelectionModel().getSelectedItem();
				if(testCase) {
					def tgtSQL = new File("${Variables.path}Target/${testCase.name}.sql")
					def tgtCSV = new File("${Variables.path}Target/Result/${testCase.name}.csv")
					def srcSQL = new File("${Variables.path}Source/${testCase.name}.sql")
					def srcCSV = new File("${Variables.path}Source/Result/${testCase.name}.csv")
					def compCSV = new File("${Variables.path}Report/${testCase.name}.csv")
					
					def after = new File("${Variables.path}Target/${testCase.name}_After.bat")
					def before = new File("${Variables.path}Target/${testCase.name}_Before.bat")
					
					
					if(tgtSQL.exists())
						tgtSQL.delete()
					if(srcSQL.exists())
						srcSQL.delete()
					if(compCSV.exists())
						compCSV.delete()					
					if(srcCSV.exists())
						srcCSV.delete()
					if(tgtCSV.exists())
						tgtCSV.delete()					
					
					if(after.exists())
						after.delete()
					if(before.exists())
						before.delete()
					
					init.summary.testCases.remove(testCase)
					init.summary.ready()
					init.ui.menu()
					
				} else {
					init.ui.alert("Delete test case", "No test case selected.")
				}
			}
		} catch(all) {
			init.ui.alert("Delete test case", "Something went wrong.")
		}
	}
}
