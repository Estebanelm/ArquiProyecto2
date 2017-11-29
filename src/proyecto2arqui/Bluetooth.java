package proyecto2arqui;

import java.io.IOException;
import java.util.Date;
import org.korecky.bluetooth.client.hc06.entity.RFCommBluetoothDevice;
import org.korecky.bluetooth.client.hc06.listener.RFCommClientEventListener;
import org.korecky.bluetooth.client.hc06.event.ErrorEvent;
import org.korecky.bluetooth.client.hc06.event.MessageReceivedEvent;
import org.korecky.bluetooth.client.hc06.event.ProgressUpdatedEvent;
import org.korecky.bluetooth.client.hc06.event.ScanFinishedEvent;
import org.korecky.bluetooth.client.hc06.listener.BluetoothScanEventListener;

public class Bluetooth{
    
    public static RFCommBluetoothDevice deviceFound;
    
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
        RFCommBluetoothDevice selectedDevice = deviceFound;
        RFCommClientThread commThread = new RFCommClientThread(selectedDevice.getUrl(), '\n', new char[]{'a'}, new RFCommClientEventListener() {
                            @Override
                            public void error(ErrorEvent evt) {
                                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                            }

                            @Override
                            public void messageReceived(MessageReceivedEvent evt) {
                                System.out.println(String.format("[%s] %s", new Date(), evt.getMessage()));
                            }
                        });
        commThread.start();

        // Send message to HC06 module
        commThread.send(command);
    }
    /*public void ReceiveMessage()
    {
        try{
            System.out.println("Connecting to " + urlDevice);
    
            StreamConnection streamConnection = (StreamConnection) Connector.open(urlDevice);
    
            // Send some text to server
            InputStream is = streamConnection.openInputStream();
            byte[] b = new byte[200];
            Thread.sleep(200);
            is.read(b);
            is.close();
            streamConnection.close();
            stringSensores = b.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
