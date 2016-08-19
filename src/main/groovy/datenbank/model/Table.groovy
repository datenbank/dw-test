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
		
		def tmp
		
		if(column in columns) {
			tmp = columns.find {it == column}
			tmp.columnRef = column.columnRef
			tmp.testType = column.testType
		} else {
			columns << column
		}
	}

	def boolean equals(def o) {
		def eq = false
		if(o?.database == database  &&	o?.schema == schema && o?.table == table)
			eq = true
		
		return eq  //add schema also
	}
	
	def String toString() {
		return "${database}.${schema}.${table}"	
	}
}
