package proyecto2arqui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.korecky.bluetooth.client.hc06.entity.RFCommBluetoothDevice;
import org.korecky.bluetooth.client.hc06.listener.RFCommClientEventListener;
import org.korecky.bluetooth.client.hc06.event.ErrorEvent;
import org.korecky.bluetooth.client.hc06.event.MessageReceivedEvent;
import org.korecky.bluetooth.client.hc06.event.ProgressUpdatedEvent;
import org.korecky.bluetooth.client.hc06.event.ScanFinishedEvent;
import org.korecky.bluetooth.client.hc06.listener.BluetoothScanEventListener;

public class Bluetooth{
    
    public static RFCommBluetoothDevice deviceFound;
    public static String urlAConectar;
    public static String stringSensores;
    
    public void execute() throws Exception {
        // Prepare search thread
        BluetoothScanThread scanThread = new BluetoothScanThread(new BluetoothScanEventListener() {
            @Override
            public void error(ErrorEvent evt) {
                // When error happenes
                evt.getError().printStackTrace();
            }

            @Override
            public void scanFinished(ScanFinishedEvent evt) {
                System.out.println("");
                System.out.println("Found RFComm decices");
                int i = 1;
                for (RFCommBluetoothDevice device : evt.getFoundDevices()) {
                    System.out.println(String.format("%d:", i));
                    System.out.println(String.format("   Address: %s", device.getAddress()));
                    System.out.println(String.format("   Name: %s", device.getName()));
                    System.out.println(String.format("   URL: %s", device.getUrl()));
                    i++;
                    deviceFound = device;
                    urlAConectar = device.getUrl();
                }
                System.out.println();
            }

            @Override
            public void progressUpdated(ProgressUpdatedEvent evt) {
                System.out.println(String.format("[%d/%d] %s", evt.getWorkDone(), evt.getWorkMax(), evt.getMessage()));
            }
        });

        // Start search of bluetooth device
        scanThread.start();
        scanThread.scanDevices();
    }
    
    public void sendMessageToDevice(String command) throws IOException{
        try{
            System.out.println("Connecting to " + urlAConectar);
    
            StreamConnection streamConnection = (StreamConnection) Connector.open(urlAConectar);
    
            // Send some text to server
            byte data[] = command.getBytes();
            OutputStream os = streamConnection.openOutputStream();
            os.write(data); //'1' means ON and '0' means OFF
            os.close();
            streamConnection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ReceiveMessage()
    {
        try{
            System.out.println("Connecting to " + urlAConectar);
    
            StreamConnection streamConnection = (StreamConnection) Connector.open(urlAConectar);
            System.out.println("se conecto");
            // Send some text to server

            InputStream is = streamConnection.openInputStream();
            int byteCount = is.available();
            byte[] rawBytes;
            if(byteCount > 0)
             {
                rawBytes = new byte[byteCount];
                is.read(rawBytes);
                stringSensores = rawBytes.toString();
              }
            is.close();
            streamConnection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
