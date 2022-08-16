/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author harolD
 */
public class Archivo_Excel {

    Workbook wb;

    public Archivo_Excel(File excel, JTable j){

        DefaultTableModel table = new DefaultTableModel();
        j.setModel(table);
        try {
            wb = WorkbookFactory.create(new FileInputStream(excel));
            Sheet hoja = wb.getSheetAt(0);
            Iterator fila = hoja.rowIterator();
            int indiceFila = -1;

            while (fila.hasNext()) {
                indiceFila++;
                Row row = (Row) fila.next();
                Iterator columa = row.cellIterator();
                Object[] list = new Object[5];
                int indiceColumn = -1;
                while (columa.hasNext()) {
                    indiceColumn++;
                    Cell celda = (Cell) columa.next();
                    if (indiceFila == 0) {
                        table.addColumn(celda.getStringCellValue());
                    } else {
                        if (celda != null) {
                            switch (celda.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    list[indiceColumn] = (int) Math.round(celda.getNumericCellValue());
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    list[indiceColumn] = celda.getStringCellValue();
                                    break;
                            }
                        }
                    }
                }
                   if (indiceFila != 0)table.addRow(list);
            }
        } catch (InvalidFormatException | IOException ex) {
            Logger.getLogger(Archivo_Excel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void file(File f, JTable j) {
        if (f.exists()) {
            Archivo_Excel obg = new Archivo_Excel(f, j);

        }
    }
}
