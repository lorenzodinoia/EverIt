package it.uniba.di.sms1920.everit.customer.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

public class NfcManager implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private NfcActivity activity;

    public NfcManager(NfcActivity activity) {
        this.activity = activity;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String outString = this.activity.getPayload();
        byte[] outBytes = outString.getBytes();
        NdefRecord outRecord = NdefRecord.createMime(MIME_TEXT_PLAIN, outBytes);

        return new NdefMessage(outRecord);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        this.activity.signalResult();
    }

    /**
     * Callback to be implemented by a sender activity
     */
    public interface NfcActivity {
        String getPayload();
        void signalResult();
    }
}
