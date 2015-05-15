package com.epicelements.spotnsave;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.epicelements.spotnsave.BTClass;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class ikonnectHandler extends BroadcastReceiver {

   BluetoothAdapter a;


   // $FF: synthetic method
   static void a(ikonnectHandler var0, String var1) {
      var0.a(var1);
   }

   private void a(String var1) {
      Log.i("L8_SOS", var1);
   }

   public void onReceive(Context var1, Intent var2) {
      this.a = BluetoothAdapter.getDefaultAdapter();
      switch(var2.getIntExtra("android.bluetooth.profile.extra.STATE", -1)) {
      case 2:
         Set var3 = this.a.getBondedDevices();
         if(var3.size() > 0) {
            Iterator var4 = var3.iterator();

            BluetoothDevice var5;
            do {
               if(!var4.hasNext()) {
                  var5 = null;
                  break;
               }

               var5 = (BluetoothDevice)var4.next();
            } while(!var5.getName().equalsIgnoreCase("iKonnect"));

            this.a.cancelDiscovery();
            
            if(var5 != null) {
               UUID var7 = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

               try {
                  BluetoothSocket var10 = var5.createRfcommSocketToServiceRecord(var7);
                  this.a("Trying to connect");
                  var10.connect();
                  this.a("Connect successful");
                  Toast.makeText(var1, "Connected with iKonnect watch", 1).show();
                  InputStream var11 = var10.getInputStream();
                  this.a("Got Input Stream");
                  (new Thread(new BTClass(this, var11, new Handler(), var1))).start();
               } catch (IOException var12) {
                  this.a("IO EX");
                  var12.printStackTrace();
               } catch (Exception var13) {
                  this.a("GENERAL EX");
                  var13.printStackTrace();
               }
            }
         }
      case -1:
      case 0:
      case 1:
      default:
      }
   }
}
