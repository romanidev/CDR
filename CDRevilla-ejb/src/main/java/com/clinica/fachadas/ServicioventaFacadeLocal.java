/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.fachadas;

import com.clinica.entidades.Servicioventa;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface ServicioventaFacadeLocal {

  void create(Servicioventa servicioventa);

  void edit(Servicioventa servicioventa);

  void remove(Servicioventa servicioventa);

  Servicioventa find(Object id);

  List<Servicioventa> findAll();

  List<Servicioventa> findRange(int[] range);

  int count();
  
}
