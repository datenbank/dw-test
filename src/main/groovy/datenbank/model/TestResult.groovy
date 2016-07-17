package datenbank.model

import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class TestResult extends Observable {
	def file = ""
	
	def compared = 0
	def skipped = 0
	def errors = 0 
	def total = 0 
	def elapsed, start
	def linesNotInSource = 0
	def linesNotInTarget = 0
	def resultFlag = 0
	
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
	
}
