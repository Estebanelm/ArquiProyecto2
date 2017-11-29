/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2arqui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.*;
/**
 *
 * @author Pelo
 */
public class Proyecto2Arqui extends Frame implements ActionListener, WindowListener{

    private Label lblSensorTemperatura;    // Declare a Label component 
    private Label lblSensorLluvia;    // Declare a Label component
    private Label lbTecho;    // Declare a Label component 
    private TextField tfSensorTemperatura; // Declare a TextField component 
    private TextField tfSensorLluvia; // Declare a TextField component 
    private TextField tfTecho; // Declare a TextField component 
    private Button btnAbrirTecho;   // Declare a Button component
    private Button btnCerrarTecho;
    private double temperatura = 0;
    private double lluvia = 0;
    private String techo;
    private static Bluetooth bluetooth;
    
    public Proyecto2Arqui()
    {
        
        setLayout(new FlowLayout());
      /*   
      lblSensorTemperatura = new Label("Sensor de temperatura");  // construct the Label component
      add(lblSensorTemperatura);   // "super" Frame container adds Label component
      
      tfSensorTemperatura = new TextField("0", 10); // construct the TextField component
      tfSensorTemperatura.setEditable(false);       // set to read-only
      add(tfSensorTemperatura);                     // "super" Frame container adds TextField component
      
      lblSensorLluvia = new Label("Sensor de lluvia");  // construct the Label component
      add(lblSensorLluvia);                // "super" Frame container adds Label component
 
      tfSensorLluvia = new TextField("0", 10); // construct the TextField component
      tfSensorLluvia.setEditable(false);       // set to read-only
      add(tfSensorLluvia);                     // "super" Frame container adds TextField component
      */
      lbTecho = new Label("Techo");  // construct the Label component
      add(lbTecho);                // "super" Frame container adds Label component
 
      tfTecho = new TextField("Cerrado", 10); // construct the TextField component
      tfTecho.setEditable(false);       // set to read-only
      add(tfTecho);                     // "super" Frame container adds TextField component
 
      btnAbrirTecho = new Button("Abrir Techo");   // construct the Button component
      add(btnAbrirTecho);                    // "super" Frame container adds Button component
      
      btnCerrarTecho = new Button("Cerrar Techo");   // construct the Button component
      add(btnCerrarTecho);                    // "super" Frame container adds Button component
 
      btnAbrirTecho.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            techo = "Abierto";
            tfTecho.setText(techo);
             try {
                 bluetooth.sendMessageToDevice("1");
             } catch (IOException ex) {
                 Logger.getLogger(Proyecto2Arqui.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
        });
      
      btnCerrarTecho.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            techo = "Cerrado";
            tfTecho.setText(techo);
             try {
                 bluetooth.sendMessageToDevice("0");
             } catch (IOException ex) {
                 Logger.getLogger(Proyecto2Arqui.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
      });
 
         addWindowListener(this);
      setTitle("Cubre plantas");  // "super" Frame sets its title
      setSize(250, 100);        // "super" Frame sets its initial window size

 
      setVisible(true);         // "super" Frame shows

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Motor motorPlanta = new Motor();
        Monitor monitorPlanta = new Monitor();
        bluetooth = new Bluetooth();
        try {
            bluetooth.execute();
        } catch (Exception ex) {
            Logger.getLogger(Proyecto2Arqui.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Proyecto2Arqui();
        
        
        
    }
    
     @Override
   public void actionPerformed(ActionEvent evt) {
      ++temperatura; // Increase the counter value
      // Display the counter value on the TextField tfCount
      tfSensorTemperatura.setText(temperatura + ""); // Convert int to String
   }
    @Override
   public void windowClosing(WindowEvent evt) {
      System.exit(0);  // Terminate the program
   }
   
   // Not Used, but need to provide an empty body to compile.
   @Override public void windowOpened(WindowEvent evt) { }
   @Override public void windowClosed(WindowEvent evt) { }
   @Override public void windowIconified(WindowEvent evt) { }
   @Override public void windowDeiconified(WindowEvent evt) { }
   @Override public void windowActivated(WindowEvent evt) { }
   @Override public void windowDeactivated(WindowEvent evt) { }
   
}
