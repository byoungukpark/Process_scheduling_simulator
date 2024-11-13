package schduling_apptest02;

import java.util.ArrayList;
import java.util.Arrays;

class RR_schduling {
	make_processes new_ps; //랜덤 프로세스
	use_processor new_co; //생성 프로세서
	ArrayList<Integer> ready = new ArrayList<>(); // 대기열
	int count_p = 0;
	int rr_time = 2;
	
	RR_schduling(make_processes tum,int p,int e,int rr){
		new_ps = tum; //프로세스 생성
		new_co = new use_processor(p,e); //프로세서 생성
		rr_time = rr;
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
			is_rr_finish(); //새로운 들어온 대기열 채운 후 rr 시간 다 됨놈 다시 대기열 진입
			
			if(ready.isEmpty()) { //대기열 비어있으면 프로세서는 하던거 함
				new_co.kepwork_co();
				
			}else { //아니면 가능한 프로세서에 프로세스 진입
				if(new_co.is_canplay()) { //사용가능 프로세서가 있음
					while(new_co.is_canplay() && (ready.size() != 0)) {
						//프로세스에 임시 저장되있는 이전 실행정보를 불러옴
						int bt_tum = new_ps.processes_list.get(ready.get(0)).past_bt;
						int using_tum = new_ps.processes_list.get(ready.get(0)).core_using_time;
						new_co.enter_co(new_co.co_index.get(0),ready.get(0),bt_tum,using_tum); //프로세서 진입 
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
	
	void is_rr_finish() {
		for(int i = 0;i<new_co.all_processor.size();i++) { //종료된 프로세스 찾기
			if(new_co.all_processor.get(i).core_using_time != 0
					&& new_co.all_processor.get(i).core_using_time % rr_time == 0) {
				int p_index = new_co.all_processor.get(i).processes_index;
				
				//코어의 프로세스 bt진행 시간, 코어 사용 시간을 다시 프로세스로 임시저장
				new_ps.processes_list.get(p_index).past_bt = new_co.all_processor.get(i).count_bt;
				new_ps.processes_list.get(p_index).core_using_time = new_co.all_processor.get(i).core_using_time;
				
				ready.add(p_index); //다시 대기열 진입
				
				new_co.clear_co(i);
			}
		}
	}
	
	void is_finish(int t) {
		for(int i = 0;i<new_co.all_processor.size();i++) { //종료된 프로세스 찾기
			int p = new_co.all_processor.get(i).processes_index;
			if(new_ps.processes_list.get(p).time[1] <= new_co.all_processor.get(i).count_bt) {
				int p_index = new_co.all_processor.get(i).processes_index;
				new_ps.processes_list.get(p_index).core_using_time = new_co.all_processor.get(i).core_using_time;
				
				culculate(t, new_ps.processes_list.get(p).time[1], p, new_co.all_processor.get(i).type);
				
				count_p++;
				new_co.clear_co(i);
			}
		}
	}
	
	void culculate(int t,int bt,int prosesses_index,char type) { //계산
		new_ps.processes_list.get(prosesses_index).time[3] = 
				t - new_ps.processes_list.get(prosesses_index).time[0]+1; //t- at = tt / t - wt
		
		
		int true_bt = new_ps.processes_list.get(prosesses_index).core_using_time;
		new_ps.processes_list.get(prosesses_index).time[2] =
				new_ps.processes_list.get(prosesses_index).time[3] - true_bt;
		
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
