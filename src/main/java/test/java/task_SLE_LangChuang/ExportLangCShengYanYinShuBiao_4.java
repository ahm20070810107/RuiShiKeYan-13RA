package test.java.task_SLE_LangChuang;

import com.RuiShiKeYan.Common.Method.DateFormat;
import com.RuiShiKeYan.Common.Method.LocalHostInfo;
import com.RuiShiKeYan.Common.Method.SaveExcelTool;
import com.alibaba.fastjson.JSONObject;
import com.yiyihealth.data.DaX.reader.DSExcelReader2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.RuiShiKeYan.SubMethod.LangCShengYanYinShuPublicInfo;
/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2017/10/25
 * Time:下午3:09
 */
public class ExportLangCShengYanYinShuBiao_4 {
    static int k= 2;
    static int m=14;
    static JSONObject document=null;
    static LangCShengYanYinShuPublicInfo langCShengYanYinShuPublicInfo;
    public static void mainFunction(int kValue,int mValue,LangCShengYanYinShuPublicInfo obj) throws Exception
    {
        k=kValue;
        m=mValue;
        langCShengYanYinShuPublicInfo=obj;
        SaveExcelTool saveExcelTool = new SaveExcelTool();
        SXSSFSheet sheet = saveExcelTool.getSheet("");
        langCShengYanYinShuPublicInfo.fillExcelTitle(sheet,BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,false);
        writeToExcel(sheet);
        saveExcelTool.saveExcel("交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx");
        saveNotInYinSuBiao(k,m);  //获取分析2表未在2表但在pid验证的pid
        langCShengYanYinShuPublicInfo.getCacliteExcel("狼疮肾炎因素分析计算删除列-含肾损害.xlsx","交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx",BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,
                "交付/狼疮肾炎因素表-分析4计算用-含肾损害-"+k+"-"+m+".xlsx","");
        langCShengYanYinShuPublicInfo.getCacliteExcel("狼疮肾炎因素分析计算删除列-含肾损害.xlsx","交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx",BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,
                "交付/狼疮肾炎因素表-分析4计算用-含肾损害-独立验证中心-"+k+"-"+m+".xlsx","one");
        langCShengYanYinShuPublicInfo.getCacliteExcel("狼疮肾炎因素分析计算删除列-含肾损害.xlsx","交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx",BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,
                "交付/狼疮肾炎因素表-分析4计算用-含肾损害-训练测试中心-"+k+"-"+m+".xlsx","muti");


        getCacliteExcel("狼疮肾炎因素分析计算删除列-不含肾损害.xlsx","交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx",BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,
                "交付/狼疮肾炎因素表-分析4计算用-不含肾损害-"+k+"-"+m+".xlsx","");
        getCacliteExcel("狼疮肾炎因素分析计算删除列-不含肾损害.xlsx","交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx",BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,
                "交付/狼疮肾炎因素表-分析4计算用-不含肾损害-独立验证中心-"+k+"-"+m+".xlsx","one");
        getCacliteExcel("狼疮肾炎因素分析计算删除列-不含肾损害.xlsx","交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx",BaseInfo_Title_ListValue_DBCondition.titleLCShengYanYinShuB4,
                "交付/狼疮肾炎因素表-分析4计算用-不含肾损害-训练测试中心-"+k+"-"+m+".xlsx","muti");
    }
    public static   void getCacliteExcel(String deleteFileName,String srouceFileName,String title,String savedFile,String strXunLianFlag) throws Exception  //获取删除指定列的表
    {
        System.out.println("getCacliteExcel");
        Map<String,String> mapDeleteCloum = new HashMap<String, String>();
        ReadFromExcelToMap.readFromExcelToMap(mapDeleteCloum, LocalHostInfo.getPath()+deleteFileName,"删除列");
        String[] titles = title.split(",");

        SaveExcelTool saveExcelTool = new SaveExcelTool();
        SXSSFSheet sheet = saveExcelTool.getSheet("");
        langCShengYanYinShuPublicInfo.fillExcelTitle(sheet,title,mapDeleteCloum);

        JSONObject config = new JSONObject();
        config.put("filename", LocalHostInfo.getPath()+srouceFileName);
        config.put("source_type", "excel");
        DSExcelReader2 excelReader = new DSExcelReader2(config);

        int RowNum=1;
        while ((document = excelReader.nextDocument()) != null) {
            int length=titles.length;
            if("1".equals(getNumberValue(ReadFromExcelToMap.getJSonValue(document,"系统_肾损害"))))
                continue;
            if(strXunLianFlag.equals("one"))
            {
                if(!document.getString("医院").equals("武汉同济医院"))
                    continue;
            }else if(strXunLianFlag.equals("muti"))
            {
                if(document.getString("医院").equals("武汉同济医院"))
                    continue;
            }
            Row row = sheet.createRow(RowNum++);
            for (int i = 0; i < titles.length; i++) {
                if(document.getString(titles[i]) !=null)
                    row.createCell(i).setCellValue(getNumberValue(document.getString(titles[i])));
            }
            for(Map.Entry<String,ArrayList> map:langCShengYanYinShuPublicInfo.mapLeiJiFenZu.entrySet())
            {
                if(mapDeleteCloum.containsKey("系统_"+map.getKey()))
                    continue;
                String value=getNumberValue(ReadFromExcelToMap.getJSonValue(document,"系统_"+map.getKey()));
                if(value.equals("0")||value.equals("1"))
                    row.createCell(length++).setCellValue(Integer.valueOf(value));
                else
                    row.createCell(length++).setCellValue(value);
            }
            for(Map.Entry<String,ArrayList> map:langCShengYanYinShuPublicInfo.mapLeiJiSubFenZu.entrySet())
            {
                if(mapDeleteCloum.containsKey(map.getKey()))
                    continue;
                String value=getNumberValue(ReadFromExcelToMap.getJSonValue(document,map.getKey()));
                if(value.equals("0")||value.equals("1"))
                    row.createCell(length++).setCellValue(Integer.valueOf(value));
                else
                    row.createCell(length++).setCellValue(value);
            }

        }
        saveExcelTool.saveExcel(savedFile);
        System.out.println("OK");
    }

    private static String getNumberValue(String value)
    {
        if(value.indexOf(".") >-1)
        {
            return value.substring(0,value.indexOf("."));
        }
        return value;
    }
    private static void saveNotInYinSuBiao(int k,int m) throws Exception
    {
        Map<String ,String> mapPID = new HashMap<String, String>();
        HashMap<String, String> mapExceptPID= new HashMap<String, String>();
        HashMap<String, String> mapYinSuTable= new HashMap<String, String>();
        langCShengYanYinShuPublicInfo.fillExceptPID(mapExceptPID,true);
        String fileName= LocalHostInfo.getPath()+"交付/狼疮肾炎因素表-分析4用-"+k+"-"+m+".xlsx";
        getPIDInfo(mapPID,mapExceptPID);
        ReadFromExcelToMap.readFromExcelToMap(mapYinSuTable,fileName,"患者（PID）");

        SaveExcelTool saveExcelTool= new SaveExcelTool();
        SXSSFSheet sheet = saveExcelTool.getSheet("");
        saveExcelTool.fillExcelTitle("移除步骤,PID");

        int RowNum=1;
        for(Map.Entry<String,String> map:mapPID.entrySet())
        {
            if(!mapYinSuTable.containsKey(map.getKey()))
            {
                Row row = sheet.createRow(RowNum++);
                row.createCell(0).setCellValue("狼疮肾炎因素分析4");
                row.createCell(1).setCellValue(map.getKey());
            }
        }
        saveExcelTool.saveExcel("交付/移除组PID列表-分析4-"+k+"-"+m+".xlsx");

    }

    private static void getPIDInfo(Map<String,String> mapPID,HashMap<String, String> mapExceptPID)throws Exception
    {
        String fileName= LocalHostInfo.getPath()+"交付/PID验证列表.xlsx";

        JSONObject config = new JSONObject();
        config.put("filename", fileName);
        config.put("source_type", "excel");

        DSExcelReader2 excelReader = new DSExcelReader2(config);
        while ((document = excelReader.nextDocument()) != null) {
            if (!document.get("患者（PID）").toString().equals(""))
                if(!mapExceptPID.containsKey(document.getString("患者（PID）")))
                    mapPID.put(document.getString("患者（PID）"), "0");
        }
    }


    private static void writeToExcel(SXSSFSheet sheet)
   {
       int RowNum=1;
       for(Map.Entry<String,JSONObject> map :langCShengYanYinShuPublicInfo.mapLCShengYanPID.entrySet())
       {
           System.out.println(RowNum);
           String strPID=map.getKey();
           JSONObject jsonPID=map.getValue();
        //   String strLCShengYanTime = jsonPID.getString("狼疮性肾炎时间天");
         //  String strGuanCQZhongDian=jsonPID.getString("观察期终点");
           String strsleTIme = DateFormat.getNextDay(jsonPID.getString("SLE时间天"), m);
           String strFinalFenZu="";
           if(jsonPID.getString("狼疮性肾炎分组")!=null &&(jsonPID.getString("狼疮性肾炎分组").equals("1")))
           {
               strFinalFenZu="1";
             //  strGuanCQZhongDian=strLCShengYanTime;
           }else if((jsonPID.getString("狼疮性肾炎分组")==null ||jsonPID.getString("狼疮性肾炎分组").equals(""))&&jsonPID.getString("确诊SLE后病程分组").equals("1"))
           {
               strFinalFenZu="2";
           }else {
               continue;
           }
           Row row = sheet.createRow(RowNum++);
           row.createCell(0).setCellValue(strPID);
           row.createCell(1).setCellValue(strFinalFenZu);
           row.createCell(2).setCellValue(jsonPID.getString("医院"));
           row.createCell(3).setCellValue(jsonPID.getString("出生年"));
           if(langCShengYanYinShuPublicInfo.mapBasicInfo.containsKey(strPID))
           {
               JSONObject jsonBasicInfo=langCShengYanYinShuPublicInfo.mapBasicInfo.get(strPID);
               row.createCell(4).setCellValue(jsonBasicInfo.getString("性别"));
               row.createCell(5).setCellValue(jsonBasicInfo.getString("地域"));
           }

          // row.createCell(5).setCellValue(strGuanCQZhongDian);
           Integer age=-1;

           if(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.containsKey(strPID)) {
               row.createCell(6).setCellValue(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.get(strPID).getString("诊断时间天").substring(0,4));
               row.createCell(7).setCellValue(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.get(strPID).getString("诊断时间年减去出生年"));
               row.createCell(8).setCellValue(getAgeGroup(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.get(strPID).getString("诊断时间年减去出生年")));

               age=Integer.valueOf(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.get(strPID).getString("初发时间天").substring(0,4))-
                       Integer.valueOf(jsonPID.getString("出生年"));
               row.createCell(9).setCellValue(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.get(strPID).getString("初发时间天").substring(0,4));
               row.createCell(10).setCellValue(age);
               row.createCell(11).setCellValue(getAgeGroup(age));
               row.createCell(12).setCellValue(langCShengYanYinShuPublicInfo.mapQZShiJianBiao.get(strPID).getString("诊断时间天减去初发时间天"));
           }

           fillLeftColumForExcel(row,13,strPID,strsleTIme);
       }

   }
    private static String getAgeGroup(Integer age)
    {
        if(age==null ||age==-1)
            return "异常";
        if(age.intValue()>=0 &&age.intValue()<=18)
            return "青少年";
        else if(age.intValue()>=19 &&age.intValue()<=49)
            return "成人";
        else if(age.intValue()>=50 &&age.intValue()<=100)
            return "晚发";
        else
        {
            return "异常";
        }

    }
    private static String getAgeGroup(String strage)
    {

        if(strage==null ||strage.equals(""))
            return "异常";
        Integer age=-1;
        try
        {  if(strage.indexOf(".")>0)
            age=Integer.valueOf(strage.substring(0,strage.indexOf(".")));
        else age=Integer.valueOf(strage);
        }catch (Exception e){e.printStackTrace();}
        if(age.intValue()>=0 &&age.intValue()<=18)
            return "青少年";
        else if(age.intValue()>=19 &&age.intValue()<=49)
            return "成人";
        else if(age.intValue()>=50 &&age.intValue()<=100)
            return "晚发";
        else
        {
            return "异常";
        }

    }

   private static void fillLeftColumForExcel(Row row,int columNum,String strPid,String strLCShengYanTime)
   {
        for (Map.Entry<String,ArrayList> map:langCShengYanYinShuPublicInfo.mapLeiJiFenZu.entrySet())
        {
            JSONObject dd= langCShengYanYinShuPublicInfo.fill6To10(strPid,map.getValue(),strLCShengYanTime,map.getKey());
            if(dd==null) {
          //      mapItem.put(map.getKey(),null);
                row.createCell(columNum++).setCellValue(0);
            }
            else
            {
           //     mapItem.put(map.getKey(),dd);
                row.createCell(columNum++).setCellValue(1);
            }
        }

       for (Map.Entry<String,ArrayList> map:langCShengYanYinShuPublicInfo.mapLeiJiSubFenZu.entrySet())
       {
           JSONObject dd= langCShengYanYinShuPublicInfo.fill6To10(strPid,map.getValue(),strLCShengYanTime,map.getKey());
           if(dd==null) {
         //      mapSubItem.put(map.getKey(),null);
               row.createCell(columNum++).setCellValue(0);
           }
           else
           {
        //       mapSubItem.put(map.getKey(),dd);
               row.createCell(columNum++).setCellValue(1);
           }
       }
   }

}
