package datenbank.model

import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
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
