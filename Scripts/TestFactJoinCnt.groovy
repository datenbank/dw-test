def alias = []

("b".."z").each {
 alias << it
}

model.tables.findAll {it.testType.contains("JOIN")}.each {
	def sql = "SELECT COUNT(*) FROM ${it.schema}.${it.table} a\r\n"
    
    def i=0
    it.columns.findAll {it.testType.contains("JOIN")}.each {
      	sql += "INNER JOIN ${it.columnRef.tableRef.schema}.${it.columnRef.tableRef.table} ${alias[i]} ON a.${it.column}=${alias[i]}.${it.columnRef.column}\r\n"
        
        i++
    }
    if(it.where != "-")
    	sql += "WHERE $it.where\r\n"
        
    def file = new File("${path}Target/${it.group}#${it.table}_FACT_JOIN_CNT.sql")
	if(!file.exists()) {
		file << sql
	}    
        
        
    println sql
    
    def sqlSrc = "SELECT COUNT(*) FROM ${it.schema}.${it.table} a\r\n"
    if(it.where != "-")
    	sqlSrc += "WHERE $it.where\r\n"
    def fileSrc = new File("${path}Source/${it.group}#${it.table}_FACT_JOIN_CNT.sql")
	if(!fileSrc.exists()) {
		fileSrc << sqlSrc
	}
    println sqlSrc
    
}