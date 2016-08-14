package datenbank.event
import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class NewScript implements EventHandler<ActionEvent> {

	def init

	@Override
	public void handle(ActionEvent arg0) {
		try {
			def tcName = init.ui.input("New script", "Enter name", "")

			def file = new File("${Variables.path}Scripts/${tcName}.groovy")
			if(!file.exists()) {
				file.withWriter('UTF-8') {
					it.writeLine ""
			
				}
				
				init.ui.menu()
				init.ui.codeEditor(file, 'Java')
			} else {
				init.ui.alert("New script", "A script with that name already exists.")
			}
		} catch(e) {
			init.ui.alert("New script", "Was not created $e")
		}
		
		
	}
}
