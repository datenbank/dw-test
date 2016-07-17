package datenbank.model

import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Column extends Observable {
	
	def column, dataType
	def isPrimaryKey = 0
	
	def columnRef
	def tableRef
	
	def testType
	
	def boolean equals(def o) {
		return o.column == column
	}
	def String toString() {
		return "${column}"	
	}

}