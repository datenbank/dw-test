package datenbank.engine


import datenbank.model.Variables
import datenbank.model.Summary
import datenbank.model.TestCase

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Init {
	
	def ui	
	def summary 
	
	def rt = new ResultTester()
	def ex = new Executor()
	
	def init() {
		
		if(summary) {
			summary.deleteObserver(ui)
		}
		
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
		
		summary.ready()
		summary
	}

}
