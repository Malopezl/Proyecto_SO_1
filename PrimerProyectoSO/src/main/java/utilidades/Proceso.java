
package utilidades;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Marcos Sierra 
 */
public class Proceso extends Thread{
    private final String ID;
    private final Date INICIO;
    private Date fechaFinal;
    private double total;
    private double restante;
    private String estado;
    
    /**
     * Constructor del nuevo proceso.
     * @param id  Nombre que recibira el proceso.
     * 
     */
    
    public Proceso(String id){
        this.INICIO = new Date();
        this.ID = id;
        fechaFinal = null;
        this.total = -1;
        Random nuevoAleatorio = new Random(System.currentTimeMillis());
        while(total < 1){
            total = nuevoAleatorio.nextInt(100);
        }
        this.restante = total;
        this.estado = EstadoProceso.NUEVO.getEstado();
    }
    
    @Override
    public void run(){
        while(!(this.estado.equals(EstadoProceso.FINALIZADO.getEstado()))){
           System.out.println("Sigo vivo");
            if(this.estado.equals(EstadoProceso.ATENDIDO.getEstado())){
                    this.restante -= 0.10;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Proceso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        this.fechaFinal = new Date();
    }
    /**
     * Cambia el estado a EN_ESPERA
     * 
     */
    public void setEstadoEspera(){
        this.estado = EstadoProceso.EN_ESPERA.getEstado();
    }
    /**
     * Cambia el estado a ATENDIDO
     */
    public void setEstadoAtendido(){
        this.estado = EstadoProceso.ATENDIDO.getEstado();
    }
    /**
     * Cambia el estado a BLOQUEADO
     */
    public void setEstadoBloqueado(){
        this.estado = EstadoProceso.BLOQUEADO.getEstado();
    }
    /**
     * Cambia el estado a FINALIZADO
     */
    public void setEstadoFinalizado(){
        this.estado = EstadoProceso.FINALIZADO.getEstado();
    }
    /**
     * 
     * @return Devuelve String con la Fecha en la que se creo el proceso 
     */
    public String getFechaInicio(){
        return FormateoDatos.FORMATO_FECHA.format(INICIO);
    }
    /**
     * 
     * @return Devuelve String con la Hora a la que se creo el metodo
     */
    public String getHoraInicio(){
        return FormateoDatos.FORMATO_HORA.format(INICIO);
    }
    /**
     * 
     * @return Devuelve String con la fecha en la que termino el proceso, en caso no haber terminado devuelve el estado 
     */
    public String getFechaFin(){
        if(this.fechaFinal != null){
            return FormateoDatos.FORMATO_FECHA.format(INICIO);
        }else{
            return this.estado;
        }
    }
    /**
     * 
     * @return Devuelve String con la hora en la que termino el proceso, en caso no haber terminado devuelve el estado en el que se encuentra
     */
    public String getHoraFin(){
        if(this.fechaFinal != null){
            return FormateoDatos.FORMATO_HORA.format(INICIO);
        }else{
            return this.estado;
        }
    }
   
    public String getID() {
        return ID;
    }

    public double getTotal() {
        return total;
    }

    public double getRestante() {
        return restante;
    }

    public String getEstado() {
        return estado;
    }
    
    
}
