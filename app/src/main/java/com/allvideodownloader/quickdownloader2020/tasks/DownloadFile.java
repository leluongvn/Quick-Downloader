package com.allvideodownloader.quickdownloader2020.tasks;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.util.Utils;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.allvideodownloader.quickdownloader2020.util.Constants.MY_ANDROID_10_IDENTIFIER_OF_FILE;

public class DownloadFile {
    public static DownloadManager downloadManager;
    public static long downloadID;


    public static void downloading(final Context context, String url, String title, String ext) {
        String cutTitle = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cutTitle = MY_ANDROID_10_IDENTIFIER_OF_FILE + title;
        } else {
            if (!title.equals("") && !title.equals("null")) {
                cutTitle = MY_ANDROID_10_IDENTIFIER_OF_FILE + title;
            } else {
                cutTitle = MY_ANDROID_10_IDENTIFIER_OF_FILE + title;
            }
        }

        if (ext.equals(".mp3")) {
            cutTitle = MY_ANDROID_10_IDENTIFIER_OF_FILE + title;
        }

        if (ext.equals(".png")) {
            cutTitle = MY_ANDROID_10_IDENTIFIER_OF_FILE + title;
        }


        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        cutTitle = cutTitle.replaceAll(characterFilter, "");
        cutTitle = cutTitle.replaceAll("['+.^:,#\"]", "");
        cutTitle = cutTitle.replace(" ", "-").replace("!", "").replace(":", "") + ext;
        if (cutTitle.length() > 100) {
            cutTitle = cutTitle.substring(0, 100) + ext;
        }
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(context.getString(R.string.downloading_des));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String folderName = Utils.DOWNLOAD_DIRECTORY;
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdir();
        }


        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, folderName + title);  // Storage directory path

        request.allowScanningByMediaScanner();
        downloadID = downloadManager.enqueue(request);

        Utils.showToast(context, context.getResources().getString(R.string.don_start));


    }


}