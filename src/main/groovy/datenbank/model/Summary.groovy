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
	
	def String toString() {
		def i = 0 
		def errors = 0
		def compared = 0
		def skipped = 0
		testCases.each { testCase ->
			i++
			errors += testCase.errors
			compared += testCase.compared
			skipped += testCase.skipped
		}
		
		return "Test cases = ${i}, Errors = ${errors}, Compared = ${compared}, Skipped = ${skipped}"
	}
	
}
