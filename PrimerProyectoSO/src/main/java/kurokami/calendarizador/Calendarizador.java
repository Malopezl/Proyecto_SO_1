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
import malopez.contadorprograma.ContadorPrograma;
/**
 *
 * @author Marcos Sierra, Sharon Gomez
 */
public class Calendarizador extends Thread{
    public static Proceso procesoEnCurso;
    private ArrayList<Proceso> listaProcesos;
    private ArrayList<String> enEspera;
    private int procesosActivos;
    private int contador;
    private boolean interrupcion;
    /**
     * Constructor de nuevo calendarizador
     */
    public Calendarizador(){
        procesoEnCurso = null;
        listaProcesos = new ArrayList<>();
        enEspera= new ArrayList<>();
        contador = -1;
        interrupcion = false;
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
           
            System.out.println("Tamaño lista: " + this.listaProcesos.size()+ "   en espera: " + this.procesosActivos);
            if((this.listaProcesos.size() > 0) && (this.procesosActivos > 0)){
                procesoActivo = listaProcesos.get(contador);
                System.out.println(procesoActivo.getID());
                if(procesoActivo.getEstado().equals(EstadoProceso.EN_ESPERA.getEstado())){
                    procesoActivo.setEstadoAtendido();
                    this.rotarListaEnEspera(procesoActivo.getID());
                    this.actualizarListaFinalizado();
                    Calendarizador.procesoEnCurso = procesoActivo;
                    if (procesoEnCurso != null)
                        PC();
                    for(i = 0; i < 40; i++){
                        try {
                            if(this.interrupcion)
                            {
                                procesoActivo.setEstadoBloqueado();
                                this.actualizarListaFinalizado();
                                Thread.sleep(this.Interrupcion());
                                this.interrupcion = false;
                                procesoActivo.setEstadoAtendido();
                                this.actualizarListaFinalizado();
                            }else{
                                this.actualizarListaRestante();
                                Thread.sleep(25);
                                if(procesoActivo.getRestante() <= 0){
                                    break;
                                }
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
                        this.actualizarListaEnEspera(procesoActivo.getID());
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
     * Este metodo agrega un proceso a la lista de procesos del calendarizador, ademas de ajustar la cantidad de procesos activos.
     * Agrega el nombre del proceso a la lista de procesos en espera.
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
        this.enEspera.add(id);
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
     * Se encarga de actualizar la cola de procesos en espera.
     * Coloca el nombre del proceso que se esta atendiendo en el jTextField.
     * @param id Recibe el nombre del proceso que se atiende en run()
     */
    private void rotarListaEnEspera(String id)
    {
        if(this.enEspera.size()>0 ){
            ArrayList<String> temporal = enEspera;
            DefaultListModel listaEnEspera = new DefaultListModel();
            int numRotaciones =0;
            
            for(String p : temporal)
            {
                listaEnEspera.addElement(p);
            }
           
            for(int i=0; i<temporal.size(); i++)
            {
                if(temporal.get(i).equals(id))
                {
                    numRotaciones=i;
                    break;
                }
            }
            
            for(int i=0; i<numRotaciones; i++)
            {
             String temporalID = temporal.get(i);
             listaEnEspera.addElement(temporalID);
             listaEnEspera.remove(0);
            }
            
            mainCalendarizador.calendarizadorListado.setModel(listaEnEspera); 
            mainCalendarizador.turnoProceso.setText((String) listaEnEspera.get(0));
            
        }
        
    }
    
    /**
     * Updated upstream
     * Este metodo obtiene el ID y la direccion de memoria del proceso que esta en ejecucion y los asigna a un label y un textfield
     */
    
    public void PC() {
        ContadorPrograma.jLabel5.setText(procesoEnCurso.getID());
        ContadorPrograma.jTextField1.setText(Integer.toHexString(procesoEnCurso.hashCode()));
    }


     /* Actualiza la lista de procesos en Espera, recibe el nombre del proceso para quitarlo de la ista.
     * @param id es el nombre del proceso que cambio al estado TERMINADO.
     */
    private void actualizarListaEnEspera(String id)
    {
        for(int i=0; i<this.enEspera.size(); i++)
            {
                if(this.enEspera.get(i).equals(id))
                {
                    this.enEspera.remove(i);
                    break;
                }
            }
        if(this.enEspera.isEmpty() ){
             mainCalendarizador.calendarizadorListado.setModel(new DefaultListModel()); 
             mainCalendarizador.turnoProceso.setText("");
        }
    }
    
    /**
     * Genera un numero ya sea aleatorio para la duracion de la interrupcion
     * En general es una duracion entre 1 y 5 segundos.
     * @return tiempoInterrupcion 
     */

    private int Interrupcion() {
       
        //Genera un numero aleatorio entre 1000 y 5000 para enviar el tiempo de interrupcion
        int tiempoInterrupcion = (int) (Math.random() * 5000) + 1000;
       
        //Se declara un numero fijo para el tiempo de interrupcion    
        //int tiempoInterrupcion = 3000;
       return tiempoInterrupcion;
               
    }
    /**
     * Cambia el estado de la variable interrupcion con el enviado, que en general es un true;
     * @param interrupcion 
     */

    public void setInterrupcion(boolean interrupcion) {
        this.interrupcion = interrupcion;
    }
    
}

