package gc.dtu.weeg.cwdtu.fregment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gc.dtu.weeg.cwdtu.R;
import gc.dtu.weeg.cwdtu.bean.instrumentfactotyinfobean;
import gc.dtu.weeg.cwdtu.utils.CodeFormat;
import gc.dtu.weeg.cwdtu.utils.InstrumemtItemseetingActivity;

public class instrumentWorkModeSetFragment extends instrumentbaseFragment {
    View mView;
    Spinner mdevicestatus;
//    Spinner mdevicetype;
    TextView mdevicetypeinfo;
    EditText maddrET;
    EditText mPowsuptime;
    EditText mElsteraddr;
    ArrayList<Map<String,String>> mdevicestatuslist;
    ArrayList<Map<String,String>> mdevicetypelist;

    ArrayList<instrumentfactotyinfobean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2valy = new ArrayList<>();

    String[] mSettings;
    private OptionsPickerView pvOptions;
    public int[] mCurrentposition={0,0};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView!=null)
        {
            return mView;
        }
        mView=inflater.inflate(R.layout.instrument_workmode_setting_layout,null,false);
        initview();
        return  mView;
    }

    private void initview() {
        initdata();
        mdevicestatus=mView.findViewById(R.id.instrument_device_status_value);
//        mdevicetype=mView.findViewById(R.id.instrument_device_type_value);
        mdevicetypeinfo=mView.findViewById(R.id.instrument_device_type_value);

        maddrET=mView.findViewById(R.id.instrument_device_addr_value);
        mPowsuptime=mView.findViewById(R.id.instrument_device_pow_suply_value);
        mElsteraddr=mView.findViewById(R.id.instrument_device_elster_value);

        setSpinneradpater(mdevicestatus,mdevicestatuslist);
//        setSpinneradpater(mdevicetype,mdevicetypelist);

        mdevicestatus.setOnItemSelectedListener(new onSpinnerSelectimp());
//        mdevicetype.setOnItemSelectedListener(new onSpinnerSelectimp());
        mdevicetypeinfo.setOnClickListener( new OnTextViewClickedListerner());
        initsettiing();

        initsettingdevisetype();
    }

    private void initsettingdevisetype() {
        pvOptions = new OptionsPickerBuilder(InstrumemtItemseetingActivity.activity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()+";"
                        + options2Items.get(options1).get(options2)
                        /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/;
                mdevicetypeinfo.setText(tx);  //mdevicetypeinfo
            }
        })
                .setTitleText("仪表选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.GRAY)   // BLACK
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
//                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(InstrumemtItemseetingActivity.activity, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }

    private void initsettiing() {
        for(int i=0;i<mdevicestatuslist.size();i++)
        {
            if(mSettings[0].equals(mdevicestatuslist.get(i).get("items")))
            {
                mdevicestatus.setSelection(i,true);
                mCurrentposition[0]=i;
                break;
            }

        }
//        for(int i=0;i<mdevicetypelist.size();i++)
//        {
//            if(mSettings[1].equals(mdevicetypelist.get(i).get("items")))
//            {
////                mdevicetype.setSelection(i,true);
//                mCurrentposition[1]=i;
//                break;
//            }
//
//        }
        options1Items.clear();
        for(int i=0;i< InstrumemtItemseetingActivity.factorysinfo.length;i++)
        {
            instrumentfactotyinfobean temp = new instrumentfactotyinfobean();
            temp.factoryinfo=InstrumemtItemseetingActivity.factorysinfo[i];
            options1Items.add(temp);
        }
        int index = -1;
        ArrayList<String> iteminfo = new ArrayList<>();
        ArrayList<String> itemvaly = new ArrayList<>();
        for(int i=0;i<InstrumemtItemseetingActivity.Instrumentinfo.length;i++)
        {
            int curitem=Integer.valueOf(InstrumemtItemseetingActivity.Instrumentinfo[i][0]);
            if(index<curitem)
            {
                if(index!=-1)
                {
                    options2Items.add(iteminfo);
                    options2valy.add(itemvaly);
                }
                iteminfo= new ArrayList<>();
                itemvaly=new ArrayList<>();
                index=curitem;
            }
            iteminfo.add(InstrumemtItemseetingActivity.Instrumentinfo[i][2]);
            itemvaly.add(InstrumemtItemseetingActivity.Instrumentinfo[i][3]);
        }
        options2Items.add(iteminfo);
        options2valy.add(itemvaly);

        mdevicetypeinfo.setText(mSettings[1]);
        maddrET.setText(mSettings[2]);
        mPowsuptime.setText(mSettings[3]);
        mElsteraddr.setText(mSettings[4]);
    }

    private void initdata() {
        Map<String,String> tmap=new HashMap<String,String>();
        tmap.put("items","请选择");
        tmap.put("value","请选择");
        mdevicestatuslist=new ArrayList<>();
        mdevicetypelist=new ArrayList<>();

        mdevicestatuslist.add(tmap);
        mdevicetypelist .add(tmap);

        for(int i=0;i<mActivity.baseinfo.length;i++)
        {
            if(mActivity.baseinfo[i][0].equals("2000")==false)
            {
                continue;
            }
            else
            {
                if(mActivity.baseinfo[i][1].equals("1"))
                {
                    Map<String,String> temp=new HashMap<>();
                    temp.put("items",mActivity.baseinfo[i][2]);
                    temp.put("value",mActivity.baseinfo[i][3]);
                    mdevicestatuslist.add(temp);
                }
//                else if(mActivity.baseinfo[i][1].equals("2"))
//                {
//                    Map<String,String> temp=new HashMap<>();
//                    temp.put("items",mActivity.baseinfo[i][2]);
//                    temp.put("value",mActivity.baseinfo[i][3]);
//                    mdevicetypelist.add(temp);
//                }
            }

        }
    }
    private ArrayAdapter<String> setSpinneradpater(Spinner spinner,ArrayList<Map<String,String>> arrayList )
    {
        //适配器
        ArrayAdapter<String> arr_adapter;
        String list[]=new String[arrayList.size()];
        for(int i=0;i<arrayList.size();i++)
        {
            list[i]=arrayList.get(i).get("items");
        }
        arr_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        return  arr_adapter;
    }
    @Override
   public ArrayList<Map<String, String>> OnbutOKPress( byte[] sendbuf) {
        ArrayList<Map<String,String>> list = new ArrayList<>();
//        mdevicestatuslist;
//        mdevicetypelist;
        for(int i=0;i<1;i++)
        {
            if(mCurrentposition[i]==0)
            {
                return  null;
            }
        }
        if(maddrET.getText().length()==0||mPowsuptime.getText().length()==0)
        {
            return null;
        }
        //仪表状态
        list.add(mdevicestatuslist.get(mCurrentposition[0]));
        int temp= Integer.valueOf(mdevicestatuslist.get(mCurrentposition[0]).get("value"));
        ByteBuffer buf;
        buf=ByteBuffer.allocateDirect(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(temp);
        buf.rewind();
        buf.get(sendbuf,16,1);

        //仪表类型
 //       list.add(mdevicetypelist.get(mCurrentposition[1]));
//        temp= Integer.valueOf(mdevicetypelist.get(mCurrentposition[1]).get("value"));

        String facINS = mdevicetypeinfo.getText().toString();

        Map<String,String> map=new HashMap<>();
        map.put("items",facINS);
        map.put("value",facINS);
        list.add(map);


        int len1= facINS.indexOf(";");
        String fac= facINS.substring(0,len1);
        Log.d("zl","InstrumemtItemseetingActivity+fac:"+fac);
        String INSType = facINS.substring(len1+1,facINS.length());
        Log.d("zl","InstrumemtItemseetingActivity+INSType:"+INSType);
        int facid=-1;
        for(int i=0;i<InstrumemtItemseetingActivity.factorysinfo.length;i++)
        {
            if(fac.equals(InstrumemtItemseetingActivity.factorysinfo[i]))
            {
                facid=i;
            }
        }

        for(int i=0;i<InstrumemtItemseetingActivity.Instrumentinfo.length;i++)
        {
            if(Integer.valueOf(InstrumemtItemseetingActivity.Instrumentinfo[i][0])== facid
                    &&INSType.equals(InstrumemtItemseetingActivity.Instrumentinfo[i][2]))
            {
                temp= Integer.valueOf(InstrumemtItemseetingActivity.Instrumentinfo[i][3]);
            }
        }

        buf=ByteBuffer.allocateDirect(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(temp);
        buf.rewind();
        buf.get(sendbuf,17,2);

        //仪表地址
        String addr=maddrET.getText().toString();
        temp= Integer.valueOf(addr);
        map=new HashMap<>();
        map.put("items",addr);
        map.put("value",addr);
        list.add(map);
        if(temp>256)
        {
            Toast.makeText(mActivity,"仪表地址不能大于256",Toast.LENGTH_SHORT).show();
            return null;
        }
        buf=ByteBuffer.allocateDirect(4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(temp);
        buf.rewind();
        buf.get(sendbuf,19,1);
        //供电时长
        String powtime=mPowsuptime.getText().toString();
        temp= Integer.valueOf(powtime);
        map=new HashMap<>();
        map.put("items",powtime);
        map.put("value",powtime);
        list.add(map);
        if(temp>5000)
        {
            Toast.makeText(mActivity,"供电时长不能超过 5000",Toast.LENGTH_SHORT).show();
            return null;
        }
        buf=ByteBuffer.allocateDirect(4);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(temp);
        buf.rewind();
        buf.get(sendbuf,24,4);
        //Elster Press 地址
        String elster=mElsteraddr.getText().toString();
        if(elster.length()>16)
        {
            Toast.makeText(mActivity,"Elster Press 地址错误",Toast.LENGTH_SHORT).show();
            return null;
        }
        else
        {
            int len=elster.length();
            for(int i=len;i<16;i++)
            {
                elster="0"+elster;
            }
        }
        byte [] elsterbyte=elster.getBytes();
        byte[] hexbyte = CodeFormat.ASCII_To_BCD(elsterbyte,elsterbyte.length);
        map=new HashMap<>();
        map.put("items",elster);
        map.put("value",elster);
        list.add(map);

        buf=ByteBuffer.allocateDirect(8);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.put(hexbyte);
        buf.rewind();
        buf.get(sendbuf,28,8);

        return list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle temp=getArguments();
        mSettings=temp.getStringArray("listdata");
    }


    private  class OnTextViewClickedListerner implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            pvOptions.show();
        }
    }
    private class onSpinnerSelectimp implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int idhere=parent.getId();
            switch(idhere)
            {
                case R.id.instrument_device_status_value:
                    mCurrentposition[0]=position;
                    break;
                case R.id.instrument_device_type_value:
                    mCurrentposition[1]=position;
                    break;

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
