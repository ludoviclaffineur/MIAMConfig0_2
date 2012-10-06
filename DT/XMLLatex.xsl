<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "1.0">
 	<xsl:template match='/'>
 	
 		<xsl:for-each select="config/hp">
 			<xsl:variable name="idHp" select="./idHp" />
 			
 				<xsl:value-of select ="./idHp" />
 			<xsl:value-of select="document('../hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/name" />
			
    	</xsl:for-each>
	
	
	</xsl:template>
    
</xsl:stylesheet>