package datenbank.model

import java.util.Observable

class QueryResult extends Observable {
	def file = ""
	def i = 0 
	def total = 0
	def errors = 0
	
	def elapsed, start
	
	def begin() {
		
		start = System.currentTimeMillis()
	}
	
	def stop() {
		
		elapsed = (System.currentTimeMillis() - start) / 1000.0
	}
	
	
	def ready() {
		setChanged()
		notifyObservers()
	}
}
