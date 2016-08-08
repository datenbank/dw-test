package datenbank.model
import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Table extends Observable {
	def table, schema, database
	def columns = []
	
	def tableRef
	
	def testType
	def where
	
	def addColumn(def column) {
		if(column in columns) {
			
		} else {
			columns << column
		}
	}

	def boolean equals(def o) {
		return o.table == table //add schema also
	}
	
	def String toString() {
		return "${table}"	
	}
}
