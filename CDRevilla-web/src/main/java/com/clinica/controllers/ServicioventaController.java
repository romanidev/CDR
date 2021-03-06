package com.clinica.controllers;

import com.clinica.entidades.Servicioventa;
import com.clinica.controllers.util.JsfUtil;
import com.clinica.controllers.util.JsfUtil.PersistAction;
import com.clinica.fachadas.ServicioventaFacadeLocal;

import java.io.Serializable;
import java.util.List;
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

@Named("servicioventaController")
@SessionScoped
public class ServicioventaController implements Serializable {

  @EJB
  private com.clinica.fachadas.ServicioventaFacadeLocal ejbFacade;
  private List<Servicioventa> items = null;
  private Servicioventa selected;

  public ServicioventaController() {
  }

  public Servicioventa getSelected() {
    return selected;
  }

  public void setSelected(Servicioventa selected) {
    this.selected = selected;
  }

  protected void setEmbeddableKeys() {
    selected.getServicioventaPK().setIdServicio(selected.getServicio().getIdServicio());
    selected.getServicioventaPK().setIdVenta(selected.getVenta().getIdVenta());
  }

  protected void initializeEmbeddableKey() {
    selected.setServicioventaPK(new com.clinica.entidades.ServicioventaPK());
  }

  private ServicioventaFacadeLocal getFacade() {
    return ejbFacade;
  }

  public Servicioventa prepareCreate() {
    selected = new Servicioventa();
    initializeEmbeddableKey();
    return selected;
  }

  public void create() {
    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ServicioventaCreated"));
    if (!JsfUtil.isValidationFailed()) {
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public void update() {
    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ServicioventaUpdated"));
  }

  public void destroy() {
    persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ServicioventaDeleted"));
    if (!JsfUtil.isValidationFailed()) {
      selected = null; // Remove selection
      items = null;    // Invalidate list of items to trigger re-query.
    }
  }

  public List<Servicioventa> getItems() {
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

  public Servicioventa getServicioventa(com.clinica.entidades.ServicioventaPK id) {
    return getFacade().find(id);
  }

  public List<Servicioventa> getItemsAvailableSelectMany() {
    return getFacade().findAll();
  }

  public List<Servicioventa> getItemsAvailableSelectOne() {
    return getFacade().findAll();
  }

  @FacesConverter(forClass = Servicioventa.class)
  public static class ServicioventaControllerConverter implements Converter {

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
      if (value == null || value.length() == 0) {
        return null;
      }
      ServicioventaController controller = (ServicioventaController) facesContext.getApplication().getELResolver().
              getValue(facesContext.getELContext(), null, "servicioventaController");
      return controller.getServicioventa(getKey(value));
    }

    com.clinica.entidades.ServicioventaPK getKey(String value) {
      com.clinica.entidades.ServicioventaPK key;
      String values[] = value.split(SEPARATOR_ESCAPED);
      key = new com.clinica.entidades.ServicioventaPK();
      key.setIdVenta(Integer.parseInt(values[0]));
      key.setIdServicio(Integer.parseInt(values[1]));
      return key;
    }

    String getStringKey(com.clinica.entidades.ServicioventaPK value) {
      StringBuilder sb = new StringBuilder();
      sb.append(value.getIdVenta());
      sb.append(SEPARATOR);
      sb.append(value.getIdServicio());
      return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
      if (object == null) {
        return null;
      }
      if (object instanceof Servicioventa) {
        Servicioventa o = (Servicioventa) object;
        return getStringKey(o.getServicioventaPK());
      } else {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Servicioventa.class.getName()});
        return null;
      }
    }

  }

}
