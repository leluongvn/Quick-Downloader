package com.allvideodownloader.quickdownloader2020.tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.api.APIServices;
import com.allvideodownloader.quickdownloader2020.api.RestClient;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.google.android.exoplayer2.upstream.DataSchemeDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class DownloadVideoFile {


    public static Context Mcontext;
    public static ProgressDialog pd;
    public static Dialog dialog;
    public static SharedPreferences prefs;
    public static Boolean fromService;
    public static String VideoUrl;

    public static void Start(final Context context, String url, Boolean service) {

        Mcontext = context;
        fromService = service;

        if (!fromService) {
            pd = new ProgressDialog(context);
            pd.setMessage(Mcontext.getResources().getString(R.string.getting_download_link));
            pd.setCancelable(false);
            pd.show();
        }
        if (url.contains("tiktok.com")) {


            new AlertDialog.Builder(context)
                    .setTitle("Tiktok is not available")
                    .setMessage("Tiktok download is not available right now.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else if (url.contains("mxtakatak")){
/*
            AndroidNetworking.post("https://takatak.katarmal.in/v1/processUrl")
                    .addBodyParameter("takatakurl", url)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!fromService) {
                                pd.dismiss();
                            }
                            String matag;
                            try {

                                JSONObject jsonObject = new JSONObject(response.toString());

                                matag = jsonObject.getString("videourl");
                                System.out.println("wojfdjhfdjh " + matag);
                                new downloadFile().Downloading(context, matag, "Mxtakatak_" + System.currentTimeMillis(), ".mp4");


                            } catch (Exception e) {
                                matag = "";

                                e.printStackTrace();
                                if (!fromService) {
                                    pd.dismiss();
                                }
                            }


                        }

                        @Override*//**//*
                        public void onError(ANError error) {
                            if (!fromService) {
                                pd.dismiss();
                            }
                        }
                    });*/
        }

        else if (url.contains("likee")) {

            new CallLikeeData().execute(url);

        } else if (url.contains("sharechat.com")) {

            new callGetShareChatData().execute(url);

        } else if (url.contains("roposo.com")) {

            new callGetRoposoData().execute(url);


        } else if (url.contains("snackvideo.com") || url.contains("sck.io")) {

            if (url.contains("snackvideo.com") || url.contains("sck.io")) {
                new callGetSnackAppData().execute(url);
            } else if (url.contains("sck.io")) {
                getSnackVideoData(url, Mcontext);

            }

        } else if (url.contains("josh")) {

            new CallJoshData().execute(url);
        } else if (url.contains("triller")) {

            new CallTrillerData().execute(url);
        } else if (url.contains("trell.co")) {

            new CalltrellData().execute(url);
        } else if (url.contains("chingari.io")) {

            new CallchingariData().execute(url);
        }

        prefs = Mcontext.getSharedPreferences("AppConfig", MODE_PRIVATE);
    }

    public static void callSnackVideoResult(String URL, String shortKey, String os, String sig, String client_key) {


        APIServices apiService = RestClient.getClient().create(APIServices.class);


        Call<JsonObject> callResult = apiService.getSnackVideoData(URL + "&" + shortKey + "&" + os + "&sig=" + sig + "&" + client_key);


        callResult.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {


                VideoUrl = response.body().getAsJsonObject("photo").get("main_mv_urls").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();


                System.out.println("video_url  " + VideoUrl);

                if (!VideoUrl.equals("")) {


                    try {

                        if (!fromService) {

                            pd.dismiss();
                        }


                        String myurldocument = VideoUrl;


                        String nametitle = "snackvideo_" +
                                System.currentTimeMillis();

                        new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        VideoUrl = "";

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("response1122334455:   " + "Failed0 " + call);

                if (!fromService) {

                    pd.dismiss();
                }

            }
        });

    }

    public static void getSnackVideoData(String str, Context vc) {
        URI uri;
        try {
            uri = new URI(str);
        } catch (Exception e) {
            e.printStackTrace();
            uri = null;
            if (!fromService) {

                pd.dismiss();
            }
        }
        assert uri != null;
        String[] uripath = uri.getPath().split("/");
        String uripath2 = uripath[uripath.length - 1];
        ArrayList<String> arrayList = new ArrayList();
        arrayList.add("mod=OnePlus(ONEPLUS A5000)");
        arrayList.add("lon=0");
        arrayList.add("country_code=in");
        String mydid = "did=" +
                "ANDROID_" + Settings.Secure.getString(vc.getContentResolver(), "android_id");

        arrayList.add(mydid);
        arrayList.add("app=1");
        arrayList.add("oc=UNKNOWN");
        arrayList.add("egid=");
        arrayList.add("ud=0");
        arrayList.add("c=GOOGLE_PLAY");
        arrayList.add("sys=KWAI_BULLDOG_ANDROID_9");
        arrayList.add("appver=2.7.1.153");
        arrayList.add("mcc=0");
        arrayList.add("language=en-in");
        arrayList.add("lat=0");
        arrayList.add("ver=2.7");


        ArrayList arrayList2 = new ArrayList(arrayList);

        String shortKey = "shortKey=" +
                uripath2;
        arrayList2.add(shortKey);

        String os = "os=" +
                "android";
        arrayList2.add(os);
        String client_key = "client_key=" +
                "8c46a905";
        arrayList2.add(client_key);

        try {
            Collections.sort(arrayList2);

        } catch (Exception str225) {
            str225.printStackTrace();
            if (!fromService) {

                pd.dismiss();
            }
        }


        String clockData = CPU.getClockData(Mcontext, TextUtils.join("", arrayList2).getBytes(StandardCharsets.UTF_8), 0);

        String nowaterurl = "https://g-api.snackvideo.com/rest/bulldog/share/get?" + TextUtils.join("&", arrayList);

        callSnackVideoResult(nowaterurl, shortKey, os, clockData, client_key);


    }


    public static class CallmxtaktakData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
                //   download_hlsganna("", ".mp4");

            } catch (Exception e) {

                System.out.println("jskdfhksdhfkshdfkhsdj " + e.getMessage());
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {

            String charSequence = "";

            try {


                String data = "";

                Iterator<Element> documentitrator = document.select("script").iterator();

                do {
                    if (!documentitrator.hasNext()) {

                        break;
                    }
                    data = documentitrator.next().data();
                    Log.e("onP4342424te:datais ", data);


                } while (!data.contains("window._state"));


                String stringbuil = data.substring(data.indexOf("{"), data.indexOf("};")) + "}";
                Log.e("onPostbjnkjh:oso_11 ", stringbuil);


                if (!document.equals("")) {
                    try {
                        JSONObject jSONObject = new JSONObject(stringbuil);
                        VideoUrl = jSONObject.getJSONObject("sharePhoto").getString("mp4Url");

                        Log.e("onPostExecute:roposo_ ", VideoUrl);

                        getSnackVideoData(jSONObject.getString("shortUrl"), Mcontext);
                        VideoUrl = charSequence;

                    } catch (Exception document2) {
                        document2.printStackTrace();

                        System.out.println("respossss112212121qerrr " + document2.getMessage());

                        if (!fromService) {

                            pd.dismiss();
                        }
                    }
                }


            } catch (Exception document22) {
                if (!fromService) {

                    pd.dismiss();
                }
                document22.printStackTrace();

                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));


            }


        }
    }
    private static class callGetShareChatData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;

        callGetShareChatData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                if (!fromService) {

                    pd.dismiss();
                }
                VideoUrl = document.select("meta[property=\"og:video:secure_url\"]").last().attr("content");



                if (!VideoUrl.equals(charSequence)) {


                    try {
                        String myurldocument = VideoUrl;


                        String nametitle = "sharechat_" +
                                System.currentTimeMillis() +
                                ".mp4";

                        new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = charSequence;
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }
            } catch (Exception document22) {
                document22.printStackTrace();
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }

    private static class callGetRoposoData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;

        callGetRoposoData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {

                if (!fromService) {

                    pd.dismiss();
                }
                VideoUrl = document.select("meta[property=\"og:video\"]").last().attr("content");
                if (VideoUrl==null || VideoUrl.equals("")){
                    VideoUrl = document.select("meta[property=\"og:video:url\"]").last().attr("content");
                }


                if (!VideoUrl.equals(charSequence)) {


                    try {
                        String myurldocument = VideoUrl;


                        String nametitle = "roposo_" +
                                System.currentTimeMillis() +
                                ".mp4";

                        new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = charSequence;
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        document2.printStackTrace();
                        Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }
                }
            } catch (Exception document22) {
                document22.printStackTrace();
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }

    public static class CallJoshData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    JSONObject jsonObject = new JSONObject(html);
                    VideoUrl = jsonObject.getJSONObject("props")
                            .getJSONObject("pageProps").getJSONObject("detail")
                            .getJSONObject("data").
                                    getString("mp4_url");

                    if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                        try {


                            String myurldocument = VideoUrl;


                            String nametitle = "joshvideo_" +
                                    System.currentTimeMillis();

                            new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }
                    return;
                }
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallTrillerData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                System.out.println("myresponseis111 0exp1 " + strArr[0]);

                this.RoposoDoc = Jsoup.connect(strArr[0].replace("-", "")).get();
                System.out.println("myresponseis111 1exp1 " + strArr[0].replace("-", ""));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {
            System.out.println("myresponseis111 2exp1 " + document.body());


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = document.select(MimeTypes.BASE_TYPE_VIDEO).last().attr("src");
                if (this.VideoUrl.startsWith("//")) {
                    this.VideoUrl = "https:" + this.VideoUrl;
                }

                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "trillervideo_" +
                                System.currentTimeMillis();

                        new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallLikeeData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                String str = strArr[0];
                if (str.contains("com")) {
                    str = str.replace("com", MimeTypes.BASE_TYPE_VIDEO);
                }
                this.RoposoDoc = Jsoup.connect("https://likeedownloader.com/results").data("id", str).userAgent("Mozilla").post();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = document.select("a.without_watermark").last().attr("href");

                if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                    try {


                        String myurldocument = VideoUrl;


                        String nametitle = "Likeevideo_" +
                                System.currentTimeMillis();

                        new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                        //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                        VideoUrl = "";
                        //   binding.etText.setText(charSequence);

                    } catch (Exception document2) {
                        System.out.println("myresponseis111 exp1 " + document2.getMessage());

                        document2.printStackTrace();
                        Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                    }

                    return;
                }

                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CalltrellData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                String html = document.select("script[id=\"__NEXT_DATA__\"]").last().html();
                if (!html.equals("")) {
                    this.VideoUrl = String.valueOf(new JSONObject(new JSONObject(html)
                            .getJSONObject("props")
                            .getJSONObject("pageProps")
                            .getJSONObject("result")
                            .getJSONObject("result").getJSONObject("trail")
                            .getJSONArray("posts").get(0).toString())
                            .get(MimeTypes.BASE_TYPE_VIDEO));

                    System.out.println("myresponseis111 exp991 " + VideoUrl);


                    if (!this.VideoUrl.equals("") && this.VideoUrl != null) {
                        try {


                            String myurldocument = VideoUrl;


                            String nametitle = "trellvideo_" +
                                    System.currentTimeMillis();

                            new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            //  Utils.startDownload(document, str, shareChatActivity, stringBuilder);
                            VideoUrl = "";
                            //   binding.etText.setText(charSequence);

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }
                    return;
                }

                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    public static class CallchingariData extends AsyncTask<String, Void, Document> {
        Document RoposoDoc;
        String VideoUrl = "";

        public Document doInBackground(String... strArr) {
            try {
                this.RoposoDoc = Jsoup.connect(strArr[0]).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.RoposoDoc;
        }

        public void onPostExecute(Document document) {


            try {

                if (!fromService) {

                    pd.dismiss();
                }

                this.VideoUrl = document.select("meta[property=\"og:video:secure_url\"]").last().attr("content");
                if (this.VideoUrl != null)
                    if (!this.VideoUrl.equals("")) {
                        try {

                            String myurldocument = VideoUrl;

                            String nametitle = "chingarivideo_" +
                                    System.currentTimeMillis();

                            new DownloadFile().downloading(Mcontext, myurldocument, nametitle, ".mp4");

                            VideoUrl = "";

                        } catch (Exception document2) {
                            System.out.println("myresponseis111 exp1 " + document2.getMessage());

                            document2.printStackTrace();
                            Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
                        }

                        return;
                    }

                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            } catch (Exception unused) {
                System.out.println("myresponseis111 exp " + unused.getMessage());


                if (!fromService) {

                    pd.dismiss();
                }
                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }


    }

    private static class callGetSnackAppData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;
        private Iterator<Element> abk;

        callGetSnackAppData() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Document doInBackground(String... strArr) {
            try {
                this.ShareChatDoc = Jsoup.connect(strArr[0]).get();
            } catch (Exception strArr2) {
                strArr2.printStackTrace();
                Log.d("ContentValues roposo_", "doInBackground: Error");
            }
            return this.ShareChatDoc;
        }

        protected void onPostExecute(Document document) {
            String charSequence = "";

            try {


                String data = "";

                Iterator<Element> documentitrator = document.select("script").iterator();

                do {
                    if (!documentitrator.hasNext()) {

                        break;
                    }
                    data = ((Element) documentitrator.next()).data();
                    Log.e("onP4342424te:datais ", data);


                } while (!data.contains("window.__INITIAL_STATE__"));


                String stringbuil = data.substring(data.indexOf("{"), data.indexOf("};")) + "}";
                Log.e("onPostbjnkjh:oso_11 ", stringbuil);


                if (!document.equals("")) {
                    try {
                        JSONObject jSONObject = new JSONObject(stringbuil);
                        VideoUrl = jSONObject.getJSONObject("sharePhoto").getString("mp4Url");

                        Log.e("onPostExecute:roposo_ ", VideoUrl);

                        getSnackVideoData(jSONObject.getString("shortUrl"), Mcontext);
                        VideoUrl = charSequence;

                    } catch (Exception document2) {
                        document2.printStackTrace();

                        System.out.println("response_download_video" + document2.getMessage());

                        if (!fromService) {

                            pd.dismiss();
                        }
                    }
                }


            } catch (Exception document22) {
                if (!fromService) {

                    pd.dismiss();
                }
                document22.printStackTrace();
                System.out.println("response_download_video" + document22.getMessage());

                Utils.showToast(Mcontext, Mcontext.getResources().getString(R.string.somthing));
            }
        }
    }


}
