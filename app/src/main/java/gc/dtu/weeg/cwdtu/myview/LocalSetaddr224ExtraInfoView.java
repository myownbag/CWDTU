package gc.dtu.weeg.cwdtu.myview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import gc.dtu.weeg.cwdtu.R;
import gc.dtu.weeg.cwdtu.myview.slidingbutton.BaseSlidingToggleButton;
import gc.dtu.weeg.cwdtu.myview.slidingbutton.SlidingToggleButton;

public class LocalSetaddr224ExtraInfoView extends LinearLayout{
    String mCursetstr;
    Context mActivity;
    SlidingToggleButton button;
    int[] ids =new int[4];
    View myview;
    SETTINGCHANGED settingchanged;
    public LocalSetaddr224ExtraInfoView(Context context,String setingstr) {
        super(context);

        mActivity = context;
        mCursetstr=setingstr;
        myview = View.inflate(mActivity, R.layout.localsetting_addr224_layout,null);
        addView(myview);
        ids[0] = R.id.local_extra_224_first;
        ids[1] = R.id.local_extra_224_second;
        ids[2] = R.id.local_extra_224_third;
        ids[3] = R.id.local_extra_224_fourth;
        initview();
    }

    private void initview() {
        int index = 0;
        int index1 = 0;
        byte[] b = new byte[4];
        String temp,temp1;


        if(mCursetstr!=null)
        {
            temp1 = mCursetstr;
            temp1+="-";
            Log.d("zl",temp1);
            while(index!=-1)
            {
                index = temp1.indexOf("-");
                if(index == -1)
                {
                    break;
                }
                temp = temp1.substring(0,index);
                if(temp.equals("开"))
                {
                    b[index1]=1;
                }
                else
                {
                    b[index1]=0;
                }
                temp1 = temp1.substring(index+1);
                index1++;
            }
            for(int i=0;i<4;i++)
            {
                button = myview.findViewById(ids[i]);
//                button.setOnCheckedChanageListener(new Onbuttonclickedimp());
                if(b[i] == 1)
                {
                    button.setChecked(true);
                }
                else
                {
                    button.setChecked(false);
                }
            }
            for(int i=0;i<4;i++)
            {
                button = myview.findViewById(ids[i]);
                button.setOnCheckedChanageListener(new Onbuttonclickedimp());
            }
        }

    }

    public byte[] getcursettings()
    {
        byte[] seting = new byte[4];

        for(int i=0;i<4;i++)
        {
            button = myview.findViewById(ids[i]);
            if(button.isChecked())
            {
                seting[i] = 1;
            }
            else
            {
                seting[i] = 0;
            }
        }


        return seting;
    }
    private class Onbuttonclickedimp implements  SlidingToggleButton.OnCheckedChanageListener {

        @Override
        public void onCheckedChanage(BaseSlidingToggleButton slidingToggleButton, boolean isChecked) {
            String s="";
                for(int i=0;i<4;i++)
                {
                    BaseSlidingToggleButton buttont = myview.findViewById(ids[i]);
                    if(buttont.isChecked())
                    {
                        s+="开";
                    }
                    else
                    {
                        s+="关";
                    }
                    if(i<3)
                    {
                        s+="-";
                    }
                }
                if(settingchanged!=null)
                {
                    settingchanged.settingchanged(s);
                }
        }
    }
    public void SETCURLISTERNER(SETTINGCHANGED i){
        settingchanged = i;
    }
    public interface  SETTINGCHANGED {
        void settingchanged(String s);
    }
}
