model.tables.findAll{ it.testType.contains('COUNT') }.each {
	def sql = "SELECT \n  COUNT(*) AS CNT_ROWS\n, '$it.table' AS TAB_NAME \nFROM ${it.schema}.${it.table}\n"
	
	if(it.where != "-")	
		sql += "WHERE ${it.where}\n"
	
	def file = new File("${path}Target/${it.tableRef.database}#${it.table}_COUNT.sql")
	if(!file.exists()) {
		file << sql
	}
	
	println sql
			
} 

model.tables.findAll{ it.testType.contains('COUNT') }.each {
	def sql = "SELECT \r\n  COUNT(*) AS CNT_ROWS\n, '$it.tableRef.table' AS TAB_NAME \nFROM ${it.tableRef.schema}.${it.tableRef.table}\n"
	if(it.tableRef.where != "-")	
		sql += "WHERE ${it.tableRef.where}\n"			
	def file = new File("${path}Source/${it.tableRef.database}#${it.table}_COUNT.sql")
	if(!file.exists()) {
		file << sql
	}		
	println sql
}