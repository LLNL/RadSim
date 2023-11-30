<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" version="1.0" encoding="UTF-8"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Remove the undocumented n42rsiA attributes -->
    <xsl:template match="n42rsiA:@*"  xmlns:n42rsiA="http://www.radiationsolutions.ca/pubdoc/n42rsiA" />

    <!-- Flir has invalid tags in their format -->
    <xsl:template match="n42rsiA:GammaSampleInfo" xmlns:n42rsiA="http://www.radiationsolutions.ca/pubdoc/n42rsiA">
    </xsl:template>

</xsl:stylesheet>
