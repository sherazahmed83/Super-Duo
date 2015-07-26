package it.jaschke.alexandria.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import it.jaschke.alexandria.AddBook;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/*
Source
https://github.com/dm77/barcodescanner/blob/a372ac9dac51cf5863bc28df9d01de97430292ab/zbar/sample/src/main/java/me/dm7/barcodescanner/zbar/sample/ScannerActivity.java
 */

public class BarCodeScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setAutoFocus(false);
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        if (rawResult.getText() != null) {
            Intent data = new Intent();
            data.putExtra(AddBook.SCANNED_CODE, rawResult.getText());
            setResult(RESULT_OK, data);
            finish();
        }
    }
}