/*
 * Copyright 2022, Lawrence Livermore National Security, LLC.
 * All rights reserved
 * 
 * Terms and conditions are given in "Notice" file.
 */
package gov.llnl.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author her1
 */
public class H2Converter
{
  public static void toCSV(String h2ConnectStr, String csvOutPath, String tableName) throws ClassNotFoundException, SQLException, IOException
  {
    H2Converter.toCSV(h2ConnectStr, Paths.get(csvOutPath).toAbsolutePath(), tableName);
  }
  
  public static void toCSV(String h2ConnectStr, Path csvOutPath, String tableName) throws ClassNotFoundException, SQLException, IOException
  {
    // For constructing the out csv file
    StringBuilder sb = new StringBuilder();

    System.out.println(h2ConnectStr);

    // Open connection
    Connection conn = DriverManager.getConnection(h2ConnectStr);

    // Select all the data
    String query = String.format("SELECT * FROM %s", tableName);
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    // get column infomration and record the name and index
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();

    // Copy the column map
    Map<String, ColumnInfo> columnInfoLinkedMap = new LinkedHashMap<>(COLUMN_ORDER_LINKED_MAP);

    // If the database column has the correct spelling (spell match in feature dictionary)
    // then the column will be in order. If the column name doesn't exists in the
    // linked map then it will be put in the end of the map.
    for (int i = 1; i <= columnCount; ++i)
    {
      String columnName = rsmd.getColumnName(i).replace("_", ".");
      String columnType = rsmd.getColumnTypeName(i);
      ColumnInfo ci = new ColumnInfo();
      ci.name = columnName;
      ci.index = i;
      ci.type = columnType;

      columnInfoLinkedMap.put(columnName, ci);
    }

    // Get rid of keys with null values
    columnInfoLinkedMap.values().removeAll(Collections.singleton(null));

    // Add header infomation to the csv info
    for (String columnName : columnInfoLinkedMap.keySet())
    {
      sb.append(columnName).append(",");
    }
    sb.deleteCharAt(sb.length() - 1); // delete the last comma
    sb.append("\n");

    // Go throught each row of the query result
    while (rs.next())
    {
      // Grab the data from each row in column order
      for (Map.Entry<String, ColumnInfo> entry : columnInfoLinkedMap.entrySet())
      {
        ColumnInfo ci = entry.getValue();
        int index = ci.index;
        String type = ci.type;
        if (type == "INTEGER")
        {
          sb.append(rs.getInt(index)).append(",");
        }
        else if (type == "BOOLEAN")
        {
          sb.append(rs.getBoolean(index)).append(",");
        }
        else if (type == "TINYINT")
        {
          sb.append(rs.getByte(index)).append(",");
        }
        else if (type == "SMALLINT")
        {
          sb.append(rs.getShort(index)).append(",");
        }
        else if (type == "BIGINT")
        {
          sb.append(rs.getLong(index)).append(",");
        }
        else if (type == "DOUBLE")
        {
          sb.append(rs.getDouble(index)).append(",");
        }
        else if (type == "REAL")
        {
          sb.append(rs.getFloat(index)).append(",");
        }
        else if (type == "VARCHAR")
        {
          sb.append("\"").append(rs.getString(index)).append("\"").append(",");
        }
      }
      sb.deleteCharAt(sb.length() - 1); // delete the last comma
      sb.append("\n");
    }
    // clean up
    rs.close();
    stmt.close();
    conn.close();
    String o = sb.toString();
    // write csv data to file
    Files.writeString(csvOutPath, sb.toString());
  }
  
  static class ColumnInfo
  {
    public String name;   // Column name
    public String type;   // Column type
    public int index;     // Index to the result object when querying the db
  }
  
  // Key is the column (feature) name, value is the ColumnInfo object
  private static final Map<String, ColumnInfo> COLUMN_ORDER_LINKED_MAP = new LinkedHashMap<>();

  static
  {
    // Background table
    COLUMN_ORDER_LINKED_MAP.put("ID", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Delta1", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Delta2", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Delta3", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Delta4", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_DeltaAvg", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Panel1", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Panel2", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Panel3", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_Panel4", null);
    COLUMN_ORDER_LINKED_MAP.put("Background_PanelAvg", null);
    
    // Classification table
    COLUMN_ORDER_LINKED_MAP.put("Classification_Action_Investigate", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Action_Release", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Action_Rescan", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_CrossTalk", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_IrregularScan", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_IsEmitting", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Motion_AlignmentError", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Motion_Correlation", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Source_Fissile", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Source_Industrial", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Source_Medical", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Source_Mixed", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Source_NORM", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Source_NonEmitting", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Vehicle_ClusterDistance", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_Vehicle_Type", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_VendorField_Alarm", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_VendorField_Gamma_Alarm", null);
    COLUMN_ORDER_LINKED_MAP.put("Classification_VendorField_Neutron_Alarm", null);
    
    // Extent table
    COLUMN_ORDER_LINKED_MAP.put("Extent_FW3QM_In", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FW3QM_Out", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FW3QM_Ratio", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWHM_In", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWHM_Out", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWHM_Ratio", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWQM_In", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWQM_Out", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWQM_Ratio", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_FWTopBottom_Ratio", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_IntensityFWHMRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_PeakIntensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_PeakVsFWHM", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_TriangleIntensityRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Extent_XLocation", null);
    
    // Falback Joint table
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_BkgChisqr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_FISCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster8_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_INDCluster9_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_MEDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_MEDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_MEDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_MEDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_MEDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_NORMCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_NORMCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_NORMCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_NORMCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_PointY", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointF_PointZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_BkgChisqr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_FISCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster8_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_INDCluster9_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_MEDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_MEDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_MEDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_MEDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_MEDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_NORMCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_NORMCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_NORMCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_NORMCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_PointY", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Joint_JointR_PointZ", null);
    
    // Fallback Statistics table
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_Background_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_FISCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster8_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_INDCluster9_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MEDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MEDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MEDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MEDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MEDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MeanY", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_MeanZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_NORMCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_NORMCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_NORMCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_NORMCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_Significance", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Front_k", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_Background_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_FISCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster8_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_INDCluster9_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MEDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MEDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MEDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MEDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MEDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MeanY", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_MeanZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_NORMCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_NORMCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_NORMCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_NORMCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_Significance", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Peak_k", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_Background_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_FISCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster8_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_INDCluster9_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MEDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MEDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MEDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MEDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MEDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MeanY", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_MeanZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_NORMCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_NORMCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_NORMCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_NORMCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_Significance", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Rear_k", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_Split_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_SplitDip", null);
    COLUMN_ORDER_LINKED_MAP.put("Fallback_Statistics_SpreadI", null);
    
    // Info Faults table
    COLUMN_ORDER_LINKED_MAP.put("Info_Faults_Faults", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Faults_FeatureFailures", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Faults_Fold", null);
    
    // Info Manipulation 1 table
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_CargoModel", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_CompactTitle", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistIntensity1", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistIntensity2", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistSNum", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistSourceInjected", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistX1", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistX2", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistY", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_DistributedTitle", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_EndVelocity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_ExtractionId", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_ExtractionInjected", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_ExtractionIntensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_GammaLeakage", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_NeutronLeakage", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_PointIntensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_PointSNum", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_PointSourceInjected", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_PointX", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_PointY", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_PointZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_StartVelocity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation_velocityChanged", null);
    
    // Info Manipulation 2 table
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_CargoModel", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_CompactTitle", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistIntensity1", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistIntensity2", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistSNum", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistSourceInjected", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistX1", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistX2", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistY", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_DistributedTitle", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_EndVelocity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_ExtractionId", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_ExtractionInjected", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_ExtractionIntensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_GammaLeakage", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_NeutronLeakage", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_PointIntensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_PointSNum", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_PointSourceInjected", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_PointX", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_PointY", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_PointZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_StartVelocity", null);
    COLUMN_ORDER_LINKED_MAP.put("Info_Manipulation2_velocityChanged", null);
    
    // Joint table
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_BkgChisqr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_FISCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster8_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_INDCluster9_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_InCabin", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_InCargo", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_Length", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_MEDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_MEDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_MEDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_MEDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_MEDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_NORMCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_NORMCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_NORMCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_NORMCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_PointX", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_PointY", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointF_PointZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_BkgChisqr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_FISCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster5_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster6_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster7_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster8_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_INDCluster9_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_InCabin", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_InCargo", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_Length", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_MEDCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_MEDCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_MEDCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_MEDCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_MEDCluster4_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_NORMCluster0_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_NORMCluster1_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_NORMCluster2_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_NORMCluster3_corr", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_PointX", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_PointY", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_JointR_PointZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Joint_Separation", null);
    
    // Motion
    COLUMN_ORDER_LINKED_MAP.put("Motion_InitialAcceleration", null);
    COLUMN_ORDER_LINKED_MAP.put("Motion_InitialVelocity", null);
    COLUMN_ORDER_LINKED_MAP.put("Motion_Jerk", null);
    
    // Segment Info table
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_CBPID", null);
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_FoldID", null);
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_FoldName", null);
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_Location_DataSourceId", null);
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_Location_IsSecondary", null);
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_Location_RpmId", null);
    COLUMN_ORDER_LINKED_MAP.put("SegmentInfo_Segments", null);
    
    // Standard table
    COLUMN_ORDER_LINKED_MAP.put("Standard_Gross_All", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Gross_Max", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Gross_Panel1", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Gross_Panel2", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Gross_Panel3", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Gross_Panel4", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio_Max", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio1_Panel1", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio1_Panel2", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio1_Panel3", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio1_Panel4", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio1_PanelAll", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio2_Panel1", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio2_Panel2", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio2_Panel3", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio2_Panel4", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio2_PanelAll", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio3_Panel1", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio3_Panel2", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio3_Panel3", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio3_Panel4", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio3_PanelAll", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio4_Panel1", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio4_Panel2", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio4_Panel3", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio4_Panel4", null);
    COLUMN_ORDER_LINKED_MAP.put("Standard_Ratio4_PanelAll", null);
    
    // Statistics table
    COLUMN_ORDER_LINKED_MAP.put("Statistics_All_IKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_All_IMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_All_ISkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_All_IStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_All_IStdRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_Background_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_FISCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_IKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_IMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster8_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_INDCluster9_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_ISkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_IStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_IStdRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MEDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MEDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MEDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MEDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MEDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MeanY", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_MeanZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_NORMCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_NORMCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_NORMCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_NORMCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_Significance", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_XKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_XMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_XSkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_XStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Front_k", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_IStdDevIMeanRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_InCabin", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_InCargo", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_Background_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_FISCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_IKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_IMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster8_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_INDCluster9_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_ISkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_IStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_IStdRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MEDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MEDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MEDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MEDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MEDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MeanY", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_MeanZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_NORMCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_NORMCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_NORMCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_NORMCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_Significance", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_XKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_XMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_XSkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_XStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Peak_k", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_Background_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_FISCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_IKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_IMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster5_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster6_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster7_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster8_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_INDCluster9_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_ISkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_IStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_IStdRatio", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MEDCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MEDCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MEDCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MEDCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MEDCluster4_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MeanY", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_MeanZ", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_NORMCluster0_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_NORMCluster1_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_NORMCluster2_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_NORMCluster3_chi", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_Significance", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_XKurt", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_XMean", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_XSkew", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_XStdDev", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Rear_k", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Split_DeltaWidth", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Split_Intensity", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Split_Position", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_SplitDip", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_Spread3D", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_SpreadI", null);
    COLUMN_ORDER_LINKED_MAP.put("Statistics_SpreadX", null);
    
    // Vehicle table
    COLUMN_ORDER_LINKED_MAP.put("Vehicle_CabinLength", null);
    COLUMN_ORDER_LINKED_MAP.put("Vehicle_CargoLength", null);
    COLUMN_ORDER_LINKED_MAP.put("Vehicle_LaneWidth", null);
    COLUMN_ORDER_LINKED_MAP.put("Vehicle_Length", null);
  }
  
}
