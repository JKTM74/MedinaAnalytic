package com.analitic;

import com.analitic.models.Service;
import com.analitic.repositories.ServiceRepository;
import com.analitic.repositories.UserRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.analitic"})
@EntityScan("com.analitic.models")
@EnableJpaRepositories("com.analitic.repositories")
public class Application {

    private static ServiceRepository serviceRepository = null;
    private static UserRepository userRepository = null;

    public Application(ServiceRepository service, UserRepository userRepository) {
        Application.serviceRepository = serviceRepository;
        Application.userRepository = userRepository;
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        //userRepository.findUsersByDepartment(1);
        //writeIntoExcel("C:\\ExcelFile\\test.xls");
    }

    @SuppressWarnings("deprecation")
    public static void writeIntoExcel(String file) throws FileNotFoundException, IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Birthdays");

        // Нумерация начинается с нуля
        Row row = sheet.createRow(0);

        // Мы запишем имя и дату в два столбца
        // имя будет String, а дата рождения --- Date,
        // формата dd.mm.yyyy
        Cell name = row.createCell(0);
        name.setCellValue("John");

        Cell birthdate = row.createCell(1);

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
        birthdate.setCellStyle(dateStyle);


        // Нумерация лет начинается с 1900-го
        birthdate.setCellValue(new Date(110, 10, 10));

        // Меняем размер столбца
        sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();
    }
}
