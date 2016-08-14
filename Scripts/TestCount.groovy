model.tables.findAll{ it.testType.contains('COUNT') }.each {
	def sql = "SELECT \r\n  COUNT(*) AS CNT_ROWS\r\n, '$it.table' AS TAB_NAME \r\nFROM ${it.schema}.${it.table}\r\n"
	
	if(it.where != "-")	
		sql += "WHERE ${it.where}\r\n"
	
	def file = new File("${path}Target/${it.tableRef.database}#${it.table}_COUNT.sql")
	if(!file.exists()) {
		file << sql
	}
	
	println sql
			
} 

model.tables.findAll{ it.testType.contains('COUNT') }.each {
	def sql = "SELECT \r\n  COUNT(*) AS CNT_ROWS\r\n, '$it.tableRef.table' AS TAB_NAME \r\nFROM ${it.tableRef.schema}.${it.tableRef.table}\r\n"
	if(it.tableRef.where != "-")	
		sql += "WHERE ${it.tableRef.where}\r\n"			
	def file = new File("${path}Source/${it.tableRef.database}#${it.table}_COUNT.sql")
	if(!file.exists()) {
		file << sql
	}		
	println sql
}