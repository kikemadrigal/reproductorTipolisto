package es.tipolisto.reproductortipolisto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Casa on 28/05/2018.
 */

public class BlueToothBroadCastReceiver extends BroadcastReceiver {
    private static final String MESNAJE_BUETOOTHBROADCASTRECEIVER="MENSAJEBROADCASTR";
    private Context context;
    private Intent intent;
    public  BlueToothBroadCastReceiver(Context context){
        this.context=context;
        //this.intent=intent;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent=intent;

        String action = intent.getAction();
        // Cuando se descubra un nuevo dispositivo...
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // Show the name and address in Log
            Log.d(MESNAJE_BUETOOTHBROADCASTRECEIVER,device.getName() + "----->" + device.getAddress());
        }
    }

    public void registrarBlueToothBroadCastReceiver(){
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.context.registerReceiver(this, filter);
    }

    public Set<BluetoothDevice> mostrarDispositivosBlueThoodConectadosPorLog(){
        boolean permitir=false;
        BluetoothAdapter bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bluetoothDevices=bluetoothAdapter.getBondedDevices();
        return bluetoothDevices;
    }

    public void desregistrarBlueToothBroadCastReceiver(){
        this.context.unregisterReceiver(this);
    }


    /*private void comprobarEstadoBlueTooth(){

        if(bluetoothAdapter==null){
            Toast.makeText(this, "BlueTooth  no está dispnible", Toast.LENGTH_LONG).show();
            Log.d(MESNAJE_MAINACTIVITY, "EL bluetooth no está disponible");
        }
        if(!bluetoothAdapter.isEnabled()){
            Toast.makeText(this, "BlueTooth  no está habilitado", Toast.LENGTH_LONG).show();
            Log.d(MESNAJE_MAINACTIVITY, "EL bluetooth no está disponible");
            //Esto generará una solicitud para habilitar Bluetooth a través de los ajustes de sistema (sin detener tu aplicación).
            /*Intent accionHabilitarBluetoothIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(accionHabilitarBluetoothIntent);
        }
    }*/

}
