package datenbank.engine


import datenbank.model.Variables
import datenbank.model.Summary
import datenbank.model.TestCase


class Init {
	
	def ui	
	def summary
	
	def init() {
		
		summary = new Summary()
		summary.addObserver(ui)
		
		def dir = new File("${Variables.path}Target")
				
		dir.eachFile() { file ->
			if(file.getName().endsWith(".sql")) {
				def name= file.getName().substring(0, file.getName().length()-4)
				
				def testCase = new TestCase(name: name)
				testCase.addObserver(ui)
				
				summary.testCases << testCase
			}
				
		}
		
		summary
	}

}
