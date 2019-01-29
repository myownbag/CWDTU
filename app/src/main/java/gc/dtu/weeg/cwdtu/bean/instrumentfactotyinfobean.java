package gc.dtu.weeg.cwdtu.bean;

import com.contrarywind.interfaces.IPickerViewData;

public class instrumentfactotyinfobean implements IPickerViewData {
   public String factoryinfo="";

    @Override
    public String getPickerViewText() {   //getPickerViewText
        return factoryinfo;
    }
}
