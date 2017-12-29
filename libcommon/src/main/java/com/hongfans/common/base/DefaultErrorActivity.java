package com.hongfans.common.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultErrorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String message = intent.getStringExtra(CrashHandler.EXTRA_STACK_TRACE);
        AlertDialog dialog = new AlertDialog.Builder(DefaultErrorActivity.this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DefaultErrorActivity.this, "copied", Toast.LENGTH_SHORT).show();
                        copyErrorToClipboard(message);
                        DefaultErrorActivity.this.finish();
                    }
                })
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DefaultErrorActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    }

    private void copyErrorToClipboard(String info) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Error information", info);
        clipboard.setPrimaryClip(clip);
    }
}
