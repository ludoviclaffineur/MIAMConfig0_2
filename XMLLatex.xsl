<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "1.0">
 	<xsl:template match='/'>
 	
 	\documentclass[12pt,a4paper]{article}
	\usepackage[utf8]{inputenc}
	\usepackage[french]{babel}
	\usepackage[T1]{fontenc}
	\usepackage{amsmath}
	\usepackage{amsfonts}
	\usepackage{amssymb}
	\usepackage{makeidx}
	\usepackage{graphicx}
	\usepackage[left=2cm,right=2cm,top=2cm,bottom=2cm]{geometry}
	\author{Musiques et Recherches}
	\title{Configuration salle : <xsl:value-of select ="config/salle/nom"/> \\ Adresse : <xsl:value-of select ="config/salle/adresse" /> \\ date : <xsl:value-of select ="config/salle/date" />}

	\begin{document}
	\maketitle
	\section{Liste des hp utilisés}
	<xsl:variable name="marque" select="document('hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/marque" />
 	<xsl:variable name="modele" select="document('hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/modele" />
 
	
 	\begin{itemize}
 		<xsl:for-each select="config/hp">
 			<xsl:variable name="idHp" select="./idHp" />
 			
 			\item <xsl:value-of select="document('hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/marque" /> - <xsl:value-of select="document('hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/modele" /> - <xsl:value-of select="document('hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/numero" />
					\includegraphics[height=30mm]{images/<xsl:value-of select="document('hp_database.xml')/hp_en_magasin/hp[@id=$idHp]/image "/>}
					
    	</xsl:for-each>
	\end{itemize}
	\end{document}
	</xsl:template>
    
</xsl:stylesheet>