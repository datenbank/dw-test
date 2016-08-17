model.tables.each {
	def sql = "SELECT BINARY_CHECKSUM(${it.columns.findAll{ it.testType.contains('HASH') }.join(', ')}) \r\nFROM ${it.schema}.${it.table}\r\n"
    if(it.where != "-")	
		sql += "WHERE ${it.where}\r\n"
    def file = new File("${path}Target/${it.database}#${it.table}_HASH.sql")
       
      
    if (!file.exists() && it.columns.findAll{ it.testType.contains('HASH')}.size() > 0 ) { 
      file << sql
    	  
    }
      
    println sql
			
} 


model.tables.each {

  	

	def sqlSrc = "SELECT BINARY_CHECKSUM(${it.tableRef.columns.findAll{it.testType.contains('HASH') }.join(', ')}) \r\nFROM ${it.tableRef.schema}.${it.tableRef.table}\r\n"
    
    if(it.tableRef.where != "-")	
		sqlSrc += "WHERE ${it.tableRef.where}\r\n"
      
    def fileSrc = new File("${path}Source/${it.database}#${it.table}_HASH.sql")
      
    if (!fileSrc.exists() && it.columns.findAll{ it.testType.contains('HASH')}.size() > 0 ) { 
      fileSrc << sqlSrc
    	  
    }
      
    println sqlSrc
			
} 