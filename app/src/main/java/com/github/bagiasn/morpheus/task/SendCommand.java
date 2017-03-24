package com.github.bagiasn.morpheus.task;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.github.bagiasn.morpheus.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * This task sends the UDP message.
 * First, it moves the button's state to progress mode.
 * Constructs the broadcast IP.
 * Broadcasts the actual message.
 * Moves the button's state to the appropriate mode
 */

public class SendCommand extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "SendCommand";
    private CircularProgressButton progressButton;
    private Context context;
    private String commandText;
    private String errorText;

    public SendCommand(Context context, CircularProgressButton progressButton) {
        this.progressButton = progressButton;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressButton != null) {
            commandText = (String) progressButton.getText();
            progressButton.setIndeterminateProgressMode(true);
            progressButton.setProgress(1);
        } else {
            cancel(true);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (isCancelled()) return false;
        try {
            InetAddress broadcastAddress = getBroadcastAddress();
            DatagramSocket datagramSocket = new DatagramSocket();
            byte[] message = commandText.getBytes();
            int messageLength = commandText.length();
            DatagramPacket packet = new DatagramPacket(message, messageLength, broadcastAddress, 12765);
            datagramSocket.send(packet);
            return true;
        } catch (SocketException s) {
            Log.d("SendCommand", s.getMessage());
            errorText = context.getString(R.string.error_socket);
        } catch (IOException io) {
            Log.d("SendCommand", io.getMessage());
            errorText = context.getString(R.string.error_io);
        } catch (SecurityException sec) {
            Log.d("SendCommand", sec.getMessage());
            errorText = context.getString(R.string.error_security);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result) {
            progressButton.setIndeterminateProgressMode(false);
            progressButton.setProgress(100);
        } else {
            progressButton.setIndeterminateProgressMode(false);
            progressButton.setProgress(-1);
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    private InetAddress getBroadcastAddress() {
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcp = wifi.getDhcpInfo();
            if (dhcp != null) {
                int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
                byte[] quads = new byte[4];
                for (int k = 0; k < 4; k++) {
                    quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
                }
                return InetAddress.getByAddress(quads);
            } else {
                Log.d(TAG, "getDhcpInfo returned null");
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return  null;
    }
}
