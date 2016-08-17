package datenbank.event

import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class CodeEditorSave implements EventHandler<ActionEvent> {

	def init
	
	def file
	def editor
	
	
	@Override
	public void handle(ActionEvent arg0) {
		file.newWriter().withWriter { w ->
			w << editor.getCodeAndSnapshot()
		}
		if(file.name == "conf.txt") {
			Variables.load()
			init.ui.menu()
		}
		init.ui.confirm("Saved", "The file is saved.")
	}
}
