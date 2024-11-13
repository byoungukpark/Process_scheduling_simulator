package schduling_apptest02;

import java.util.ArrayList;
import java.util.Scanner;

import schduling_apptest02.make_processes.processes;

class play_schduling{
	public int num = 0; //프록세스 수
	public int p = 0;
	public int e = 1;
	public int rr = 2;
	public String schduling_num = "FCFS schduling"; //기본 스케쥴
	
	public String schduling_name[] = {"FCFS schduling","RR_schduling","spn_schduling","Srtn_schduling","hrrn schduling","own_schduling"};
	public String processes_name[] = {"P_id","AT","BT","WT","TT","NTT"};
	public String processes_tum_name[] = {"P_id","AT","BT"}; //입력 창 프로세스
	public Integer core_num[] = {0,1,2,3,4};
	
	
	public make_processes main_processes;
	public use_processor main_core;
	
	//프로세스 이차원 배열 - gui 활용
	public String processes_num[][]; //at,bt,wt,tt,ntt
	
	public ArrayList<String[]> enter_processes_list = new ArrayList<>(); //입력 프로세스 임시 저장
	public String processes_tum[][]; //테이블 보여 주기용
	
	
	public void make_show_processes_tabledata01() { //임의 입력 테이블 데이터 갱신
		processes_tum = new String[enter_processes_list.size()][3];
		for(int i = 0;i<enter_processes_list.size();i++) {
			processes_tum[i][0] = Integer.toString(i+1);
			String[] tum = enter_processes_list.get(i);
			
			for(int j = 0;j<2;j++) 
				processes_tum[i][j+1] = tum[j];
		}
	}
	
	public void make_show_processes_tabledata02() { //자동입력 테이블 데이터 갱신
		processes_tum = new String[main_processes.processes_list.size()][3];
		for(int i = 0;i<main_processes.processes_list.size();i++) {
			processes_tum[i][0] = Integer.toString(i+1);
			processes_tum[i][1] = Integer.toString(main_processes.processes_list.get(i).time[0]);
			processes_tum[i][2] = Integer.toString(main_processes.processes_list.get(i).time[1]);
		}
	}
	
	
	public void change_processes_datatype() { //gui활용을 위한 타입 변환 //출력용 테이블 데이터 타입 변환
		processes_num = new String[main_processes.processes_list.size()][6];
		for(int i = 0;i<main_processes.processes_list.size();i++) {
			processes_num[i][0] = Integer.toString(i+1);
			for(int j = 0;j<4;j++) 
				processes_num[i][j+1] = Integer.toString(main_processes.processes_list.get(i).time[j]);
			processes_num[i][5] = Double.toString(main_processes.processes_list.get(i).ntt);
		}
	}
	
	public void produce_processes() { //프로세스 생성
		main_processes = new make_processes(num);
		
		if(enter_processes_list.size() > 0) {
			num = enter_processes_list.size();
			make_processes tum_processes = new make_processes(num);
			main_processes = tum_processes;
			
			for(int i =0;i<enter_processes_list.size();i++) {
				String[] str = enter_processes_list.get(i);
				main_processes.processes_list.get(i).time[0] = Integer.parseInt(str[0]);
				main_processes.processes_list.get(i).time[1] = Integer.parseInt(str[1]);
			}
		}
	}
	
	play_schduling(){
		//start_schduling();
	}
	
	public void start_schduling() {
		if(!schduling_num.isEmpty()) {
			System.out.println("starting!!!!");
			switch(schduling_num) {
			case "FCFS schduling":
				System.out.println("FCFS schduling");
				FCFS_schduling fcfs = new FCFS_schduling(main_processes,p,e);
				
				main_processes = fcfs.give_processes();
				main_core = fcfs.give_core();
				break;
			case "RR_schduling":
				System.out.println("RR_schduling");
				RR_schduling RR = new RR_schduling(main_processes,p,e,rr); //rr
				
				main_processes = RR.give_processes();
				main_core = RR.give_core();
				break;
			case "spn_schduling":
				System.out.println("spn_schduling");
				spn_schduling spn = new spn_schduling(main_processes,p,e);
				
				main_processes = spn.give_processes();
				main_core = spn.give_core();
				break;
			case "Srtn_schduling":
				System.out.println("Srtn_schduling");
				Srtn_schduling srtn = new Srtn_schduling(main_processes,p,e);
				
				main_processes = srtn.give_processes();
				main_core = srtn.give_core();
				break;
			case "hrrn schduling":
				System.out.println("hrrn schduling");
				hrrn_schduling hrrn = new hrrn_schduling(main_processes,p,e);
				
				main_processes = hrrn.give_processes();
				main_core = hrrn.give_core();
				break;
			case "own_schduling":
				System.out.println("own_schduling");
				own_schduling own = new own_schduling(main_processes,p,e);
				
				main_processes = own.give_processes();
				main_core = own.give_core();
				break;

			}
		}else {
			System.out.println("error");
		}
	}
	
}

