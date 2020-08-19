package gc.dtu.weeg.cwdtu.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import gc.dtu.weeg.cwdtu.MainActivity;
import gc.dtu.weeg.cwdtu.R;
import gc.dtu.weeg.cwdtu.fregment.PluseInputSettingFragment;
import gc.dtu.weeg.cwdtu.myview.CustomDialog;

public class PulseInfoWriteActivity extends Activity implements View.OnClickListener {

    Spinner spinner;
    EditText mdataperpulseView;
    EditText mPluseInitdataView;
    Button mWriteBut;
    ImageView mbake;
    MainActivity mainActivity;
    public CustomDialog mDialog;
    Intent intent;
    int mCurSelectedValue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluse_info_write_seting_activity_layout);
//        activity=this;
//        mtltie=findViewById(R.id.txt_titles_insitem);
//        mbutback=findViewById(R.id.imgBack_insitem);
//        mButwrite=findViewById(R.id.ins_fragment_but);
//        intent=getIntent();
//        mainActivity= MainActivity.getInstance();
        initview();
        initdata();
        initlisterner();
    }



    private void initview() {

        spinner = findViewById(R.id.plus_activity_plus_type_select);
        mdataperpulseView =findViewById(R.id.plus_activity_perinput_select);
        mPluseInitdataView =findViewById(R.id.plus_activity_initdata_select);
        mWriteBut =findViewById(R.id.pluse_info_weite_but1);
        mbake = findViewById(R.id.pluse_write_imgBackItemset);
        mDialog = CustomDialog.createProgressDialog(this, Constants.TimeOutSecond, new CustomDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(CustomDialog dialog) {
                dialog.dismiss();
                ToastUtils.showToast(getBaseContext(), "超时啦!");
            }
        });

    }

    private void initdata() {
        int index=0;
        String[] temp1;
        ArrayList<String> data_list =new ArrayList<>();
        for(String[] temp:PluseInputSettingFragment.PulseDataFormate)
        {
            data_list.add(temp[1]);
        }
        //适配器
        ArrayAdapter<String> arr_adapter;
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        mainActivity = MainActivity.getInstance();

        Intent intent = getIntent();
        PulseInfoWriteActivity.this.setResult(0,intent);

        intent = getIntent();
        String[] initdata =  intent.getStringArrayExtra("PLUSEDATA");
        spinner.setSelection(index,true);
        if(initdata!=null)
        {
            for(index = 0;index <PluseInputSettingFragment.PulseDataFormate.length;index++ )
            {
                temp1 = PluseInputSettingFragment.PulseDataFormate[index];
                if(temp1[1].equals(initdata[0]))
                {
                    spinner.setSelection(index,true);
                    break;
                }
            }
            mdataperpulseView.setText(""+initdata[1]);
            mPluseInitdataView.setText(""+initdata[2]);
        }
    }
    private void initlisterner() {
        mWriteBut.setOnClickListener(this);
        mbake.setOnClickListener(this);
        mainActivity.setOndataparse(new OncomDataParse());
        spinner.setOnItemSelectedListener(new sipneritemselectedlistenerimpl());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivity.setOndataparse(null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = getIntent();
        short perpluse = 0;
        double initdata = 0;
        byte [] headbuf={(byte)0xFD ,0x00 ,0x00 ,0x0F ,0x00 ,0x15 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00
                ,0x00 ,0x00 ,0x00 ,(byte)0x00 ,0x00 };
        switch (id)
        {
            case R.id.pluse_info_weite_but1:
                int position = (short) spinner.getSelectedItemPosition();

                short typevalue =  Short.parseShort(PluseInputSettingFragment.PulseDataFormate[position][0]);
//                Log.d("zl","PulseInfoWriteActivity spinner.getSelectedItemPosition()="+position);
//                PulseInfoWriteActivity.this.setResult(1,intent);
//                PulseInfoWriteActivity.this.finish();
//                mCurSelectedValue = typevalue;
                if(mCurSelectedValue==250 ||mCurSelectedValue==251)
                {

                }
                else
                {
                    if(mdataperpulseView.getText().length()==0)
                    {
                        ToastUtils.showToast(this,"脉冲当量必填");
                        return;
                    }
                    perpluse =  Short.parseShort(mdataperpulseView.getText().toString());
                    if(perpluse==0||perpluse>1000)
                    {
                        ToastUtils.showToast(this,"脉冲当量值必须大于0且小于1000");
                        return;
                    }
                    initdata = Double.parseDouble(mPluseInitdataView.getText().toString());
                }
                byte[] sendbuf =new byte[26];
                ByteBuffer buf1;
                buf1=ByteBuffer.allocateDirect(headbuf.length);
                buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
                buf1.put(headbuf);
                buf1.rewind();
                buf1.get(sendbuf,0,headbuf.length);

                //填写数据长度
                buf1=ByteBuffer.allocateDirect(2);
                buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
                buf1.putShort((short) 21);
                buf1.rewind();
                buf1.get(sendbuf,3,2);

                //数据地址
                buf1=ByteBuffer.allocateDirect(2);
                buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
                buf1.putShort((short) 222);
                buf1.rewind();
                buf1.get(sendbuf,headbuf.length-2,2);
                if(mCurSelectedValue==250 ||mCurSelectedValue==251)
                {
                    sendbuf[headbuf.length] =(byte) (mCurSelectedValue%0xff);
                }
                else
                {
                    //脉冲类型及传感器类型，传感器类型默认为 0
                    buf1=ByteBuffer.allocateDirect(2);
                    buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
                    buf1.putShort(typevalue);
                    buf1.rewind();
                    buf1.get(sendbuf,headbuf.length,2);

                    //脉冲当量
                    buf1=ByteBuffer.allocateDirect(2);
                    buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
                    buf1.putShort(perpluse);
                    buf1.rewind();
                    buf1.get(sendbuf,headbuf.length+2,2);

                    //初始读数
                    int inputdata =0;
                    initdata = initdata*1000/perpluse;
                    inputdata = (int) initdata;

                    buf1=ByteBuffer.allocateDirect(4);
                    buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
                    buf1.putInt(inputdata);
                    buf1.rewind();
                    buf1.get(sendbuf,headbuf.length+4,4);
                }
                CodeFormat.crcencode(sendbuf);
                Log.d("zl","PulseInfoWriteActivity "+ CodeFormat.byteToHex(sendbuf,sendbuf.length).toUpperCase()) ;
                String readOutMsg = DigitalTrans.byte2hex(sendbuf);
                verycutstatus(readOutMsg);
                break;
            case R.id.pluse_write_imgBackItemset:
                if(mDialog.isShowing())
                {
                    mDialog.dismiss();
                }
                PulseInfoWriteActivity.this.setResult(0,intent);
                PulseInfoWriteActivity.this.finish();
                break;
        }
    }
    private void verycutstatus(String readOutMsg) {
        MainActivity parentActivity1 = PulseInfoWriteActivity.this.mainActivity;
        String strState1 = parentActivity1.GetStateConnect();
        if(!strState1.equalsIgnoreCase("无连接"))
        {
            PulseInfoWriteActivity.this.mDialog.show();
            PulseInfoWriteActivity.this.mDialog.setDlgMsg("读取中...");
            //String input1 = Constants.Cmd_Read_Alarm_Pressure;
            parentActivity1.sendData(readOutMsg, "FFFF");
        }
        else
        {
            ToastUtils.showToast(PulseInfoWriteActivity.this, "请先建立蓝牙连接!");
        }
    }
    public class OncomDataParse implements MainActivity.Ondataparse{

        @Override
        public void datacometoparse(String readOutMsg1, byte[] readOutBuf1) {
                Log.d("zl",readOutMsg1);
                intent = getIntent();
                String[] strinfo=new String[3];
                int position = spinner.getSelectedItemPosition();
                strinfo[0] = PluseInputSettingFragment.PulseDataFormate[position][1];
                strinfo[1] = mdataperpulseView.getText().toString();
                strinfo[2]=mPluseInitdataView.getText().toString();
//                intent.putExtra("PLUSEDATA1",strinfo);
                PluseInputSettingFragment.SetResult(strinfo);
                if(mCurSelectedValue == 251 ||mCurSelectedValue ==250)
                {
                    PulseInfoWriteActivity.this.setResult(0,intent);
                }
                else
                {
                    PulseInfoWriteActivity.this.setResult(1,intent);
                }
                PulseInfoWriteActivity.this.finish();
        }
    }
    private class sipneritemselectedlistenerimpl implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d("zl", "OnItemSelectedListener :l= " + i);
            mCurSelectedValue = Integer.parseInt(PluseInputSettingFragment.PulseDataFormate[i][0]);
            if(mCurSelectedValue ==250 || mCurSelectedValue==251)
            {
//                mdataperpulseView.setCursorVisible(false);
//                mdataperpulseView.setFocusable(false);
//                mdataperpulseView.setTextIsSelectable(false);
                mdataperpulseView.setEnabled(false);

//                mPluseInitdataView.setCursorVisible(false);
//                mPluseInitdataView.setFocusable(false);
//                mPluseInitdataView.setTextIsSelectable(false);
                mPluseInitdataView.setEnabled(false);
            }
            else
            {
//                mdataperpulseView.setCursorVisible(true);
//                mdataperpulseView.setFocusable(true);
//                mdataperpulseView.setTextIsSelectable(true);
                mdataperpulseView.setEnabled(true);

//                mPluseInitdataView.setCursorVisible(true);
//                mPluseInitdataView.setFocusable(true);
//                mPluseInitdataView.setTextIsSelectable(true);
                mPluseInitdataView.setEnabled(true);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
