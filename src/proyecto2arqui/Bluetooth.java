package proyecto2arqui;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;

public class Bluetooth implements DiscoveryListener{
    
    private static Object lock=new Object();
    public ArrayList<RemoteDevice> devices;
    public String urlDevice;
    public Bluetooth() {
        devices = new ArrayList<RemoteDevice>();
    }
    
    public void execute() {
        
        Bluetooth listener =  new Bluetooth();
        
        try{
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC, listener);
            
            try {
                synchronized(lock){
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            
            
            System.out.println("Device Inquiry Completed. ");
            
       
            UUID[] uuidSet = new UUID[3];
            uuidSet[0]=new UUID(0x1105); //OBEX Object Push service
            uuidSet[1]=new UUID(0x0008); //OBEX Object Push service
            uuidSet[2]=new UUID(0x0003); //OBEX Object Push service
            
            int[] attrIDs =  new int[] {
                    0x0100 // Service name
            };
            
            for (RemoteDevice device : listener.devices) {
                System.out.println(device.getBluetoothAddress());
                agent.searchServices(
                        attrIDs,uuidSet,device,listener);
                
                
                try {
                    synchronized(lock){
                        lock.wait();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                
                
                System.out.println("Service search finished.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
        String name;
        try {
            name = btDevice.getFriendlyName(false);
        } catch (Exception e) {
            name = btDevice.getBluetoothAddress();
        }
        
        devices.add(btDevice);
        System.out.println("device found: " + name);
        
    }

    @Override
    public void inquiryCompleted(int arg0) {
        synchronized(lock){
            lock.notify();
        }
    }

    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++) {
            urlDevice = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (urlDevice != null) {
                break; //take the first one
            }
        }
    }
    
    public void sendMessageToDevice(String command){
        try{
            System.out.println("Connecting to " + urlDevice);
    
            StreamConnection streamConnection = (StreamConnection) Connector.open(urlDevice);
    
            // Send some text to server
            byte data[] = command.getBytes();
            OutputStream os = streamConnection.openOutputStream();
            InputStream is = streamConnection.openInputStream();
            os.write(data); //'1' means ON and '0' means OFF
            os.close();
            byte[] b = new byte[200];
            Thread.sleep(200);
            is.read(b);
            is.close();
            streamConnection.close();
            System.out.println("received " + new String(b));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
