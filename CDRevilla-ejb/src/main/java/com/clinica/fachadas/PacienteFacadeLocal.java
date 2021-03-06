/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.fachadas;

import com.clinica.entidades.Paciente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Raul
 */
@Local
public interface PacienteFacadeLocal {

  void create(Paciente paciente);
  
  public Integer persist(Paciente paciente) ;

  void edit(Paciente paciente);

  void remove(Paciente paciente);

  Paciente find(Object id);

  List<Paciente> findAll();

  List<Paciente> findRange(int[] range);

  int count();
  
}
