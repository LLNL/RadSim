<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" indent="yes" method="xml" />
    <xsl:template match="n42:RadMeasurement" xmlns:n42="http://physics.nist.gov/N42/2011/N42">
        <xsl:copy>
            <xsl:apply-templates select="@*" />
            <!-- copy some elements in a specific order  -->
            <xsl:apply-templates select="n42:MeasurementClassCode" />
            <xsl:apply-templates select="n42:StartDateTime" />
            <xsl:apply-templates select="n42:RealTimeDuration" />
            <xsl:apply-templates select="n42:Spectrum" />
            <xsl:apply-templates select="n42:GrossCounts" />
            <xsl:apply-templates select="n42:DoseRate" />
            <xsl:apply-templates select="n42:TotalDose" />
            <xsl:apply-templates select="n42:ExposureRate" />
            <xsl:apply-templates select="n42:TotalExposure" />
            <xsl:apply-templates select="n42:RadInstrumentState" />
            <xsl:apply-templates select="n42:RadDetectorState" />
            <xsl:apply-templates select="n42:RadItemState" />
            <xsl:apply-templates select="n42:OccupancyIndicator" />
            <xsl:apply-templates select="n42:RadMeasurementExtension" />
        </xsl:copy>
</xsl:template>
    <!-- Identity transform: copy everything not already covered -->
    <xsl:template match="@*|node()">
       <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
       </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
