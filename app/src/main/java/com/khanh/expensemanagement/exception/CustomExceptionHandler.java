package com.khanh.expensemanagement.exception;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.khanh.expensemanagement.ApplicationProperties;
import com.khanh.expensemanagement.ErrorActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler defaultHandler;
    private final Context context;

    public CustomExceptionHandler(Context context) {
        this.context = context;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        writeLogToFile(e);

        // Get in ErrorActivity
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Kết thúc ứng dụng
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(2);
    }

    private void writeLogToFile(Throwable e) {

        OutputStream outputStream = null;

        try {
            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use media store API for writing log on Android 10+
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, ApplicationProperties.LOG_FILE_NAME);
                values.put(MediaStore.Downloads.MIME_TYPE, "text/plain");
                values.put(MediaStore.Downloads.RELATIVE_PATH, ApplicationProperties.LOG_DIR);

                fileUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            } else {
                // Android < 10
                fileUri = Uri.fromFile(new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/ExpenseManagement/" + ApplicationProperties.LOG_FILE_NAME));
            }

            if (fileUri != null) {
                outputStream = context.getContentResolver().openOutputStream(fileUri, "wa");
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                String logText = "============ ERROR LOG ============\n" +
                        "Time: " + timeStamp + "\n" +
                        "Message: " + e.getMessage() + "\n" +
                        "StackTrace:\n";

                for (StackTraceElement element : e.getStackTrace()) {
                    logText += element.toString() + "\n";
                }

                logText += "\n";

                outputStream.write(logText.getBytes());
                Log.e("CustomExceptionHandler", "Lỗi đã được ghi vào file log.");
            }
        } catch (IOException ex) {
            Log.e("CustomExceptionHandler", "Không thể ghi log vào file!", ex);
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
