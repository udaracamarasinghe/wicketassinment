package com.mycompany.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDatasource {

	private static ExcelDatasource datasource;

	private String excelFilePath = System.getProperty("user.dir") + "/datasource/employee_db.xlsx";
	private File imagesDir = new File(System.getProperty("user.dir") + "/datasource/");

	private Map<Long, Employee> indexMap = new TreeMap<>();

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public synchronized static ExcelDatasource getDatasource() {
		if (datasource == null) {
			try {
				datasource = new ExcelDatasource();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return datasource;
	}

	public ExcelDatasource() throws IOException {

		if (!imagesDir.exists()) {
			boolean mkdir = imagesDir.mkdir();
		}

		File file = new File(excelFilePath);

		workbook = new XSSFWorkbook();

		if (!file.exists()) {
			sheet = workbook.createSheet("Employee Data");

			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("1", new Object[] { "ID", "NAME", "DOB", "AGE", "EMAIL", "PHOTO" });
			data.put("2", new Object[] { 1, "Amit", "2/12/2000", 20, "Amit@Amit.com", null });
			data.put("3", new Object[] { 2, "Saman", "2/12/1987", 33, "saman@Amit.com", null });
			data.put("4", new Object[] { 3, "Mikey", "2/12/2001", 20, "micky@Amit.com", null });

			Set<String> keyset = data.keySet();
			int rownum = 0;
			for (String key : keyset) {
				Row row = sheet.createRow(rownum++);
				Object[] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}

			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		}

		FileInputStream fileInputStream = new FileInputStream(file);

		workbook = new XSSFWorkbook(fileInputStream);

		sheet = workbook.getSheet("Employee Data");

		Iterator<Row> rowIterator = sheet.iterator();
		int i = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (i > 0) {
				Employee employee = convert(row);

				indexMap.put(employee.getId(), employee);
			}
			i++;
		}

	}

	public Employee getById(Long id) {
		return indexMap.get(id);
	}

	public List<Employee> getEmployees(long first, long count) {
		ArrayList<Employee> arrayList = new ArrayList<>(indexMap.values());

		return arrayList;
	}

	public long getSize() {
		return indexMap.size();
	}

	public Employee addEmployee(Employee employee) {
		Integer newId = indexMap.size();

		if (!indexMap.containsValue(newId)) {
			XSSFRow createRow = sheet.createRow(newId);
			XSSFCell idCell = createRow.createCell(0);
			idCell.setCellValue((Integer) newId);
			XSSFCell nameCell = createRow.createCell(1);
			nameCell.setCellValue((String) employee.getName());
			XSSFCell dobCell = createRow.createCell(2);
			dobCell.setCellValue((Date) employee.getDob());
			XSSFCell ageCell = createRow.createCell(3);
			ageCell.setCellValue((Integer) employee.getAge());
			XSSFCell emailCell = createRow.createCell(4);
			emailCell.setCellValue((String) employee.getEmail());
			XSSFCell photoCell = createRow.createCell(5);
			photoCell.setCellValue((String) employee.getPhoto());

			FileOutputStream out;
			try {
				out = new FileOutputStream(new File(excelFilePath));
				workbook.write(out);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		employee.setId(new Long(newId));

		indexMap.put(employee.getId(), employee);

		return employee;
	}

	public void updateEmployee(Employee employee) {
		if (indexMap.containsKey(employee.getId())) {
			indexMap.put(employee.getId(), employee);

			Row row = findRowById(employee.getId());

			row.getCell(1).setCellValue((String) employee.getName());
			row.getCell(1).setCellValue((Date) employee.getDob());
			row.getCell(1).setCellValue((Integer) employee.getAge());
			row.getCell(1).setCellValue((String) employee.getEmail());
			row.getCell(1).setCellValue((String) employee.getPhoto());

			FileOutputStream out;
			try {
				out = new FileOutputStream(new File(excelFilePath));
				workbook.write(out);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public Row findRowById(Long id) {
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (id.equals(new Double(row.getCell(0).getNumericCellValue()).longValue())) {
				return row;
			}
		}

		return null;
	}

	private Employee convert(Row row) {
		Iterator<Cell> cellIterator = row.cellIterator();

		Employee employee = new Employee();

		for (int i = 0; i < 6; i++) {
			Cell cell = cellIterator.next();
			switch (i) {
			case 0: {
				employee.setId(new Double(cell.getNumericCellValue()).longValue());
				break;
			}
			case 1: {
				employee.setName(cell.getStringCellValue());
				break;
			}
			case 2: {
				Date date1;
				try {
					date1 = new SimpleDateFormat("dd/MM/yyyy").parse(cell.getStringCellValue());
					employee.setDob(date1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
			case 3: {
				employee.setAge((new Double(cell.getNumericCellValue()).intValue()));
				break;
			}
			case 4: {
				employee.setEmail(cell.getStringCellValue());
				break;
			}
			case 5: {
				employee.setPhoto(cell.getStringCellValue());
				break;
			}
			}

		}
		return employee;
	}
}
