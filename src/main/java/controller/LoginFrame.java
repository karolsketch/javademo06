package controller;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import po.Member;
import po.service.MemberService;
import po.service.impl.MemberServiceImpl;

/**
 * LoginFrame
 * - 所有元件用 setBounds() 指定位置 
 * - 登入/離開按鈕使用
 * mouseClicked 
 * - macOS 字體調整 + 視窗固定大小 + 標題完整顯示
 */
public class LoginFrame extends JFrame {

	// ===== 畫面元件 =====
	private final JTextField tfUser = new JTextField();
	private final JPasswordField tfPass = new JPasswordField();
	private final JButton btnLogin = new JButton("登入");
	private final JButton btnExit = new JButton("離開");
	private final JLabel lbUser = new JLabel("帳號：");
	private final JLabel lbPass = new JLabel("密碼：");

	private final MemberService svc = new MemberServiceImpl();

	public LoginFrame() {
		// 視窗設定
		setTitle("自助租書系統 - 登入"); // 顯示完整標題
		setLayout(null); // ★ 改為絕對位置
		setSize(330, 180); // 固定視窗大小
		setResizable(false); // 不允許使用者改大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 視窗置中

		// 設定中文字型）
		Font ui = new Font("PingFang TC", Font.PLAIN, 14);
		lbUser.setFont(ui);
		lbPass.setFont(ui);
		tfUser.setFont(ui);
		tfPass.setFont(ui);
		btnLogin.setFont(ui);
		btnExit.setFont(ui);

		// ===== 絕對座標配置（x, y, width, height）=====
		lbUser.setBounds(40, 30, 60, 25);
		tfUser.setBounds(100, 30, 160, 25);

		lbPass.setBounds(40, 70, 60, 25);
		tfPass.setBounds(100, 70, 160, 25);

		btnExit.setBounds(60, 110, 80, 30);
		btnLogin.setBounds(180, 110, 80, 30);

		add(lbUser);
		add(tfUser);
		add(lbPass);
		add(tfPass);
		add(btnLogin);
		add(btnExit);

		// ===== 事件）=====
		// 登入
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String u = tfUser.getText().trim();
					String pw = new String(tfPass.getPassword());
					if (u.isEmpty() || pw.isEmpty()) {
						JOptionPane.showMessageDialog(LoginFrame.this, "請輸入帳號與密碼");
						return;
					}
					Member m = svc.login(u, pw);
					JOptionPane.showMessageDialog(LoginFrame.this, "歡迎 " + m.getName());
					new MainFrame(m).setVisible(true);
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(LoginFrame.this, ex.getMessage(), "登入失敗", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// 離開
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
	}

	// 主程式入口
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
	}
}
