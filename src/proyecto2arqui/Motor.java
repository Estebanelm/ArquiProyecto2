/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2arqui;

/**
 *
 * @author Pelo
 */
public class Motor {
    private boolean estado;

    /**
     * @return the estado
     */
    public boolean getEstado() {
        return estado;
    }
    
    public void setEstado(boolean estado){
        this.estado = estado;
    }
    
    public void encenderMotor(Bluetooth bluetooth){
        //bluetooth.enviarMensaje("encender");
    }
    
    public void apagarMotor(Bluetooth bluetooth){
       // bluetooth.enviarMensaje("apagar");
    }
}
