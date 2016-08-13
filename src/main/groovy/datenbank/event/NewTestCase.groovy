package datenbank.event
import datenbank.model.TestCase
import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class NewTestCase implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		try {
			def tcName = init.ui.input("New test case", "Enter name", "")

			def file = new File("${Variables.path}Target/${tcName}.sql")
			if(!file.exists()) {
				file.withWriter('UTF-8') {
					it.writeLine ""
			
				}
				init.summary.testCases << new TestCase(name: tcName)
				init.summary.ready()
				init.ui.menu()
			} else {
				init.ui.alert("New test case", "A test case with that name already exists.")
			}
		} catch(e) {
			init.ui.alert("New test case", "Was not created $e")
		}
		
		
	}
}
