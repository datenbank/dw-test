model.tables.each {
		


	it.columns.findAll{ it.testType.contains('COLUMN_CNT') }.each { col ->
		def sql = ""
		sql += "SELECT \r\n $col.column, COUNT(*)\r\n"
		sql += "FROM ${it.schema}.${it.table}\r\n"
		if(it.where != "-")	
			sql += "WHERE ${it.where}\r\n"	
		sql += "GROUP BY $col.column\r\n"
          def file = new File("${path}Target/${it.database}#${it.table}_${col.column}_COLUMN_CNT.sql")
		if(!file.exists()) {
			file << sql
		}
		println sql
	}
			
} 
model.tables.each {

	it.columns.findAll{ it.testType.contains('COLUMN_CNT') }.each { col ->
		def sql = ""
      	sql += "SELECT \r\n $col.columnRef.column, COUNT(*)\r\n"
		sql += "FROM ${it.schema}.${it.table}\r\n"
		if(it.tableRef.where != "-")	
			sql += "WHERE ${it.tableRef.where}\r\n"	
		sql += "GROUP BY $col.columnRef.column\r\n"
          def file = new File("${path}Source/${it.database}#${it.table}_${col.column}_COLUMN_CNT.sql")
		if(!file.exists()) {
			file << sql
		}
		println sql
	}
			
	
}