package datenbank.model

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Model {
	def tables = []
	
	
	def Table get(def tableName) {
		def t
		
		tables.each {
			if(it.table == tableName) {
				t = it
			}			
		}
		return t
	}
	
	
	def add(def table) {
		if(table in tables) {	
		} else {			
			tables << table
		}		
	}

	
	def loadModelFromFile() {
		def i = 1
		def file = new File("${Variables.path}${Variables.model}")
		if(file.exists()) {
			file.eachLine { line, number ->
				if(number<=2) 
					return
				
				try {
					def row = line.split(';')
					def st = new Table(table: row[10], schema: row[9], database: row[8], where: row[18])		
					def t = new Table(table: row[2], schema: row[1], database: row[0], tableRef: st, testType: row[16], where: row[17])				
					add t					
					t = get(t.table)
					def sc = new Column(column: row[11], dataType: row[14], isPrimaryKey: row[15], tableRef: st)		
					t.addColumn( new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], columnRef: sc, tableRef: t, testType: row[16]))
					
					} catch (Exception e) {
					println "skipping row #$i"
				}
				i++
			}
		} else {
			println "Couldn't find model file: ${Variables.path}${Variables.model}"
		}
		
		
	}
	
}
