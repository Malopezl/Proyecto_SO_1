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
import kurokami.calendarizador.estadisticaCalendarizador;
/**
 *
 * @author marcos
 */
public class Calendarizador extends Thread{
    public static Proceso procesoEnCurso;
    private ArrayList<Proceso> listaProcesos;
    private int procesosActivos;
    private int contador;
    public Calendarizador(){
        procesoEnCurso = null;
        listaProcesos = new ArrayList<>();
        contador = -1;
    }
    
    
    @Override
    public void run(){
        boolean correr = true;
        int tiempo;
        int i;
        Proceso procesoActivo;
        while(correr){
            if(listaProcesos.size() > 0 && procesosActivos > 0){
                procesoActivo = listaProcesos.get(contador);
                procesoActivo.setEstadoAtendido();
                Calendarizador.procesoEnCurso = procesoActivo;
                for(i = 0; i < 40; i++){
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Calendarizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                procesoActivo.setEstadoEspera();
                
            }
        }
    }
}
