package controller;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import po.Book;
import po.Member;
import po.TopupLog;
import po.service.MemberService;
import po.service.impl.MemberServiceImpl;

public class MainFrame extends JFrame {


	private final Member current; // 當前登入會員
	private final MemberService svc = new MemberServiceImpl();

	// ===== UI 元件 =====
	private final JLabel lbUser = new JLabel();
	private final JLabel lbAcc = new JLabel();
	private final JLabel lbBal = new JLabel();
	private final JLabel lbTime = new JLabel();

	private final DefaultTableModel bookModel = new DefaultTableModel(new Object[] { "id", "書名", "價格", "庫存" }, 0) 
	{
		@Override
		public boolean isCellEditable(int r, int c) {
			return false;
		}
	};
	private final JTable bookTable = new JTable(bookModel);
	private final JScrollPane spTable = new JScrollPane(bookTable);

	private final JButton btnRent = new JButton("租書");
	private final JButton btnReturn = new JButton("還書(輸入ID)");
	private final JButton btnTopup = new JButton("儲值");
	private final JButton btnLogs = new JButton("儲值金歷程");
	private final JButton btnExport = new JButton("匯出明細");
	private final JButton btnExit = new JButton("離開");
	private final JButton btnConfirmRent = new JButton("確定租書？");

	private final JTextArea taDetail = new JTextArea();
	private final JScrollPane spDetail = new JScrollPane(taDetail);

	// ===== 新增欄位：記住「最近一次成功租書」用來匯出 =====
	private final java.util.List<po.Book> 
	lastRented = new java.util.ArrayList<po.Book>(); // 最近一次的書單
	private java.math.BigDecimal lastTotal = java.math.BigDecimal.ZERO; // 最近一次總金額
	private java.lang.String lastTimeUI = ""; // 最近一次 UI 顯示時間

	// ===== 暫存的租書明細 =====
	private final List<Book> pending = new ArrayList<Book>();
	private BigDecimal pendingTotal = BigDecimal.ZERO;

	// ===== 時間格式 =====
	private static final DateTimeFormatter UI_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public MainFrame(Member m) {
		this.current = m;
		setTitle("自助租書系統 - 主畫面");
		getContentPane().setLayout(null); // ★ Absolute Layout
		setSize(680, 600); // 固定視窗大小
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 視窗置中

		// 字型設定
		Font ui = new Font("PingFang TC", Font.PLAIN, 14);
		lbUser.setFont(ui);
		lbAcc.setFont(ui);
		lbBal.setFont(ui);
		lbTime.setFont(ui);
		btnRent.setFont(ui);
		btnReturn.setFont(ui);
		btnTopup.setFont(ui);
		btnLogs.setFont(ui);
		btnExport.setFont(ui);
		btnExit.setFont(ui);
		btnConfirmRent.setFont(ui);
		bookTable.setFont(ui);
		taDetail.setFont(ui);

		// ===== 上方會員資訊區 =====
		JLabel titleUser = new JLabel("會員：");
		JLabel titleAcc = new JLabel("帳號：");
		JLabel titleBal = new JLabel("餘額：");
		JLabel titleTime = new JLabel("時間：");

		titleUser.setFont(ui);
		titleAcc.setFont(ui);
		titleBal.setFont(ui);
		titleTime.setFont(ui);

		// 設定座標 (x, y, 寬, 高)
		titleUser.setBounds(30, 20, 50, 25);
		lbUser.setBounds(80, 20, 100, 25);

		titleAcc.setBounds(200, 20, 50, 25);
		lbAcc.setBounds(250, 20, 100, 25);

		titleBal.setBounds(351, 20, 50, 25);
		lbBal.setBounds(390, 20, 80, 25);

		titleTime.setBounds(473, 20, 50, 25);
		lbTime.setBounds(520, 20, 154, 25);

		getContentPane().add(titleUser);
		getContentPane().add(lbUser);
		getContentPane().add(titleAcc);
		getContentPane().add(lbAcc);
		getContentPane().add(titleBal);
		getContentPane().add(lbBal);
		getContentPane().add(titleTime);
		getContentPane().add(lbTime);

		// ===== 書籍表格 =====
		spTable.setBorder(new TitledBorder("書籍清單"));
		spTable.setBounds(30, 60, 620, 200);
		getContentPane().add(spTable);
		loadBooks();

		// ===== 功能按鈕列 =====
		btnRent.setBounds(30, 270, 100, 30);
		btnReturn.setBounds(140, 270, 130, 30);
		btnTopup.setBounds(280, 270, 100, 30);
		btnLogs.setBounds(390, 270, 120, 30);
		getContentPane().add(btnRent);
		getContentPane().add(btnReturn);
		getContentPane().add(btnTopup);
		getContentPane().add(btnLogs);

		// ===== 租書明細區 =====
		spDetail.setBounds(30, 320, 480, 180);
		spDetail.setBorder(new TitledBorder("租書明細："));
		taDetail.setEditable(false);
		getContentPane().add(spDetail);

		btnConfirmRent.setBounds(520, 440, 130, 40);
		getContentPane().add(btnConfirmRent);

		// ===== 匯出與離開 =====
		btnExport.setBounds(30, 520, 120, 30);
		btnExit.setBounds(550, 520, 100, 30);
		getContentPane().add(btnExport);
		getContentPane().add(btnExit);

		// ===== 初始化顯示 =====
		refreshHeader();
		tickTimeOnce();
		startClock();

		// ===== 事件處理（全部 mouseClicked）=====

		// (1) 租書（加入明細）
		btnRent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = bookTable.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(MainFrame.this, "請先選書");
					return;
				}
				int id = (int) bookModel.getValueAt(row, 0);
				String title = String.valueOf(bookModel.getValueAt(row, 1));
				BigDecimal price = new BigDecimal(String.valueOf(bookModel.getValueAt(row, 2)));
				int stock = (int) bookModel.getValueAt(row, 3);
				if (stock <= 0) {
					JOptionPane.showMessageDialog(MainFrame.this, "庫存不足");
					return;
				}
				Book b = new Book();
				b.setId(id);
				b.setTitle(title);
				b.setPrice(price);
				b.setStock(stock);
				pending.add(b);
				pendingTotal = pendingTotal.add(price);
				renderPendingDetail();
			}
		});

		// (2) 確定租書（寫入 DB）
		btnConfirmRent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (pending.isEmpty()) {
					JOptionPane.showMessageDialog(MainFrame.this, "尚未選擇任何書");
					return;
				}
				if (current.getBalance().compareTo(pendingTotal) < 0) {
					JOptionPane.showMessageDialog(MainFrame.this, "餘額不足，請先儲值");
					return;
				}
				try {
					// ★★★ 1) 先把「這次要租的清單/總金額/時間」存起來，給匯出用
					lastRented.clear();
					lastRented.addAll(pending); // 本次清單
					lastTotal = pendingTotal; // 本次總金額
					lastTimeUI = lbTime.getText(); // 本次時間（右上角顯示的字串）

					// 2) 正式寫入：逐本租書
					for (Book b : pending) {
						svc.rent(current.getUsername(), b.getId());
					}

					// 3) 成功後清空待租，更新畫面
					pending.clear();
					pendingTotal = BigDecimal.ZERO;
					taDetail.setText("");
					loadBooks();
					refreshMemberAndHeader();

					JOptionPane.showMessageDialog(MainFrame.this, "租書成功！");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// (3) 還書
		btnReturn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String s = JOptionPane.showInputDialog(MainFrame.this, "輸入要歸還的書籍ID");
				if (s == null || s.isBlank())
					return;
				try {
					int id = Integer.parseInt(s.trim());
					svc.returnBook(current.getUsername(), id);
					loadBooks();
					JOptionPane.showMessageDialog(MainFrame.this, "還書成功！");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// (4) 儲值
		btnTopup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String s = JOptionPane.showInputDialog(MainFrame.this, "輸入儲值金額（現金）");
				if (s == null || s.isBlank())
					return;
				try {
					BigDecimal amt = new BigDecimal(s.trim());
					svc.topup(current.getUsername(), amt);
					refreshMemberAndHeader();
					JOptionPane.showMessageDialog(MainFrame.this, "儲值成功！");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// (5) 儲值金歷程
		btnLogs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				List<TopupLog> logs = svc.getTopupLogs(current.getUsername());
				DefaultTableModel model = new DefaultTableModel(new Object[] { "金額(+儲值 -租書)", "交易後餘額", "時間" }, 0);
				for (TopupLog t : logs) {
					model.addRow(new Object[] { t.getAmount(), t.getBalance(), t.getTime() });
				}
				JTable tbl = new JTable(model);
				JScrollPane sp = new JScrollPane(tbl);
				sp.setPreferredSize(new java.awt.Dimension(560, 260));
				JOptionPane.showMessageDialog(MainFrame.this, sp, "儲值金歷程（" + current.getUsername() + "）",
						JOptionPane.PLAIN_MESSAGE);
			}
		});

	
		// (6) 匯出明細（★只匯出「當次租借」的資料）
		btnExport.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				try {
					// 1如果還沒有完成一次租書，就提醒使用者
					if (lastRented == null || lastRented.isEmpty()) {
						javax.swing.JOptionPane.showMessageDialog(MainFrame.this, "尚未有可匯出的『當次租借明細』\n請先完成一次『確定租書？』");
						return;
					}

					// 2匯出到桌面
					java.nio.file.Path out = java.nio.file.Paths.get(System.getProperty("user.home"), "Desktop",
							"member_rent.xlsx");

					// 3呼叫新的 util.ExcelExporter.exportCurrent()
					util.ExcelExporter.exportCurrent(current.getUsername(), // 會員帳號
							lastTimeUI, // 當次租書時間（UI顯示的時間字串）
							lastRented, // 當次租的書本清單
							lastTotal, // 當次租的總金額
							out // 匯出檔案路徑
					);

					// 4匯出
					javax.swing.JOptionPane.showMessageDialog(MainFrame.this, "已匯出到桌面：\\n" + out);

				} catch (Exception ex) {
					javax.swing.JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage(), "錯誤",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		/*
		 * btnExport.addMouseListener(new MouseAdapter(){
		 * 
		 * @Override public void mouseClicked(MouseEvent e){ try{ Path out =
		 * Paths.get(System.getProperty("user.home"), "Desktop", "member_logs.xlsx");
		 * util.ExcelExporter.exportLogs(current.getUsername(), out);
		 * JOptionPane.showMessageDialog(MainFrame.this, "已匯出到：" + out);
		 * }catch(Exception ex){ JOptionPane.showMessageDialog(MainFrame.this,
		 * ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE); } } });
		 */
		// (7) 離開
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
	}

	
	private void loadBooks() {
		bookModel.setRowCount(0);
		List<Book> books = new po.dao.impl.BookDaoImpl().findAll();
		for (Book b : books) {
			bookModel.addRow(new Object[] { b.getId(), b.getTitle(), b.getPrice(), b.getStock() });
		}
		if (bookModel.getRowCount() > 0)
			bookTable.setRowSelectionInterval(0, 0);
	}

	private void renderPendingDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		int i = 1;
		for (Book b : pending) {
			sb.append(i++).append(". ").append(b.getTitle()).append("  租借價格：").append(b.getPrice()).append("\n");
		}
		sb.append("總金額：").append(pendingTotal);
		taDetail.setText(sb.toString());
	}

	private void refreshHeader() {
		lbUser.setText(current.getName());
		lbAcc.setText(current.getUsername());
		lbBal.setText(String.valueOf(current.getBalance()));
		lbTime.setText(LocalDateTime.now().format(UI_FMT));
	}

	private void refreshMemberAndHeader() {
		try {
			Member fresh = svc.login(current.getUsername(), current.getPassword());
			current.setBalance(fresh.getBalance());
			refreshHeader();
		} catch (Exception ignore) {
		}
	}

	private void tickTimeOnce() {
		lbTime.setText(LocalDateTime.now().format(UI_FMT));
	}

	private void startClock() {
		new Timer(1000, e -> lbTime.setText(LocalDateTime.now().format(UI_FMT))).start();
	}
}
