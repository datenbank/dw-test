package datenbank.event

import datenbank.model.Model
import datenbank.model.Variables
import javafx.event.ActionEvent
import javafx.event.EventHandler

class RunScript implements EventHandler<ActionEvent> {

	def init
	def file

	@Override
	public void handle(ActionEvent arg0) {
		def m = new Model()
		m.loadModelFromFile()
		Binding binding = new Binding();
		binding.setVariable("model", m);
		binding.setVariable("path", Variables.path);
		GroovyShell shell = new GroovyShell(binding);
		try {
			shell.evaluate(file.text);
			Variables.load()
			init.ui.summary = init.init()
			init.ui.menu()
		} catch(all) {
			init.ui.alert("Open file error", "Couldn't open file. Please check that it exists!")
		}
	}
}
