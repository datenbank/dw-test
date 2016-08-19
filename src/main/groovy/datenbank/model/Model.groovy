package datenbank.model

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Model {
	def tables = []
	def srcTables = []

	def Table get(def tableName, def schema, def database) {
		def t

		tables.each {
			if(it.table == tableName && it.schema == schema && it.database == database) {
				t = it
			}
		}
		return t
	}


	def addSrc(def table) {
		if(table in srcTables) {
		} else {
			srcTables << table
		}
	}
	def Table getSrc(def tableName, def schema, def database) {
		def t

		srcTables.each {
			if(it.table == tableName && it.schema == schema && it.database == database) {
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

	def writeModelToFile() {
		def file = new File("${Variables.path}${Variables.model}")
		file.write("")
		
		tables.each { t ->
			t.columns.each { c -> 
				if(c.columnRef)
					file << "${t.database}${Variables.csvSeperator}${t.schema}${Variables.csvSeperator}${t.table}${Variables.csvSeperator}${c.column}${Variables.csvSeperator}1${Variables.csvSeperator}YES${Variables.csvSeperator}${c.dataType}${Variables.csvSeperator}${c.isPrimaryKey}${Variables.csvSeperator}${c.columnRef.tableRef.database}${Variables.csvSeperator}${c.columnRef.tableRef.schema}${Variables.csvSeperator}${c.columnRef.tableRef.table}${Variables.csvSeperator}${c.columnRef.column}${Variables.csvSeperator}1${Variables.csvSeperator}YES${Variables.csvSeperator}${c.columnRef.dataType}${Variables.csvSeperator}${c.columnRef.isPrimaryKey}${Variables.csvSeperator}${c.testType}${Variables.csvSeperator}${t.where}${Variables.csvSeperator}${c.columnRef.tableRef.where}\r\n" }
		}
	}

	def loadModelFromFile() {
		def i = 1

		def fileSrc = new File("${Variables.path}${Variables.sourceModel}")
		def fileTgt = new File("${Variables.path}${Variables.targetModel}")
		if(fileSrc.exists()) {
			fileSrc.eachLine { line, number ->
				def row = line.split("${Variables.csvSeperator}")
				def st = new Table(table: row[2], schema: row[1], database: row[0], where: "-")
				addSrc st

				st = getSrc(st.table, st.schema, st.database)

				st.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], tableRef: st))
			}
		}


		if(fileTgt.exists()) {
			fileTgt.eachLine { line, number ->
				def row = line.split("${Variables.csvSeperator}")
				def t = new Table(table: row[2], schema: row[1], database: row[0], where: "-")
				add t
				t = get(t.table, t.schema, t.database)
				t.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], tableRef: t, testType: "-"))
			}
		}
		


		

		def file = new File("${Variables.path}${Variables.model}")
		if(file.exists()) {
			file.eachLine { line, number ->

				try {
					def row = line.split("${Variables.csvSeperator}")
					def st = new Table(table: row[10], schema: row[9], database: row[8])
					def t = new Table(table: row[2], schema: row[1], database: row[0])
					add t
					t = get(t.table, t.schema, t.database)

					addSrc st
					st = getSrc(st.table, st.schema, st.database)

					def sc = new Column(column: row[11], dataType: row[14], isPrimaryKey: row[15], tableRef: st, testType: row[16])

					st.where = row[18]
					t.tableRef = st
					t.where = row[17]


					t.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], columnRef: sc, tableRef: t, testType: row[16]))
					st.addColumn(sc)
				} catch (Exception e) {
					println "skipping row #$i $e"
				}
				i++
			}
		} else {
			println "Couldn't find model file: ${Variables.path}${Variables.model}"
		}
	}
}
