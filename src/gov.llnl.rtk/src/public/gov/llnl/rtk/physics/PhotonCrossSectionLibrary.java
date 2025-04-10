/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gov.llnl.rtk.physics;

/**
 *
 * @author nelson85
 */
public interface PhotonCrossSectionLibrary
{

  /**
   * Get the cross sections by energy for an element.
   *
   * @param element
   * @return
   */
  PhotonCrossSections get(Element element);

  /**
   * Get the cross sections by element for a material.
   *
   * @param material
   * @return
   */
  PhotonCrossSections get(Material material);

}
