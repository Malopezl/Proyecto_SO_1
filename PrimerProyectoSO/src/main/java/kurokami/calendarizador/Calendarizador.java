/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurokami.calendarizador;
import utilidades.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
/**
 *
 * @author marcos
 */
public class Calendarizador extends Thread{
    public static Proceso procesoEnCurso;
    private ArrayList<Proceso> listaProcesos;
    private int procesosActivos;
    private int contador;
    /**
     * Constructor de nuevo calendarizador
     */
    public Calendarizador(){
        procesoEnCurso = null;
        listaProcesos = new ArrayList<>();
        contador = -1;
    }
    /**
     * 
     */
    @Override
    public void run(){
        boolean correr = true;
        int i;
        Proceso procesoActivo;
        while(correr){
            if (procesoEnCurso != null)
                PC();
            System.out.println("Tamaño lista: " + this.listaProcesos.size()+ "   en espera: " + this.procesosActivos);
            if(this.listaProcesos.size() > 0 && this.procesosActivos > 0){
                procesoActivo = listaProcesos.get(contador);
                System.out.println(procesoActivo.getID());
                if(procesoActivo.getEstado().equals(EstadoProceso.EN_ESPERA.getEstado())){
                    procesoActivo.setEstadoAtendido();
                    this.actualizarListaFinalizado();
                    Calendarizador.procesoEnCurso = procesoActivo;
                    for(i = 0; i < 40; i++){
                        try {
                            this.actualizarListaRestante();
                            Thread.sleep(25);
                            if(procesoActivo.getRestante() <= 0){
                                break;
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Calendarizador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(procesoActivo.getRestante() > 0){
                        procesoActivo.setEstadoEspera();
                    }else{
                        procesoActivo.setEstadoFinalizado();
                        procesosActivos -= 1;
                    }
                    this.actualizarListaFinalizado();
                }
                contador+=1;
                if(contador == listaProcesos.size() ){
                    contador = 0;
                }
                
                
            }
        }
    }
    /**
     * Este metodo agrega un proceso a la lista de procoesos del calendarizador, ademas de ajustar la cantidad de procesos activos.
     * @param id Recibe el nombre que tendra el proceso 
     */
    public void nuevoProceso(String id){
        Proceso nuevo = new Proceso(id);
        nuevo.setEstadoEspera();
        nuevo.start();
        if(listaProcesos.isEmpty()){
           this.contador = 0;
           this.procesosActivos = 1;
        }else{
            this.procesosActivos += 1;
        }
        this.listaProcesos.add(nuevo);
        this.actualizarLista();
    }
    
    /**
     * Actualiza todas las listas que se muestran en la clase estadisticaCalendarizador.
     */
    private void actualizarLista(){
        DefaultListModel procesoID = new DefaultListModel();
        DefaultListModel inicio = new DefaultListModel();
        DefaultListModel tiempoTotal = new DefaultListModel();
        DefaultListModel tiempoRestante = new DefaultListModel();
        DefaultListModel finalizacion = new DefaultListModel();
        for(Proceso p : listaProcesos){
            procesoID.addElement(p.getID());
            inicio.addElement(p.getHoraInicio());
            tiempoTotal.addElement(Double.toString(p.getTotal()));
            tiempoRestante.addElement(Double.toString(p.getRestante()));
            finalizacion.addElement(p.getHoraFin());
        }
        estadisticaCalendarizador.estadisticaInicio.setModel(inicio);
        estadisticaCalendarizador.estadisticaProceso.setModel(procesoID);
        estadisticaCalendarizador.estadisticaRestante.setModel(tiempoRestante);
        estadisticaCalendarizador.estadisticaTotal.setModel(tiempoTotal);
        estadisticaCalendarizador.estadisticaFinal.setModel(finalizacion);
    }
    /**
     * Actualiza los valores dentro de la lista de tiempo restante en la clase estadisticaCalendarizador
     */
    private void actualizarListaRestante(){
        DefaultListModel tiempoRestante = new DefaultListModel();
        for(Proceso p : listaProcesos){
            tiempoRestante.addElement(Double.toString(p.getRestante()));
        }
        estadisticaCalendarizador.estadisticaRestante.setModel(tiempoRestante);
    }
    /**
     * Actualiza los valores dentro de la lista de hora de finalizacion dentro de la clase estadistica calendarizacion.
     */
    private void actualizarListaFinalizado(){
        DefaultListModel finalizacion = new DefaultListModel();
        for(Proceso p : listaProcesos){
            finalizacion.addElement(p.getHoraFin());
        }
        estadisticaCalendarizador.estadisticaFinal.setModel(finalizacion);
    }
    
    /**
     * Este metodo obtiene el ID y la direccion de memoria del proceso que esta en ejecucion y los asigna a un label y un textfield
     */
    
    public void PC() {
        ContadorPrograma.jLabel5.setText(procesoEnCurso.getID());
        ContadorPrograma.jTextField1.setText(Integer.toHexString(procesoEnCurso.hashCode()));
    }
}
