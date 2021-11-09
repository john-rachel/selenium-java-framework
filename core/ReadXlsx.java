package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;	
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/* 
 * This class contains read data from xlsx using apache poi
 * 
 */
public class ReadXlsx {

	public static String[][] getData(String filePath,String sheetName, String tableName) throws IOException{
		
		Row row = null;
        Cell cell = null;


        int occurence=1;
        int x=0,x1=0,y=0,y1=0,ci=0,cj=0;
        String[][] tabArray=null;
        Row nextrow;
        Cell nextcel=null;
        try {
               FileInputStream fis = new FileInputStream(filePath);
               Workbook wb = new XSSFWorkbook(fis);
               Sheet sh =wb.getSheet(sheetName); //wb.getSheetAt(0);
               
               Iterator<Row> rowIte = sh.rowIterator();
               while(rowIte.hasNext())
               {
                     nextrow = rowIte.next();

                     Iterator<Cell> cellIte = nextrow.cellIterator();
                     while(cellIte.hasNext())
                     {
                            nextcel = cellIte.next();

                            if(nextcel.getStringCellValue().equals(tableName)){
                                   if(occurence==1){
                                          x = nextcel.getRowIndex();
                                          y = nextcel.getColumnIndex();
                                          occurence=0;
                                   }
                                   else{
                                          x1 = nextcel.getRowIndex();
                                          y1 = nextcel.getColumnIndex();
                                   }

                            }

                     }
               }

               tabArray=new String[x1-x-1][y1-y-1];

               ci=0;
               for (int i=x+1;i<x1;i++,ci++)
               {
                     cj=0;
                     for (int j=y+1;j<y1;j++,cj++){
                            //System.out.println(tabArray[ci][cj]=nextcel;

                            row = sh.getRow(i);
                            cell = row.getCell(j);

//                          System.out.println(cell.toString());
                            tabArray[ci][cj]=cell.toString();
                            
                                                             System.out.println("");

                     }
               }
               return (tabArray);

        } catch (FileNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               return null;
        }
       


	}
}
