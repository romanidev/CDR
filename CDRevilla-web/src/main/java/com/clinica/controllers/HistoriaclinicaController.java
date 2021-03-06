package com.clinica.controllers;

import com.clinica.entidades.Historiaclinica;
import com.clinica.controllers.util.JsfUtil;
import com.clinica.controllers.util.JsfUtil.PersistAction;
import com.clinica.fachadas.HistoriaclinicaFacadeLocal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("historiaclinicaController")
@SessionScoped
public class HistoriaclinicaController implements Serializable {

  @EJB
  private com.clinica.fachadas.HistoriaclinicaFacadeLocal ejbFacade;
  private List<Historiaclinica> items = null;
  private Historiaclinica selected;

  public HistoriaclinicaController() {
  }

  public Historiaclinica getSelected() {
    return selected;
  }

  public void setSelected(Historiaclinica selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
  }

  protected void initializeEmbeddableKey() {
  }

  private HistoriaclinicaFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Historiaclinica prepareCreate() {
    selected = new Historiaclinica();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("HistoriaclinicaCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("HistoriaclinicaUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("HistoriaclinicaDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Historiaclinica> getItems() {
    if (items == null) {
      items = getFacade().findAll();
    }
    return items;
  }

  private void persist(PersistAction persistAction, String successMessage) {
    if (selected != null) {
      setEmbeddableKeys();
      try {
        if (persistAction != PersistAction.DELETE) {
          getFacade().edit(selected);
        } else {
          getFacade().remove(selected);
        }
        JsfUtil.addSuccessMessage(successMessage);
      } catch (EJBException ex) {
        String msg = "";
        Throwable cause = ex.getCause();
        if (cause != null) {
          msg = cause.getLocalizedMessage();
        }
        if (msg.length() > 0) {
          JsfUtil.addErrorMessage(msg);
        } else {
          JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
      } catch (Exception ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
      }
    }
  }

  public Historiaclinica getHistoriaclinica(java.lang.Integer id) {
    return getFacade().find(id);
  }

  public List<Historiaclinica> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public Map<Integer, Historiaclinica> getItemsAvailableSelectOne() {

    Map<Integer, Historiaclinica> combo = new HashMap<>();

    List<Historiaclinica> list = getFacade().findAll();
    for (Historiaclinica item : list) {
      combo.put(item.getIdHistoriaClinica(), item);
    }
    return combo;
  }

  @FacesConverter(forClass = Historiaclinica.class)
  public static class HistoriaclinicaControllerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      HistoriaclinicaController controller = (HistoriaclinicaController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "historiaclinicaController");
      return controller.getHistoriaclinica(getKey(value));
    }

    java.lang.Integer getKey(String value) {
      java.lang.Integer key;
      key = Integer.valueOf(value);
      return key;
    }

    String getStringKey(java.lang.Integer value) {
      StringBuilder sb = new StringBuilder();
      sb.append(value);
      return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
      if (object == null) {
        return null;
      }
      if (object instanceof Historiaclinica) {
        Historiaclinica o = (Historiaclinica) object;
        return getStringKey(o.getIdHistoriaClinica());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Historiaclinica.class.getName()});
        return null;
      }
    }

  }

}
