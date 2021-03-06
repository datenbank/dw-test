package datenbank.model

import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class TestCase extends Observable {
	def name = "" //
	
	def elapsed, start //Ex
	def errors = -1 //Ex
	
	def elapsedTest, startTest //
	def compared = 0 //
	def skipped = 0 //	
	def resultFlag  //
	def linesNotInSource //
	def linesNotInTarget //

	def type = 0
	
	def isDefaultGroup() {
		getGroup() == "Default"
	}
	
	def getGroup() {
		def fileSplit = name.split("#")
		def grp = ""
		if(fileSplit.size()>1)
			grp = fileSplit[0]
		else
			grp = "Default"		
			
		return grp
	}
	
	
	def runDate = new Date(System.currentTimeMillis())
	
	def ready() {
		setChanged()
		notifyObservers()
	}
	
	def begin() {
		
		start = System.currentTimeMillis()
	}
	
	def stop() {
		
		elapsed = (System.currentTimeMillis() - start) / 1000.0
	}
	
	def beginTest() {
		
		startTest = System.currentTimeMillis()
	}
	
	def stopTest() {
		
		elapsedTest = (System.currentTimeMillis() - startTest) / 1000.0
	}
	
	def String toString() {
		def str = ""
		def errorDesc = "SUCCESS"
		if(errors < 3)
		 	errorDesc = "FAILURE"
		
		if(type == 0)
			str += "${name}"
				
		if(type == 1)
			str += "${name}\t${errorDesc}\t${elapsed}"
		
		if(type == 2)
			str += "${name}\t${errorDesc}\t${compared}\t${skipped}\t${resultFlag}\t${elapsedTest}"
			
		return str
	}
}
