package schduling_apptest02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

class own_schduling {
	make_processes new_ps; //랜덤 프로세스
	use_processor new_co; //생성 프로세서
	ArrayList<Integer> ready = new ArrayList<>(); // 전체 대기열
	ArrayList<Integer> ready_e = new ArrayList<>(); // e 코어 전용 대기열
	ArrayList<Integer> ready_p = new ArrayList<>(); // p 코어 전용 대기열
	ArrayList<Integer> bt = new ArrayList<>(); // 모든 프로세서들의 bt
	ArrayList<Integer> e_index = new ArrayList<>(); //e코어의 인덱스
	ArrayList<Integer> p_index = new ArrayList<>(); //p코어의 인덱스
	int count_p = 0;
	

	
	own_schduling(make_processes tum,int p,int e){
		new_ps = tum; //프로세스 생성
		new_co = new use_processor(p,e); //프로세서 생성
		play();
	} 
	
	void play() {
		int t = 0;
		
		for(int i = 0; i<new_ps.processes_list.size();i++) { // 모든 프로세서의 bt를 저장
			bt.add(new_ps.processes_list.get(i).time[1]);
		}
		
		int mid = midValue(bt); // 모든 bt들의 중앙 값
		
		for(int i = 0; i<new_co.all_processor.size();i++) { // type이 E인 프로세서의 인덱스와 P인 프로세서의 인덱스를 각각 저장
			if(new_co.all_processor.get(i).type == 'E')
				e_index.add(i);
			else
				p_index.add(i);
		}
		
		
		while(new_ps.processes_list.size() != count_p) { //모든 프로세스가 일을 완료하면 종료
			for(int i = 0;i<new_ps.processes_list.size();i++) { //해당 시간(t) 해당하는 at 찾고 대기열에 넣기
				if(new_ps.processes_list.get(i).time[0] == t) {
					ready.add(i);
					
				}
			}
			ready_p.clear(); //p대기열 초기화
			ready_e.clear(); //e대기열 초기화
			for(int k = 0; k<ready.size();k++) { //전체 대기열의 크기만큼 반복
				int p = new_ps.processes_list.get(ready.get(k)).time[1]; // 해당 대기열 프로세스의 bt
				if (p > mid) { // bt가 중앙값보다 크면 p대기열에 아니면 e대기열에 저장
					ready_p.add(k); 

				}
				else {
					ready_e.add(k);
				}

			}
			
			
			if(ready.isEmpty()) { //대기열 비어있으면 프로세서는 하던거 함
				new_co.kepwork_co();
				
			}else { //아니면 가능한 프로세서에 프로세스 진입
				if(new_co.is_canplay()) { //사용가능 프로세서가 있음
					while(new_co.is_canplay_e() && (ready_e.size() != 0)) { // 우선 e 대기열에 있는 프로세스을 e코어가 비어있다면 진입
						new_co.enter_co(new_co.co_index.get(0),ready.get(ready_e.get(0)),0,0); //프로세서 진입						
						ready.set(ready_e.get(0),-1);
						ready_e.remove(0);
					}
					while(new_co.is_canplay_p() && (ready_p.size() != 0)) { // p 대기열에 있는 프로세스를 p코어가 비어있다면 진입
						new_co.enter_co(new_co.co_index.get(0),ready.get(ready_p.get(0)),0,0); //프로세서 진입
						ready.set(ready_p.get(0),-1);
						ready_p.remove(0);												

					}
					ready.removeIf(i-> i == -1);
					while(new_co.is_canplay() && (ready.size() != 0)) { // 이후 남는 프로세스가 있다면 기존 FCFS처럼 순서대로 진입
						new_co.enter_co(new_co.co_index.get(0),ready.get(0),0,0); //프로세서 진입
						if(new_ps.processes_list.get(ready.get(0)).time[1] > mid) 
							ready_p.remove(0);						
						else
							ready_e.remove(0);
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
		System.out.println(mid);
		
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
	
	
	int midValue(ArrayList<Integer> A) {
		Collections.sort(A);
		int midValue = A.get(A.size()/2);
		return midValue;
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