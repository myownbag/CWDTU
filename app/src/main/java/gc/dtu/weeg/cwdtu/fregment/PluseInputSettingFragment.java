package gc.dtu.weeg.cwdtu.fregment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import gc.dtu.weeg.cwdtu.MainActivity;
import gc.dtu.weeg.cwdtu.R;
import gc.dtu.weeg.cwdtu.utils.CodeFormat;
import gc.dtu.weeg.cwdtu.utils.Constants;
import gc.dtu.weeg.cwdtu.utils.DigitalTrans;
import gc.dtu.weeg.cwdtu.utils.InstrumemtItemseetingActivity;
import gc.dtu.weeg.cwdtu.utils.PulseInfoWriteActivity;
import gc.dtu.weeg.cwdtu.utils.ToastUtils;

/**
 *
 */
public  class PluseInputSettingFragment extends BaseFragment {
    View mView;
    TextView mPluseTypeView;
    TextView mdataperpulseView;
    TextView mPluseInitdataView;
    Button mReadBut;
    LinearLayout mContainerView;

    private static String[] mSettingResult;
   public static String[][] PulseDataFormate =
            {
                    {"0","无脉冲"},
                    {"1","单脉冲"},
                    {"2","双脉冲"},
                    {"3","双脉冲（对比）"},
                    {"4","双脉冲（正反）"},
                    {"100","三脉冲"},
                    {"250","保存脉冲"},
                    {"251","清除脉冲"},
            };

    @Override
    public void OndataCometoParse(String readOutMsg1, byte[] readOutBuf1) {
         MainActivity.getInstance().mDialog.dismiss();
         Log.d("zl",readOutMsg1);
        if(mIsatart==false)
        {
            return;
        }
        if(readOutBuf1.length<5)
        {
            ToastUtils.showToast(getActivity(), "数据长度短");

            return;
        }
        else
        {
            if(readOutBuf1[3]!=(readOutBuf1.length-5))
            {
                ToastUtils.showToast(getActivity(), "数据长度异常");

                return;
            }
        }

        byte[] plusetype ={0,0};
        plusetype[0] = readOutBuf1[16];
        ByteBuffer buf;
        buf=ByteBuffer.allocateDirect(2); //无额外内存的直接缓存
        buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
        buf.put(plusetype);
        buf.rewind();
        short typepluse = buf.getShort();
        if(typepluse == 255)
            typepluse = 0;
        for(String[] temp:PulseDataFormate)
        {
           if( Short.valueOf(temp[0]) == typepluse)
           {
               mPluseTypeView.setText(temp[1]);
           }
        }

        buf=ByteBuffer.allocateDirect(2); //无额外内存的直接缓存
        buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
        buf.put(readOutBuf1,18,2);
        buf.rewind();
        short dataperpulse = buf.getShort();
        mdataperpulseView.setText(""+dataperpulse);

        buf=ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
        buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
        buf.put(readOutBuf1,20,4);
        buf.rewind();
        int plusedata = buf.getInt();
        int showdataintpart = plusedata*dataperpulse/1000;
        int showdatafloatpart = plusedata*dataperpulse%1000;

        mPluseInitdataView.setText(""+showdataintpart+"."+showdatafloatpart);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.pluse_inpute_seting_fragment_layout,null,false);
        initView();
        initListener();
        return mView;
    }

    private void initView() {
        mPluseTypeView  = mView.findViewById(R.id.plus_fragment_plus_type_select);
        mdataperpulseView   = mView.findViewById(R.id.plus_fragment_perinput_select);
        mPluseInitdataView = mView.findViewById(R.id.plus_fragment_initdata_select);
        mReadBut = mView.findViewById(R.id.pluse_info_read_but1);
        mContainerView = mView.findViewById(R.id.pluse_content_container);
    }
    private void initListener() {
        mReadBut.setOnClickListener(new OnButtonClickiIIMPL());
        mContainerView.setOnClickListener(new OnButtonClickiIIMPL());
    }
    private class OnButtonClickiIIMPL implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            int id = view.getId();
            switch(id)
            {
                case R.id.pluse_info_read_but1:
                    mIsatart = true;
                    byte[] temp={(byte)0xFD,0x00 ,0x00 ,0x0D ,0x00 ,0x19 ,0x00 ,0x00 ,0x00 ,0x00
                            ,0x00 ,0x00 ,0x00 ,0x00 ,(byte)0xCE ,0x07 ,0x42 ,(byte)0x92};
                    ByteBuffer buf;
                    buf=ByteBuffer.allocateDirect(2); //无额外内存的直接缓存
                    buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
                    buf.putShort((short) 222);
                    buf.rewind();
                    buf.get(temp,14,2);
                    CodeFormat.crcencode(temp);
                    String readOutMsg = DigitalTrans.byte2hex(temp);
                    Log.d("zl",CodeFormat.byteToHex(temp,temp.length).toUpperCase());
                    verycutstatus(readOutMsg);
                    break;
                case R.id.pluse_content_container:
                    Intent intent;
                    intent=new Intent(MainActivity.getInstance(), PulseInfoWriteActivity.class);
                    String[] strinfo=new String[3];
                    strinfo[0] = mPluseTypeView.getText().toString();
                    strinfo[1] = mdataperpulseView.getText().toString();
                    strinfo[2] = mPluseInitdataView.getText().toString();
                    intent.putExtra("PLUSEDATA",strinfo);
                    startActivityForResult(intent, Constants.PLUSESETTINGFLAG);
                    break;
            }

        }
    }

    private void verycutstatus(String readOutMsg) {
        MainActivity parentActivity1 = (MainActivity) getActivity();
        String strState1 = parentActivity1.GetStateConnect();
        if(!strState1.equalsIgnoreCase("无连接"))
        {
            parentActivity1.mDialog.show();
            parentActivity1.mDialog.setDlgMsg("读取中...");
            //String input1 = Constants.Cmd_Read_Alarm_Pressure;
            parentActivity1.sendData(readOutMsg, "FFFF");
        }
        else
        {
            ToastUtils.showToast(getActivity(), "请先建立蓝牙连接!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("zl","PluseInputSettingFragment onActivityResult = "+resultCode);
        if(resultCode!=0)
        {
            Intent intent = MainActivity.getInstance().getIntent();
            String[] temp = mSettingResult;
            mSettingResult = null;
            if(temp!=null)
            {
                mPluseTypeView.setText(temp[0]);
                mdataperpulseView.setText(temp[1]);
                mPluseInitdataView.setText(temp[2]);
            }

        }
    }

    public static void  SetResult(String[] result)
    {
        mSettingResult = result;
    }
}
