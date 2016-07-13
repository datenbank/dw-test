package datenbank.model

import java.util.Observable

class Summary extends Observable {
	
	def testResults = []
	def queryResults = []
	
	def ready() {
		setChanged()
		notifyObservers()
	}
}
