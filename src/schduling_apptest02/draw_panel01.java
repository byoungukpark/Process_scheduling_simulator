package schduling_apptest02;

import javax.swing.JPanel;

import schduling_apptest02.make_processes.processes;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;


//class draw_panel01 extends JPanel{ //배경 그림	
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		
//	}
//}

class draw_panel02 extends JPanel implements Runnable{ //간트차트 그림
	int st_x = 60;
	int y = 50; 
	int xx = 1; 
	int yy = 30;
	BufferedImage buffer; // buffer for double buffering
	
	play_schduling data;
	public boolean start = false;
	
	ArrayList<ArrayList<p_block>> block_list = new ArrayList<>(); //모든 프로세스 블록
	
	class p_block{
		int[] b= {0,0,0,0}; //블럭 규격
		int[] c; //색갈
		int pid = 0;
		
		p_block(int[] b,int[] c,int pid){
			this.b = b;
			this.c = c;
			this.pid = pid;
		}
	}
	
	public void clear() {
		block_list.clear();
		start = false;
		xx = 0;
	}
	
	void cul_time() {
		int x = 0;
		int y = 44; //50
		int xx = 30;
		int yy = 30; //고정
		int b_p = 0;;
		int count_x = 0;
		
		for(int i = 0;i<data.main_core.all_processor.size();i++) {
			ArrayList<p_block> new_c = new ArrayList<>();
			block_list.add(new_c);
			
			for(int j = 0;j<data.main_core.all_processor.get(i).time.size();j++) {
				int p = data.main_core.all_processor.get(i).time.get(j);
				
				if(j != 0 && b_p != p) { // 처음이 아니고 프로세서가 변할때
					if(b_p > 0) { //b_p != 0
						int[] new_color = data.main_processes.processes_list.get(b_p-1).itemcolor;
						int[] b = {st_x+x,50 + y*i,(count_x+1)*xx,yy};
						p_block new_b = new p_block(b,new_color,b_p);
						block_list.get(i).add(new_b);
					}
					else {
						int[] new_color = {0,0,0};
						int[] b = {st_x+x,50 + y*i,(count_x+1)*xx,yy};
						p_block new_b = new p_block(b,new_color,b_p);
						block_list.get(i).add(new_b);
					}
					
					x= x + count_x * xx;
					count_x = 0;
				}
				
				if(j == (data.main_core.all_processor.get(i).time.size())-1) { //오로지 마지막 프로세서
					if(p > 0) { //p != 0
						int[] new_color = data.main_processes.processes_list.get(p-1).itemcolor;
						int[] b = {st_x+x,50 + y*i,(count_x+1)*xx,yy};
						p_block new_b = new p_block(b,new_color,p);
						block_list.get(i).add(new_b);
					}
					else {
						int[] new_color = {0,0,0};
						int[] b = {st_x+x,50 + y*i,(count_x+1)*xx,yy};
						p_block new_b = new p_block(b,new_color,p);
						block_list.get(i).add(new_b);
					}
					
					x= x + count_x * xx;
					count_x = 0;
				}
				
				b_p = p;
				count_x++;
			}  //하나 프로세서 끝
			x = 0;
			count_x = 0;
			b_p = 0;
		}
		
//		//올바른지 출력용
//		for(int i = 0;i<block_list.size();i++) {
//			System.out.println("");
//			for(int j = 0;j<block_list.get(i).size();j++) {
//				System.out.print("block : "+Arrays.toString(block_list.get(i).get(j).b)); //Arrays.toString(block_list.get(i).get(j).b)
//				System.out.print(">> color : "+Arrays.toString(block_list.get(i).get(j).c));
//				System.out.println("");
//			}
//			System.out.println("");
//		}
		
	}
	
	public void draw_start(play_schduling scheduling) {
		data = scheduling; //데이터 객체 생성
		cul_time();
		start = true;
		
		setOpaque(false); //배경 색 없음
		//setBackground(new Color(255,0,0,0)); //(new Color(255,0,0,0))
		
		Thread thread = new Thread(this);
        thread.start(); // start the thread
        
	}

	@Override
	public void run() {
		while (xx <3000) {
			xx += 2;
			
			repaint();
			
			if(!start)
				break;
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	 
	@Override
	public void paintComponent(Graphics g) { //실제로 그리는 부분
		if(block_list.size()>0) {
			super.paintComponent(g);
			
	        if (buffer == null) {
	            // create the buffer if it hasn't been created yet
	            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	        }
	
	        // get the graphics context of the buffer
	        Graphics2D g2 = buffer.createGraphics();
	
	        // clear the buffer
	        g2.setColor(Color.BLACK); //Color.WHITE //getBackground()
	        g2.fillRect(0, 0, getWidth(), getHeight());
	
	        // draw the shape onto the buffer
	        g2.setColor(Color.BLACK);
	        g2.fillRect(xx, 0, 2000, 300); //g2.fillRect(100, 0, xx, 0);
	
	        // draw the buffer onto the visible screen
	        g.drawImage(buffer, 0, 0, null); //필수 개새끼 //g.drawImage(buffer, 0, 0, null); //필수 개새끼
	        
	        
	        g.setColor(new Color(255,255,255));
			g.fillRect(st_x - 6 , 0, 3300, 3); //상단 눈금선
			for(int i = 0;i<3300;i +=30) {
				if(i > st_x) {
						g.fillRect(54 , 0, 4, 182);
					if(i % 30 == 0)
						g.fillRect(i , 0, 2, 8);
				}
			}
			g.fillRect(st_x - 6 , 182, 3300, 3); //하단 눈금선
			for(int i = 0;i<3300;i +=30) {
				if(i > st_x) {
						//g.fillRect(54 , 182, 4, 182);
					if(i % 30 == 0)
						g.fillRect(i , 178, 2, 7);
				}
			}
	        
	        for(int i = 0;i<block_list.size();i++) { // 각 프로세서
	        	g.setColor(Color.WHITE);
	        	g.drawString((i+1)+". ", 20, 35+42*i);
				for(int j = 0;j<block_list.get(i).size();j++) {
					int[] a = block_list.get(i).get(j).b;
					int[] c = block_list.get(i).get(j).c;
					
					g.setColor(new Color(c[0],c[1],c[2]));
					g.fillRect(a[0], a[1]-38, a[2], a[3]);
					
					if(block_list.get(i).get(j).pid != 0) {
						String p_name = "p"+Integer.toString(block_list.get(i).get(j).pid);
						if(j == block_list.get(i).size()-1) {
							g.setColor(Color.BLACK); //글자 그리기 
							g.drawString(p_name, a[0]+(a[2]/2)-10, a[1]-18); //a[1]+20
						}else {
							g.setColor(Color.BLACK); //글자 그리기 
							g.drawString(p_name, a[0]+(a[2]/2)-22, a[1]-18); //a[1]+20
						}
					}
				}
				
			}
	        g.drawImage(buffer, xx, 0, null);
	        
	        
		}
	}
	
}
