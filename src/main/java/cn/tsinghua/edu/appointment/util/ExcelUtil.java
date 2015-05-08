package cn.tsinghua.edu.appointment.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import cn.tsinghua.edu.appointment.domain.Appointment;
import cn.tsinghua.edu.appointment.exception.BasicException;

public class ExcelUtil {

	public static final String DEFAULT_EXPORT_FOLDER = "D:\\Workspace\\apache-tomcat-8.0.21-appointment\\webapps\\appointment\\export\\";
	public static final String EXPORT_PREFIX = "export/";
	public static final String EXPORT_SUFFIX = ".xlsx";
	public static final String ZIP_SUFFIX = ".zip";
	public static final String DEFAULT_EXCEL = DEFAULT_EXPORT_FOLDER + "export_template.xlsx";

	public static void exportToExcel(Appointment app, String filename)
			throws BasicException {
		String filepath = DEFAULT_EXPORT_FOLDER + filename;
		try {
			InputStream input = new FileInputStream(DEFAULT_EXCEL);
			Workbook wb = WorkbookFactory.create(input);
			Sheet sheet = wb.getSheetAt(0);
			Cell cell = null;

			// 咨询信息
			cell = sheet.getRow(1).createCell(1);
			cell.setCellValue(DateUtil.convertDate(app.getStartTime()));
			cell = sheet.getRow(1).createCell(3);
			cell.setCellValue(DateUtil.convertDate(app.getEndTime()));
			cell = sheet.getRow(1).createCell(5);
			cell.setCellValue(app.getTeacher());

			// 咨询申请表
			cell = sheet.getRow(4).createCell(1);
			cell.setCellValue(app.getStudentInfo().getName());
			cell = sheet.getRow(4).createCell(3);
			cell.setCellValue(app.getStudentInfo().getGender());
			cell = sheet.getRow(4).createCell(5);
			cell.setCellValue(app.getStudentInfo().getStudentId());
			cell = sheet.getRow(4).createCell(7);
			cell.setCellValue(app.getStudentInfo().getSchool());
			cell = sheet.getRow(5).createCell(1);
			cell.setCellValue(app.getStudentInfo().getHometown());
			cell = sheet.getRow(5).createCell(3);
			cell.setCellValue(app.getStudentInfo().getMobile());
			cell = sheet.getRow(5).createCell(5);
			cell.setCellValue(app.getStudentInfo().getEmail());
			cell = sheet.getRow(6).createCell(1);
			cell.setCellValue(app.getStudentInfo().getExperience());
			cell = sheet.getRow(7).createCell(1);
			cell.setCellValue(app.getStudentInfo().getProblem());

			// 同学反馈表
			cell = sheet.getRow(10).createCell(1);
			cell.setCellValue(app.getStudentFeedback().getProblem());
			cell = sheet.getRow(11).createCell(1);
			cell.setCellValue(app.getStudentFeedback().getName());
			if(!app.getStudentFeedback().getChoices().isEmpty()) {
				for (int i = 12; i <= 23; ++i) {
					cell = sheet.getRow(i).createCell(2);
					cell.setCellValue(app.getStudentFeedback().getChoices()
							.charAt(i - 12) + "");
				}
			}
			cell = sheet.getRow(24).createCell(1);
			cell.setCellValue(app.getStudentFeedback().getScore());
			cell = sheet.getRow(25).createCell(1);
			cell.setCellValue(app.getStudentFeedback().getFeedback());

			// 咨询师反馈表
			cell = sheet.getRow(28).createCell(1);
			cell.setCellValue(app.getTeacherFeedback().getTeacherName());
			cell = sheet.getRow(28).createCell(3);
			cell.setCellValue(app.getTeacherFeedback().getTeacherId());
			cell = sheet.getRow(28).createCell(5);
			cell.setCellValue(app.getTeacherFeedback().getStudentName());
			cell = sheet.getRow(29).createCell(1);
			cell.setCellValue(app.getTeacherFeedback().getProblem());
			cell = sheet.getRow(30).createCell(1);
			cell.setCellValue(app.getTeacherFeedback().getSolution());
			cell = sheet.getRow(31).createCell(1);
			cell.setCellValue(app.getTeacherFeedback().getAdviceToCenter());

			FileOutputStream output = new FileOutputStream(filepath);
			wb.write(output);
			input.close();
			output.close();
		} catch (Exception e) {
			throw new BasicException("导出错误");
		}
	}
}
