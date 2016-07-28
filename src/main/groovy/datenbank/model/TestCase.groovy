package datenbank.model

import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class TestCase extends Observable {
	def name = "" //

	def elapsed, start //Ex
	def elapsedTest, startTest //
	
	def compared = 0 //
	def skipped = 0 //
	def errors = 0 //Ex
	def resultFlag = 0 //
	def linesNotInSource = 0 //
	def linesNotInTarget = 0 //

	def executor = 0
	def tester = 0
	
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
}
