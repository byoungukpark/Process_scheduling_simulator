package schduling_apptest02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class hrrn_schduling {
	make_processes new_ps; //랜덤 프로세스
	use_processor new_co; //생성 프로세서
	ArrayList<Integer> ready = new ArrayList<>(); // 대기열
	int count_p = 0;
	
	hrrn_schduling(make_processes tum,int p,int e){
		new_ps = tum; //프로세스 생성
		new_co = new use_processor(p,e); //프로세서 생성
		play();
	} 
	
	void play() {
		int t = 0;
		
		while(new_ps.processes_list.size() != count_p) { //모든 프로세스가 일을 완료하면 종료
			for(int i = 0;i<new_ps.processes_list.size();i++) { //해당 시간(t) 해당하는 at 찾고 대기열에 넣기
				if(new_ps.processes_list.get(i).time[0] == t) {
					ready.add(i);
				}
			}
			if(ready.size() > 1)
				response_ratio_solt(t); //응답률에 따른 대기열 계산
			
			if(ready.isEmpty()) { //대기열 비어있으면 프로세서는 하던거 함
				new_co.kepwork_co();
				
			}else { //아니면 가능한 프로세서에 프로세스 진입
				if(new_co.is_canplay()) { //사용가능 프로세서가 있음
					while(new_co.is_canplay() && (ready.size() != 0)) {
						new_co.enter_co(new_co.co_index.get(0),ready.get(0),0,0); //프로세서 진입 
						ready.remove(0); //대기열에서 삭제
					}
					new_co.kepwork_co();
				}else {//이미 모든 프로세서가 일하는중 
					new_co.kepwork_co();
				}
				
			}
			
			is_finish(t);
			t++;
		}

		printing();
		
	}
	void is_finish(int t) {
		for(int i = 0;i<new_co.all_processor.size();i++) { //종료된 프로세스 찾기
			int p = new_co.all_processor.get(i).processes_index;
			if(new_ps.processes_list.get(p).time[1] <= new_co.all_processor.get(i).count_bt) {
				
				culculate(t, new_ps.processes_list.get(p).time[1], p, new_co.all_processor.get(i).type);
				
				count_p++;
				new_co.clear_co(i);
			}
		}
	}
	
	
	void response_ratio_solt(int t) { //응답률 정렬
		for(int i = 0;i<ready.size();i++) {
			int new_wt = t - new_ps.processes_list.get(ready.get(i)).time[0];
			double a = (new_wt + new_ps.processes_list.get(ready.get(i)).time[1])
					 / (double)new_ps.processes_list.get(ready.get(i)).time[1];
			new_ps.processes_list.get(ready.get(i)).resp_r = Math.round(a*100)/100.0;
			
			System.out.println("t: "+t +", p"+(ready.get(i)+1) + " ratio: "+new_ps.processes_list.get(ready.get(i)).resp_r);
			
		}
		System.out.println(" ");
		
		for(int i = 0;i<ready.size()-1;i++) {
			for(int j = i+1;j<ready.size();j++) {
				if(new_ps.processes_list.get(ready.get(i)).resp_r < new_ps.processes_list.get(ready.get(j)).resp_r)
					Collections.swap(ready,i,j);
			}
		}
//		for(int i = 0;i<ready.size()-1;i++) {
//			for(int j = i+1;j<ready.size();j++) {
//				if(ready.get(i)<ready.get(j))
//					Collections.swap(ready,i,j);
//			}
//		}
	}
	
	
	void culculate(int t,int bt,int prosesses_index,char type) { //계산
		new_ps.processes_list.get(prosesses_index).time[3] = 
				t - new_ps.processes_list.get(prosesses_index).time[0]+1; //t- at = tt / t - wt
		
		if(type == 'P') {
			int a = bt / 2;
			a += bt % 2; //p코어 사용시 bt
			a = new_ps.processes_list.get(prosesses_index).time[3] - a; //tt - bt
			new_ps.processes_list.get(prosesses_index).time[2] = a;
		}else {
			new_ps.processes_list.get(prosesses_index).time[2] = 
					new_ps.processes_list.get(prosesses_index).time[3] - bt;
		}
		
		double a = new_ps.processes_list.get(prosesses_index).time[3] / (double)bt;
		new_ps.processes_list.get(prosesses_index).ntt = Math.round(a*100)/100.0;
	}
	
	
	void printing() { //출력
		System.out.println("    at bt wt tt  ntt");
		for(int i = 0;i<new_ps.processes_list.size();i++)
			System.out.println("p"+ (i+1)+ "= "+Arrays.toString(new_ps.processes_list.get(i).time) +
					" "+new_ps.processes_list.get(i).ntt);
		
		for(int i = 0;i<new_co.all_processor.size();i++) {
			if(i != (new_co.all_processor.size())) {
				System.out.println(" ");
				System.out.print((i+1)+" "+new_co.all_processor.get(i).type+"_core scheduling time : ");
			}
			for(int j = 0;j<new_co.all_processor.get(i).time.size();j++) {
				System.out.print(new_co.all_processor.get(i).time.get(j) + " ");
			}
			System.out.println("");
			System.out.println(" >> use_electric : "+new_co.all_processor.get(i).electric);
			
		}
	}
	
	public make_processes give_processes() {
		return new_ps;
	}
	public use_processor give_core() {
		return new_co;
	}
	
	
}
