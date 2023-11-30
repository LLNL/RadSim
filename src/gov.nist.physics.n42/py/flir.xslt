<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" version="1.0" encoding="UTF-8"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

		<!-- Remove the undocumented flir attributes -->
		<xsl:template match="flir:@*"  xmlns:flir="https://www.detectionsupport.com/radiation/xml" />

		<!-- Flir has invalid tags in their format -->
		<xsl:template match="n42:Latitude" xmlns:n42="http://physics.nist.gov/N42/2011/N42">
			<n42:LatitudeValue><xsl:apply-templates/></n42:LatitudeValue>
		</xsl:template>

		<xsl:template match="n42:Longitude" xmlns:n42="http://physics.nist.gov/N42/2011/N42">
			<n42:LongitudeValue><xsl:apply-templates/></n42:LongitudeValue>
		</xsl:template>

		<xsl:template match="n42:Elevation" xmlns:n42="http://physics.nist.gov/N42/2011/N42">
			<n42:ElevationValue><xsl:apply-templates/></n42:ElevationValue>
		</xsl:template>

</xsl:stylesheet>
