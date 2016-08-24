package datenbank.model
import java.util.Observable

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Table extends Observable {
	def table, schema, database, group
	def columns = []
	
	def tableRef
		
	def where = "-"
	
	def String getTestType() {
		def testTypeTmp = ""
		int i = 0
		columns.each { col ->
			if(!testTypeTmp.contains(col.testType)) {
				if(i>0)
					testTypeTmp =  testTypeTmp+" | "+col.testType
				else 
					testTypeTmp = col.testType
				i++
			}
		}
		return testTypeTmp
	}
	
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
		
		if(group) {
			if(o?.database == database  &&	o?.schema == schema && o?.table == table && o?.group == group)
				eq = true
		} else {		
			if(o?.database == database  &&	o?.schema == schema && o?.table == table)
			eq = true
		}
		
		return eq  //add schema also
	}
	
	def String toString() {
		if(group)
			return "${group} - ${database}.${schema}.${table}"
		else
			return "${database}.${schema}.${table}"	
	}
}
