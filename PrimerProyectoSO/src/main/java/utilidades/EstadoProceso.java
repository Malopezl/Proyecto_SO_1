/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

/**
 *
 * @author marcos
 */
public enum EstadoProceso {
    ATENDIDO("ATENDIDO"),
    EN_ESPERA("EN_ESPERA"),
    BLOQUEADO("BLOQUEADO"),
    FINALIZADO("FINALIZADO"),
    NUEVO("NUEVO");
    
    private final String estado;
    
    EstadoProceso(String estado){
        this.estado = estado;
    }
    
    public String getEstado(){
        return estado;
    }
    
}
