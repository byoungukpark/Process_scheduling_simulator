package schduling_apptest02;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


class Schduling_swing {
	private JFrame frame;
	private JTextField txtAa;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_1;
	
	JLabel lblNewLabel_3_2; //p코어 수 출력
	JLabel lblNewLabel_3_1_1; //e코어 수 출력 
	
	JLabel lblNewLabel_2_2_2_2_2; //마지막 코어 전력
	
	JLabel lblNewLabel_2_4_2_2_1; // 각 코어 전력 계산
	
	JLabel lblNewLabel_2_2_2_2_2_2; //총 시간 s
	
	
	JTable table_1; //프로세스 입력
	DefaultTableModel model_1;
	
	JTable table; //프로세스 결과
	DefaultTableModel model; //저장
	
	int p_core_num = 0;
	int e_core_num = 1;
	
	
	play_schduling scheduling;
	
	draw_panel02 draw; //차트
	
	
	private class MyActionListener01 implements ActionListener { //시작 버튼 엑션
 		public void actionPerformed(ActionEvent e) {
			System.out.println("starting_but!!!!");
			
			if(scheduling.num == 0) { //프로세스 생성 자체를 안하고 스케줄링 시작할때
				int tum = 10;
				if(isInteger(txtAa.getText())) {
					tum = Integer.parseInt(txtAa.getText());
					if(!(tum >= 0 || tum < 100))
		 				tum = 10;
				}
				scheduling.num = tum;
				scheduling.produce_processes(); //디폴트로 10개 프로세스 생성
				scheduling.make_show_processes_tabledata02();
				update_show_processes_table();
			}
			
			int rr = 2;
			if(isInteger(textField_1.getText())) {
				rr = Integer.parseInt(textField_1.getText());
				if(!(rr >= 1 || rr < 10))
	 				rr = 2;
			}
			scheduling.rr = rr;
			
			scheduling.start_schduling(); //스케줄링 시작
			
			update_processes_table(); //결과 테이블 갱신
			
			draw.clear(); //이전 간트차트 초기화
			draw.draw_start(scheduling); //간트차트 그리기
			
			uptate_electric();
			
			scheduling.main_processes.clear_pastdata(); //비선점인 스케줄러 방식의 프로세스들은 past_bt,core_using_time를 초기화 해줘야됨
		}
	}
	
	private class MyActionListener02 implements ActionListener { //스케줄링 선택 콤보 박스 
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox) e.getSource(); // 콤보박스 알아내기
			scheduling.schduling_num = cb.getSelectedItem().toString();
		}
	}
	
	private class MyActionListener03 implements ActionListener { //p_core 수 ++
 		public void actionPerformed(ActionEvent e) {
 			if((p_core_num + e_core_num) < 4)
 				p_core_num++;
 			
 			lblNewLabel_3_2.setText("P : "+p_core_num);
 			scheduling.p = p_core_num;
		}
	}
	
	private class MyActionListener04 implements ActionListener { //E_core 수 ++
 		public void actionPerformed(ActionEvent e) {
 			if((p_core_num + e_core_num) < 4)
 				e_core_num++;
 			
 			lblNewLabel_3_1_1.setText("E : "+e_core_num);
 			scheduling.e = e_core_num;
		}
	}
	
	private class MyActionListener05 implements ActionListener { //p_core 수 --
 		public void actionPerformed(ActionEvent e) {
 			if(!(p_core_num <= 0))
 				p_core_num--;
 			
 			lblNewLabel_3_2.setText("P : "+p_core_num);
 			scheduling.p = p_core_num;
		}
	}
	
	private class MyActionListener06 implements ActionListener { //E_core 수 --
 		public void actionPerformed(ActionEvent e) {
 			if(!(e_core_num <= 0))
 				e_core_num--;
 			
 			lblNewLabel_3_1_1.setText("E : "+e_core_num);
 			scheduling.e = e_core_num;
		}
	}
	
	private class MyActionListener07 implements ActionListener { // enter new 버튼
 		public void actionPerformed(ActionEvent e) {
 			int at_tum = 0;
 			int bt_tum = 1;
 			
			if(isInteger(textField.getText())) {
				at_tum = Integer.parseInt(textField.getText());
				if(!(at_tum >= 0 || at_tum < 10))
					at_tum = 0;
			}
			
			if(isInteger(textField_2.getText())) {
				bt_tum = Integer.parseInt(textField_2.getText());
				if(!(bt_tum >= 1 || bt_tum < 10))
					bt_tum = 1;
			}
			String[] new_atbt = {Integer.toString(at_tum),Integer.toString(bt_tum)};
			scheduling.enter_processes_list.add(new_atbt);
			scheduling.produce_processes();
			
			scheduling.make_show_processes_tabledata01();
 			update_show_processes_table();
 			
 			DefaultTableModel dm1 = (DefaultTableModel)table.getModel();
 			cleartable(dm1);
 			
		}
	}
	
	private class MyActionListener08 implements ActionListener { // clear 버튼
 		public void actionPerformed(ActionEvent e) {
 			DefaultTableModel dm = (DefaultTableModel)table_1.getModel();
 			cleartable(dm);
 			scheduling.enter_processes_list.clear();
 			scheduling.num = 0;
 			DefaultTableModel dm1 = (DefaultTableModel)table.getModel();
 			cleartable(dm1);
		}
	}
	
	
	
	private class MyActionListener10 implements ActionListener { //프로세스 수 입력 버튼 //enter outo 버튼
 		public void actionPerformed(ActionEvent e) {
 			int tum = 10;
			if(isInteger(txtAa.getText())) {
				tum = Integer.parseInt(txtAa.getText());
				if(!(tum >= 0 || tum < 100))
	 				tum = 10;
			}
			scheduling.num = tum;
			
			DefaultTableModel dm = (DefaultTableModel)table_1.getModel();
 			cleartable(dm);
 			scheduling.enter_processes_list.clear();
			
			scheduling.produce_processes();
			scheduling.make_show_processes_tabledata02();
			update_show_processes_table();
			
			DefaultTableModel dm1 = (DefaultTableModel)table.getModel();
 			cleartable(dm1);
			
			System.out.println("outo_but!!!! >> " + scheduling.num);
		}
	}
	
//	###################################### 아래는 함수
	
	public Schduling_swing() {
		scheduling = new play_schduling();
		initialize();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Schduling_swing window = new Schduling_swing();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	boolean isInteger(String strValue) {
	    try {
	      Integer.parseInt(strValue);
	      return true;
	    } catch (NumberFormatException ex) {
	      return false;
	    }
	}
	
	public void update_processes_table() {
		scheduling.change_processes_datatype();
		DefaultTableModel dm = (DefaultTableModel)table.getModel();
		cleartable(dm);
		for(int i = 0;i<scheduling.processes_num.length;i++)
			dm.addRow(scheduling.processes_num[i]);
	}
	
	public void update_show_processes_table() {
		//scheduling.make_show_processes_tabledata01();
		DefaultTableModel dm = (DefaultTableModel)table_1.getModel();
		cleartable(dm);
		for(int i = 0;i<scheduling.processes_tum.length;i++)
			dm.addRow(scheduling.processes_tum[i]);
	}
	
	public void cleartable(DefaultTableModel dm) {
		if(dm.getRowCount() == 0) return;
		else {
			dm.setNumRows(0);
		}
	}
	
	public void uptate_electric() {
		String str = "";
		float all_ele = 0;
		String time = "";
		
		for(int i = 0;i<scheduling.main_core.all_processor.size();i++) {
			if(i==3) {
				str += Integer.toString(i+1)+".core( "+scheduling.main_core.all_processor.get(i).type+
						" ) 소요전력  :  " + Float.toString(scheduling.main_core.all_processor.get(i).electric) + " v ";
			}else {
				str += Integer.toString(i+1)+".core( "+scheduling.main_core.all_processor.get(i).type+
						" ) 소요전력  :  " + Float.toString(scheduling.main_core.all_processor.get(i).electric) + " v     ";
			}
			all_ele += scheduling.main_core.all_processor.get(i).electric;
		}
		lblNewLabel_2_4_2_2_1.setText(str);
		lblNewLabel_2_2_2_2_2.setText(String.format("%.1f", all_ele) + " v");
		time = Integer.toString(scheduling.main_core.all_processor.get(0).time.size());
		lblNewLabel_2_2_2_2_2_2.setText(time+" s");
	}

	
	//////////////////////// gui 틀
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 0));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 0, 0));
		panel_2.setBounds(12, 120, 1162, 533);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBackground(new Color(0, 0, 0));
		panel_3_1.setLayout(null);
		panel_3_1.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		panel_3_1.setBounds(385, 10, 390, 180);
		panel_2.add(panel_3_1);
		
		JButton btnNewButton_2_1 = new JButton("P core +");
		btnNewButton_2_1.addActionListener(new MyActionListener03());	
		btnNewButton_2_1.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1.setBackground(new Color(0, 0, 0));
		btnNewButton_2_1.setBounds(70, 44, 84, 41);
		panel_3_1.add(btnNewButton_2_1);
		
		lblNewLabel_3_2 = new JLabel("P : "+p_core_num);
		lblNewLabel_3_2.setFont(new Font("굴림", Font.PLAIN, 16));
		lblNewLabel_3_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_3_2.setBackground(new Color(0, 0, 0));
		lblNewLabel_3_2.setBounds(266, 44, 54, 41);
		panel_3_1.add(lblNewLabel_3_2);
		
		lblNewLabel_3_1_1 = new JLabel("E : 1");
		lblNewLabel_3_1_1.setFont(new Font("굴림", Font.PLAIN, 16));
		lblNewLabel_3_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_3_1_1.setBackground(Color.YELLOW);
		lblNewLabel_3_1_1.setBounds(266, 103, 54, 41);
		panel_3_1.add(lblNewLabel_3_1_1);
		
		JButton btnNewButton_2_1_1 = new JButton("P core -");
		btnNewButton_2_1_1.addActionListener(new MyActionListener05());
		btnNewButton_2_1_1.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_1.setBackground(Color.BLACK);
		btnNewButton_2_1_1.setBounds(166, 44, 84, 41);
		panel_3_1.add(btnNewButton_2_1_1);
		
		JButton btnNewButton_2_1_2 = new JButton("E core +");
		btnNewButton_2_1_2.addActionListener(new MyActionListener04());
		btnNewButton_2_1_2.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_2.setBackground(Color.BLACK);
		btnNewButton_2_1_2.setBounds(70, 103, 84, 41);
		panel_3_1.add(btnNewButton_2_1_2);
		
		JButton btnNewButton_2_1_1_1 = new JButton("E core -");
		btnNewButton_2_1_1_1.addActionListener(new MyActionListener06());
		btnNewButton_2_1_1_1.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_1_1.setBackground(Color.BLACK);
		btnNewButton_2_1_1_1.setBounds(166, 103, 84, 41);
		panel_3_1.add(btnNewButton_2_1_1_1);
		
		JPanel panel_3_1_1 = new JPanel();
		panel_3_1_1.setBackground(new Color(0, 0, 0));
		panel_3_1_1.setForeground(new Color(0, 0, 0));
		panel_3_1_1.setLayout(null);
		panel_3_1_1.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		panel_3_1_1.setBounds(12, 10, 350, 180);
		panel_2.add(panel_3_1_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Processes");
		lblNewLabel_2_1_1.setForeground(Color.WHITE);
		lblNewLabel_2_1_1.setBackground(Color.BLACK);
		lblNewLabel_2_1_1.setBounds(12, 10, 123, 23);
		panel_3_1_1.add(lblNewLabel_2_1_1);
		
		
		
		
		//입력 프로세스 테이블
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBackground(Color.BLACK);
		scrollPane_1.setBounds(181, 43, 157, 87); //(181, 43, 157, 87); //(181, 10, 157, 160);
		panel_3_1_1.add(scrollPane_1);
		
		model_1 = new DefaultTableModel(scheduling.processes_tum,scheduling.processes_tum_name){  
	   		public boolean isCellEditable(int row, int column){ //셀 수정 못하게 하는 부분
	   			return false;
			}
   		};
		
		table_1 = new JTable(model_1);
		table_1.setRowSorter(new TableRowSorter<DefaultTableModel>(model_1)); // 테이블 정렬
		table_1.setBackground(new Color(0, 0, 0));
		table_1.setForeground(new Color(255, 255, 255));
		scrollPane_1.setViewportView(table_1);
		
		
		
		txtAa = new JTextField();
		txtAa.setBackground(new Color(255, 148, 0));
		txtAa.setForeground(new Color(0, 0, 0));
		txtAa.setText("n");
		txtAa.setColumns(10);
		txtAa.setBounds(104, 43, 50, 23);
		panel_3_1_1.add(txtAa);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("Processes n :");
		lblNewLabel_2_1_1_1.setForeground(Color.WHITE);
		lblNewLabel_2_1_1_1.setBackground(Color.BLACK);
		lblNewLabel_2_1_1_1.setBounds(12, 43, 80, 23);
		panel_3_1_1.add(lblNewLabel_2_1_1_1);
		
		textField = new JTextField();
		textField.setText("at");
		textField.setForeground(Color.BLACK);
		textField.setColumns(10);
		textField.setBackground(new Color(255, 148, 0));
		textField.setBounds(12, 107, 65, 23);
		panel_3_1_1.add(textField);
		
		textField_2 = new JTextField();
		textField_2.setText("bt");
		textField_2.setForeground(Color.BLACK);
		textField_2.setColumns(10);
		textField_2.setBackground(new Color(255, 148, 0));
		textField_2.setBounds(89, 107, 65, 23);
		panel_3_1_1.add(textField_2);
		
		JButton btnNewButton_2_1_3 = new JButton("Enter auto");
		btnNewButton_2_1_3.addActionListener(new MyActionListener10());	
		btnNewButton_2_1_3.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_3.setBackground(Color.BLACK);
		btnNewButton_2_1_3.setBounds(12, 76, 142, 23);
		panel_3_1_1.add(btnNewButton_2_1_3);
		
		JButton btnNewButton_2_1_3_1 = new JButton("Enter new");
		btnNewButton_2_1_3_1.addActionListener(new MyActionListener07());	
		btnNewButton_2_1_3_1.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_3_1.setBackground(Color.BLACK);
		btnNewButton_2_1_3_1.setBounds(12, 140, 142, 23);
		panel_3_1_1.add(btnNewButton_2_1_3_1);
		
		JButton btnNewButton_2_1_3_2 = new JButton("clear");
		btnNewButton_2_1_3_2.addActionListener(new MyActionListener08());	
		btnNewButton_2_1_3_2.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_3_2.setBackground(Color.BLACK);
		btnNewButton_2_1_3_2.setBounds(181, 140, 157, 23); //(181, 140, 157, 23); //(104, 10, 50, 23); 
		panel_3_1_1.add(btnNewButton_2_1_3_2);
		
		JPanel panel_3_1_2 = new JPanel();
		panel_3_1_2.setBackground(new Color(0, 0, 0));
		panel_3_1_2.setLayout(null);
		panel_3_1_2.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		panel_3_1_2.setBounds(800, 10, 350, 180);
		panel_2.add(panel_3_1_2);
		
		JLabel lblNewLabel_3_2_1 = new JLabel("Schduling : ");
		lblNewLabel_3_2_1.setForeground(Color.WHITE);
		lblNewLabel_3_2_1.setBackground(Color.BLACK);
		lblNewLabel_3_2_1.setBounds(46, 20, 100, 31);
		panel_3_1_2.add(lblNewLabel_3_2_1);
		
		
		JComboBox comboBox = new JComboBox(scheduling.schduling_name); //스케줄링 선택 콤보 박스 
		comboBox.addActionListener(new MyActionListener02());
		comboBox.setForeground(new Color(255, 255, 255));
		comboBox.setBackground(new Color(0, 0, 0));
		comboBox.setBounds(158, 24, 133, 23);
		panel_3_1_2.add(comboBox);
		
		JLabel lblNewLabel_2_1 = new JLabel("RR qun : ");
		lblNewLabel_2_1.setBounds(46, 61, 61, 18);
		panel_3_1_2.add(lblNewLabel_2_1);
		lblNewLabel_2_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_2_1.setBackground(new Color(0, 0, 0));
		
		textField_1 = new JTextField();
		textField_1.setText("2");
		textField_1.setForeground(Color.BLACK);
		textField_1.setColumns(10);
		textField_1.setBackground(new Color(255, 148, 0));
		textField_1.setBounds(125, 61, 33, 23);
		panel_3_1_2.add(textField_1);
		
		JButton btnNewButton_2_1_1_1_1 = new JButton("START"); //시작 버튼 
		btnNewButton_2_1_1_1_1.addActionListener(new MyActionListener01());	
		btnNewButton_2_1_1_1_1.setFont(new Font("굴림", Font.PLAIN, 16));
		btnNewButton_2_1_1_1_1.setForeground(new Color(255, 148, 0));
		btnNewButton_2_1_1_1_1.setBackground(Color.BLACK);
		btnNewButton_2_1_1_1_1.setBounds(46, 107, 245, 51);
		panel_3_1_2.add(btnNewButton_2_1_1_1_1);
		
		JPanel panel_3_1_2_1 = new JPanel();
		panel_3_1_2_1.setBackground(new Color(0, 0, 0));
		panel_3_1_2_1.setLayout(null);
		panel_3_1_2_1.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		panel_3_1_2_1.setBounds(12, 200, 350, 323);
		panel_2.add(panel_3_1_2_1);
		
		
		
		// 출력 프로세스 테이블
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(0, 0, 0));
		scrollPane.setBounds(12, 10, 326, 303);
		panel_3_1_2_1.add(scrollPane);
		
		model = new DefaultTableModel(scheduling.processes_num,scheduling.processes_name){  
	   		public boolean isCellEditable(int row, int column){ //셀 수정 못하게 하는 부분
	   			return false;
			}
   		};
		
		table = new JTable(model);
		table.setRowSorter(new TableRowSorter<DefaultTableModel>(model)); // 테이블 정렬
		table.setBackground(new Color(0, 0, 0));
		table.setForeground(new Color(255, 255, 255));
		scrollPane.setViewportView(table);
		
		
		
		//// 간트 차트 출력 패널
		JPanel panel_3_1_2_1_1 = new JPanel();
		panel_3_1_2_1_1.setBackground(new Color(0, 0, 0));
		panel_3_1_2_1_1.setLayout(null);
		panel_3_1_2_1_1.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		panel_3_1_2_1_1.setBounds(385, 200, 765, 239);
		panel_2.add(panel_3_1_2_1_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBackground(Color.BLACK);
		scrollPane_2.setBounds(12, 10, 741, 219);
		panel_3_1_2_1_1.add(scrollPane_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 0, 0));
		panel_1.setLayout(null);
		panel_1.setPreferredSize(new Dimension(3300, 184));//274 //254 //180
		scrollPane_2.setViewportView(panel_1);
		
		draw = new draw_panel02(); // 간트차트
		draw.setBounds(10,10,3300, 184); // 10,10,867,274
		panel_1.add(draw);
	
		
		////레비블들 출력 패널
		JPanel panel_3_1_2_1_1_1 = new JPanel();
		panel_3_1_2_1_1_1.setBackground(new Color(0, 0, 0));
		panel_3_1_2_1_1_1.setLayout(null);
		panel_3_1_2_1_1_1.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		panel_3_1_2_1_1_1.setBounds(385, 449, 765, 74);
		panel_2.add(panel_3_1_2_1_1_1);
		
		//마지막 전력, 시간 출력 레이블 들
		JLabel lblNewLabel_2_4_2_2 = new JLabel("총 소요 전력 :");
		lblNewLabel_2_4_2_2.setForeground(Color.WHITE);
		lblNewLabel_2_4_2_2.setBounds(36, 42, 84, 15); //.setBounds(36, 42, 84, 15); //.setBounds(79, 42, 84, 15);
		panel_3_1_2_1_1_1.add(lblNewLabel_2_4_2_2);
		
		lblNewLabel_2_2_2_2_2 = new JLabel("0.0 v");
		lblNewLabel_2_2_2_2_2.setForeground(Color.WHITE);
		lblNewLabel_2_2_2_2_2.setBounds(123, 42, 100, 15); //.setBounds(123, 42, 50, 15); //.setBounds(170, 42, 50, 15); 
		panel_3_1_2_1_1_1.add(lblNewLabel_2_2_2_2_2);
		
		lblNewLabel_2_4_2_2_1 = new JLabel(" 소요 전력 :");
		lblNewLabel_2_4_2_2_1.setForeground(Color.WHITE);
		lblNewLabel_2_4_2_2_1.setBounds(36, 17, 687, 15); ////.setBounds(36, 17, 687, 15); //.setBounds(79, 17, 84, 15);
		panel_3_1_2_1_1_1.add(lblNewLabel_2_4_2_2_1);
		
		
		JLabel lblNewLabel_2_4_2_2_2 = new JLabel("총 소요 시간 :");
		lblNewLabel_2_4_2_2_2.setForeground(Color.WHITE);
		lblNewLabel_2_4_2_2_2.setBounds(232, 42, 84, 15);
		panel_3_1_2_1_1_1.add(lblNewLabel_2_4_2_2_2);
		
		lblNewLabel_2_2_2_2_2_2 = new JLabel("0 s");
		lblNewLabel_2_2_2_2_2_2.setForeground(Color.WHITE);
		lblNewLabel_2_2_2_2_2_2.setBounds(323, 42, 50, 15);
		panel_3_1_2_1_1_1.add(lblNewLabel_2_2_2_2_2_2);
		
		JPanel panel_3_1_2_1_2 = new JPanel();
		panel_3_1_2_1_2.setBackground(new Color(0, 0, 0));
		panel_3_1_2_1_2.setBounds(24, 10, 1136, 107);
		panel.add(panel_3_1_2_1_2);
		panel_3_1_2_1_2.setLayout(null);
		panel_3_1_2_1_2.setBorder(new LineBorder(new Color(255, 148, 0), 4));
		
		ImageIcon img01 = new ImageIcon(".\\image\\logo.png.png");
		JLabel lblNewLabel = new JLabel(img01);
		lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(12, 10, 144, 80);
		panel_3_1_2_1_2.add(lblNewLabel);
		
		
		JLabel lblNewLabel_1 = new JLabel("Process Scheduling Simulator");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(167, 19, 451, 66);
		panel_3_1_2_1_2.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 28));
		
		JLabel lblNewLabel_3_2_1_1 = new JLabel("Team name : 윈도우 19");
		lblNewLabel_3_2_1_1.setForeground(Color.WHITE);
		lblNewLabel_3_2_1_1.setBackground(Color.BLACK);
		lblNewLabel_3_2_1_1.setBounds(986, 32, 138, 24);
		panel_3_1_2_1_2.add(lblNewLabel_3_2_1_1);
		
		JLabel lblNewLabel_3_2_1_1_1 = new JLabel("Creation Date : 2023.05.12");
		lblNewLabel_3_2_1_1_1.setForeground(Color.WHITE);
		lblNewLabel_3_2_1_1_1.setBackground(Color.BLACK);
		lblNewLabel_3_2_1_1_1.setBounds(974, 10, 150, 24);
		panel_3_1_2_1_2.add(lblNewLabel_3_2_1_1_1);
	}
}

public class Schduling_app01 {

	public static void main(String[] args) {
		Schduling_swing window = new Schduling_swing();
	}
}
