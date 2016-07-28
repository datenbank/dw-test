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
	
}
