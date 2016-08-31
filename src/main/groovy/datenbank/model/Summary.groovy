package datenbank.model

import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Summary extends Observable {
	
	def testCases = []
	
	def ready() {
		setChanged()
		notifyObservers()
	}
	
	def getGroups() {
		def groups = []
		
		testCases.each {
			
			def group = it.group
			
			if(!groups.contains(group))
				groups << group
		}
		return groups
	}
	
	def String toString() {
		def i = 0 
		def errors = 0
		def compared = 0
		def skipped = 0
		testCases.each { testCase ->
			i++
			if(testCase.errors < 3 && testCase.errors > 0)
				errors += 1
			compared += testCase.compared
			skipped += testCase.skipped
		}
		
		return "Test cases = ${i}, Errors = ${errors}, Compared = ${compared}, Skipped = ${skipped}"
	}
	
}
