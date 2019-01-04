package com.koolearn.guonei.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.util.*;

public class ExcelUtil extends DefaultHandler {
       
    private SharedStringsTable sst;   
    private String lastContents;   
    private boolean nextIsString;
    private String cellPosition;
  
    private int sheetIndex = -1;
    private Map<Integer, String> colMap = new LinkedHashMap<>();
    private List<Map> list = new ArrayList<>();
    private int curRow = 0;   
    private int colSize = 0;

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 判断Excel的版本,获取Workbook
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        if (!file.exists()) {
            FileOutputStream fileOut = null;
            try {
                if(file.getName().endsWith(EXCEL_XLS)){	 //Excel 2003
                    wb = new HSSFWorkbook();
                }else if(file.getName().endsWith(EXCEL_XLSX)){	// Excel 2007/2010
                    wb = new XSSFWorkbook();
                }
                wb.createSheet();
                fileOut = new FileOutputStream(file);
                wb.write(fileOut);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                fileOut.close();
            }
        }
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith(EXCEL_XLS)){	 //Excel 2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){	// Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 根据map内容生成excel文件
     * @param dataList 具体填写内容（key是序号，从0开始递增）
     * @param finalXlsxPath  excel文件的保存位置
     */
    public static void writeExcel(List<Map> dataList, String finalXlsxPath){
        OutputStream out = null;
        try {
            // 获取总列数
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbook(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            /**
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();	// 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
                // 得到要插入的每一条记录
                Map dataMap = dataList.get(j);
                int cloumnCount = dataMap.entrySet().size();
                for (int k = 0; k <= cloumnCount; k++) {
                    // 在一行内循环
                    Cell cell = row.createCell(k);
                    cell.setCellValue((String) dataMap.get(k));
                }
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**  
     * 读取第一个工作簿的入口方法  
     * @param path  
     */  
    public void readOneSheet(String path) throws Exception {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        readOneSheet(fileInputStream);
    }

    /**
     * 重载方法 readOneSheet
     * @param inputStream
     * @throws Exception
     */
    public void readOneSheet(InputStream inputStream) throws Exception {
        OPCPackage pkg = OPCPackage.open(inputStream);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        InputStream sheet = r.getSheet("rId1");

        InputSource sheetSource = new InputSource(sheet);
        parser.parse(sheetSource);

        sheet.close();
    }
       
       
    /**  
     * 读取所有工作簿的入口方法  
     * @param path  
     * @throws Exception 
     */  
    public void process(String path) throws Exception {   
        OPCPackage pkg = OPCPackage.open(path);   
        XSSFReader r = new XSSFReader(pkg);   
        SharedStringsTable sst = r.getSharedStringsTable();   
  
        XMLReader parser = fetchSheetParser(sst);   
  
        Iterator<InputStream> sheets = r.getSheetsData();   
        while (sheets.hasNext()) {   
            curRow = 0;   
            sheetIndex++;   
            InputStream sheet = sheets.next();   
            InputSource sheetSource = new InputSource(sheet);   
            parser.parse(sheetSource);   
            sheet.close();   
        }   
    }

    public List<Map> getList() {
        return list;
    }
       
    /**  
     * 该方法自动被调用，每读一行调用一次，在方法中写自己的业务逻辑即可 
     * @param sheetIndex 工作簿序号 
     * @param curRow 处理到第几行 
     * @param rowMap 当前数据行的数据集合
     */  
    public void optRow(int sheetIndex, int curRow, Map<Integer,String> rowMap) {
        if (rowMap.isEmpty()) {
            return;
        }
        Map<Integer, String> cellMap = new LinkedHashMap<>();
        for (int i = 0; i < colSize; i++) {
            String val = rowMap.get(i) == null ? "" : rowMap.get(i);
            cellMap.put(i, val);
        }
        list.add(cellMap);
    }   
       
       
    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {   
        XMLReader parser = XMLReaderFactory   
                .createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        this.sst = sst;   
        parser.setContentHandler(this);   
        return parser;   
    }   
       
    public void startElement(String uri, String localName, String name,   
            Attributes attributes) throws SAXException {   
        // c => 单元格  
        if (name.equals("c")) {
            cellPosition = attributes.getValue("r");
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true  
            String cellType = attributes.getValue("t");   
            if (cellType != null && cellType.equals("s")) {   
                nextIsString = true;   
            } else {   
                nextIsString = false;   
            }   
        }   
        // 置空   
        lastContents = "";   
    }   
       
       
    public void endElement(String uri, String localName, String name)   
            throws SAXException {   
        // 根据SST的索引值的到单元格的真正要存储的字符串  
        // 这时characters()方法可能会被调用多次  
        if (nextIsString) {   
            try {   
                int idx = Integer.parseInt(lastContents);   
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx))   
                        .toString();   
            } catch (Exception e) {   
  
            }   
        }   
  
        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引  
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符  
        if (name.equals("v")) {   
            String value = lastContents.trim();   
            value = value.equals("") ? " " : value;


            int charVal = (int)cellPosition.substring(0,1).charAt(0);
            //excel的cell的坐标第一个大写字母-'A'，获取列的index
            int index = charVal-65;

            if (cellPosition.length()==2&&Objects.equals("1",cellPosition.substring(1,2))) {
                colSize++;
            } else {
                colMap.put(index, value);
            }
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法  
            if (name.equals("row")) {
                //初始化存储所有col的list
                optRow(sheetIndex, curRow, colMap);
                colMap = new LinkedHashMap<>();
                curRow++;
            }
        }   
    }   
  
    public void characters(char[] ch, int start, int length)   
            throws SAXException {   
        // 得到单元格内容的值  
        lastContents += new String(ch, start, length);   
    }

    //测试
    public static void main(String[] args) throws Exception{

        //读excel的例子
        /*String fName = "test_case.xlsx";
        String excelName = "/Users/longzhiwu/Downloads/听一刻素材/test_excels/" + fName;


        Long time = System.currentTimeMillis();
        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.readOneSheet(excelName);
        Long endtime = System.currentTimeMillis();

        List<Map> contentList = excelUtil.getList();
        for (int i = 0; i < contentList.size(); i++) {
            System.out.println(contentList.get(i));
        }

        System.out.println("ExcelUtil 解析数据" + contentList.size() + "条;耗时" + (endtime - time)+"毫秒");*/



        //写excel的例子
        List<Map> dataList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Map<Integer, String> dataMap = new HashMap<>();
            dataMap.put(0, "数字"+i);
            dataMap.put(1, "我是内容");
            dataList.add(dataMap);
        }

        ExcelUtil.writeExcel(dataList,"/Users/longzhiwu/Downloads/听一刻素材/test_excels/write_test.xlsx");

    }
  
}  