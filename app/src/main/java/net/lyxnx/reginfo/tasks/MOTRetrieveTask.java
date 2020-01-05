//package net.lyxnx.reginfo.tasks;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//import net.lyxnx.reginfo.R;
//import net.lyxnx.reginfo.activity.MOTInfoActivity;
//import net.lyxnx.reginfo.reg.RegFetcher;
//import net.lyxnx.reginfo.reg.mot.MOTInfo;
//
//public class MOTRetrieveTask extends RetrieveTask<MOTInfo> {
//
//    public MOTRetrieveTask(String reg, Activity activity, FrameLayout progressContainer) {
//        super(reg, activity, progressContainer);
//    }
//
//    @Override
//    MOTInfo fetch() {
//        return RegFetcher.getMOTInfo(reg);
//    }
//
//    @Override
//    void onSuccess(MOTInfo info) {
//        if (info == null) {
//            Toast.makeText(activity.get(), R.string.error, Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if (info.getResults().isEmpty()) {
//            Toast.makeText(activity.get(), R.string.no_mot_yet, Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        Intent i = new Intent(activity.get(), MOTInfoActivity.class);
//        i.putExtra("info", info);
//        activity.get().startActivity(i);
//    }
//}