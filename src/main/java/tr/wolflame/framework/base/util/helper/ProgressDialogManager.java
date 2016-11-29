package tr.wolflame.framework.base.util.helper;


import android.content.Context;
import android.widget.Toast;

import tr.wolflame.framework.base.util.LogApp;


public class ProgressDialogManager {

    private static final String TAG = "ProgressDialogManager";

    private Context mContext;

    //public static ProgressDialog pd;
    public static TransparentProgressDialog pd;

    public ProgressDialogManager(Context context) {
        this.mContext = context;
    }

    private static boolean start = false, finish = false;

    private static long count = 0;

    public synchronized void add() {

        if (!start) {
            start = LoadProgressDialog(mContext);

        }

        if (start)
            count++;

        LogApp.d(TAG, "add and count: " + String.valueOf(count) + " start: " + String.valueOf(start));
    }


    public synchronized void add(String dialogText) {

        if (!start) {
            start = LoadProgressDialog(mContext);

        }

        if (dialogText != null && !dialogText.isEmpty()) {

           /* pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pd.setCancelable(false);

            pd.setContentView(R.layout.layout_custom_progressdialog);*/

            // TextView mProgressText_TV = (TextView) pd.findViewById(R.id.textView_dialogText);

            // mProgressText_TV.setText(String.valueOf(dialogText));
        }

        if (start)
            count++;

        LogApp.d("ProgressDialog", "add and count: " + String.valueOf(count) + " start: " + String.valueOf(start));

    }

    public synchronized void decrease() {

        count--;

        LogApp.d(TAG, "decrease and count: " + String.valueOf(count));

        isFinished();

    }


    public synchronized boolean isFinished() {

        if (count <= 0) {
            finish = true;
            start = false;
            count = 0;

            try {
                pd.cancel();

            } catch (Exception e) {

                LogApp.d("ProgressDialog", String.valueOf(e.toString()));
            }
        } else {
            finish = false;
        }

        LogApp.d(TAG,  "finish: " + String.valueOf(finish) + " count: " + String.valueOf(count));

        return finish;

    }

    private boolean LoadProgressDialog(Context context) {

        boolean resultValid = false;

        try {

            /*  pd = new ProgressDialog(context);
              pd.setTitle("Lütfen Bekleyiniz..");
              pd.setMessage("Yükleniyor..");
              pd.setCancelable(false);
              pd.show(); */


            /*pd = new ProgressDialog(context); //, R.style.MyAlertDialogStyle); //android.R.style.Theme_NoTitleBar_Fullscreen);

            pd.show();

            UtilityMethods.Log.d("ProgressDialog", "show");

            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            //pd.setProgressStyle(R.style.MyAlertDialogStyle);
            pd.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            pd.setCancelable(false);

            pd.setContentView(R.layout.layout_custom_progressdialog);*/

            /*pd = new ProgressDialog(context);
            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pd.setCancelable(false);
            pd.show();*/

            /*pd = new ProgressDialog(context, R.style.MyAlertDialogStyle); //android.R.style.Theme_NoTitleBar_Fullscreen);
            pd.show();

            UtilityMethods.Log.d("ProgressDialog", "show");

            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pd.setCancelable(false);

            RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            LayoutInflater inflater = (LayoutInflater)   mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.layout_custom_progressdialog, null);

            view.setBackgroundColor(Color.TRANSPARENT);

            pd.setContentView(view, mParams);*/


            //pd = new ProgressDialog(context , R.style.MyProgressDialogStyle); //, R.style.MyAlertDialogStyle); //android.R.style.Theme_NoTitleBar_Fullscreen);
            //pd = new ProgressDialog(context , android.R.style.Theme_NoTitleBar_Fullscreen);

            //pd.setProgressStyle(R.style.MyProgressDialogStyle);
            //pd.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


            pd = TransparentProgressDialog.show(mContext,"","");
            //pd.showCustomDrawable(R.drawable.progress_spinner);
            //pd.showCustomLayout(R.layout.layout_custom_progress_dialog);

            resultValid = true;

        } catch (Exception e) {

            LogApp.e(TAG,  String.valueOf(e.toString()));
        }


        return resultValid;

    }


	/*public void ChangeProgressDialogText(String title, String message) {

		//pd.cancel();
		//pd = new ProgressDialog(mContext);
		pd.setTitle(title);
	    pd.setMessage(message);
	   // pd.setCancelable(false);
	    //pd.show();
	}*/


    public void setProgressDialogText(String title, String message) {

        //pd.cancel();
        //pd = new ProgressDialog(mContext);
        pd.setTitle(title);
        //  pd.setMessage(message);
        // pd.setCancelable(false);
        //pd.show();
    }


    public void showResult(Boolean result) {
        if (result) {

            Toast.makeText(mContext, "oldu", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(mContext, "olmadı", Toast.LENGTH_SHORT).show();
        }
    }


}
