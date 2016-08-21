package datenbank.model

import groovy.sql.Sql
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
					file << "${t.database}${Variables.csvSeperator}${t.schema}${Variables.csvSeperator}${t.table}${Variables.csvSeperator}${c.column}${Variables.csvSeperator}${c.ordinal}${Variables.csvSeperator}YES${Variables.csvSeperator}${c.dataType}${Variables.csvSeperator}${c.isPrimaryKey}${Variables.csvSeperator}${c.columnRef.tableRef.database}${Variables.csvSeperator}${c.columnRef.tableRef.schema}${Variables.csvSeperator}${c.columnRef.tableRef.table}${Variables.csvSeperator}${c.columnRef.column}${Variables.csvSeperator}${c.columnRef.ordinal}${Variables.csvSeperator}YES${Variables.csvSeperator}${c.columnRef.dataType}${Variables.csvSeperator}${c.columnRef.isPrimaryKey}${Variables.csvSeperator}${c.testType}${Variables.csvSeperator}${t.where}${Variables.csvSeperator}${c.columnRef.tableRef.where}\r\n"
			}
		}
	}

	def loadModelFromDb(group) {

		def sql
		def sqlSrc
		def srcQuery
		def tgtQuery
		if(group == "Default") {
			sql = Sql.newInstance(Variables.targetConnection, Variables.targetDriver )
			sqlSrc = Sql.newInstance(Variables.sourceConnection, Variables.sourceDriver )

			if(Variables.targetDriver.contains("Oracle")) {
				tgtQuery = "select '-', '-', table_name, column_name, 1, nullable, data_type, 0 from cols"
			} else {
				tgtQuery = '''
			SELECT TABLE_CATALOG,
			       TABLE_SCHEMA,
			       TABLE_NAME,
			       COLUMN_NAME,
			       ORDINAL_POSITION,
			       IS_NULLABLE,
			       DATA_TYPE,
			       
			                (SELECT MAX('1')
			                 FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu
								INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc 
									ON kcu.CONSTRAINT_NAME=tc.CONSTRAINT_NAME and tc.CONSTRAINT_TYPE='PRIMARY KEY'
									AND tc.TABLE_CATALOG=c.TABLE_CATALOG
									AND tc.TABLE_SCHEMA=c.TABLE_SCHEMA
									AND tc.TABLE_NAME=c.TABLE_NAME
			                 WHERE kcu.TABLE_CATALOG=c.TABLE_CATALOG
			                   AND kcu.TABLE_SCHEMA=c.TABLE_SCHEMA
			                   AND kcu.TABLE_NAME=c.TABLE_NAME
			                   AND kcu.COLUMN_NAME=c.COLUMN_NAME) IS_PK
			FROM INFORMATION_SCHEMA.COLUMNS c
			'''
			}

			if(Variables.sourceDriver.contains("Oracle")) {
				srcQuery = "select '-', '-', table_name, column_name, 1, nullable, data_type, '0' from cols"
			} else {
				srcQuery = '''
			SELECT TABLE_CATALOG,
			       TABLE_SCHEMA,
			       TABLE_NAME,
			       COLUMN_NAME,
			       ORDINAL_POSITION,
			       IS_NULLABLE,
			       DATA_TYPE,
			       
			                (SELECT MAX('1')
			                 FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu
								INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc 
									ON kcu.CONSTRAINT_NAME=tc.CONSTRAINT_NAME and tc.CONSTRAINT_TYPE='PRIMARY KEY'
									AND tc.TABLE_CATALOG=c.TABLE_CATALOG
									AND tc.TABLE_SCHEMA=c.TABLE_SCHEMA
									AND tc.TABLE_NAME=c.TABLE_NAME
			                 WHERE kcu.TABLE_CATALOG=c.TABLE_CATALOG
			                   AND kcu.TABLE_SCHEMA=c.TABLE_SCHEMA
			                   AND kcu.TABLE_NAME=c.TABLE_NAME
			                   AND kcu.COLUMN_NAME=c.COLUMN_NAME) IS_PK
			FROM INFORMATION_SCHEMA.COLUMNS c
			'''
			}
		} else {
			sql = Sql.newInstance(Variables.config.groups."${group}".target, Variables.config.groups."${group}".targetDriver)
			sqlSrc = Sql.newInstance(Variables.config.groups."${group}".source, Variables.config.groups."${group}".sourceDriver)

			if(Variables.config.groups."${group}".targetDriver.contains("Oracle")) {
				tgtQuery = "select '-', '-', table_name, column_name, 1, nullable, data_type, '0' from cols"
			} else {
				tgtQuery = '''
			SELECT TABLE_CATALOG,
			       TABLE_SCHEMA,
			       TABLE_NAME,
			       COLUMN_NAME,
			       ORDINAL_POSITION,
			       IS_NULLABLE,
			       DATA_TYPE,
			       
			                (SELECT MAX('1')
			                 FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu
								INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc 
									ON kcu.CONSTRAINT_NAME=tc.CONSTRAINT_NAME and tc.CONSTRAINT_TYPE='PRIMARY KEY'
									AND tc.TABLE_CATALOG=c.TABLE_CATALOG
									AND tc.TABLE_SCHEMA=c.TABLE_SCHEMA
									AND tc.TABLE_NAME=c.TABLE_NAME
			                 WHERE kcu.TABLE_CATALOG=c.TABLE_CATALOG
			                   AND kcu.TABLE_SCHEMA=c.TABLE_SCHEMA
			                   AND kcu.TABLE_NAME=c.TABLE_NAME
			                   AND kcu.COLUMN_NAME=c.COLUMN_NAME) IS_PK
			FROM INFORMATION_SCHEMA.COLUMNS c
			'''
			}

			if(Variables.config.groups."${group}".sourceDriver.contains("Oracle")) {
				srcQuery = "select '-', '-', table_name, column_name, 1, nullable, data_type, '0' from cols"
			} else {
				srcQuery = '''
			SELECT TABLE_CATALOG,
			       TABLE_SCHEMA,
			       TABLE_NAME,
			       COLUMN_NAME,
			       ORDINAL_POSITION,
			       IS_NULLABLE,
			       DATA_TYPE,
			       
			                (SELECT MAX('1')
			                 FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu
								INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc 
									ON kcu.CONSTRAINT_NAME=tc.CONSTRAINT_NAME and tc.CONSTRAINT_TYPE='PRIMARY KEY'
									AND tc.TABLE_CATALOG=c.TABLE_CATALOG
									AND tc.TABLE_SCHEMA=c.TABLE_SCHEMA
									AND tc.TABLE_NAME=c.TABLE_NAME
			                 WHERE kcu.TABLE_CATALOG=c.TABLE_CATALOG
			                   AND kcu.TABLE_SCHEMA=c.TABLE_SCHEMA
			                   AND kcu.TABLE_NAME=c.TABLE_NAME
			                   AND kcu.COLUMN_NAME=c.COLUMN_NAME) IS_PK
			FROM INFORMATION_SCHEMA.COLUMNS c
			'''
			}
		}



		sqlSrc.eachRow(srcQuery) { row ->

			def st = new Table(table: row[2], schema: row[1], database: row[0], where: "-")
			addSrc st

			st = getSrc(st.table, st.schema, st.database)

			st.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7] == null ? 0 : 1, tableRef: st, ordinal: row[4]))
		}
		sql.eachRow(tgtQuery) { row ->

					def t = new Table(table: row[2], schema: row[1], database: group, where: "-")
					add t

					t = get(t.table, t.schema, t.database)

					t.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7] == null ? 0 : 1, tableRef: t, ordinal: row[4], testType: "-"))
				}
		loadModelFromFile()
	}

	def loadModelFromFile() {
		def i = 1

		def fileSrc = new File("${Variables.path}${Variables.sourceModel}")
		def fileTgt = new File("${Variables.path}${Variables.targetModel}")
		if(fileSrc.exists()) {
			fileSrc.eachLine { line, number ->
				def row = line.split("${Variables.csvSeperator}")
				def t = new Table(table: row[2], schema: row[1], database: row[0], where: "-")
				add t

				t = get(t.table, t.schema, t.database)

				t.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], tableRef: t, ordinal: row[4]))
			}
		}


		if(fileTgt.exists()) {
			fileTgt.eachLine { line, number ->
				def row = line.split("${Variables.csvSeperator}")
				def t = new Table(table: row[2], schema: row[1], database: row[0], where: "-")
				add t
				t = get(t.table, t.schema, t.database)
				t.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], tableRef: t, testType: "-", ordinal: row[4]))
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

					def sc = new Column(column: row[11], dataType: row[14], isPrimaryKey: row[15], tableRef: st, testType: row[16], ordinal: row[12])

					st.where = row[18]
					t.tableRef = st
					t.where = row[17]


					t.addColumn(new Column(column: row[3], dataType: row[6], isPrimaryKey: row[7], columnRef: sc, tableRef: t, testType: row[16], ordinal: row[4]))
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
