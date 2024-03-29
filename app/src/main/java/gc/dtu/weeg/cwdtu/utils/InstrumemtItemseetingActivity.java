package gc.dtu.weeg.cwdtu.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Map;

import gc.dtu.weeg.cwdtu.MainActivity;
import gc.dtu.weeg.cwdtu.R;
import gc.dtu.weeg.cwdtu.fregment.instrumentComSetFragment;
import gc.dtu.weeg.cwdtu.fregment.instrumentWorkModeSetFragment;
import gc.dtu.weeg.cwdtu.fregment.instrumentbaseFragment;
import gc.dtu.weeg.cwdtu.fregment.instrumenttimegapFragment;
import gc.dtu.weeg.cwdtu.myview.CustomDialog;

public class InstrumemtItemseetingActivity extends FragmentActivity implements View.OnClickListener {

    private TextView mtltie;
    private ImageView mbutback;
    private MainActivity mainActivity;
    private ArrayList<instrumentbaseFragment> fragments;
    private instrumentComSetFragment fragment1;
    private instrumenttimegapFragment fragment2;
    private instrumentWorkModeSetFragment fragment3;
    private instrumentWorkModeSetFragment fragment4;
    private Button mButwrite;
    public static Activity activity;
    public ArrayList<Map<String,String>> settings;
    public CustomDialog mDialog;
    public static String baseinfo[][]={
            {"1998","1","1","300"}, // reg,item,seletc,value
            {"1998","1","2","600"},
            {"1998","1","4","1200"},
            {"1998","1","8","2400"},
            {"1998","1","16","4800"},
            {"1998","1","32","9600"},
            {"1998","1","64","19200"},

            {"1998","2","0","无"},
            {"1998","2","1","偶"},
            {"1998","2","2","奇"},

            {"1998","3","0","5"},
            {"1998","3","4","6"},
            {"1998","3","8","7"},
            {"1998","3","12","8"},

            {"1998","4","16","0.5"},
            {"1998","4","0","1"},
            {"1998","4","48","1.5"},
            {"1998","4","32","2"},

            {"2000","1","关闭","0"},
            {"2000","1","打开","1"},


            {"2000","2","Empty","0"},
            {"2000","2","ActarisMeter","1"},
            {"2000","2","HytroMeter","2"},
            {"2000","2","SiemensMeter","3"},
            {"2000","2","Kamstrup","4"},
            {"2000","2","LUG_2wr6","5"},
            {"2000","2","WEEG_Gas","6"},
            {"2000","2","FC6000H","7"},
            {"2000","2","WEEG_Gas_ISM","8"},

            {"2000","2","MFGD_Modbus","1000"},
            {"2000","2","Trancy 1.2","1001"},
            {"2000","2","Trancy 1.3","1002"},
            {"2000","2","Trancy_Modbus","1003"},

            {"2000","2","C.N.","1004"},
            {"2000","2","C.N._Modbus","1005"},
            {"2000","2","PTZ_BOX with Kp","1006"},
            {"2000","2","PTZ_BOX without Kp","1007"},
            {"2000","2","PTZ_BOX V3","1008"},
            {"2000","2","PTZ_BOX V3-2","1009"},
            {"2000","2","Corus","1010"},
            {"2000","2","Corus 2003","1011"},
            {"2000","2","Corus_Modbus","1012"},
            {"2000","2","SEVC-D 3.0","1013"},
            {"2000","2","Elster","1014"},
            {"2000","2","Elster_Modbus","1015"},
            {"2000","2","MFFD_Modbus","1016"},
            {"2000","2","EVC300","1017"},
            {"2000","2","AS Ultrasonic","1018"},
            {"2000","2","PTZ_BOX CV","1019"},
            {"2000","2","AS Ultrasonic_80","1020"},
            {"2000","2","Trancy cpuCard","1021"},
            {"2000","2","PTZBox_IGSM_Modbus","1030"},
            {"2000","2","机床五厂","1031"},
          //  {"2000","2","科隆流量计","1032"},

              {"2000","2","天津新科通用","1032"},
              {"2000","2","天津新科MODBUS","1033"},
            {"2000","2","上海飞奥MODBUS","1034"},

            {"2000","2","德文V5","1035"},

            {"2000","2","Control Valve","10500"},
            {"2000","2","电压读取","10501"},
    };
    public static String factorysinfo[]={"Empty","天信仪表","苍南仪表","德闻仪表","埃创仪表","Elster仪表","爱知仪表","卓度仪表","阀门控制","供水仪表","机床五厂","天津新科","上海飞奥"};
    public static String Instrumentinfo[][]={
            {"0","0","Empty","0"}, //factorysinfo中的序号，   有无子菜单（0：无，1：有） ,子菜单选项  ,选项值
            {"1","1","Trancy 1.2","1001"},
            {"1","1","Trancy 1.3","1002"},
            {"1","1","Trancy_Modbus","1003"},
            {"1","1","Trancy cpuCard","1021"},

            {"2","1","通用","1004"},
            {"2","1","Modbus","1005"},
            {"2","1","EVC300","1017"},

            {"3","1","with Kp","1006"},
            {"3","1","without Kp","1007"},
            {"3","1","V3","1008"},
            {"3","1","V3-2","1009"},
            {"3","1","CV","1019"},
            {"3","1","PTZBox_IGSM_Modbus","1030"},
            {"3","1","V5","1035"},

            {"4","1","通用","1010"},
            {"4","1","2003","1011"},
            {"4","1","Modbus","1012"},
            {"4","1","SEVC-D 3.0","1013"},

            {"5","1","通用","1014"},
            {"5","1","Modbus","1015"},

            {"6","1","100以上","1018"},
            {"6","1","80以下","1020"},

            {"7","1","MFGD","1000"},
            {"7","1","MFFD","1016"},

            {"8","0","阀门控制","10500"},

            {"9","0","脉冲采集","5000"},
            {"9","0","肯特流量计","5001"},
            {"9","0","科隆流量计","5002"},

            {"10","0","通用","1031"},

            {"11","0","通用","1032"},
            {"11","0","MODBUS","1033"},

            {"12","0","MODBUS","1034"},
    };

    int reg;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instrumemt_itemset_layout);
        activity=this;
        mtltie=findViewById(R.id.txt_titles_insitem);
        mbutback=findViewById(R.id.imgBack_insitem);
        mButwrite=findViewById(R.id.ins_fragment_but);
        intent=getIntent();
        mainActivity=MainActivity.getInstance();
        initview();
        initdata();

    }

    private void initview() {
        mDialog = CustomDialog.createProgressDialog(this, Constants.TimeOutSecond, new CustomDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(CustomDialog dialog) {
                dialog.dismiss();
                ToastUtils.showToast(getBaseContext(), "超时啦!");
            }
        });
        mbutback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstrumemtItemseetingActivity.this.finish();
            }
        });
        fragments=new ArrayList<instrumentbaseFragment>();
        reg=intent.getIntExtra("regaddr",-1);
        initfragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (reg)
        {
            case 1998:
            case 1997:
                Bundle bundle_1 = new Bundle();
                String[] settings=new String[4];
                settings[0]=intent.getStringExtra("buad");
                settings[1]=intent.getStringExtra("parity");
                settings[2]=intent.getStringExtra("databit");
                settings[3]=intent.getStringExtra("stopbit");
                bundle_1.putStringArray("settings",settings);
                fragment1.setArguments(bundle_1);
                transaction.replace(R.id.content_insitem,fragment1);
                //fragments.get(0)
                break;
            case 1999:
                String setgap=intent.getStringExtra("recordgap");
                Bundle bundle_2 = new Bundle();
                bundle_2.putString("settings",setgap);
                fragment2.setArguments(bundle_2);
                transaction.replace(R.id.content_insitem, fragment2);
                break;
            case 2000:
                Bundle bundle_3 = new Bundle();
                String[] tempset=intent.getStringArrayExtra("listdata");
                bundle_3.putStringArray("listdata",tempset);
                bundle_3.putInt("regsetting",2000);
                fragment3.setArguments(bundle_3);
                transaction.replace(R.id.content_insitem, fragment3);
                break;
            case 2001:
                Bundle bundle_4 = new Bundle();
                String[] tempset1=intent.getStringArrayExtra("listdata");
                bundle_4.putStringArray("listdata",tempset1);
                bundle_4.putInt("regsetting",2001);
                fragment4.setArguments(bundle_4);
                transaction.replace(R.id.content_insitem, fragment4);
                break;
        }
        transaction.commit();
        mainActivity.setOndataparse(new Onbluetoothdataparse());
        mButwrite.setOnClickListener(this);
    }

    private void initfragment() {
        fragment1=new instrumentComSetFragment();
        fragments.add(fragment1);
        fragment2=new instrumenttimegapFragment();
        fragments.add(fragment2);
        fragment3=new instrumentWorkModeSetFragment();
        fragments.add(fragment3);
        fragment4=new instrumentWorkModeSetFragment();
        fragments.add(fragment4);
    }

    private void initdata()
    {
        intent=getIntent();
        String titlehere=intent.getStringExtra("title");

        if(titlehere.indexOf("2000")>-1)
        {
            mtltie.setText("第一路");
        }
        else if(titlehere.indexOf("2001")>-1)
        {
            mtltie.setText("第二路");
        }
        else
        {
            mtltie.setText(titlehere);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivity.setOndataparse(null);
    }

    public static Activity getcurinstance()
    {
       return activity;
    }

    @Override
    public void onClick(View v) {
        byte [] sendbuf = new byte[0];
        byte [] headbuf={(byte)0xFD ,0x00 ,0x00 ,0x0F ,0x00 ,0x15 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00
                ,0x00 ,0x00 ,0x00 ,(byte)0xCF ,0x07 };
        ByteBuffer buf1;
        buf1=ByteBuffer.allocateDirect(headbuf.length);
        buf1=buf1.order(ByteOrder.LITTLE_ENDIAN);
        Log.d("zl","InstrumemtItemseetingActivity onClick:"+reg);
        switch (reg)
        {
            case 1997:
                headbuf[3]=0x12;
                headbuf[14]= (byte) 0xCD;
                sendbuf=new byte[23];
                break;
            case 1998:
                headbuf[3]=0x12;
                headbuf[14]= (byte) 0xCE;
                sendbuf=new byte[23];
                break;
            case 1999:
                headbuf[3]=0x0F;
                headbuf[14]= (byte) 0xCF;
                sendbuf=new byte[20];
                break;
            case 2000:
                headbuf[3]=0x27;
                headbuf[14]= (byte) 0xD0;
                sendbuf=new byte[44];
                break;
            case 2001:
                headbuf[3]=0x27;
                headbuf[14]= (byte) 0xD1;
                sendbuf=new byte[44];
                break;
        }
        buf1.put(headbuf);
        buf1.rewind();
        buf1.get(sendbuf,0,headbuf.length);
        if(reg == 1997)
        {
            settings=fragments.get(0).OnbutOKPress(sendbuf);
        }
        else
        {
            settings=fragments.get(reg-1998).OnbutOKPress(sendbuf);
        }


        if(settings==null)
        {
            return;
        }
        CodeFormat.crcencode(sendbuf);
//        Log.d("zl","instrument out: "+CodeFormat.byteToHex(sendbuf,sendbuf.length).toUpperCase());
        String readOutMsg = DigitalTrans.byte2hex(sendbuf);
        verycutstatus(readOutMsg);
//        if(settings==null)
//        {
//            Toast.makeText(this,"请完善写入参数",Toast.LENGTH_SHORT).show();
//        }
    }

    private void verycutstatus(String readOutMsg) {
        MainActivity parentActivity1 = InstrumemtItemseetingActivity.this.mainActivity;
        String strState1 = parentActivity1.GetStateConnect();
        if(!strState1.equalsIgnoreCase("无连接"))
        {
            InstrumemtItemseetingActivity.this.mDialog.show();
            InstrumemtItemseetingActivity.this.mDialog.setDlgMsg("读取中...");
            //String input1 = Constants.Cmd_Read_Alarm_Pressure;
            parentActivity1.sendData(readOutMsg, "FFFF");
        }
        else
        {
            ToastUtils.showToast(InstrumemtItemseetingActivity.this, "请先建立蓝牙连接!");
        }
    }

    private  class Onbluetoothdataparse implements MainActivity.Ondataparse
    {
        @Override
        public void datacometoparse(String readOutMsg1, byte[] readOutBuf1) {
            if(settings!=null)
            {
                String temp[]=new String[settings.size()];
                for(int i=0;i<settings.size();i++)
                {
                    temp[i]=settings.get(i).get("items");
                }
                intent.putExtra("returnsettings",temp);
                intent.putExtra("regaddr",reg);
                InstrumemtItemseetingActivity.this.setResult(1,intent);
                InstrumemtItemseetingActivity.this.mDialog.dismiss();
                InstrumemtItemseetingActivity.this.finish();
            }
        }
    }
}
