model.tables.each {
		
	def sql = ""
	def i = 0
	it.columns.findAll{ it.testType.contains('NONSENSE') }.each { col ->
		if(i==0)
			sql += "SELECT \r\n  'NONSENSE_SUM' as TEST_TYPE \r\n"
		if(col.dataType.contains("int") || col.dataType.contains("float") || col.dataType.contains("decimal"))
			sql += ", SUM($col.column)\r\n"
		i++
	}
	if(i>0) {
		sql += "FROM ${it.schema}.${it.table}\r\n"
		if(it.where != "-")	
			sql += "WHERE ${it.where}\r\n"	
		def file = new File("${path}Target/${it.database}#${it.table}_NONSENSE.sql")
		if(!file.exists()) {
			file << sql
		}
		println sql
	}
			
} 
model.tables.each {
	def sql = ""
	def i = 0
	it.columns.findAll{ it.testType.contains('NONSENSE') }.each { col ->
		if(i==0)
			sql += "SELECT \r\n  'NONSENSE_SUM' as TEST_TYPE \r\n"
		if(col.columnRef.dataType.contains("int") || col.columnRef.dataType.contains("float") || col.columnRef.dataType.contains("decimal"))
			sql += ", SUM($col.columnRef.column)\r\n"
		i++
	}
	if(i>0) {
		sql += "FROM ${it.tableRef.schema}.${it.tableRef.table}\r\n"
		if(it.tableRef.where != "-")	
			sql += "WHERE ${it.tableRef.where}\r\n"	
		def file = new File("${path}Source/${it.database}#${it.table}_NONSENSE.sql")
		if(!file.exists()) {
			file << sql
		}
		println sql
	}
			
	
}