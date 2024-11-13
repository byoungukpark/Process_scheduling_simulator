package schduling_apptest02;

import java.util.ArrayList;

class make_processes{
	ArrayList<processes> processes_list = new ArrayList<>(); //모든 프로세스 정보기록
	
	class processes{
		int[] time = {(int)(Math.random()*10),(int)(Math.random()*9)+1,0,0}; //at, bt, wt, tt
		double ntt = 0; //ntt
		double resp_r = 0; //response ratio
		int past_bt = 0;
		int core_using_time = 0;
		int[] itemcolor = {(int)((Math.random()*175)+80),(int)((Math.random()*175)+80),(int)((Math.random()*175)+80)}; //256
	}
	
	make_processes(int num){
		for(int i = 0; i < num; i++) {
			processes new_p = new processes();
			processes_list.add(new_p);
		}
	}
	
	public void clear_pastdata() {
		for(int i = 0;i<processes_list.size();i++) {
			processes_list.get(i).past_bt = 0;
			processes_list.get(i).core_using_time = 0;
		}
	}
	
}
