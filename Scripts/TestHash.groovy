println "Target: "
model.tables.each {
  
  	def col = it.columns.findAll{it.testType.contains('HASH') }
    
    def ref = []
      
    col.each {
    	ref << it.column
    }
  
	def sql = "SELECT BINARY_CHECKSUM(${ref.join(', ')}) \r\nFROM ${it.schema}.${it.table}\r\n"
    if(it.where != "-")	
		sql += "WHERE ${it.where}\r\n"
    def file = new File("${path}Target/${it.group}#${it.table}_HASH.sql")
       
      
    if (!file.exists() && it.columns.findAll{ it.testType.contains('HASH')}.size() > 0 ) { 
      file << sql
       
    	  
    }
  	if(it.columns.findAll{ it.testType.contains('HASH')}.size() > 0 )
      	println sql
			
} 

println "Source: "
model.tables.each {

  	def col = it.columns.findAll{it.testType.contains('HASH') }
    
    def ref = []
      
    col.each {
    	ref << it.columnRef.column
    }
	if(it.tableRef) {
      def sqlSrc = "SELECT BINARY_CHECKSUM(${ref.join(', ')}) \r\nFROM ${it.tableRef.schema}.${it.tableRef.table}\r\n"

      if(it.tableRef.where != "-")	
          sqlSrc += "WHERE ${it.tableRef.where}\r\n"

      def fileSrc = new File("${path}Source/${it.group}#${it.table}_HASH.sql")

      if (!fileSrc.exists() && it.columns.findAll{ it.testType.contains('HASH')}.size() > 0 ) { 
        fileSrc << sqlSrc

      }
      println sqlSrc
    }
      
    
			
} 