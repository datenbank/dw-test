SELECT '0TargetDatabase','TargetSchema','TargetTable','TargetColumn','TargetColumnOrdinal','TargetColumnNullable','TargetColumnDataType','TargetIsPrimaryKey','SourceDatabase','SourceSchema','SourceTable','SourceColumn','SourceColumnOrdinal','SourceColumnNullable','SourceColumnDataType','SourceIsPrimaryKey','TestType','TargetTableWhere','SourceTableWhere'

UNION
SELECT '0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18'
UNION
SELECT TABLE_CATALOG,
       TABLE_SCHEMA,
       TABLE_NAME,
       COLUMN_NAME,
       cast(ORDINAL_POSITION as varchar),
       IS_NULLABLE,
       DATA_TYPE,
       ISNULL(
                (SELECT MAX('1')
                 FROM [INFORMATION_SCHEMA].[KEY_COLUMN_USAGE] kcu
					INNER JOIN [INFORMATION_SCHEMA].[TABLE_CONSTRAINTS] tc 
						ON kcu.CONSTRAINT_NAME=tc.CONSTRAINT_NAME and tc.CONSTRAINT_TYPE='PRIMARY KEY'
						AND tc.[TABLE_CATALOG]=c.TABLE_CATALOG
						AND tc.[TABLE_SCHEMA]=c.[TABLE_SCHEMA]
						AND tc.[TABLE_NAME]=c.[TABLE_NAME]
                 WHERE kcu.TABLE_CATALOG=c.TABLE_CATALOG
                   AND kcu.TABLE_SCHEMA=c.TABLE_SCHEMA
                   AND kcu.TABLE_NAME=c.TABLE_NAME
                   AND kcu.COLUMN_NAME=c.COLUMN_NAME),'0') IS_PK

	,TABLE_CATALOG,
       TABLE_SCHEMA,
       TABLE_NAME,
       COLUMN_NAME,
       cast(ORDINAL_POSITION as varchar),
       IS_NULLABLE,
       DATA_TYPE,
       ISNULL(
                (SELECT MAX('1')
                 FROM [INFORMATION_SCHEMA].[KEY_COLUMN_USAGE] kcu 
					INNER JOIN [INFORMATION_SCHEMA].[TABLE_CONSTRAINTS] tc 
						ON kcu.CONSTRAINT_NAME=tc.CONSTRAINT_NAME and tc.CONSTRAINT_TYPE='PRIMARY KEY'
						AND tc.[TABLE_CATALOG]=c.TABLE_CATALOG
						AND tc.[TABLE_SCHEMA]=c.[TABLE_SCHEMA]
						AND tc.[TABLE_NAME]=c.[TABLE_NAME]
                 WHERE kcu.TABLE_CATALOG=c.TABLE_CATALOG
                   AND kcu.TABLE_SCHEMA=c.TABLE_SCHEMA
                   AND kcu.TABLE_NAME=c.TABLE_NAME
                   AND kcu.COLUMN_NAME=c.COLUMN_NAME),'0') IS_PK, '-', '-', '-'
FROM [INFORMATION_SCHEMA].[COLUMNS] c
order by 1,2,3,5

