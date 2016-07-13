model.tables.each {

	def sql = ""
	def groupBy = ""
	def i = 0
	it.columns.findAll{ it.testType.contains('SUMBY') }.each { col ->
		if(i==0)
			sql += "SELECT \r\n  'SUMBY' as TEST_TYPE \r\n"
		sql += ", SUM($col.column)\r\n"
			
		i++
	}
	it.columns.findAll{ it.testType.contains('GROUPBY') }.each { col ->
		
		if(groupBy == "") {
			groupBy += " $col.columnRef.column\r\n"
		} else {
			groupBy += ", $col.columnRef.column\r\n"
		}
			
		
	}
	
	
	if(i>0) {
		sql += ",$groupBy FROM ${it.schema}.${it.table}\r\n "
		if(it.tableRef.where != "-")	
			sql += "WHERE ${it.where}\r\n"	
		sql += "GROUP BY $groupBy"
		
		def file = new File("${path}Target/${it.table}_SUMBY.sql")
		if(!file.exists()) {
			file << sql
		}
		println sql
	}
	
	
} 
model.tables.each {

	def sql = ""
	def groupBy = ""
	def i = 0
	it.columns.findAll{ it.testType.contains('SUMBY') }.each { col ->
		if(i==0)
			sql += "SELECT \r\n  'SUMBY' as TEST_TYPE \r\n"
		sql += ", SUM($col.columnRef.column)\r\n"
			
		i++
	}
	it.columns.findAll{ it.testType.contains('GROUPBY') }.each { col ->
		if(groupBy == "") {
			groupBy += " $col.columnRef.column\r\n"
		} else {
			groupBy += ", $col.columnRef.column\r\n"
		}
			
		
	}
	
	
	if(i>0) {
		sql += ",$groupBy FROM ${it.tableRef.schema}.${it.tableRef.table}\r\n "
		if(it.tableRef.where != "-")	
			sql += "WHERE ${it.tableRef.where}\r\n "	
		sql += "GROUP BY $groupBy"
		def file = new File("${path}Source/${it.table}_SUMBY.sql")
		if(!file.exists()) {
			file << sql
		}
		println sql
	}
	
	
} 
