package util;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import po.Book;

/**
 * ExcelExporter：只匯出「當次租借明細」
 *
 */
public class ExcelExporter {

    /**
     * 匯出當次租借明細
     * @param username  會員帳號（顯示用）
     * @param displayTime  UI 顯示時間（yyyy/MM/dd HH:mm:ss）
     * @param items     本次租借的書本清單
     * @param total     本次總金額
     * @param out       匯出路徑（例如桌面）
     * @return          匯出的檔案路徑（回傳給呼叫端顯示）
     */
    public static Path exportCurrent(String username,
                                     String displayTime,
                                     List<Book> items,
                                     BigDecimal total,
                                     Path out) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("沒有可匯出的當次租借明細。");
        }

        try (Workbook wb = new XSSFWorkbook()) {

            // 建一張工作表
            Sheet s = wb.createSheet("當次租借明細");

            int r = 0;

            // 標題列（簡單幾行，讓老師一眼看懂）
            Row t1 = s.createRow(r++); t1.createCell(0).setCellValue("帳號");       t1.createCell(1).setCellValue(username);
            Row t2 = s.createRow(r++); t2.createCell(0).setCellValue("時間");       t2.createCell(1).setCellValue(displayTime);
            s.createRow(r++); // 空行

            // 表頭
            Row head = s.createRow(r++);
            head.createCell(0).setCellValue("序號");
            head.createCell(1).setCellValue("書名");
            head.createCell(2).setCellValue("價格");

            // 明細
            int idx = 1;
            for (Book b : items) {
                Row row = s.createRow(r++);
                row.createCell(0).setCellValue(idx++);
                row.createCell(1).setCellValue(b.getTitle());
                // 注意：Excel 的數字用 double，BigDecimal 要轉
                row.createCell(2).setCellValue(b.getPrice().doubleValue());
            }

            // 總金額
            s.createRow(r++); // 空行
            Row totalRow = s.createRow(r++);
            totalRow.createCell(0).setCellValue("總金額");
            totalRow.createCell(1).setCellValue(total.doubleValue());

            // 自動調欄寬
            for (int c = 0; c <= 2; c++) {
                s.autoSizeColumn(c);
            }

            // 寫檔
            try (FileOutputStream fos = new FileOutputStream(out.toFile())) {
                wb.write(fos);
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
